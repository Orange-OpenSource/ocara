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

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.adapters.ItemRVAdapter
import com.orange.ocara.mobile.ui.convertLongToTime
import java.util.*

class HomeAuditsAdapter(audits: ArrayList<AuditModel>,val onAuditClicked: (Int) -> Unit) :
        ItemRVAdapter<AuditModel, HomeAuditsAdapter.ViewHolder>(audits) {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val auditName: TextView = view.findViewById(R.id.auditNameTV)
        val icon: ImageView = view.findViewById(R.id.icon)
        val date: TextView = view.findViewById(R.id.auditDateTV)

    }

    override fun getLayout() = R.layout.home_audits_listview_item

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.setOnClickListener {
            onAuditClicked(data[position].id)
        }
        viewHolder.auditName.text = data[position].name
        viewHolder.date.text = convertLongToTime(data[position].date)
        viewHolder.icon.setImageResource(
                if (data[position].isTerminated)
                    R.drawable.audit_closed_icon
                else
                    R.drawable.ic_outline_description_24)

    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

}