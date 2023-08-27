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

public class AuditEquipmentForCurrentRouteModel extends AuditEquipmentWithNumberOfCommentAndOrderModel {
    private boolean isSelected;

    public AuditEquipmentForCurrentRouteModel(BuilderWrapper<?> builderWrapper) {
        super(builderWrapper);
        this.isSelected = builderWrapper.isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected static abstract class BuilderWrapper<T extends BuilderWrapper<T>> extends AuditEquipmentWithNumberOfCommentAndOrderModel.BuilderWrapper<T> {
        private boolean isSelected;

        public T setSelected(boolean selected) {
            isSelected = selected;
            return self();
        }

        public AuditEquipmentForCurrentRouteModel build() {
            return new AuditEquipmentForCurrentRouteModel(this);
        }
    }

    public static class Builder extends BuilderWrapper<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }
}
