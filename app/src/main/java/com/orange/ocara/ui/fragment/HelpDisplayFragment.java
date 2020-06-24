/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL-2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.fragment;

import androidx.fragment.app.FragmentActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.orange.ocara.R;
import com.orange.ocara.ui.model.HelpAssetPathUiModel;
import com.orange.ocara.ui.model.HelpPageUiModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


/**
 * a view that displays the help document of the app
 */
@EActivity(R.layout.fragment_layout_help_display)
public class HelpDisplayFragment extends FragmentActivity {

    @ViewById(R.id.pdf_view)
    PDFView pdfView;

    @Extra
    String subject = "com.orange.ocara.ui.activity.HelpDisplayFragment_"; // key for the Help page

    private static final int defaultPageNumber = 0;

    private final HelpAssetPathUiModel pathUiModel = new HelpAssetPathUiModel();

    private final HelpPageUiModel contentUiModel = new HelpPageUiModel();

    @AfterViews
    public void initView() {

        /*
         * This tool has many options. See how to configure the {@link PDFView} here :
         * https://github.com/barteksc/AndroidPdfViewer
         */
        pdfView
                .fromAsset(pathUiModel.get())
                .defaultPage(contentUiModel.getOrDefault(subject, defaultPageNumber))
                .swipeHorizontal(false)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }
}
