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
import android.util.DisplayMetrics
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentPhotoCommentBinding
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.helpers.CameraHelper
import com.orange.ocara.mobile.ui.helpers.DeleteCommentInterface
import com.orange.ocara.mobile.ui.helpers.Editable
import com.orange.ocara.mobile.ui.helpers.GalleryHelper
import com.orange.ocara.mobile.ui.managers.ImageDrawerManager
import com.orange.ocara.mobile.ui.managers.SelectImageFromGalleryManager
import com.orange.ocara.mobile.ui.managers.TakeCameraImageManager
import com.orange.ocara.mobile.ui.viewmodel.PhotoCommentViewModel
import com.orange.ocara.utils.enums.CommentType
import dagger.hilt.android.AndroidEntryPoint
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver


@AndroidEntryPoint
class PhotoCommentFragment : OcaraFragment(), GalleryHelper, CameraHelper, Editable,
    DeleteCommentInterface {
    lateinit var binding: FragmentPhotoCommentBinding
    private val args: PhotoCommentFragmentArgs by navArgs()

    var deleteOnBack: Boolean = false

    lateinit var viewModel: PhotoCommentViewModel

    lateinit var selectImageFromGalleryManager: SelectImageFromGalleryManager
    lateinit var imageDrawerManager: ImageDrawerManager
    private var editMode: Boolean = false
    lateinit var menu: Menu

    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var takeCameraImageLauncher: ActivityResultLauncher<Intent>

    lateinit var takeCameraImageManager: TakeCameraImageManager

    private fun registerLauncherForActivityResult() {
        launcher =
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        registerLauncherForActivityResult()
        initDeleteOnBack()
        selectImageFromGalleryManager = SelectImageFromGalleryManager(this, this, launcher)
        takeCameraImageManager = TakeCameraImageManager(this, takeCameraImageLauncher, this)
    }

    private fun initDeleteOnBack() {
        // if we are using this screen as a create not as edit then we should delete the comment
        // when back-button is clicked
        if (args.fromCreate) {
            deleteOnBack = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_photo_comment, container, false
        )
        setHasOptionsMenu(true)
        loadImagePath()
        initClickListeners()
//        if (!args.fromCreate) {
//            hideLowerButtons()
//        }
        return binding.root
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(PhotoCommentViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.photo_comment_menu, menu)
        menu.findItem(R.id.edit_mode).setContentDescription(getString(R.string.content_desc_edit_photo))
//        if (!args.fromCreate) {
//            hideMenuItems()
//        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_mode -> startEditMode()
            R.id.color_picker -> showColorPickerDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startEditMode() {
        editMode = true
        showLowerButtons()
        hideMenuItems()
        showColorPickerIcon()
        binding.retakeBtn.visibility = View.INVISIBLE
    }

    private fun showColorPickerIcon() {
        val colorPicker = menu.findItem(R.id.color_picker)
        colorPicker.isVisible = true
    }

    private fun exitEditMode() {
        editMode = false
//        if (args.fromCreate)
        showLowerButtons()
        showMenuItems()
        hideColorPickerIcon()
//        binding.retakeBtn.visibility = View.VISIBLE
    }

    private fun hideColorPickerIcon() {
        val colorPicker = menu.findItem(R.id.color_picker)
        colorPicker.isVisible = false
    }

    private fun showMenuItems() {
        val editItem = menu.findItem(R.id.edit_mode)
        editItem.isVisible = true
    }

    private fun showColorPickerDialog() {
        ColorPickerPopup.Builder(requireContext())
            .initialColor(imageDrawerManager.getPainterColor()) // Set initial color
            .enableBrightness(false) // Enable brightness slider or not
            .enableAlpha(false) // Enable alpha slider or not
            .okTitle("Ok")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(false)
            .build()
            .show(view, object : ColorPickerObserver() {
                override fun onColorPicked(color: Int) {
                    imageDrawerManager.setPainterColor(color)
                }
            })
    }

    private fun hideMenuItems() {
        val editItem = menu.findItem(R.id.edit_mode)
        editItem.isVisible = false
    }

    private fun showLowerButtons() {
        binding.cancel.visibility = View.VISIBLE
        binding.validate.visibility = View.VISIBLE
        binding.retakeBtn.visibility = View.VISIBLE
    }


    private fun initClickListeners() {
        addClickListenerForCancelButton()
        addClickListenerForSaveButton()
        addClickListenerForRetakeBtn()
    }

    private fun addClickListenerForRetakeBtn() {
        binding.retakeBtn.setOnClickListener {
            if (args.commentType == CommentType.FILE)
                selectImageFromGalleryManager.selectImage()
            else
                takeCameraImageManager.takeImage()
        }
    }

    private fun addClickListenerForSaveButton() {
        binding.validate.setOnClickListener {
            imageDrawerManager.save()
            exitEditMode()
            deleteOnBack = false

            viewModel.updateAttachment(args.commentId, viewModel.getPhotoAttachment())
                .subscribeAndObserve {
                    requireActivity().onBackPressed()
                }
        }
    }

    private fun addClickListenerForCancelButton() {
        binding.cancel.setOnClickListener {
            if (editMode) {
                exitEditMode()
                imageDrawerManager.cancelAllDrawingMade()
            } else {
                requireActivity().onBackPressed()
//                deleteComment(args.commentId)
            }
        }
    }

    private fun loadImagePath() {
        viewModel.getAttachment(args.commentId)
            .subscribeAndObserve { path ->
                setImage(path)
            }
    }

    private fun setImage(path: String) {

        imageDrawerManager = ImageDrawerManager(binding.photo, path, this)
        viewModel.setPhotoAttachment(path)
    }

    override fun onImageSelectedFromGallery(imageFileName: String) {
//        viewModel.updateAttachment(args.commentId, imageFileName)
//                .subscribeAndObserve {
//                    setImage(imageFileName)
//                }

        setImage(imageFileName)
    }

    override fun imageSaved(image: String) {
//        viewModel.updateAttachment(args.commentId, image)
//            .subscribeAndObserve {
//                setImage(image)
//            }
        setImage(image)
    }

    override fun getLifecycleScope() = lifecycleScope

    override fun onBack(): Boolean {
        return when {
            imageDrawerManager.onBack() -> {
                true
            }
            editMode -> {
                exitEditMode()
                true
            }
            deleteOnBack -> {
                deleteOnBack = false
                // we set it to false we because when the comment is deleted it will call the on-back method
                // from the activity which will call this function (onBack) again,so in order to not
                // get into a cycle we set it to false
                deleteComment(args.commentId)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun deleteComment(commentId: Int) {
        viewModel.deleteComment(commentId)
            .subscribeAndObserve {
                requireActivity().onBackPressed()
            }
    }

    override fun isEditMode() = editMode
}