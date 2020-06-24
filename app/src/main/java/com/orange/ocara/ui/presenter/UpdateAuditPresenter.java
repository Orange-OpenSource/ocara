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

import com.orange.ocara.business.model.AuditModel;
import com.orange.ocara.business.model.RulesetModel;
import com.orange.ocara.business.model.UserModel;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.data.cache.db.ModelManager;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.data.cache.model.AuditorEntity;
import com.orange.ocara.data.net.model.Ruleset;
import com.orange.ocara.data.net.model.RulesetEntity;
import com.orange.ocara.ui.binding.AuditNameDoesNotExistValidator;
import com.orange.ocara.ui.binding.AuditValidator;
import com.orange.ocara.ui.binding.AuthorNamePatternValidator;
import com.orange.ocara.ui.binding.BindingResult;
import com.orange.ocara.ui.binding.UniqueReviewValidator;
import com.orange.ocara.ui.binding.UserNamePatternValidator;
import com.orange.ocara.ui.binding.Validator;
import com.orange.ocara.ui.contract.UpdateAuditContract;
import com.orange.ocara.ui.model.AuditFormUiModel;
import com.orange.ocara.ui.model.AuditUiModel;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static com.orange.ocara.tools.ListUtils.newArrayList;
import static timber.log.Timber.d;

/**
 * The presenter for the updating of audits
 *
 * Implementation of {@link UpdateAuditContract.UpdateAuditUserActionsListener}
 *
 * It is related to a view named {@link com.orange.ocara.ui.activity.UpdateAuditActivity}, which
 * implements the other part of the contract : {@link UpdateAuditContract.UpdateAuditView}
 */
@RequiredArgsConstructor
public class UpdateAuditPresenter implements UpdateAuditContract.UpdateAuditUserActionsListener {

    /**
     * the related view
     */
    private final UpdateAuditContract.UpdateAuditView view;

    /**
     * a service that helps on retrieving {@link Ruleset}s
     */
    private final RuleSetService ruleSetService;

    /**
     * a manager for models, that helps on retrieving {@link AuditEntity}s
     */
    private final ModelManager modelManager;

    /**
     * a bunch of {@link AuditValidator} for {@link AuditFormUiModel} validations
     */
    private final List<Validator<AuditFormUiModel>> validators;

    /**
     * Instantiate
     *
     * @param view a {@link UpdateAuditContract.UpdateAuditView}
     * @param ruleSetService a service for {@link Ruleset}s
     * @param modelManager a service for {@link AuditEntity}s
     */
    public UpdateAuditPresenter(UpdateAuditContract.UpdateAuditView view, RuleSetService ruleSetService, ModelManager modelManager) {
        this.view = view;
        this.ruleSetService = ruleSetService;
        this.modelManager = modelManager;
        validators = newArrayList(new AuthorNamePatternValidator(), new AuditNameDoesNotExistValidator(modelManager));
    }

    @Override
    public void validateAudit(AuditFormUiModel form) {
        d("Message=Validating the whole audit;");

        view.disableSaveButton();

        if (auditHasChanged(form.getInitialAudit(), form.getActualAudit())) {
            view.showSaveAuditButton();
        } else {
            view.showContinueAuditButton();
        }

        BindingResult errors = new BindingResult();
        for (Validator<AuditFormUiModel> validator : validators) {
            validator.validate(form, errors);
        }

        if (!errors.hasErrors()) {
            view.enableSaveButton();
        }
    }

    @Override
    public void updateAudit(Long auditId, AuditModel newInfo) {

        AuditEntity audit = modelManager.getAudit(auditId);

        audit.setAuthor(makeAuditor(newInfo));
        audit.setName(newInfo.getName());
        audit.setLevel(newInfo.getLevel());

        modelManager.updateAudit(audit);

        view.showAuditUpdatedSuccessfully();
    }

    /**
     * Helper function that creates a new {@link AuditorEntity}
     *
     * This function has been created, so as to make the unit test working. Without that,
     * ActiveAndroid prevents developers from creating a new {@link android.graphics.ColorSpace.Model}
     * in any unit test, without creating the whole {@link android.content.Context}.
     *
     * @param review a {@link AuditModel}
     * @return a new {@link AuditorEntity}
     */
    AuditorEntity makeAuditor(AuditModel review) {
        return AuditorEntity.builder().userName(review.getAuthorName()).build();
    }

    @Override
    public void loadSite(AuditEntity audit) {

        view.showSite(audit.getSite());

        if (audit.getSite() == null) {
            view.disableSaveButton();
        }
    }

    @Override
    public void loadRuleset(AuditEntity audit) {
        RulesetEntity ruleset = ruleSetService.getRuleSet(audit.getRuleSetRef(), audit.getRuleSetVersion(), false);

        if (ruleset != null) {
            view.showRuleset(makeRulesetModel(ruleset));
        } else {
            view.disableSaveButton();
        }
    }

    @Override
    public void loadAuthor(AuditEntity audit) {

        AuditorEntity author = audit.getAuthor();
        view.showAuthor(author);
        validateAuthor(author);
    }

    @Override
    public void loadLevel(AuditEntity audit) {
        view.showLevel(audit.getLevel());
    }

    @Override
    public void loadAudit(Long id) {
        AuditEntity audit = modelManager.getAudit(id);
        if (audit != null) {
            view.showAudit(audit);
            validateReview(audit);
        }
    }

    /**
     * We'd rather use some value object (ie {@link AuditUiModel}), and define the function equals()
     *
     * @param reference an {@link AuditModel}
     * @param actual an other {@link AuditModel}
     * @return true if the two audits are different.
     */
    private boolean auditHasChanged(AuditModel reference, AuditModel actual) {
        boolean sameLevel = actual.getLevel() == reference.getLevel();
        boolean sameAuditName = reference.getName().equalsIgnoreCase(actual.getName());
        boolean sameAuthor = reference.getAuthorName().equals(actual.getAuthorName());

        return !sameAuthor || !sameAuditName || !sameLevel;
    }

    void validateAuthor(UserModel user) {
        d("Message=Validating the author only");

        BindingResult errors = new BindingResult();
        new UserNamePatternValidator().validate(user, errors);
        if (errors.hasErrors()) {
            view.disableSaveButton();
        }
    }

    void validateReview(AuditModel audit) {
        d("Message=Validating the review only");

        BindingResult errors = new BindingResult();
        new UniqueReviewValidator(modelManager).validate(audit, errors);
        if (errors.hasErrors()) {
            view.disableSaveButton();
        }
    }


    private RulesetModel makeRulesetModel(RulesetEntity in) {

        return RulesetModel
                .builder()
                .author(in.getAuthorName())
                .comment(in.getComment())
                .date(in.getDate())
                .reference(in.getReference())
                .ruleCategoryName(in.getRuleCategoryName())
                .stat(in.getStat())
                .type(in.getType())
                .version(in.getVersion())
                .id(in.getId())
                .build();
    }
}
