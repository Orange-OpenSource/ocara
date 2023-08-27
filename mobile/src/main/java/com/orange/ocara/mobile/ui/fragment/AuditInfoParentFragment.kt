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

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentCreateAuditBinding
import com.orange.ocara.mobile.ui.adapters.RulesetDropListAdapter
import com.orange.ocara.mobile.ui.adapters.RulesetItemSelectedListener
import com.orange.ocara.mobile.ui.dialogs.*
import com.orange.ocara.mobile.ui.setFocusBackgroundForTextField
import com.orange.ocara.mobile.ui.showProgressDialog
import com.orange.ocara.mobile.ui.testing.EspressoIdlingResource
import com.orange.ocara.mobile.ui.viewmodel.AuditInfoParentViewModel


abstract class AuditInfoParentFragment : OcaraFragment() {
    val GREY = Color.argb(80, 10 * 16 + 10, 10 * 16 + 10, 10 * 16 + 10)
    lateinit var binding: FragmentCreateAuditBinding
    lateinit var viewModel: AuditInfoParentViewModel
    var mDownloadRuleSetProgressDialog: ProgressDialog? = null
    lateinit var confirmDownloadDialog: GenericConfirmationDialog

    abstract fun getViewModelInstance(): AuditInfoParentViewModel
    abstract fun initSeperateViews()

    //    abstract fun enableMainBtn()
//    abstract fun disableMainBtn()
    abstract fun loadData()

    //    abstract fun onAddAuditorAction()
    abstract fun onAddAuditorAction()
    abstract fun onAddSiteAction()
    abstract fun onSiteUpdated(site: SiteModel)
    abstract fun onAuditorUpdated(auditor: AuditorModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clearAudit()
                isEnabled = false
                activity?.onBackPressed()

            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_create_audit, container, false
        )

        binding.auditNameET.text?.let {
            if (binding.auditNameET.text!!.isEmpty()) {
                binding.auditNameET.contentDescription = getString(R.string.enter_audit_name)
            }
        }
        viewModel = getViewModelInstance()
        viewModel.clearAudit()
        binding.auditNameET.text?.clear()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initViews()
        return binding.root
    }

    private fun initViews() {
        setFocusColorForAllEditText()
//        disableMainBtn()
        initActionBar()
        initRulesetAdapter()
        initAuditorInfoButton()
        initSiteInfoButton()
        initEditText()
        initClickListeners()
        loadData()
        initSeperateViews()
    }

    private fun initEditText() {
//        val textListener = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkFieldsToEnableCreateButton()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//        }

        val auditNameTextListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkFieldsToEnableCreateButton()
            }

            override fun afterTextChanged(s: Editable?) {
                binding.auditNameET.contentDescription = ""
                binding.siteET.contentDescription = ""
                binding.auditorET.contentDescription = ""
                viewModel.updateAuditName(binding.auditNameET.text.toString())
                if (binding.fillAudNameTxt.visibility == View.VISIBLE) {
                    if (!binding.auditNameET.text!!.trim().isEmpty()) {
                        binding.fillAudNameTxt.visibility = View.GONE
                        ViewCompat.setBackground(
                                binding.auditNameET,
                                ResourcesCompat.getDrawable(
                                        requireContext().resources,
                                        R.drawable.edit_text_bg_focus,
                                        null
                                )
                        )
                    }
                }
            }
        }
        binding.auditNameET.addTextChangedListener(auditNameTextListener)
//        binding.auditorET.addTextChangedListener(textListener)
//        binding.siteET.addTextChangedListener(textListener)


//        setOnClickListenerForDrawableInTextField(
//            binding.siteET, DrawablePosition.DRAWABLE_RIGHT
//        )

        binding.expertiseInfo.setOnClickListener { showExpertiseInfoDialog() }
        binding.siteET.setOnClickListener {
            onSiteBtnClick()
        }

        binding.siteET.setOnTouchListener { v, event ->
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                onSiteBtnClick()
            }
            false
        }
//        setOnClickListenerForDrawableInTextField(
//            binding.auditorET, DrawablePosition.DRAWABLE_RIGHT
//        )
        binding.auditorET.setOnClickListener {
            onAuditorBtnClick()
        }
        binding.auditorET.setOnTouchListener { v, event ->
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                onAuditorBtnClick()
            }
            false
        }

    }

    fun showExpertiseInfoDialog() {
        val expertiseInfoDialog = ExpertiseInfoDialog()
        expertiseInfoDialog.show(parentFragmentManager, RulesetInfoDialog.TAG)
    }

    private fun onSiteBtnClick() {

        binding.auditNameET.contentDescription = ""
        binding.siteET.contentDescription = ""
        binding.auditorET.contentDescription = ""
        if (viewModel.allSitesLive.value!!.isEmpty()) {
            onAddSiteAction()
        } else {
            onChooseSite()
        }
    }

    private fun onAuditorBtnClick() {

        binding.auditNameET.contentDescription = ""
        binding.siteET.contentDescription = ""
        binding.auditorET.contentDescription = ""
        if (viewModel.allAuditorsLive.value!!.isEmpty()) {
            onAddAuditorAction()
        } else {
            onChooseAuditor()
        }
    }

    protected fun onChooseAuditor() {
        var dialog = childFragmentManager.findFragmentByTag(SelectAuditorDialog.TAG)
        if (dialog != null && dialog.isVisible) {
            return
        }

        dialog = SelectAuditorDialog(
                viewModel,
                onAuditorSelected = this::onAuditorSelected,
                onCreateAuditorClicked = this::onAddAuditorAction
        )
        dialog.show(childFragmentManager, SelectAuditorDialog.TAG)
    }

    protected fun onAuditorSelected(item: AuditorModel) {
        viewModel.updateSelectedAuditor(item)
    }

    protected fun onChooseSite() {
        var dialog = childFragmentManager.findFragmentByTag(SelectSiteDialog.TAG)
        if (dialog != null && dialog.isVisible) {
            return
        }
        dialog = SelectSiteDialog(
                viewModel,
                onSiteSelected = this::onSiteSelected,
                onCreateSiteClicked = this::onAddSiteAction
        )
        dialog.show(childFragmentManager, SelectSiteDialog.TAG)
    }

    protected fun onSiteSelected(item: SiteModel) {
        viewModel.updateSelectedSite(item)
    }

//    fun checkFieldsToEnableCreateButton() {
//        if (validateAudit()) {
//            enableMainBtn()
//        } else {
//            disableMainBtn()
//        }
//    }

    fun initRulesetAdapter() {

        viewModel.rulesetsLiveData.observe(viewLifecycleOwner) { rulesetModels ->
//            Toast.makeText(requireContext() , "RULESETS "+ rulesetModels.size , Toast.LENGTH_LONG).show()

            val adapter =
                    RulesetDropListAdapter(requireContext(), rulesetModels)

            binding.referentialSpinner.adapter = adapter

//            checkFieldsToEnableCreateButton()

            binding.referentialSpinner.onItemSelectedListener =
                    object : RulesetItemSelectedListener() {
                        override fun onRulesetSelected(position: Int) {
                            selectRuleset(position, rulesetModels)
                        }
                    }

        }
    }

    fun onDownloadRuleSet(rulesetModels: List<RulesetModel>, pos: Int) {
//        binding.referentialSpinner.setSelection(pos)
        binding.referentialSpinner.onDetachedFromWindow()
//        onDownloadRuleSetSelected(rulesetModels[pos])
        showRulesetInfoDialog(rulesetModels[pos])
    }

    fun onDownloadRuleSetSelected(rulesetModel: RulesetModel) {
        confirmDownloadDialog =
                GenericConfirmationDialog(getString(R.string.download_ref_dialog_title),
                        getString(R.string.download_ref_dialog_content, rulesetModel.type),
                        getString(R.string.confirm),
                        getString(R.string.cancel),
                        onConfirmed = { onDownloadConfirmed(rulesetModel) },
                        {})
        confirmDownloadDialog.show(parentFragmentManager, "confirmDownloadDialog")
//        val window: Window = confirmDownloadDialog.dialog!!.getWindow()!!
//        window.setLayout(Constraints.LayoutParams.FILL_PARENT, Constraints.LayoutParams.FILL_PARENT)

    }

    fun onDownloadConfirmed(rulesetModel: RulesetModel) {
        mDownloadRuleSetProgressDialog =
                showProgressDialog(requireContext(), getString(R.string.downloading))
        viewModel.downloadRuleset(rulesetModel)

        viewModel.rulesetDownloadCompleteLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                // this is essential to so this function doesn't get called again with true
                // when this dialog is created again
                viewModel.rulesetDownloadCompleteLiveData.postValue(null)
                if (mDownloadRuleSetProgressDialog != null) {
                    mDownloadRuleSetProgressDialog?.dismiss()
                }
                confirmDownloadDialog.dismiss()

                //for testing.
                EspressoIdlingResource.decrement()

            }
        }
    }

    fun initSiteInfoButton() {
        viewModel.currentSite.observe(viewLifecycleOwner) {
            onSiteChanged()
        }

        binding.siteInfo.setOnClickListener {
            val dialog = SiteInfoDialog(parentFragmentManager, viewModel, onSiteUpdated = this::onSiteUpdated)
            dialog.show(childFragmentManager, SiteInfoDialog.TAG)

        }
        onSiteChanged()
    }

    fun onSiteChanged() {
        if (viewModel.hasSelectedSite()) {
//                binding.siteInfo.visibility = View.VISIBLE
            binding.siteInfo.setImageResource(R.drawable.ic_info_black)
            binding.siteInfo.isEnabled = true
            binding.siteET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0)
        } else {
//                binding.siteInfo.visibility = View.GONE
            binding.siteInfo.setImageResource(R.drawable.ic_outline_info_24)
            binding.siteInfo.isEnabled = false

            if (viewModel.allSitesLive.value == null || viewModel.allSitesLive.value!!.isEmpty()) {
                binding.siteET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_icon_content_add_24_px,
                        0
                )
            } else {
                binding.siteET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.search_icon,
                        0
                )
            }

        }
    }

    fun initAuditorInfoButton() {
        viewModel.currentAuditor.observe(viewLifecycleOwner) {
            onAuditorChanged()
        }
        binding.auditorInfo.setOnClickListener {
            val dialog = AuditorInfoDialog(parentFragmentManager, viewModel, onAuditorUpdated = this::onAuditorUpdated)
            dialog.show(childFragmentManager, AuditorInfoDialog.TAG)
        }

        onAuditorChanged()
//        viewModel.auditorDeleted.observe(viewLifecycleOwner) {
//            binding.auditorInfo.setImageResource(R.drawable.ic_outline_info_24)
//            binding.auditorInfo.isEnabled = false
//            binding.auditorET.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    R.drawable.search_icon,
//                    0
//            )
//        }
    }

    fun onAuditorChanged() {
        if (viewModel.hasSelectedAuditor()) {
//                binding.auditorInfo.visibility = View.VISIBLE
            binding.auditorInfo.setImageResource(R.drawable.ic_info_black)
            binding.auditorInfo.isEnabled = true
            binding.auditorET.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.search_icon,
                    0
            )
        } else {
            binding.auditorInfo.setImageResource(R.drawable.ic_outline_info_24)
            binding.auditorInfo.isEnabled = false
            if (viewModel.allAuditorsLive.value == null || viewModel.allAuditorsLive.value!!.isEmpty()) {
                binding.auditorET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_icon_content_add_24_px,
                        0
                )
            } else {
                binding.auditorET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.search_icon,
                        0
                )
            }

//                binding.auditorInfo.visibility = View.GONE
        }
    }

    private fun setRulesetContentDescription(ruleSet: RulesetModel) {
        var contentDesc = getString(
                R.string.rulesetInDroplistFormat,
                ruleSet.type,
                ruleSet.version.toInt())
        if (ruleSet.isLocallyAvailable) {
            contentDesc = contentDesc + " " + getString(R.string.rulesetInDroplist_content_desc_downloaded)
        } else {
            contentDesc = contentDesc + " " + getString(R.string.rulesetInDroplist_content_desc_not_downloaded)
        }
        binding.referentialSpinner.setContentDescription(contentDesc)

    }

    private fun selectRuleset(position: Int/*,ruleset: RulesetModel*/, rulesets: List<RulesetModel>) {
        setRulesetContentDescription(ruleSet = rulesets[position])
        if (!rulesets[position].isLocallyAvailable) {
            enableRulesetDownloadWarning(rulesets[position])
            onDownloadRuleSet(rulesets, position)

        } else {
            hideRulesetDownloadWarning()
//            binding.referentialSpinner.setSelection(rulesets.indexOf(rulesets[position]))
            viewModel.savePreferedRuleset(rulesets[position])
        }
        updateReferentialInfoBtn(rulesets[position])
    }

    private fun hideRulesetDownloadWarning() {
//        binding.referentialDownloadBtn.visibility = View.GONE
        binding.referentialWarningTv.visibility = View.GONE

    }

    private fun enableRulesetDownloadWarning(ruleset: RulesetModel) {
//        binding.referentialDownloadBtn.visibility = View.VISIBLE
        binding.referentialWarningTv.visibility = View.VISIBLE

        binding.referentialDownloadBtn.setOnClickListener { showRulesetInfoDialog(ruleset) }
    }


    fun updateReferentialInfoBtn(ruleset: RulesetModel) {
//        if (ruleset.isLocallyAvailable) {
//            binding.referentialInfo.setImageResource(R.drawable.info_19371_1)
//        } else {
//            binding.referentialInfo.setImageResource(R.drawable.info_19371_2)
//        }
        binding.referentialInfo.setOnClickListener { showRulesetInfoDialog(ruleset) }
    }

    abstract fun navigateToViewReferential(ruleset: RulesetModel)

    fun showRulesetInfoDialog(ruleset: RulesetModel) {
        val rulesetInfoDialog =
                RulesetInfoDialog(parentFragmentManager, ruleset, viewModel) {
                    navigateToViewReferential(it)
                }
        rulesetInfoDialog.show(parentFragmentManager, RulesetInfoDialog.TAG)
    }

    private fun initActionBar() {
//        (activity as AppCompatActivity).supportActionBar?.setLogo(R.drawable.file_edit)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
//        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.new_audit_title)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            viewModel.clearAudit()
        }
        if (item.itemId == R.id.view_referentials) {
            NavHostFragment.findNavController(this).navigate(R.id.action_view_ref)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_audit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun selectRadioButton(button: RadioButton) {

        if (!button.isChecked) button.isChecked = true
        button.background = requireActivity().getDrawable(R.drawable.radio_btn_checked_bg)
    }

    fun unSelectRadioButton(button: RadioButton) {
        if (button.isChecked) button.isChecked = false
        button.background = requireActivity().getDrawable(R.drawable.radio_btn_not_checked_bg)
    }

    fun initClickListeners() {
        binding.noviceSwitchButton.setOnClickListener {
            selectRadioButton(binding.noviceSwitchButton)
            unSelectRadioButton(binding.expertSwitchButton)
            viewModel.expertMode = false
        }
        binding.expertSwitchButton.setOnClickListener {
            selectRadioButton(binding.expertSwitchButton)
            unSelectRadioButton(binding.noviceSwitchButton)
            viewModel.expertMode = true
        }

//        binding.selectSite.setOnClickListener {
//            val selectSiteDialog = SelectSiteDialog(childFragmentManager, requireActivity(), viewModel)
//            selectSiteDialog.show(childFragmentManager, SelectSiteDialog.TAG)
//        }
//        binding.siteInfo.setOnClickListener {
//            val siteInfoDialog = SiteInfoDialog(childFragmentManager, requireActivity(), viewModel)
//            siteInfoDialog.show(childFragmentManager, SiteInfoDialog.TAG)
//        }
//        binding.auditorInfo.setOnClickListener {
//            val auditorInfoDialog = AuditorInfoDialog(childFragmentManager, requireActivity(), viewModel)
//            auditorInfoDialog.show(childFragmentManager, AuditorInfoDialog.TAG)
//        }
//        binding.selectAuditor.setOnClickListener {
//            val selectAuditorDialog = SelectAuditorDialog(childFragmentManager, requireActivity(), viewModel)
//            selectAuditorDialog.show(childFragmentManager, SelectAuditorDialog.TAG)
//        }

    }

    fun setFocusColorForAllEditText() {
        setFocusBackgroundForTextField(binding.auditNameET, requireActivity())
        setFocusBackgroundForTextField(binding.auditorET, requireActivity())
        setFocusBackgroundForTextField(binding.siteET, requireActivity())
    }

    fun getSelectedRuleset(): RulesetModel? {
        if (binding.referentialSpinner.selectedItem != null)
            return binding.referentialSpinner.selectedItem as RulesetModel
        return null
    }

    fun validateAudit(): Boolean {
        val ruleset = getSelectedRuleset()
        return !(binding.siteET.text.isEmpty() || binding.auditNameET.text!!.isEmpty() || binding.auditorET.text.isEmpty()) &&
                ruleset != null && ruleset.isLocallyAvailable
    }

    fun validateAuditWithErrorMessage(): Boolean {
        val ruleset = getSelectedRuleset()
        if (binding.auditNameET.text!!.trim().isEmpty()) {
            binding.auditorET.clearFocus()
            binding.siteET.clearFocus()

            binding.auditNameET.requestFocus()
            binding.auditNameET.contentDescription = getString(R.string.fill_field_error_title) + " " + getString(R.string.enter_audit_name)
            binding.auditNameET.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            ViewCompat.setBackground(
                    binding.auditNameET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )

            binding.fillAudNameTxt.visibility = View.VISIBLE
            binding.fillSiteTxt.visibility = View.GONE
            binding.fillAuditorTxt.visibility = View.GONE
//            showErrorDialogForTxt()
            return false
        } else if (binding.siteET.text.isEmpty()) {
            binding.auditNameET.contentDescription = ""
            binding.auditorET.contentDescription = ""

            binding.auditNameET.clearFocus()
            binding.auditorET.clearFocus()

            binding.siteET.requestFocus()
            binding.siteET.contentDescription = getString(R.string.fill_field_error_title) + " " + getString(R.string.enter_site_name)
            binding.siteET.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES

            ViewCompat.setBackground(
                    binding.siteET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )

            binding.fillAudNameTxt.visibility = View.GONE
            binding.fillSiteTxt.visibility = View.VISIBLE
            binding.fillAuditorTxt.visibility = View.GONE
//            showErrorDialogForTxt()
            return false
        } else if (binding.auditorET.text.isEmpty()) {
            binding.auditNameET.contentDescription = ""
            binding.siteET.contentDescription = ""

            binding.auditNameET.clearFocus()
            binding.siteET.clearFocus()

            binding.auditorET.requestFocus()
            binding.auditorET.contentDescription = getString(R.string.fill_field_error_title) + " " + getString(R.string.enter_auditor_name)
            binding.auditorET.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES

            ViewCompat.setBackground(
                    binding.auditorET,
                    ResourcesCompat.getDrawable(resources, R.drawable.edit_text_bg_error, null)
            )

            binding.fillAudNameTxt.visibility = View.GONE
            binding.fillSiteTxt.visibility = View.GONE
            binding.fillAuditorTxt.visibility = View.VISIBLE
//            showErrorDialogForTxt()
            return false
        } else if (ruleset == null || !ruleset.isLocallyAvailable) {
            binding.auditNameET.clearFocus()
            binding.auditorET.clearFocus()
            binding.siteET.clearFocus()
            binding.referentialSpinner.requestFocus()
            binding.referentialSpinner.contentDescription = binding.referentialSpinner.contentDescription.toString() + " " + getString(R.string.referential_warring)
            binding.referentialSpinner.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
            binding.referentialSpinner.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES

//            showErrorDialogForRuleSet()
            return false
        }
        binding.fillAudNameTxt.visibility = View.GONE
        binding.fillSiteTxt.visibility = View.GONE
        binding.fillAuditorTxt.visibility = View.GONE
        return true
    }

    override fun onPause() {
//        viewModel.clearAuditName()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearAudit()
        super.onDestroy()
    }
}