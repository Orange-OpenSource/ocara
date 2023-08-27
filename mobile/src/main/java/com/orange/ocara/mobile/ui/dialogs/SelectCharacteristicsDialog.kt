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

package com.orange.ocara.mobile.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogSelectCharacteristicsBinding
import com.orange.ocara.mobile.setFullScreen
import com.orange.ocara.mobile.ui.adapters.CharacteristicsDialogAdapter
import com.orange.ocara.mobile.ui.viewmodel.AuditEquipmentWithCharacteristicsViewModel

class SelectCharacteristicsDialog(val viewModel: AuditEquipmentWithCharacteristicsViewModel) :
        OcaraDialog<DialogSelectCharacteristicsBinding>(R.layout.dialog_select_characteristics) {

    lateinit var characteristicsDialogAdapter: CharacteristicsDialogAdapter
    private val auditEquipment: AuditEquipmentModel = viewModel.getCopyOfAuditEquipment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setFullScreen()
    }
    private fun initRecyclerView() {
        characteristicsDialogAdapter = CharacteristicsDialogAdapter(requireContext(), auditEquipment, object :
                CharacteristicsDialogAdapter.ItemStateChanged {
            override fun onItemSelected(equipment: EquipmentModel) {
                addSubEquipmentToModel(equipment)
            }

            override fun onItemUnSelected(equipment: EquipmentModel) {
                removeSubEquipmentFromModel(equipment)
            }

        })
        binding.subObjectsList.adapter = characteristicsDialogAdapter
        binding.subObjectsList.layoutManager = LinearLayoutManager(requireContext())
    }
    fun addSubEquipmentToModel(equipmentModel: EquipmentModel){
        auditEquipment.children.add(AuditEquipmentModel.Builder()
                .setAudit(auditEquipment.audit)
                .setEquipment(equipmentModel)
                .setName(equipmentModel.name)
                .build())
    }
    fun removeSubEquipmentFromModel(equipmentModel: EquipmentModel){
        for(child in auditEquipment.children){
            if(checkChildEqualsEquipment(child,equipmentModel)) {
                auditEquipment.children.remove(child)
                break
            }
        }
    }
    fun checkChildEqualsEquipment(child:AuditEquipmentModel,equipmentModel: EquipmentModel):Boolean{
        return child.equipment.reference.equals(equipmentModel.reference)
    }
    override fun initClickListeners() {
        binding.close.setOnClickListener { dismiss() }
        binding.cancel.setOnClickListener { dismiss() }
        binding.validate.setOnClickListener { updateCharacteristics() }
    }

    private fun updateCharacteristics() {
        viewModel.updateSubEquipments(auditEquipment)
        dismiss()
    }
    companion object {
        const val TAG = "SelectCharacteristicsDialog"
    }
}