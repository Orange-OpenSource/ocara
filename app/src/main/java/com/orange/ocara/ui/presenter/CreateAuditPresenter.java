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

package com.orange.ocara.ui.presenter;

import com.orange.ocara.business.binding.ErrorBundle;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsRequest;
import com.orange.ocara.business.interactor.RetrieveRulesetsTask.RetrieveRulesetsResponse;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetRequest;
import com.orange.ocara.business.interactor.SavePreferredRulesetTask.SavePreferredRulesetResponse;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.ui.activity.CreateAuditActivity;
import com.orange.ocara.ui.binding.AuditValidator;
import com.orange.ocara.ui.binding.AuthorNamePatternValidator;
import com.orange.ocara.ui.binding.BindingResult;
import com.orange.ocara.ui.binding.RulesetStateValidator;
import com.orange.ocara.ui.binding.SiteExistsValidator;
import com.orange.ocara.ui.binding.Validator;
import com.orange.ocara.ui.contract.CreateAuditContract;
import com.orange.ocara.ui.model.AuditFormUiModel;

import org.androidannotations.annotations.Background;

import java.util.List;

import lombok.RequiredArgsConstructor;
import timber.log.Timber;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * The presenter for the creation of audits
 *
 * Implementation of {@link CreateAuditContract.CreateAuditUserActionsListener}
 *
 * It is related to a view named {@link com.orange.ocara.ui.activity.CreateAuditActivity}, which
 * implements the other part of the contract : {@link CreateAuditContract.CreateAuditView}
 */
@RequiredArgsConstructor
public class CreateAuditPresenter implements CreateAuditContract.CreateAuditUserActionsListener {
    /**
     * the related view
     *
     * Here, we should only refer to {@link CreateAuditContract.CreateAuditView}, but for now, we
     * still need some functions that are in the activity, such as CreateAuditActivity#validateFields()
     */
    private final CreateAuditActivity view;

    /**
     * a {@link UseCase} for the retrieval of a {@link List} of {@link Ruleset}s
     */
    private final UseCase<RetrieveRulesetsRequest, RetrieveRulesetsResponse> mRetrieveRulesetsTask;

    /**
     * a {@link UseCase} for saving a {@link Ruleset} preference
     */
    private final UseCase<SavePreferredRulesetRequest, SavePreferredRulesetResponse> mSavePreferredRulesetTask;

    /**
     * a bunch of {@link AuditValidator}s for the validation of an {@link AuditFormUiModel}
     */
    private final List<Validator<AuditFormUiModel>> auditValidators = newArrayList(new AuthorNamePatternValidator(), new RulesetStateValidator(), new SiteExistsValidator());

    @Override
    public void loadRulesets() {
        RetrieveRulesetsRequest request = new RetrieveRulesetsRequest();
        mRetrieveRulesetsTask.executeUseCase(request, new UseCase.UseCaseCallback<RetrieveRulesetsResponse>() {
            @Override
            public void onComplete(RetrieveRulesetsResponse response) {
                Timber.d("ActivityMessage=Rulesets retrieval is successful;RulesetsCount=%d", response.getCount());
                view.showRulesetList(response.getItems());
                view.enableRulesInfo();
                view.hideProgressDialog();
            }

            @Background
            @Override
            public void onError(ErrorBundle errors) {
                Timber.d("ActivityMessage=Rulesets retrieval failed;ErrorMessage=%s", errors.getMessage());
                view.hideProgressDialog();
                view.showDownloadError();
            }
        });
    }

    @Override
    public void savePreferredRuleset(RulesetModel ruleset) {
        mSavePreferredRulesetTask.executeUseCase(
                new SavePreferredRulesetRequest(ruleset),
                new UseCase.UseCaseCallback<SavePreferredRulesetResponse>() {

                    @Override
                    public void onComplete(SavePreferredRulesetResponse responseValue) {
                        Timber.d("Item saved as preference");
                        view.hideProgressDialog();
                        view.validateFields();
                    }

                    @Override
                    public void onError(ErrorBundle errors) {
                        Timber.e("Item could not be saved as preference");
                        view.hideProgressDialog();
                    }
                });
    }

    @Override
    public void validateAudit(AuditFormUiModel audit) {
        BindingResult errors = new BindingResult();
        for (Validator validator : auditValidators) {
            validator.validate(audit, errors);
        }

        if (errors.hasErrors()) {
            view.disableSaveButton();
        } else {
            view.enableSaveButton();
        }
    }
}
