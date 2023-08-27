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
import android.view.accessibility.AccessibilityEvent
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentTermsOfUseItemContentBinding
import com.orange.ocara.mobile.databinding.FragmentTermsOfUseItemContentBindingImpl

class TermsOfUseItemContentFragment : OcaraFragment() {
    lateinit var binding: FragmentTermsOfUseItemContentBinding
    private val args: TermsOfUseItemContentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_terms_of_use_item_content, container, false
        )
        binding.termsItemTitle.text = resources.getStringArray(R.array.terms_title_texts)[args.itemIdx]
        binding.termsItemContent.text = resources.getStringArray(R.array.terms_content_texts)[args.itemIdx]

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.termsItemTitle.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);

    }

}