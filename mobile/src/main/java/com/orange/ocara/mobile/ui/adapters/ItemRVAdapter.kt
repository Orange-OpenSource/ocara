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
import androidx.recyclerview.widget.RecyclerView
import com.orange.ocara.mobile.R

abstract class ItemRVAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {
    var data = ArrayList<T>()

    constructor(data: ArrayList<T>) : this() {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
                .inflate(getLayout(), parent, false)
        return getViewHolder(view)
    }

    protected abstract fun getViewHolder(view: View) : VH
    protected abstract fun getLayout() : Int

    protected var onClick: ((T) -> Unit)? = null
    fun update(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClick: (T) -> Unit) {
        this.onClick = onClick
    }

    override fun getItemCount() = data.size

}