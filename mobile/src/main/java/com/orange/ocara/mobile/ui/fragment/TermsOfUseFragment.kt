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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentTermsOfUseBinding
import com.orange.ocara.mobile.ui.adapters.TermsOfUseAdapter

class TermsOfUseFragment : OcaraFragment() {
    lateinit var binding: FragmentTermsOfUseBinding
    lateinit var termsAdapter: TermsOfUseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_terms_of_use, container, false
        )
        termsAdapter = TermsOfUseAdapter(
            resources.getStringArray(R.array.terms_title_texts).toCollection(ArrayList()),
            this::onTermsItemClicked
        )
        binding.termsItems.adapter = termsAdapter
        binding.termsItems.layoutManager = LinearLayoutManager(requireContext())
        binding.termsItems.addItemDecoration(
            DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL)
        )

        return binding.root
    }

    private fun onTermsItemClicked(idx: Int) {
        NavHostFragment.findNavController(this)
            .navigate(TermsOfUseFragmentDirections.actionTermsOfUseToTermsItemFragment(idx))
    }

}