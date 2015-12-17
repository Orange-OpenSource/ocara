/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.model.AuditObject;
import com.orange.ocara.model.Comment;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.modelStatic.Response;
import com.orange.ocara.tools.injection.DaggerHelper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EViewGroup(resName="audit_object_item")
public class AuditObjectItemView extends FrameLayout {

    @ViewById(resName="audit_object_item")
    FrameLayout auditObjectItem;

    @ViewById(resName="audit_object_title")
    TextView title;
    @ViewById(resName="audited_object_background")
    ViewGroup imageBackground;
    @ViewById(resName="audit_object_image")
    ImageView image;

    @Inject
    Picasso picasso;

    @ViewById(resName="comments_number_badge")
    BadgeView badge;

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

        DaggerHelper.inject(this, context);
    }


    /**
     * Binds an AuditObject to the view.
     *
     * @param auditObject AuditObject bound
     */
    public void bind(AuditObject auditObject) {
        ObjectDescription objectDescription = auditObject.getObjectDescription();

        picasso.load(Uri.parse(objectDescription.getIcon().toString())).placeholder(android.R.color.black).fit().into(image);
        imageBackground.setBackgroundColor(getResources().getColor(getColorResId(auditObject.getResponse())));

        title.setText(auditObject.getName());
        badgeNotifierCommentsAuditObject(auditObject);

    }

    private static int getColorResId(Response response) {
        switch (response) {
            case OK:
                return R.color.green;
            case NOK:
                return R.color.red;
            case DOUBT:
                return R.color.yellow;
            case NoAnswer:
                return R.color.white;
            default:
                return R.color.white;
        }
    }
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public  void badgeNotifierCommentsAuditObject(AuditObject auditObject){

        if (badge != null) {
            badge.hide();
        }
        List<Comment> nbrComments=auditObject.getComments();
        if (!auditObject.getComments().isEmpty()) {
            badge.setText(String.valueOf(nbrComments.size()));
            badge.setBadgeMargin(0, 4);
            badge.show();
        }
    }

}
