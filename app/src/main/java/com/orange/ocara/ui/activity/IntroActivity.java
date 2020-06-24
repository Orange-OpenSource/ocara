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

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.orange.ocara.R;
import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.CheckTermsStatusTask.CheckTermsStatusResponse;
import com.orange.ocara.business.interactor.InitTask;
import com.orange.ocara.business.interactor.UseCase.UseCaseCallback;
import com.orange.ocara.ui.IntroUiConfig;
import com.orange.ocara.ui.contract.IntroContract.IntroUserActionsListener;
import com.orange.ocara.ui.contract.IntroContract.IntroView;
import com.orange.ocara.ui.fragment.TermsOfUseAcceptanceFragment_;
import com.orange.ocara.ui.fragment.TutorialDisplayFragment_;
import com.orange.ocara.ui.routing.DestinationController;
import com.orange.ocara.ui.routing.Navigation;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import lombok.RequiredArgsConstructor;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.orange.ocara.business.interactor.CheckOnboardingStatusTask.CheckOnboardingStatusResponse;
import static com.orange.ocara.business.interactor.CheckRemoteStatusTask.CheckRemoteStatusResponse;
import static timber.log.Timber.d;
import static timber.log.Timber.i;

/** Starting view of the app */
@EActivity
public class IntroActivity extends AppCompatActivity implements IntroView {

    public static final int SHOW_TERMS_REQUEST_CODE = 1;

    public static final int SHOW_TUTORIAL_REQUEST_CODE = 2;

    public static final int LEAVE_INTRO_REQUEST_CODE = 3;

    @Bean(DestinationController.class)
    Navigation navController;

    @Bean(IntroUiConfig.class)
    IntroUiConfig uiConfig;

    private IntroUserActionsListener actionsListener;

    @AfterInject
    void afterInject() {

        actionsListener = uiConfig.actionsListener();
        this.start();
    }

    @Override
    public void start() {

        this.actionsListener.checkRemote(new RemoteCallback(this));
        this.actionsListener.checkInit(new InitCallback(this));
    }

    @Override
    public void navigateToHome() {

        // we leave this activity, and try to open the main activity
        d("ActivityMessage=Opening main activity");
        navController.navigateToHome(IntroActivity.this, LEAVE_INTRO_REQUEST_CODE);
    }

    @Override
    public void showTerms() {

        d("ActivityMessage=Showing terms");
        TermsOfUseAcceptanceFragment_
                .intent(IntroActivity.this)
                .startForResult(SHOW_TERMS_REQUEST_CODE);
    }

    @OnActivityResult(SHOW_TERMS_REQUEST_CODE)
    void onTermsClosed(int resultCode) {

        d("ActivityMessage=Terms completed;ResultCode=%d", resultCode);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            this.attemptNextDialog(SHOW_TUTORIAL_REQUEST_CODE);
        }
    }

    @Override
    public void attemptNextDialog(int code) {
        if (code == SHOW_TERMS_REQUEST_CODE) {
            actionsListener.checkTerms(new CheckTermsStatusCallback(this));
        } else if (code == SHOW_TUTORIAL_REQUEST_CODE) {
            actionsListener.checkTutorial(new CheckTutorialStatusCallback(this));
        } else {
            this.navigateToHome();
        }
    }

    @Override
    public void showTutorial() {

        d("ActivityMessage=Showing tutorial");
        TutorialDisplayFragment_
                .intent(IntroActivity.this)
                .startForResult(SHOW_TUTORIAL_REQUEST_CODE);
    }

    @OnActivityResult(SHOW_TUTORIAL_REQUEST_CODE)
    void onTutorialClosed(int resultCode) {

        d("ActivityMessage=Tutorial completed;ResultCode=%d", resultCode);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            this.attemptNextDialog(LEAVE_INTRO_REQUEST_CODE);
        }
    }

    @Override
    public void showError(String message) {

        // do nothing yet
        i("ErrorMessage=%s;", message);
    }

    @RequiredArgsConstructor
    static class CheckTermsStatusCallback implements UseCaseCallback<CheckTermsStatusResponse> {

        private final IntroView view;

        @Override
        public void onComplete(CheckTermsStatusResponse response) {

            if (response.isAccepted()) {
                // the terms have been accepted. We can go to the next step.
                view.attemptNextDialog(SHOW_TUTORIAL_REQUEST_CODE);
            } else {
                // the terms have never been findAll, or they were previously declined. So we show them again.
                view.showTerms();
            }
        }

        @Override
        public void onError(ErrorBundle errors) {

            view.showError(errors.getMessage());
        }
    }

    /**
     * Callback that manages the response to the loading of a tutorial
     */
    @RequiredArgsConstructor
    static class CheckTutorialStatusCallback implements UseCaseCallback<CheckOnboardingStatusResponse> {

        private final IntroView view;

        @Override
        public void onComplete(CheckOnboardingStatusResponse response) {

            if(response.isOngoing()) {
                // the tutorial has not been completed yet
                view.showTutorial();
            } else {
                // the tutorial is completed
                view.attemptNextDialog(LEAVE_INTRO_REQUEST_CODE);
            }
        }

        @Override
        public void onError(ErrorBundle errors) {

            // if the process fails, we still should proceed to the next step
//            view.showError(errors.getMessage());
            view.attemptNextDialog(LEAVE_INTRO_REQUEST_CODE);
        }
    }

    /**
     * Callback that manages the response to the initialization of the app
     */
    @RequiredArgsConstructor
    static class InitCallback implements UseCaseCallback<InitTask.InitResponse> {

        private final IntroView view;

        @Override
        public void onComplete(InitTask.InitResponse initResponse) {
            view.attemptNextDialog(SHOW_TERMS_REQUEST_CODE);
        }

        @Override
        public void onError(ErrorBundle errors) {

            // if the process fails, we still should proceed to the next step
//            view.showError(errors.getMessage());
            view.attemptNextDialog(SHOW_TERMS_REQUEST_CODE);
        }
    }

    @RequiredArgsConstructor
    static class RemoteCallback implements UseCaseCallback<CheckRemoteStatusResponse> {

        private final Context context;

        @Override
        public void onComplete(CheckRemoteStatusResponse response) {

            if (!response.isSuccess()) {
                makeText(context, R.string.running_offline, LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(ErrorBundle errors) {

            // do nothing yet...
        }
    }
}
