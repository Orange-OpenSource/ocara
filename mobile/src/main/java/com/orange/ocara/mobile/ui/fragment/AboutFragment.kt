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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentAboutNewdesignBinding
import com.orange.ocara.mobile.ui.managers.ShowAndCollapseViewWithAnimationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class AboutFragment : OcaraFragment() {

    private lateinit var binding: FragmentAboutNewdesignBinding
    private val arrayListOfExpandableViews = arrayListOf<View>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_about_newdesign, container, false)

        initViews()
        setupExpandAndCollapseBehaviour()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        menu.findItem(R.id.help).isVisible = false
        menu.findItem(R.id.about).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun initViews(){
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.about)
        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(null)
//        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(R.drawable.ic_logo_ocara_topbar)

        arrayListOfExpandableViews.add(binding.aboutItem1Content)
        arrayListOfExpandableViews.add(binding.aboutItem2Content)
        arrayListOfExpandableViews.add(binding.aboutItem3Content)
        arrayListOfExpandableViews.add(binding.aboutItem4Content)
        arrayListOfExpandableViews.add(binding.aboutItem5Content)

    }


    private fun setupExpandAndCollapseBehaviour() {
        ShowAndCollapseViewWithAnimationManager( view = binding.aboutItem1Content, controller = binding.aboutItem1Layout, arrowIcon = binding.aboutItem1DropdownIcon, false)
        ShowAndCollapseViewWithAnimationManager(view = binding.aboutItem2Content, controller = binding.aboutItem2Layout, arrowIcon = binding.aboutItem2DropdownIcon, isShown = false)
        ShowAndCollapseViewWithAnimationManager(view = binding.aboutItem3Content, controller = binding.aboutItem3Layout, arrowIcon = binding.aboutItem3DropdownIcon, isShown = false)
        ShowAndCollapseViewWithAnimationManager(view = binding.aboutItem4Content, controller = binding.aboutItem4Layout, arrowIcon = binding.aboutItem4DropdownIcon, isShown = false)
        ShowAndCollapseViewWithAnimationManager(view = binding.aboutItem5Content, controller = binding.aboutItem5Layout, arrowIcon = binding.aboutItem5DropdownIcon, isShown = false)


    }

    internal class onTextClickListener constructor(private val viewsToToggleVisibility: ArrayList<View>) : View.OnClickListener {
//        var viewsToToggleVisibility: Array<View>
        override fun onClick(v: View) {
            if (viewsToToggleVisibility[0].visibility == View.VISIBLE) {
                for (viewToToggleVisibility in viewsToToggleVisibility) {
                    viewToToggleVisibility.visibility = View.GONE
                }
                (v as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_small, 0)
            } else {
                for (viewToToggleVisibility in viewsToToggleVisibility) {
                    viewToToggleVisibility.visibility = View.VISIBLE
                }
                (v as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down_small, 0)
            }
        }


    }


}