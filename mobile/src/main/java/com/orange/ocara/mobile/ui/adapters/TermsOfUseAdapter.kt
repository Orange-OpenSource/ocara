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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.data.cache.database.NonTables.RuleAnswerAndLabel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.getIconFromAnswer
import com.orange.ocara.utils.enums.Answer

class TermsOfUseAdapter(data: ArrayList<String>,val onTermsItemClicked: (itemIdx: Int) -> Unit) :
        ItemRVAdapter<String, TermsOfUseAdapter.ViewHolder>(data) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.content_txt)
    }

    override fun getViewHolder(view: View) = ViewHolder(view)

    override fun getLayout() = R.layout.terms_list_item

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.label.text = data.get(position)
        holder.itemView.setOnClickListener {
            onTermsItemClicked(position)
        }
    }

}