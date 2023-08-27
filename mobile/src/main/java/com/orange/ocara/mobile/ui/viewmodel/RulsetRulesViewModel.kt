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
import com.orange.ocara.domain.interactors.LoadEquipmentsWithRulesetId
import com.orange.ocara.domain.interactors.LoadRulesWithEquipmentNamesForRuleSet
import com.orange.ocara.domain.models.EquipmentModel
import com.orange.ocara.domain.models.RuleWithEquipmentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RulsetRulesViewModel @Inject constructor(private val loadRulesWithEquipmentNamesForRuleSet
: LoadRulesWithEquipmentNamesForRuleSet) : ViewModel() {

    val allrules = ArrayList<RuleWithEquipmentModel?>()
    val rulesLiveData = MutableLiveData<List<RuleWithEquipmentModel?>?>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getRules(rulesetRef: String , rulesetVer: String) {
        loadRulesWithEquipmentNamesForRuleSet.execute(rulesetRef , rulesetVer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe{rules->
                    rulesLiveData.postValue(rules)
                    allrules.clear()
                    allrules.addAll(rules)
                }.let {
                    disposables.add(it)
                }
    }
    fun filterRules(filter: String) {
        val result = ArrayList<RuleWithEquipmentModel>()
        allrules.forEach { rule ->
            if (rule?.rule?.label?.toLowerCase()?.contains(filter) == true) {
                result.add(rule)
            }
        }
        rulesLiveData.postValue(result)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}