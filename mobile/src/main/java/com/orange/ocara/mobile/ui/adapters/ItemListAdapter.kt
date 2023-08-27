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

import android.widget.BaseAdapter
import java.util.*

abstract class ItemListAdapter<T> : BaseAdapter() {
    protected val objects = ArrayList<T>()
    open fun update(objects: List<T>) {
        this.objects.clear()
        this.objects.addAll(objects)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): T {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getObjects(): List<T> {
        return objects
    }
}