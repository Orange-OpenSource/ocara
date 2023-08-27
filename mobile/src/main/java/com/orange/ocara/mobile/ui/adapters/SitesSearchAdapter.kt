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
import com.orange.ocara.domain.models.SiteModel
import com.orange.ocara.mobile.R

class SitesSearchAdapter (private val sites: ArrayList<SiteModel>) :
        BottomSheetSearchAdapter<SiteModel>() {

    override fun getName(position: Int): String {
        return data.get(position).name
    }
}