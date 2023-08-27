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

package com.orange.ocara.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditForAuditListModel
import com.orange.ocara.mobile.R

class AuditsCopyModeAdapter(data: ArrayList<AuditForAuditListModel>) :
        ItemRVAdapter<AuditForAuditListModel, AuditsCopyModeAdapter.ViewHolder>(data) {

    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val auditName: TextView = view.findViewById(R.id.auditName)
        val siteName: TextView = view.findViewById(R.id.siteName)
        val rulesetName: TextView = view.findViewById(R.id.rulesetName)
    }

    override fun getLayout() = R.layout.audit_copy_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audit = data[position]
        holder.auditName.text = "${audit.name} (V${audit.version})"
        holder.siteName.text = "${audit.site.name}"
        holder.rulesetName.text = "${audit.ruleset.reference} (${audit.ruleset.version})"
        holder.view.setOnClickListener {
            onClick?.invoke(audit)
        }
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }
}