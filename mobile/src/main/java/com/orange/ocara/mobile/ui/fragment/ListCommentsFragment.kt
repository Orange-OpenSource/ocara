/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.CommentForCommentListModel
import com.orange.ocara.domain.models.CommentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentListCommentsBinding
import com.orange.ocara.mobile.ui.adapters.AudioCommentsAdapter
import com.orange.ocara.mobile.ui.adapters.PhotoCommentsAdapter
import com.orange.ocara.mobile.ui.adapters.TextCommentsAdapter
import com.orange.ocara.mobile.ui.dialogs.GenericDeleteConfirmationDialog
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.helpers.CameraHelper
import com.orange.ocara.mobile.ui.helpers.GalleryHelper
import com.orange.ocara.mobile.ui.managers.*
import com.orange.ocara.mobile.ui.viewmodel.ListCommentsViewModel
import com.orange.ocara.utils.TimeUtils
import com.orange.ocara.utils.enums.CommentType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListCommentsFragment : OcaraFragment(), GalleryHelper, CameraHelper {
    lateinit var binding: FragmentListCommentsBinding
    val args: ListCommentsFragmentArgs by navArgs()
    lateinit var audioCommentsAdapter: AudioCommentsAdapter
    lateinit var photoCommentsAdapter: PhotoCommentsAdapter
    lateinit var textCommentsAdapter: TextCommentsAdapter
    private val audioSuperManager: PlayAudioSuperManager = PlayAudioSuperManager()
    lateinit var selectImageFromGalleryManager: SelectImageFromGalleryManager
    lateinit var takeCameraImageManager: TakeCameraImageManager
    lateinit var selectImageFromGalleryLauncher: ActivityResultLauncher<Intent>
    lateinit var takeCameraImageLauncher: ActivityResultLauncher<Intent>
    lateinit var showFloatingActionButtonsManager: ShowFloatingActionButtonsManager
    var editMode: Boolean = false
    val viewModel: ListCommentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncherForActivityResult()
        selectImageFromGalleryManager =
            SelectImageFromGalleryManager(this, this, selectImageFromGalleryLauncher)
        takeCameraImageManager = TakeCameraImageManager(this, takeCameraImageLauncher, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_comments, container, false
        )
        setupCreateCommentViewsManager()
//        setupExpandAndCollapseBehaviour()
        changeActionBarTitle()
        setHasOptionsMenu(true)
        addClickListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showNoCommentsView()
        loadComments()
        changeActionBarTitle()

    }

    private fun loadComments() {
        viewModel.audioCommentsLiveData.observe(viewLifecycleOwner) {
            showComments()
            binding.audioCommentsTv.text = getString(R.string.audio_comments, it.size)
            initAudioCommentsList(it)
            setupExpandAndCollapseBehaviourForAudioComments()
        }
        viewModel.photoCommentsLiveData.observe(viewLifecycleOwner) {
            showComments()
            binding.photoCommentsTv.text = getString(R.string.photo_comments, it.size)
            initPhotoCommentsList(it)
            setupExpandAndCollapseBehaviourForPhotoComments()
        }
        viewModel.textCommentsLiveData.observe(viewLifecycleOwner) {
            showComments()
            binding.textCommentsTv.text = getString(R.string.text_comments, it.size)
            initTextCommentsList(it)
            setupExpandAndCollapseBehaviourForTxtComments()
        }
        viewModel.getAuditEquipmentComments(args.auditEquipmentId)
    }

    private fun initAudioCommentsList(comments: List<CommentForCommentListModel>) {
        audioCommentsAdapter = AudioCommentsAdapter(
            context = requireContext(),
            comments = comments as ArrayList<CommentForCommentListModel>,
            audioSuperManager = audioSuperManager,
            onClickListener = this::onCommentClicked
        )
        binding.audioCommentsList.adapter = audioCommentsAdapter
        binding.audioCommentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.audioCommentsList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initPhotoCommentsList(comments: List<CommentForCommentListModel>) {
        photoCommentsAdapter = PhotoCommentsAdapter(
            comments = comments as ArrayList<CommentForCommentListModel>,
            onClickListener = this::onCommentClicked
        )
        binding.photoCommentsList.adapter = photoCommentsAdapter
        binding.photoCommentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.photoCommentsList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initTextCommentsList(comments: List<CommentForCommentListModel>) {
        textCommentsAdapter = TextCommentsAdapter(
            comments = comments as ArrayList<CommentForCommentListModel>,
            onClickListener = this::onCommentClicked
        )
        binding.textCommentsList.adapter = textCommentsAdapter
        binding.textCommentsList.layoutManager = LinearLayoutManager(requireContext())
        binding.textCommentsList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun registerLauncherForActivityResult() {
        selectImageFromGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null)
                    selectImageFromGalleryManager.createCommentWithImage(result.data?.data!!)
            }
        takeCameraImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK)
                    takeCameraImageManager.imageTaken()
            }
    }

    private fun setupExpandAndCollapseBehaviourForTxtComments() {
        if (textCommentsAdapter.itemCount > 0) {
            binding.textCommentsDropDownIcon.visibility = View.VISIBLE
            ShowAndCollapseViewWithAnimationManager(
                view = binding.textCommentsList,
                controller = binding.textCommentsCardTitle,
                arrowIcon = binding.textCommentsDropDownIcon,
                isShown = true
            )
        } else {
            binding.textCommentsDropDownIcon.visibility = View.GONE
            binding.textCommentsCardTitle.setOnClickListener { insertTextComment() }
        }
    }

    private fun setupExpandAndCollapseBehaviourForAudioComments() {
        if (audioCommentsAdapter.itemCount > 0) {
            binding.audioCommentsDropDownIcon.visibility = View.VISIBLE
            ShowAndCollapseViewWithAnimationManager(
                view = binding.audioCommentsList,
                controller = binding.audioCommentsCardTitle,
                arrowIcon = binding.audioCommentsDropDownIcon,
                isShown = true
            )
        } else {
            binding.audioCommentsDropDownIcon.visibility = View.GONE
            binding.audioCommentsCardTitle.setOnClickListener {
                navigateToRecordComment()
            }
        }
    }

    private fun setupExpandAndCollapseBehaviourForPhotoComments() {
        if (photoCommentsAdapter.itemCount > 0) {
            binding.photoCommentsDropDownIcon.visibility = View.VISIBLE
            ShowAndCollapseViewWithAnimationManager(
                view = binding.photoCommentsList,
                controller = binding.photoCommentsCardTitle,
                arrowIcon = binding.photoCommentsDropDownIcon,
                isShown = true
            )
        } else {
            binding.photoCommentsDropDownIcon.visibility = View.GONE
            binding.photoCommentsCardTitle.setOnClickListener {
//                selectImageFromGalleryManager.selectImage()
                takeCameraImageManager.takeImage()
            }
        }
    }

    private fun changeActionBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.comments_label)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.comment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    lateinit var menuItemDelete: MenuItem
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_comment) {
            menuItemDelete = item
            enterEditMode()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showComments() {
        binding.noCommentsFoundLayout.visibility = View.GONE
        binding.commentsListLayout.visibility = View.VISIBLE
    }

    private fun showNoCommentsView() {
        binding.noCommentsFoundLayout.visibility = View.VISIBLE
        binding.commentsListLayout.visibility = View.GONE
    }

    private fun onCommentClicked(comment: CommentModel) {
        when (comment.type) {
            CommentType.AUDIO -> navigateToAudioComment(comment.id)
            CommentType.TEXT -> navigateToTextComment(comment.id, false, 0)
            CommentType.PHOTO, CommentType.FILE -> navigateToImageComment(
                comment.id,
                comment.type,
                false
            )
            else -> {
            }
        }
    }

    private fun enterEditMode() {
        showFloatingActionButtonsManager.hideOtherButtons()
        editMode = true
        audioCommentsAdapter.setEditMode(true)
        photoCommentsAdapter.setEditMode(true)
        textCommentsAdapter.setEditMode(true)
        menuItemDelete.isVisible = false
        binding.addComment.visibility = View.GONE
        binding.delete.visibility = View.VISIBLE
    }

    private fun exitEditMode() {
        editMode = false
        audioCommentsAdapter.setEditMode(false)
        photoCommentsAdapter.setEditMode(false)
        textCommentsAdapter.setEditMode(false)
        menuItemDelete.isVisible = true
        binding.addComment.visibility = View.VISIBLE
        binding.delete.visibility = View.GONE
    }

    private fun addClickListeners() {
        binding.micComment.setOnClickListener {
            navigateToRecordComment()
        }
        binding.imageComment.setOnClickListener {
            selectImageFromGalleryManager.selectImage()
        }
        binding.cameraComment.setOnClickListener {
            takeCameraImageManager.takeImage()
        }
        binding.textComment.setOnClickListener {
            insertTextComment()
        }
        binding.delete.setOnClickListener {
            val numComm: Int = viewModel.getNumberOfSelectedComments()
            if (numComm <= 0) return@setOnClickListener

            val deleteDialog = GenericDeleteConfirmationDialog(
                title = getString(R.string.delete_comments),
                msg = if (numComm <= 1) getString(R.string.delete_comments_confirmation_msg_one) else getString(
                    R.string.delete_comments_confirmation_msg_multiple,
                    numComm
                ),
                onDeleteConfirmed = this::deleteSelectedComments
            )
            deleteDialog.show(childFragmentManager, "DeleteCommentDialog")
        }
    }

    private fun deleteSelectedComments() {
        viewModel.deleteSelectedComments()
            .subscribeAndObserve {
                reloadFragment()
            }
    }

    private fun reloadFragment() {
        editMode = false
        NavHostFragment.findNavController(this)
            .navigate(ListCommentsFragmentDirections.actionListCommentsFragmentSelf(args.auditEquipmentId))
    }

    private fun insertTextComment() {
//        viewModel.insertComment(buildCommentModel(CommentType.TEXT, ""))
//                .subscribeAndObserve {
//                    navigateToTextComment(it)
//                }

        navigateToTextComment(0, true, args.auditEquipmentId)
    }

    private fun navigateToTextComment(it: Int, isNewComment: Boolean, equipmentId: Int) {
        val action = ListCommentsFragmentDirections
            .actionListCommentsFragmentToTextCommentFragment(
                commentId = it,
                isNewComment = isNewComment,
                equipmentId = equipmentId
            )

        NavHostFragment.findNavController(this).navigate(action)
    }


    private fun setupCreateCommentViewsManager() {
        showFloatingActionButtonsManager = ShowFloatingActionButtonsManager.Builder()
            .addButton(binding.addComment)
            .cancelButton(binding.cancel)
            .otherButtons(binding.addCommentsActionButtons)
            .build()
    }

    private fun buildCommentModel(commentType: CommentType, attachment: String = ""): CommentModel {
        return CommentModel(0, commentType, TimeUtils.getTimeInFormatForDatebase(), attachment, "")
            .equipmentIdBuider(args.auditEquipmentId.toLong())
    }


    private fun navigateToImageComment(id: Int, commType: CommentType, fromCreate: Boolean) {
        NavHostFragment.findNavController(this)
            .navigate(
                ListCommentsFragmentDirections.actionListCommentsFragmentToPhotoCommentFragment(
                    id,
                    commType,
                    fromCreate
                )
            )
    }

    private fun navigateToAudioComment(commentId: Int) {
        NavHostFragment.findNavController(this)
            .navigate(
                ListCommentsFragmentDirections.actionListCommentsFragmentToAudioCommentNav(
                    commentId = commentId
                )
            )
    }

    private fun navigateToRecordComment() {
        NavHostFragment.findNavController(this)
            .navigate(
                ListCommentsFragmentDirections.actionListCommentsFragmentToRecordCommentNav(
                    equipmentId = args.auditEquipmentId
                )
            )
    }

    override fun onPause() {
        super.onPause()
        audioSuperManager.stopAll()
    }

    override fun onImageSelectedFromGallery(imageFileName: String) {
        viewModel.insertComment(buildCommentModel(CommentType.FILE, imageFileName))
            .subscribeAndObserve {
                navigateToImageComment(it, CommentType.FILE, true)
            }
    }

    override fun getLifecycleScope() = lifecycleScope

    override fun imageSaved(attachment: String) {
        viewModel.insertComment(buildCommentModel(CommentType.PHOTO, attachment))
            .subscribeAndObserve {
                navigateToImageComment(it, CommentType.PHOTO, true)
            }
    }

    override fun onBack(): Boolean {
        if (editMode) {
            exitEditMode()
            return true
        }
        return false
    }

}