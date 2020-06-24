/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

package com.orange.ocara.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.LoadRuleExplanationsTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.ExplanationModel;
import com.orange.ocara.tools.ListUtils;
import com.orange.ocara.ui.BrowseExplanationsUiConfig;
import com.orange.ocara.ui.model.ExplanationUiModel;
import com.orange.ocara.ui.view.CustomViewFlipper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.ui.contract.BrowseExplanationsContract.ExplanationDisplayUserActionsListener;
import static com.orange.ocara.ui.contract.BrowseExplanationsContract.ExplanationDisplayView;

/**
 * Activity that displays a carousel for explanations/illustrations
 */
@EActivity(R.layout.activity_illustrations)
public class BrowseExplanationsActivity extends BaseActivity implements ExplanationDisplayView {

    @ViewById(R.id.previous_button)
    ImageView leftButton;

    @ViewById(R.id.next_button)
    ImageView rightButton;

    @ViewById(R.id.view_flipper)
    CustomViewFlipper carousel;

    @Bean(BrowseExplanationsUiConfig.class)
    BrowseExplanationsUiConfig uiConfig;

    @Extra
    long ruleId;

    @Extra
    String iconName;

    TextView comment;

    ImageView photo;

    private List<ExplanationUiModel> data = ListUtils.newArrayList();

    private ExplanationDisplayUserActionsListener actionsListener;

    @AfterViews
    protected void afterView() {

        actionsListener = uiConfig.actionsListener();
        actionsListener.loadExplanations(ruleId, new UseCase.UseCaseCallback<LoadRuleExplanationsTask.LoadRuleExplanationsResponse>() {
            @Override
            public void onComplete(LoadRuleExplanationsTask.LoadRuleExplanationsResponse response) {
                if (response.isEmpty()) {

                } else {
                    BrowseExplanationsActivity.this.displayExplanations(response.getData());
                }
            }

            @Override
            public void onError(ErrorBundle errors) {

            }
        });
    }

    @Override
    public void displayExplanations(List<ExplanationModel> input) {

        if (iconName != null) {
            updateLogo(iconName);
        }

        ExplanationUiModel item;
        for (int i = 0; i < input.size(); i++) {
            item = new ExplanationUiModel(input.get(i));

            View view = ((LayoutInflater) this.getSystemService((Context.LAYOUT_INFLATER_SERVICE)))
                    .inflate(R.layout.illustration, null);
            carousel.addView(view);

            comment = view.findViewById(R.id.comment_picture);
            comment.setText(item.getComment());

            photo = view.findViewById(R.id.photo);
            if (item.isIllustrated()) {
                Picasso.with(this).load(item.getImage()).placeholder(R.color.dark_grey).into(photo);
            }

            this.data.add(item);
        }

        carousel.setDisplayedChild(0);

        leftButton.setOnClickListener(v -> BrowseExplanationsActivity.this.showPreviousElement());
        rightButton.setOnClickListener(v -> BrowseExplanationsActivity.this.showNextElement());
        showElement(0);
    }

    @Override
    public void displayNothing() {
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
    }

    @Override
    public void showPreviousElement() {
        carousel.showPrevious();
        showElement(currentIndex());
    }

    @Override
    public void showNextElement() {
        carousel.showNext();
        showElement(currentIndex());
    }

    @Override
    public void showElement(int index) {

        Timber.v("CurrentIndex= " + index);

        ExplanationUiModel current = data.get(index);
        setTitle(current.getLabel());

        leftButton.setEnabled(!reachedMaxLeft());
        rightButton.setEnabled(!reachedMaxRight());
    }

    @Override
    public boolean reachedMaxLeft() {
        return currentIndex() == 0;
    }

    @Override
    public boolean reachedMaxRight() {
        return currentIndex() == data.size() - 1;
    }

    private int currentIndex() {
        return carousel.indexOfChild(carousel.getCurrentView());
    }

}


