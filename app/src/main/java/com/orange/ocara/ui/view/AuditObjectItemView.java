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

package com.orange.ocara.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.business.service.RuleSetService;
import com.orange.ocara.business.service.impl.RuleSetServiceImpl;
import com.orange.ocara.data.cache.model.AuditObjectEntity;
import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.data.cache.model.ResponseModel;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import timber.log.Timber;

/**
 * a {@link FrameLayout} dedicated to display an {@link AuditObjectEntity}
 */
@EViewGroup(R.layout.audit_object_item)
public class AuditObjectItemView extends FrameLayout {

    @ViewById(R.id.audit_object_item)
    FrameLayout auditObjectItem;

    @ViewById(R.id.audit_object_title)
    TextView title;

    @ViewById(R.id.audited_object_background)
    ViewGroup imageBackground;

    @ViewById(R.id.audit_object_image)
    ImageView image;

    @ViewById(R.id.comments_number_badge)
    BadgeView badge;

    @Bean(RuleSetServiceImpl.class)
    RuleSetService mRuleSetService;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public AuditObjectItemView(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public AuditObjectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static int getColorResId(ResponseModel response) {
        switch (response) {
            case OK:
                return R.color.green;
            case NOK:
                return R.color.red;
            case DOUBT:
                return R.color.yellow;
            case NO_ANSWER:
                return R.color.white;
            default:
                return R.color.white;
        }
    }

    /**
     * Binds an AuditObject to the view.
     *
     * @param auditObject AuditObject bound
     */
    public void bind(AuditObjectEntity auditObject) {
        String objectDescriptionId = auditObject.getObjectDescriptionId();
        background(auditObject, objectDescriptionId);

    }

    @Background
    void background(final AuditObjectEntity auditObject, final String objectDescriptionId) {
        final EquipmentEntity objectDescriptionFormRef = mRuleSetService.getObjectDescriptionFromRef(auditObject.getAudit().getRuleSetRef(), auditObject.getAudit().getRuleSetVersion(), objectDescriptionId);
        displayAction(auditObject, objectDescriptionFormRef);
    }

    @UiThread
    void displayAction(final AuditObjectEntity auditObject, final EquipmentEntity equipment) {
        final String path = getContext().getExternalCacheDir() + File.separator + equipment.getIcon();
        File icon = new File(path);

        Timber.v("Message=Trying to load image;Icon=%s", icon);
        Picasso
                .with(getContext())
                .load(icon)
                .error(android.R.color.black)
                .fit()
                .into(image);

        imageBackground.setBackgroundColor(getResources().getColor(getColorResId(auditObject.getResponse())));

        title.setText(auditObject.getName());
        badgeNotifierCommentsAuditObject(auditObject);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void badgeNotifierCommentsAuditObject(AuditObjectEntity auditObject) {

        if (badge != null) {
            badge.hide();
            List<CommentEntity> nbrComments = auditObject.getComments();
            if (nbrComments != null && !nbrComments.isEmpty()) {
                final String text = String.valueOf(nbrComments.size());
                badge.setText(text);
                badge.setBadgeMargin(0, 4);
                badge.show();
            }
        }
    }

}
