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

package com.orange.ocara.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.ChangeOnboardingStepTask.ChangeOnboardingStepResponse;
import com.orange.ocara.business.interactor.SkipOnboardingTask.SkipOnboardingResponse;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.business.model.OnboardingItemModel;
import com.orange.ocara.ui.TutorialDisplayUiConfig;
import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayUserActionsListener;
import com.orange.ocara.ui.contract.TutorialDisplayContract.TutorialDisplayView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.orange.ocara.business.interactor.CompleteOnboardingTask.CompleteOnboardingResponse;
import static com.orange.ocara.business.interactor.LoadOnboardingItemsTask.LoadOnboardingItemsResponse;
import static timber.log.Timber.d;

/** {@link FragmentActivity} that displays the tutorial of the app */
@EActivity(R.layout.fragment_layout_tutorial)
public class TutorialDisplayFragment extends FragmentActivity implements TutorialDisplayView {

    @ViewById(R.id.screen_viewpager)
    ViewPager screenPager;

    TutorialContentAdapter introViewPagerAdapter;

    @ViewById(R.id.tab_indicator)
    TabLayout tabIndicator;

    @ViewById(R.id.btn_next)
    Button btnNext;

    @ViewById(R.id.btn_get_started)
    Button btnGetStarted;

    @ViewById(R.id.btn_skip)
    Button btnSkip;

    @Bean(TutorialDisplayUiConfig.class)
    TutorialDisplayUiConfig uiConfig;

    /** a listener for the actions in this view */
    private TutorialDisplayUserActionsListener actionsListener;

    private Animation btnAnim;

    @AfterInject
    void setUp() {

        actionsListener = uiConfig.actionsListener();
        actionsListener.loadOnboarding(new LoadOnboardingItemsCallback(this));
    }

    /** hides the view */
    @Override
    public void finishView() {

        makeText(this.getApplicationContext(), R.string.tutorial_ended, LENGTH_SHORT)
                .show();
        super.onBackPressed();

    }

    /** hides the view */
    @Override
    public void cancelView() {

        makeText(this.getApplicationContext(), R.string.tutorial_cancelled, LENGTH_SHORT)
                .show();

        super.onBackPressed();
    }

    /**
     * initializes all the components of the view with some content
     *
     * @param items a bunch of data
     */
    @Override
    public void showItems(List<OnboardingItemModel> items) {

        // init views
        btnAnim = AnimationUtils.loadAnimation(TutorialDisplayFragment.this, R.anim.button_animation);

        // setting up viewpager
        introViewPagerAdapter = new TutorialContentAdapter(this, items);
        screenPager.setAdapter(introViewPagerAdapter);

        // setting up tablayout
        tabIndicator.setupWithViewPager(screenPager);

        // tab layout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                d("ActivityMessage=New tab selected;SelectedPosition=%d", tab.getPosition());
                actionsListener.notifyStepChanged(tab.getPosition(), new NotifyOnboardingStepChangedCallback(TutorialDisplayFragment.this));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing yet
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing yet
            }
        });

        // NEXT button click listener
        btnNext.setOnClickListener(v -> {

            d("ActivityMessage=Clicking on the NEXT button");
            actionsListener.openNextStep(new ChangeOnboardingStepCallback(TutorialDisplayFragment.this));
        });

        // GET STARTED button click listener
        btnGetStarted.setOnClickListener(v -> {
            d("ActivityMessage=Clicking on the GET STARTED button");
            actionsListener.completeOnboarding(new CompleteOnboardingCallback(TutorialDisplayFragment.this));
        });

        // SKIP button click listener
        btnSkip.setOnClickListener(v -> {
            d("ActivityMessage=Clicking on the SKIP button");
            actionsListener.skipOnboarding(new SkipOnboardingCallback(TutorialDisplayFragment.this));
        });
    }

    @Override
    public void showError(String message) {

        // do nothing yet
    }

    /**
     * updates the {@link ViewPager}
     *
     * @param position an index
     */
    @Override
    public void showStep(int position) {

        d("ActivityMessage=Showing new step;PreviousIndex=%d;NewIndex=%d", screenPager.getCurrentItem(), position);
        screenPager.setCurrentItem(position);
    }

    /**
     * hides the GET STARTED button, shows the tabs indicator and the NEXT button
     */
    @Override
    public void showNextButton() {

        btnSkip.setVisibility(View.VISIBLE);
        btnGetStarted.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * shows the GET STARTED button, hides the tabs indicator and hides the NEXT button
     */
    @Override
    public void showStartButton() {

        btnSkip.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);

        // setup animation on the GET STARTED button
        btnGetStarted.setAnimation(btnAnim);
    }

    /** an implementation of {@link PagerAdapter} that manages the content of the tutorial */
    static class TutorialContentAdapter extends PagerAdapter {

        private final Context mContext;

        private final List<OnboardingItemModel> mListScreen;

        /**
         * instantiate
         *
         * @param mContext    context of the adapter
         * @param mListScreen a bunch of items to handle
         */
        TutorialContentAdapter(Context mContext, List<OnboardingItemModel> mListScreen) {
            this.mContext = mContext;
            this.mListScreen = mListScreen;
        }

        @Override
        public int getCount() {
            return mListScreen.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutScreen = inflater.inflate(R.layout.layout_screen, null);

            ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
            TextView title = layoutScreen.findViewById(R.id.intro_title);
            TextView description = layoutScreen.findViewById(R.id.intro_description);

            title.setText(mListScreen.get(position).getTitle());
            description.setText(mListScreen.get(position).getDescription());
            imgSlide.setImageResource((mListScreen.get(position).getScreenImg()));

            container.addView(layoutScreen);

            return layoutScreen;
        }
    }

    /** a callback that deals with the content of the tutorial */
    @RequiredArgsConstructor
    static class LoadOnboardingItemsCallback implements UseCaseCallback<LoadOnboardingItemsResponse> {

        private final TutorialDisplayView view;

        @Override
        public void onComplete(LoadOnboardingItemsResponse response) {

            view.showItems(response.getItems());
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }

    /** a callback that notifies that the tutorial has been done */
    @RequiredArgsConstructor
    static class CompleteOnboardingCallback implements UseCaseCallback<CompleteOnboardingResponse> {

        private final TutorialDisplayView view;

        @Override
        public void onComplete(CompleteOnboardingResponse response) {

            view.finishView();
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }

    /** a callback that notifies that the tutorial has been interrupted */
    @RequiredArgsConstructor
    static class SkipOnboardingCallback implements UseCaseCallback<SkipOnboardingResponse> {

        private final TutorialDisplayView view;

        @Override
        public void onComplete(SkipOnboardingResponse response) {

            view.cancelView();
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }

    /** a callback that notifies about a change in the item to display */
    @RequiredArgsConstructor
    static class ChangeOnboardingStepCallback implements UseCaseCallback<ChangeOnboardingStepResponse> {

        private final TutorialDisplayView view;

        @Override
        public void onComplete(ChangeOnboardingStepResponse response) {

            if (response.isLastStep()) {
                view.showStartButton();
            } else {
                view.showNextButton();
            }
            view.showStep(response.getTargetPosition());
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }


    /** a callback that notifies about a change in the item to display */
    @RequiredArgsConstructor
    static class NotifyOnboardingStepChangedCallback implements UseCaseCallback<ChangeOnboardingStepResponse> {

        private final TutorialDisplayView view;

        @Override
        public void onComplete(ChangeOnboardingStepResponse response) {

            if (response.isLastStep()) {
                view.showStartButton();
            } else {
                view.showNextButton();
            }
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }
}
