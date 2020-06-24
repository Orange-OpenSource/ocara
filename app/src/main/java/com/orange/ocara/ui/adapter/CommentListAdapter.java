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

package com.orange.ocara.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.orange.ocara.data.cache.model.CommentEntity;
import com.orange.ocara.ui.view.CommentItemView;
import com.orange.ocara.ui.view.CommentItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class CommentListAdapter extends ItemListAdapter<CommentEntity> {

    @RootContext
    Activity mActivity;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CommentItemView commentItemView;

        if (convertView != null && convertView instanceof CommentItemView) {
            commentItemView = (CommentItemView) convertView;
        } else {
            commentItemView = CommentItemView_.build(mActivity);
        }

        CommentEntity comment = getItem(position);
        commentItemView.bind(comment);
        return commentItemView;
    }
}
