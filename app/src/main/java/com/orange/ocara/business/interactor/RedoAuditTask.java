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

package com.orange.ocara.business.interactor;

import androidx.annotation.NonNull;

import com.orange.ocara.business.binding.BizError;
import com.orange.ocara.business.repository.AuditRepository;
import com.orange.ocara.data.cache.model.AuditEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * a {@link UseCase} for copying an {@link AuditEntity}
 */
@RequiredArgsConstructor
public class RedoAuditTask implements UseCase<RedoAuditTask.RedoAuditRequest, RedoAuditTask.RedoAuditResponse> {

    private final AuditRepository auditRepository;

    @Override
    public void executeUseCase(@NonNull RedoAuditRequest request, UseCaseCallback<RedoAuditResponse> callback) {

        RedoAuditResponse response;

        try {
            AuditEntity cloneAudit = auditRepository.cloneOne(request.getAudit().getId());
            response = RedoAuditResponse.ok(cloneAudit);
            callback.onComplete(response);
        } catch (Exception ex) {
            Timber.e(ex, "TaskMessage=The redoing of an audit failed;TaskError=%s", ex.getMessage());
            callback.onError(new BizError("The redoing of an audit failed", ex));
        }
    }

    /**
     * the request dedicated to the {@link RedoAuditTask}
     */
    @Getter
    @RequiredArgsConstructor
    public static final class RedoAuditRequest implements UseCase.RequestValues {

        private final AuditEntity audit;
    }

    /**
     * when the request is successful, the response contains the audit that has been done
     */
    @RequiredArgsConstructor
    public static final class RedoAuditResponse implements UseCase.ResponseValue {

        @Getter
        private final String code;

        @Getter
        private final AuditEntity audit;

        public boolean isOk() {
            return "OK".equals(code);
        }

        /**
         * gives an instance of a positive {@link RedoAuditResponse}
         *
         * @param audit the successful result of the task
         * @return a valid {@link RedoAuditResponse}
         */
        static RedoAuditResponse ok(AuditEntity audit) {
            return new RedoAuditResponse("OK", audit);
        }
    }

}
