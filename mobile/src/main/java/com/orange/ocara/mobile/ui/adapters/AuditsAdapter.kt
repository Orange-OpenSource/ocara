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

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditForAuditListModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.convertLongToDateTime
import com.orange.ocara.mobile.ui.convertLongToDateTimeAuditList

class AuditsAdapter(
        data: ArrayList<AuditForAuditListModel>,
        private val context: Context,
        private val clickListener: ClickListeners
) :
        ItemRVAdapter<AuditForAuditListModel, AuditsAdapter.ViewHolder>(data) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val auditName: TextView = view.findViewById(R.id.auditName)
        val modificationDate: TextView = view.findViewById(R.id.modificationDate)
        val siteName: TextView = view.findViewById(R.id.siteName)
        val copied: TextView = view.findViewById(R.id.copied)
        val edit: ImageView = view.findViewById(R.id.edit)
        val auditContainer: ConstraintLayout = view.findViewById(R.id.audit_item_container)
    }

    fun addCopiedItem(audit: AuditForAuditListModel) {
        changeAllExistingAuditsToNotCopied()
        data.add(audit)
        notifyItemInserted(data.size - 1)
    }

    /*
        this function searches for a copied audit and it finds any it will make non copied so the
        "copied!" text won't appear next to them
     */
    private fun changeAllExistingAuditsToNotCopied() {
        data.forEachIndexed { i, audit ->
            if (audit.isCopied) {
                audit.isCopied = false
                notifyItemChanged(i)
            }
        }
    }

    override fun getLayout() = R.layout.audit_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audit = data[position]
        if (position == 0)
            holder.auditContainer.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES

        holder.auditName.text = "${audit.name} - V${audit.version}"
        holder.siteName.text = audit.site.name
        holder.modificationDate.text =
                context.getString(R.string.modification_date, convertLongToDateTimeAuditList(audit.date))
        holder.auditContainer.contentDescription = holder.auditName.text.toString() + " " + holder.siteName.text + " " +
                holder.modificationDate.text+ " "+ String.format(context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                String.format(context.getString(R.string.content_desc_item_list_pos) , position+1)
                addClickListeners(holder, audit)
        holder.icon.isFocusable = true
        if (!audit.isInProgress) {
            holder.icon.contentDescription = context.getString(R.string.content_desc_closed_audit)
            holder.icon.setImageResource(R.drawable.ic_audit_check)
//            holder.edit.setImageResource(R.drawable.ic_trash)
        } else {
            holder.icon.contentDescription = context.getString(R.string.content_desc_ong_audit)

        }
        if (audit.isCopied) {
            holder.copied.visibility = View.VISIBLE
        } else {
            holder.copied.visibility = View.INVISIBLE
        }
    }

    private fun addClickListeners(holder: ViewHolder, audit: AuditForAuditListModel) {
        holder.view.setOnClickListener {
            onClick?.invoke(audit)
        }
        holder.edit.setOnClickListener {
            if (audit.isInProgress) {
                showPopupMenu(it, holder, audit)
            } else {
//                clickListener.onDeleteClicked(audit.id)
                showClosedAuditPopupMenu(it, holder, audit)
            }
        }
    }

    private fun showPopupMenu(it: View, holder: ViewHolder, audit: AuditForAuditListModel) {
        val popup = PopupMenu(it.context, it)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.list_audit_edit_icon_menu, popup.menu)
        addMenuItemClickListeners(popup, audit)
        popup.show()
        holder.view.setBackgroundColor(Color.GRAY)
        popup.setOnDismissListener {
            holder.view.setBackgroundColor(Color.WHITE)
        }
    }


    private fun addMenuItemClickListeners(popup: PopupMenu, audit: AuditForAuditListModel) {
        popup.menu.findItem(R.id.edit).setOnMenuItemClickListener {
            clickListener.onEditClicked(audit)
            true
        }
        popup.menu.findItem(R.id.complete).setOnMenuItemClickListener {
            clickListener.onCompleteClicked(audit)
            true
        }
        popup.menu.findItem(R.id.copy).setOnMenuItemClickListener {
            clickListener.onCopyClicked(audit)
            true
        }
        popup.menu.findItem(R.id.delete).setOnMenuItemClickListener {
            clickListener.onDeleteClicked(audit)
            true
        }
    }

    private fun showClosedAuditPopupMenu(
            it: View,
            holder: ViewHolder,
            audit: AuditForAuditListModel
    ) {
        val popup = PopupMenu(it.context, it)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.list_closed_audit_edit_icon_menu, popup.menu)
        addClosedMenuItemClickListeners(popup, audit)
        popup.show()
        holder.view.setBackgroundColor(Color.GRAY)
        popup.setOnDismissListener {
            holder.view.setBackgroundColor(Color.WHITE)
        }
    }

    private fun addClosedMenuItemClickListeners(popup: PopupMenu, audit: AuditForAuditListModel) {

        popup.menu.findItem(R.id.copy).setOnMenuItemClickListener {
            clickListener.onCopyClicked(audit)
            true
        }
        popup.menu.findItem(R.id.delete).setOnMenuItemClickListener {
            clickListener.onDeleteClicked(audit)
            true
        }
    }

    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    interface ClickListeners {
        fun onEditClicked(auditId: AuditForAuditListModel)
        fun onDeleteClicked(auditId: AuditForAuditListModel)
        fun onCompleteClicked(auditId: AuditForAuditListModel)
        fun onCopyClicked(audit: AuditForAuditListModel)
    }
}