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

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.helpers.OnBackPressedListener

abstract class OcaraFragment : Fragment(), OnBackPressedListener {
    override fun onBack(): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (menu.findItem(R.id.preferences) != null)
            menu.findItem(R.id.preferences) .isVisible = false

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> {
                navigateToHelp()
            }
            R.id.about -> {
                navigateToAbout()
            }
            R.id.terms_of_use->{
                navigateToTerms()
            }
            R.id.preferences->{
                navigateToPrefs()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun navigateToHelp() {
        val name: String = getClassNameForHelp()
        System.err.println("DEBUG __ name : " + name)
        val subjectBundle = Bundle()
        subjectBundle.putString("subject", name)
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.helpFragment, subjectBundle)
    }
     open fun getClassNameForHelp() : String {
        return this::class.java.name
    }
    fun navigateToAbout() {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.aboutFragment)
    }
    fun navigateToTerms() {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.termsOfUseContentFragment)
    }

    fun navigateToPrefs() {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.preferencesFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
        (activity as AppCompatActivity).supportActionBar?.setHomeActionContentDescription(getString(R.string.home_btn_content_desc));

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setHomeActionContentDescription(getString(R.string.home_btn_content_desc));

    }
    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }


    open fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}