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
import androidx.navigation.fragment.navArgs
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.orange.ocara.data.help.mobile.HelpAssetMobilePathUiModel
import com.orange.ocara.data.help.mobile.HelpPageMobileUiModel
import com.orange.ocara.mobile.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class HelpFragment : OcaraFragment() {

    //    @ViewById(R.id.pdf_view)
    var pdfView: PDFView? = null

    //    @Extra
//    var subject = ""
    val args: HelpFragmentArgs by navArgs()

    private val defaultPageNumber = 0

    private val pathUiModel: HelpAssetMobilePathUiModel = HelpAssetMobilePathUiModel()

    private val contentUiModel: HelpPageMobileUiModel = HelpPageMobileUiModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_layout_help_display, container, false)
        pdfView = rootView.findViewById(R.id.pdf_view)
        //        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.default_options_menu, menu)
        menu.findItem(R.id.help).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        initView()
        super.onResume()
    }

    fun initView()
    {

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.help_screen_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.setLogo(null)

        /*
         * This tool has many options. See how to configure the {@link PDFView} here :
         * https://github.com/barteksc/AndroidPdfViewer
         */


        /*
         * This tool has many options. See how to configure the {@link PDFView} here :
         * https://github.com/barteksc/AndroidPdfViewer
         */pdfView!!.fromAsset(pathUiModel.get())
            .defaultPage(contentUiModel.getOrDefault(args.subject, defaultPageNumber))
            .swipeHorizontal(false)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()
    }

}

