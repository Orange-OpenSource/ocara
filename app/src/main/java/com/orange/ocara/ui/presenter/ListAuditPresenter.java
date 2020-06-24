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
import com.orange.ocara.business.interactor.RedoAuditTask;
import com.orange.ocara.business.interactor.UseCase;
import com.orange.ocara.data.cache.model.AuditEntity;
import com.orange.ocara.ui.contract.ListAuditContract;

import lombok.RequiredArgsConstructor;

/**
 * a listener for actions that may appear in a {@link ListAuditContract.ListAuditView}
 *
 * Implements {@link ListAuditContract.ListAuditUserActionsListener}
 */
@RequiredArgsConstructor
public class ListAuditPresenter implements ListAuditContract.ListAuditUserActionsListener {

    private final ListAuditContract.ListAuditView view;

    private final UseCase<RedoAuditTask.RedoAuditRequest, RedoAuditTask.RedoAuditResponse> redoAuditTask;

    @Override
    public void createNewAudit(AuditEntity itemToCopy) {

        RedoAuditTask.RedoAuditRequest request = new RedoAuditTask.RedoAuditRequest(itemToCopy);
        redoAuditTask.executeUseCase(request, new UseCase.UseCaseCallback<RedoAuditTask.RedoAuditResponse>() {
            @Override
            public void onComplete(RedoAuditTask.RedoAuditResponse response) {

                view.showSetupPath(response.getAudit());
            }

            @Override
            public void onError(ErrorBundle errors) {
                // do nothing yet
            }
        });
    }
}
