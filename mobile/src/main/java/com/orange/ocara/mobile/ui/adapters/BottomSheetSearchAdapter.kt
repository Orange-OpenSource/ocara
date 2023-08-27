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

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.domain.models.AuditorModel
import com.orange.ocara.mobile.R

abstract class BottomSheetSearchAdapter<T> :
        ItemRVAdapter<T,BottomSheetSearchAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val auditor: TextView = view.findViewById(R.id.item_name)
    }

    override fun getLayout() = R.layout.select_auditor_site_listview_item

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.setOnClickListener {
            onClick?.invoke(data[position])
        }
        viewHolder.auditor.text = getName(position)
    }


    override fun getViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    protected abstract fun getName(position : Int) : String

}