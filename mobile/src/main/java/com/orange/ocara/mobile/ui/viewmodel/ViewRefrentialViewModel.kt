/*
 *
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 *
 */

package com.orange.ocara.mobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.ocara.domain.interactors.DownloadRulesetAndInsertRx
import com.orange.ocara.domain.interactors.InsertSiteTask
import com.orange.ocara.domain.interactors.RetrieveDownloadedRulesetsTaskWithRxJava
import com.orange.ocara.domain.models.RulesetModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.ui.models.LightEquipment
import com.orange.ocara.mobile.ui.models.LightRule
import com.orange.ocara.mobile.ui.models.LightRuleset
import com.orange.ocara.mobile.ui.models.LightSite
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@HiltViewModel
class ViewRefrentialViewModel @javax.inject.Inject constructor(
        private val retrieveDownloadedRulesetsTask: RetrieveDownloadedRulesetsTaskWithRxJava
) : ViewModel() {

    var rulesetId: Long = 0
    var equipments: ArrayList<LightEquipment> = ArrayList()
    var rules: ArrayList<LightRule> = ArrayList()
    var rulesetsLiveData = MutableLiveData<List<RulesetModel>>()

    public fun getDownloadedRulesets() {
        retrieveDownloadedRulesetsTask.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : SingleObserver<List<RulesetModel>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: List<RulesetModel>) {
                        rulesetsLiveData.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }

}