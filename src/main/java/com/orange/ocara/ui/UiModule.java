/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui;

import com.orange.ocara.ui.activity.AboutActivity_;
import com.orange.ocara.ui.activity.AuditObjectsExpertModeActivity_;
import com.orange.ocara.ui.activity.AuditObjectsNoviceModeActivity_;
import com.orange.ocara.ui.activity.CreateAuditActivity_;
import com.orange.ocara.ui.activity.CreateSiteActivity_;
import com.orange.ocara.ui.activity.DetailsActivityInGrid_;
import com.orange.ocara.ui.activity.DetailsActivity_;
import com.orange.ocara.ui.activity.EditCommentAudioActivity_;
import com.orange.ocara.ui.activity.EditCommentPhotoActivity_;
import com.orange.ocara.ui.activity.EditCommentTextActivity_;
import com.orange.ocara.ui.activity.IllustrationsActivity_;
import com.orange.ocara.ui.activity.ListAuditActivity_;
import com.orange.ocara.ui.activity.ListAuditCommentActivity_;
import com.orange.ocara.ui.activity.ListAuditObjectCommentActivity_;
import com.orange.ocara.ui.activity.ListRulesActivity_;
import com.orange.ocara.ui.activity.ResultAuditActivity_;
import com.orange.ocara.ui.activity.SettingsActivity_;
import com.orange.ocara.ui.activity.SetupAuditPathActivity_;
import com.orange.ocara.ui.activity.UpdateAuditActivity_;
import com.orange.ocara.ui.dialog.AudioPlayerDialog_;
import com.orange.ocara.ui.dialog.AudioRecorderDialog_;
import com.orange.ocara.ui.fragment.AudioPlayerFragment_;
import com.orange.ocara.ui.fragment.HandicapPieChartFragment_;
import com.orange.ocara.ui.fragment.ObjectsByCategoryFragment_;
import com.orange.ocara.ui.fragment.ObjectsByRuleSetFragment_;
import com.orange.ocara.ui.view.AuditObjectItemView_;
import com.orange.ocara.ui.view.CommentItemView_;
import com.orange.ocara.ui.view.IllustrationCommentItemView_;

import dagger.Module;

@Module(
        complete = false,    // Here we enable object graph validation
        library = true,
        injects = {
                // Activities
                ListAuditActivity_.class,
                CreateAuditActivity_.class,
                CreateSiteActivity_.class,
                UpdateAuditActivity_.class,
                SetupAuditPathActivity_.class,
                DetailsActivityInGrid_.class,
                DetailsActivity_.class,
                AuditObjectsNoviceModeActivity_.class,
                AuditObjectsExpertModeActivity_.class,
                ResultAuditActivity_.class,
                AboutActivity_.class,
                SettingsActivity_.class,
                ListAuditCommentActivity_.class,
                ListRulesActivity_.class,
                ListAuditObjectCommentActivity_.class,
                EditCommentTextActivity_.class,
                EditCommentPhotoActivity_.class,
                EditCommentAudioActivity_.class,
                IllustrationsActivity_.class,

                // Fragments
                ObjectsByCategoryFragment_.class,
                ObjectsByRuleSetFragment_.class,
                HandicapPieChartFragment_.class,
                AudioPlayerFragment_.class,

                // Dialogs
                AudioRecorderDialog_.class,
                AudioPlayerDialog_.class,

                // View
                AuditObjectItemView_.class,
                CommentItemView_.class,
                IllustrationCommentItemView_.class
        }
)
public final class UiModule {

}
