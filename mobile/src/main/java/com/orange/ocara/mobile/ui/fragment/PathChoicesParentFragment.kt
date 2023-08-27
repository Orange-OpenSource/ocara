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

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.domain.models.AuditModel
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentPathChoicesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class PathChoicesParentFragment : OcaraFragment() {
    lateinit var binding: FragmentPathChoicesBinding
    lateinit var menu: Menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_path_choices, container, false)
        setHasOptionsMenu(true)
        initActionBar()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initActionBar()

    }
    private fun initActionBar() {
//        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.path_choices)
        (activity as AppCompatActivity).supportActionBar?.setTitle(getTitle())
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(getOptionsMenu(), menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.terms_of_use -> {
                navigateToTermsOfUse()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    abstract fun getOptionsMenu(): Int
    abstract fun handleEquipmentClick(id: Int)
    abstract fun navigateToTermsOfUse()
    abstract fun getTitle(): Int

}