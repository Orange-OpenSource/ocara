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
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentHomeBinding
import com.orange.ocara.mobile.ui.extensionFunctions.subscribeAndObserve
import com.orange.ocara.mobile.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : OcaraFragment(){
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.saveDefaultRuleset()
        initParagraph()
         initData()
        initButtonsClickListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initActionBar()
    }
    private fun initData() {
        viewModel.getNumberOfAudits().subscribeAndObserve {
            binding.allAuditsBtn.text = getString(R.string.all_audits_with_cnt,it)
        }
    }

    private fun initParagraph() {
        binding.knowMoreTitle.setOnClickListener {
            if (binding.parTv.visibility == View.VISIBLE) {
                binding.parTv.visibility = View.GONE
                binding.knowMoreIcon.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_arrow_drop_down))
                binding.knowMoreTitle.contentDescription = getString(R.string.want_to_know_more) + getString(R.string.want_to_know_more_not_exp)
            } else {
                binding.parTv.visibility = View.VISIBLE
                binding.knowMoreIcon.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_arrow_drop_up))
                binding.knowMoreTitle.contentDescription = getString(R.string.want_to_know_more) + getString(R.string.want_to_know_more_exp)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.view_referentials)
            NavHostFragment.findNavController(this).navigate(R.id.action_view_ref)
//        else if (item.itemId == R.id.terms_of_use)
//            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_termsOfUseContentFragment)
//        else if (item.itemId == R.id.help){
//            NavHostFragment.findNavController(this).navigate(
//                    HomeFragmentDirections
//                            .actionHelp("com.orange.ocara.ui.fragment.CreateAuditFragment" ))
//        }
        else if (item.itemId == R.id.about){
            NavHostFragment.findNavController(this).navigate(
                    HomeFragmentDirections
                            .actionAbout())
        }


        return super.onOptionsItemSelected(item)
    }

    private fun initButtonsClickListeners() {
        binding.newAuditBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_createAuditFragment)
        }
        binding.allAuditsBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_listAuditsFragment)
        }
    }

    private fun initActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setLogo(R.drawable.ic_logo_ocara_topbar)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.home_screen_title)

        setHasOptionsMenu(true)
    }
}