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
import android.os.Bundle
import android.view.*
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.orange.ocara.data.cache.database.NonTables.AuditEquipmentForReport
import com.orange.ocara.domain.models.AuditInfoForReportModel
import com.orange.ocara.domain.models.ProfileAnswersUIModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentReportBinding
import com.orange.ocara.mobile.ui.adapters.ChartChoiceAdapterRefactored
import com.orange.ocara.mobile.ui.adapters.ProfilesIconAdapter
import com.orange.ocara.mobile.ui.adapters.ReportEquipmentsAdapter
import com.orange.ocara.mobile.ui.dialogs.DeleteRulesetDialog
import com.orange.ocara.mobile.ui.dialogs.GenericConfirmationDialog
import com.orange.ocara.mobile.ui.dialogs.ReportOpenFailDialog
import com.orange.ocara.mobile.ui.dialogs.SignReportDialog
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.managers.ShowAndCollapseViewWithAnimationManager
import com.orange.ocara.mobile.ui.showProgressDialog
import com.orange.ocara.mobile.ui.viewmodel.DOCFormat
import com.orange.ocara.mobile.ui.viewmodel.ReportViewModel
import com.orange.ocara.mobile.workers.DocExportWorker
import com.orange.ocara.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : OcaraFragment() {
    lateinit var binding: FragmentReportBinding
    lateinit var viewModel: ReportViewModel

    lateinit var equipmentsAdapter: ReportEquipmentsAdapter
    lateinit var resumeTable: TableLayout
    lateinit var resumeTitle: TableRow
    var mExportingProgressDialog: ProgressDialog? = null

    @Inject
    lateinit var profilesIconAdapter: ProfilesIconAdapter

    @Inject
    lateinit var chartChoiceAdapterRefactored: ChartChoiceAdapterRefactored

    lateinit var showAuditInfo: ShowAndCollapseViewWithAnimationManager
    lateinit var showEquipments: ShowAndCollapseViewWithAnimationManager
    lateinit var showPieChart: ShowAndCollapseViewWithAnimationManager
    lateinit var showTable: ShowAndCollapseViewWithAnimationManager

//    lateinit var reportTableManager: ReportTableManager
//    lateinit var pieChartManager: PieChartManager

    lateinit var workManager: WorkManager

    private val args: ReportFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_report, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)

        loadAuditInfo()
        initButtonsListeners()
        return binding.root
    }

    private fun initButtonsListeners() {

        binding.infoResultsBtn.contentDescription = String.format(getString(R.string.content_desc_item_list_size), 4) +" "+ String.format(getString(R.string.content_desc_item_list_pos) , 1)+ " " + getString(R.string.results)
        binding.viewPathsBtn.contentDescription = String.format(getString(R.string.content_desc_item_list_size), 4) +" "+ String.format(getString(R.string.content_desc_item_list_pos) , 2)+ " " + getString(R.string.summary)
        binding.infoLockBtn.contentDescription = String.format(getString(R.string.content_desc_item_list_size), 4) +" "+ String.format(getString(R.string.content_desc_item_list_pos) , 3)+ " " + getString(R.string.lock_audit)
        binding.infoExportBtn.contentDescription = String.format(getString(R.string.content_desc_item_list_size), 4) +" "+ String.format(getString(R.string.content_desc_item_list_pos) , 4)+ " " + getString(R.string.action_export_audit_docx)

        binding.infoResultsBtn.setOnClickListener { navigateToReportResults() }
        binding.viewPathsBtn.setOnClickListener {
//            Toast.makeText(requireContext(),"ViewPath",Toast.LENGTH_LONG).show()
            navigateToScores()
        }
        binding.infoLockBtn.setOnClickListener {
//            onLockAudit()
            NavHostFragment.findNavController(this)
                    .navigate(ReportFragmentDirections.actionReportFragmentToSignFragment(args.auditId))
        }
        binding.infoExportBtn.setOnClickListener { onReportExporting() }
//        binding.signBtn.setOnClickListener {
//            signDialog()
//        }
    }

    private fun signDialog() {
        val sign = SignReportDialog()
        sign.show(parentFragmentManager, DeleteRulesetDialog.TAG)
    }

    private fun onReportExporting() {
//        val chooseDocFormatDialog = ChooseDocFormatDialog(viewModel)
//        chooseDocFormatDialog.show(childFragmentManager, "SelectDocFormatDialog")
        mExportingProgressDialog =
                showProgressDialog(requireContext(), getString(R.string.exporting))
        initWorkManager()
        viewModel.onDocumentFormatSelected(DOCFormat.WORD)


    }

    private fun onLockAudit() {
        val confirmDialog = GenericConfirmationDialog(
                title = getString(R.string.lock_audit),
                msg = getString(R.string.lock_audit_dialog_content),
                confirmString = getString(R.string.confirm),
                cancelString = getString(R.string.cancel),
                onConfirmed = this::performLock, {}
        )
        confirmDialog.show(childFragmentManager, "ConfirmLockDialog")
    }

    private fun performLock() {
        viewModel.lockAudit(args.auditId).subscribeAndObserve {
            NavHostFragment.findNavController(this)
                    .navigate(ReportFragmentDirections.actionReportFragmentToListAudits())
        }
    }

//    private fun navigateToReportSummary() {
//        NavHostFragment.findNavController(this)
//                .navigate(ReportFragmentDirections.actionReportFragmentToReportSummaryFragment())
//    }

    private fun navigateToReportResults() {
        NavHostFragment.findNavController(this)
                .navigate(ReportFragmentDirections.actionReportFragmentToReportChartFragment(args.auditId))
    }


    private fun initWorkManager() {

        workManager = WorkManager.getInstance(requireContext().applicationContext)
        viewModel.prepareAuditModelForExporting(args.auditId)

        onObserveExportingFlag()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.report_export_item -> {
//
//                //  onDemoWorker(workManager)
//                onReportExporting()
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

//    private fun initTableViews() {
//        resumeTable = binding.resultResumeTable
//        resumeTitle = binding.tableRow
//    }

//    private fun initSelectedProfilesAdapter() {
//        binding.selectedProfilesList.adapter = profilesIconAdapter
//        binding.selectedProfilesList.layoutManager = LinearLayoutManager(requireContext(),
//                LinearLayoutManager.HORIZONTAL, false)
//    }

//    private fun addObservers() {
//        viewModel.selectedProfilesLiveData.observe(viewLifecycleOwner) {
//            setAdapterData(it)
//            pieChartManager.updatePieChart(it)
//            reportTableManager.updateScoresTable(it)
//        }
//    }

    private fun setAdapterData(it: ArrayList<ProfileAnswersUIModel>?) {
        val data = ArrayList<String>()
        it!!.forEach { item ->
            data.add(item.profileTypeModel.icon)
        }
        profilesIconAdapter.data = data
        profilesIconAdapter.notifyDataSetChanged()
    }

//    private fun initClickListeners() {
//        binding.selectProfiles.setOnClickListener {
//            val selectProfilesDialog = SelectProfilesDialog(viewModel)
//            selectProfilesDialog.show(childFragmentManager, "SelectProfilesDialog")
//        }
//    }

    private fun loadAuditInfo() {
        viewModel.loadAuditInfo(args.auditId)
        viewModel.auditInfoLiveData.observe(viewLifecycleOwner, {

            setAuditInfo(it)
        })


//        viewModel.getEquipments(args.auditId).subscribeAndObserve {
//        //    setEquipmentsForReport(it)
//        }
        // viewModel.getAuditScores(args.auditId)


    }

//    private fun setEquipmentsForReport(equipments: List<AuditEquipmentForReport>) {
//        equipmentsAdapter = ReportEquipmentsAdapter(equipments as ArrayList<AuditEquipmentForReport>)
//        equipmentsAdapter.setOnClickListener {
//            navigateToReportEquipments(it)
//        }
//        binding.equipmentsList.adapter = equipmentsAdapter
//        binding.equipmentsList.layoutManager = LinearLayoutManager(requireContext())
//    }

    private fun navigateToScores() {
//        NavHostFragment.findNavController(this)
//            .navigate(ReportFragmentDirections.actionReportFragmentToReportSummaryFragment(args.auditId))

        NavHostFragment.findNavController(this)
                .navigate(ReportFragmentDirections.actionReportFragmentToReportViewRouteFragment(args.auditId, binding.infoLockBtn.visibility == View.VISIBLE))
    }

//    private fun navigateToReportEquipments(it: AuditEquipmentForReport) {
//        NavHostFragment.findNavController(this)
//            .navigate(ReportFragmentDirections.actionReportFragmentToReportEquipmentsFragment(it.id))
//    }

    private fun setAuditInfo(audit: AuditInfoForReportModel) {
        if (!audit.isInProgress) {
            binding.infoLockBtn.visibility = View.GONE
        } else {
            binding.infoLockBtn.visibility = View.VISIBLE
        }
        binding.infoAuditName.text = audit.name
        binding.infoRefType.text = audit.rulesetType
        binding.infoAuditor.text = audit.auditorName
        binding.infoDate.text = audit.date
        binding.infoDate.contentDescription = audit.fullDate
        binding.infoSite.text = audit.siteName
        var lvl: String = audit.level.name.toLowerCase()
        lvl = lvl.replaceFirst(lvl[0], lvl[0].toUpperCase())
        binding.infoExpertise.text = lvl
    }


    /*
    * Report Export
    * */
    private fun onObserveExportingFlag() {
        viewModel.initWorkManagerForExportingFlag.observe(viewLifecycleOwner, {

            if (it) {
                if (workManager != null) onEnqueueExportWorker(workManager)
            }
        })
    }

    private fun onEnqueueExportWorker(workManager: WorkManager) {

        val workRequest = OneTimeWorkRequestBuilder<DocExportWorker>()
                .build()
        workManager.enqueueUniqueWork("ReportDocxExport", ExistingWorkPolicy.KEEP, workRequest)

        onExportWorkerResult()
    }

    private fun onExportWorkerResult() {
        workManager.getWorkInfosForUniqueWorkLiveData("ReportDocxExport")
                .observe(viewLifecycleOwner) { workInfoList ->

                    workInfoList.forEach {
                        val data = it.outputData.getString(DocExportWorker.EXPORT_FILE_PATH)


                        if (it.state == WorkInfo.State.SUCCEEDED) {

                            Timber.d("Data : $data")

                            data?.let { path -> onExportSucceedAction(path) }
                        } else if (it.state == WorkInfo.State.FAILED) {

                            Timber.d("Data : $data")
                            onExportFailedAction()
                        }
                    }
                }

    }

    private fun onExportSucceedAction(path: String) {

//        Toast.makeText(context, "file path : $path", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        if (!FileUtils.openFile(requireContext(), path)) {
            val errorDialog = ReportOpenFailDialog(path)
            errorDialog.show(childFragmentManager, ReportOpenFailDialog.TAG)

//            Toast.makeText(
//                requireContext(),
//                "Sorry cannot open file, please install doc file opening app",
//                Toast.LENGTH_LONG
//            ).show()
        }
    }

    private fun onExportFailedAction() {

//        Toast.makeText(context, "file path : $path", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        Toast.makeText(requireContext(), getString(R.string.export_fail), Toast.LENGTH_LONG).show()

    }

    fun hideProgressDialog() {
        if (mExportingProgressDialog != null) {
            mExportingProgressDialog?.dismiss()
        }
    }


}