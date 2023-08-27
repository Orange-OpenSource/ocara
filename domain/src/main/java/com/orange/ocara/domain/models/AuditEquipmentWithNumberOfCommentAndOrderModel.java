/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */


package com.orange.ocara.domain.models;

public class AuditEquipmentWithNumberOfCommentAndOrderModel extends AuditEquipmentModel {
    private int numberOfComments;
    private int order;

    protected AuditEquipmentWithNumberOfCommentAndOrderModel(BuilderWrapper<?> builderWrapper) {
        super(builderWrapper);
        this.numberOfComments = builderWrapper.numberOfComments;
        this.order = builderWrapper.order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    protected static abstract class BuilderWrapper<T extends BuilderWrapper<T>> extends AuditEquipmentModel.BuilderWrapper<T> {
        private int numberOfComments;
        private int order;

        public T setNumberOfComments(int numberOfComments) {
            this.numberOfComments = numberOfComments;
            return self();
        }

        public T setOrder(int order) {
            this.order = order;
            return self();
        }

        public AuditEquipmentWithNumberOfCommentAndOrderModel build() {
            return new AuditEquipmentWithNumberOfCommentAndOrderModel(this);
        }
    }

    public static class Builder extends BuilderWrapper<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
