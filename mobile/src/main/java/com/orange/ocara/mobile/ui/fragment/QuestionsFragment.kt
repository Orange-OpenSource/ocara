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

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentQuestionsBinding
import com.orange.ocara.mobile.ui.MainActivity
import com.orange.ocara.mobile.ui.adapters.AnswerQuestionsAdapter
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.helpers.OnAnswerSelected
import com.orange.ocara.mobile.ui.viewmodel.QuestionsViewModel
import com.orange.ocara.utils.enums.Answer
import com.orange.ocara.utils.enums.AuditLevel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuestionsFragment : OcaraFragment(), OnAnswerSelected {
    lateinit var binding: FragmentQuestionsBinding
    val args: QuestionsFragmentArgs by navArgs()
    private val viewModel: QuestionsViewModel by viewModels()
    lateinit var questionsAdapter: AnswerQuestionsAdapter

    // this variable will be used to close last opened question when opening a new question
    var lastSelectedQuestionIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataInViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getClassNameForHelp(): String {
        if (viewModel.getMode() == AuditLevel.EXPERT)
            return this::class.java.name + ".E"
        else
            return this::class.java.name + ".N"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_questions, container, false
        )
        addObserversForLiveData()
        setHasOptionsMenu(true)
        initClickListeners()
        addListenerForCollapsingAnyQuestion()
        viewModel.loadRuleSetData()
        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(null)

        return binding.root
    }

    private fun addListenerForCollapsingAnyQuestion() {
        binding.expandableList.setOnGroupExpandListener { currentExpandedQuestionIndex ->
            if (lastSelectedQuestionIndex != -1
                && currentExpandedQuestionIndex != lastSelectedQuestionIndex
            ) {
                binding.expandableList.collapseGroup(lastSelectedQuestionIndex);
            }
            lastSelectedQuestionIndex = currentExpandedQuestionIndex;
        }
    }
//    private fun getTitle(): String? {
//        if (viewModel.getMode() == AuditLevel.EXPERT)
//            return viewModel.getEquipmentName()
////            getString(R.string.audit_object_expert_mode_title,
////                    viewModel.getEquipmentName(), viewModel.getCurrentAuditEquipmentIndex() + 1, viewModel.getAuditEquipments().size)
//        else
//            return getString(
//                R.string.audit_object_novice_mode_title,
//                viewModel.getEquipmentName(),
//                viewModel.getCurrentQuestionIndex() + 1,
//                viewModel.getNumberOfQuestion()
//            )
//    }

    private fun getTitle(): String? {
        return viewModel.getEquipmentName()
    }

    private fun getSubTitle(): String? {
        if (viewModel.getMode() == AuditLevel.EXPERT)
            return ""
        else
            return getString(
                R.string.audit_object_novice_mode_sub_title,
                viewModel.getCurrentQuestionIndex() + 1,
                viewModel.getNumberOfQuestion()
            )
    }

    private fun initClickListeners() {
        binding.nextBtn.setOnClickListener {
            viewModel.getNextQuestion()
        }
        binding.preBtn.setOnClickListener {
            viewModel.getPreQuestion()
        }
    }

    private fun addObserversForLiveData() {
        viewModel.addObserverForLabel(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
            binding.nextBtn.text = if (it == QuestionsViewModel.NEXT_LABEL)
                getString(R.string.next)
            else {
                getString(R.string.valider)
            }
            if (it == QuestionsViewModel.NEXT_LABEL) {
//                binding.nextBtn.icon = null
                binding.nextBtn.icon = resources.getDrawable(R.drawable.chevron_right)
                binding.nextBtn.iconGravity = MaterialButton.ICON_GRAVITY_END
            } else {
                binding.nextBtn.icon = resources.getDrawable(R.drawable.ic_round_check_black_24)
                binding.nextBtn.iconGravity = MaterialButton.ICON_GRAVITY_START
            }
        })


        viewModel.addObserverForDisplayedQuestions(
            QuestionsViewModel.ObserverData(
                viewLifecycleOwner
            ) {
                Timber.d("nnnn addObserverForDisplayedQuestions")
                (requireActivity() as MainActivity).supportActionBar!!.title = getTitle()
//                (requireActivity() as MainActivity).supportActionBar!!.subtitle = getSubTitle()
                (requireActivity() as MainActivity).supportActionBar!!.setSubtitle(Html.fromHtml("<font color='#00000'><b>" + getSubTitle() + "</b></font>"));

                displayedQuestionObserver(it)
            })
        viewModel.addObserverForAnswersSaved(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
            answersSavedObserver(it)
        })
        viewModel.addObserverForAuditId(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
            navigateToCurrentRouteFragment(it)
        })
        viewModel.addObserverForBackBtnVis(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
            binding.preBtn.visibility = it
        })

        viewModel.addObserverForRuleSetData(QuestionsViewModel.ObserverData(viewLifecycleOwner) {
        })
    }

    private fun navigateToCurrentRouteFragment(auditId: Int) {
        if (auditId != -1) {
            NavHostFragment.findNavController(this)
                .navigate(
                    QuestionsFragmentDirections.actionQuestionsFragmentToCurrentRouteFragment(
                        auditId
                    )
                )
//            findNavController().navigateUp()
            viewModel.postValueInAuditId(-1)
        }
    }
//    private fun navigateToCurrentRouteFragment(auditId: Int) {
//        if (auditId != -1) {
//            NavHostFragment.findNavController(this)
//                .navigate(
//                    QuestionsFragmentDirections.actionQuestionsFragmentToCurrentRouteFragment(
//                        auditId
//                    )
//                )
//            viewModel.postValueInAuditId(-1)
//        }
//    }

    private fun answersSavedObserver(it: Int) {
        if (it == QuestionsViewModel.SAVE_FOR_VALIDATE_SUCC_RES) {
            viewModel.loadAuditId()
            viewModel.postValueInAnswersSaved(it)
        } else {
            findNavController().navigateUp()
        }
    }

    private fun displayedQuestionObserver(it: List<QuestionAnswerModel>) {
        questionsAdapter = AnswerQuestionsAdapter(
            requireContext(),
            it,
            this::onAnswerSelected,
            this::navigateToIllustrations
        )
        binding.expandableList.setAdapter(questionsAdapter)
        if (viewModel.getMode() == AuditLevel.BEGINNER) {
            binding.expandableList.expandGroup(0)
        }

    }

    private fun navigateToIllustrations(ruleRef: String) {
        NavHostFragment.findNavController(this)
            .navigate(
                QuestionsFragmentDirections.actionQuestionsFragmentToExplanationsFragment(
                    ruleRef, viewModel.getRulSetData().reference, viewModel.getRulSetData().version
                )
            )
    }

    private fun initDataInViewModel() {
        viewModel.loadData(args.auditEquipmentId)
    }

    override fun onAnswerSelected(ruleAnswer: RuleAnswerModel, answer: Answer) {
        ruleAnswer.answer = answer
        ruleAnswer.questionAnswer.updateAnswer()
        questionsAdapter.notifyDataSetChanged()
    }

    override fun onBack(): Boolean {

        val confirmDialog = GenericConfirmationDialog(
            title = getString(R.string.question_answer_save_dialog_title),
            msg = getString(R.string.question_answer_save_dialog_content),
            confirmString = getString(R.string.question_answer_save_dialog_confirm),
            cancelString = getString(R.string.question_answer_save_dialog_cancel),
            onConfirmed = this::onSaveConfirm,
            onCancel = this::onCancelSave
        )
        confirmDialog.show(childFragmentManager, "ConfirmAnswersDialog")
        return true
    }

    fun onCancelSave() {
        findNavController().navigateUp()
    }

    fun onSaveConfirm() {
        viewModel.saveAnswers(saveforValidate = false)
    }

    override fun onPause() {
        (requireActivity() as MainActivity).supportActionBar!!.subtitle = ""
        super.onPause()
    }
}