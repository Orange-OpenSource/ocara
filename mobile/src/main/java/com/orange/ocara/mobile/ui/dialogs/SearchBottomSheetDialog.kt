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

package com.orange.ocara.mobile.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.BottomDialogSearchBinding
import com.orange.ocara.mobile.ui.adapters.BottomSheetSearchAdapter
import com.orange.ocara.mobile.ui.extensionFunctions.afterTextChanged

abstract class SearchBottomSheetDialog<T>()
    : OcaraBottomSheetDialog<BottomDialogSearchBinding>(R.layout.bottom_dialog_search) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearch()
        initClickListeners()
        binding.objectsList.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.titleTextview.text = getTitle()
        binding.add.text = getButtonText()

    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.add.setOnClickListener {

            onAddClicked()
        }
//        binding.searchEt.setIconified(false)
//        binding.searchEt.setOnClickListener { view->binding.searchEt.setIconified(false) }
    }


    protected fun bindAdapter(adapter: BottomSheetSearchAdapter<T>) {
        binding.objectsList.adapter = adapter
    }

    private fun initSearch() {
        binding.searchEt.afterTextChanged { newText ->
            onSearchTextChange(newText)
        }
        binding.searchEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.closeSearch.visibility = View.VISIBLE
            }else{
                binding.closeSearch.visibility = View.GONE
            }
        }
        binding.closeSearch.setOnClickListener {v->
            binding.searchEt.setText("")
            hideKeyboard()
            //binding.searchEt.clearFocus()
        }
//        binding.searchEt.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                onSearchTextChange(newText)
//                return false
//            }
//
//        })
    }

    protected abstract fun initAdapter()
    protected abstract fun onAddClicked()
    protected abstract fun onSearchTextChange(newText: String?)
    protected abstract fun getTitle(): String

    protected abstract fun getButtonText(): String
}