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

package com.orange.ocara.mobile.ui.dialogs

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.View
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.DialogExpertiseInfoBinding
import com.orange.ocara.mobile.setFullScreen
import java.util.*


class ExpertiseInfoDialog(
) : OcaraDialog<DialogExpertiseInfoBinding>(R.layout.dialog_expertise_info) {

    companion object {

        const val TAG = "ExpertiseInfoDialog"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val expTitleString = SpannableString(getString(R.string.exp_info_expert_title_no_bullet))
        expTitleString.setSpan(BulletSpan(20, Color.BLACK, 7), 0, expTitleString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.expertInfoTitleTv.setText(expTitleString)



        val noviceModeTitleString = SpannableString(getString(R.string.exp_info_novice_title_no_bullet))
        noviceModeTitleString.setSpan(BulletSpan(20, Color.BLACK, 7), 0, noviceModeTitleString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.noviceInfoTitleTv.setText(noviceModeTitleString)


        if (Locale.getDefault().language.toLowerCase() == "fr") {
            val expString = SpannableString("Suppose une connaissance des règles d'accessibilité\nS'adresse à un utilisateur habitué à réaliser des audits\nOn affichage l'ensemble des règles sur un seul écran")
            expString.setSpan(BulletSpan(20, Color.BLACK, 5), 0, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            expString.setSpan(BulletSpan(20, Color.BLACK, 5), 52, 108, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            expString.setSpan(BulletSpan(20, Color.BLACK, 5), 109, expString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.expertInfoTv.setText(expString)


            var noviceString = SpannableString("S'adresse à un nouvel utilisateur sans connaissance des règles d'accessibilité\nOn affiche séquentiellement les règles à auditer\nUne seule règle par écran")
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 5), 0, 78, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 5), 79, 127, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 5), 128, noviceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.noviceInfoTv.setText(noviceString)


        } else {
            val string = SpannableString("Assumes knowledge of accessibility rules\n Aimed at a user accustomed to carrying out audits\nAll the rules are displayed on a single screen")
            string.setSpan(BulletSpan(20, Color.BLACK, 10), 0, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            string.setSpan(BulletSpan(20, Color.BLACK, 10), 41, 90, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            string.setSpan(BulletSpan(20, Color.BLACK, 10), 92, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.expertInfoTv.setText(string)


            var noviceString = SpannableString("For a new user\nThe rules to be audited are displayed sequentially\nOnly one rule per screen")
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 10), 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 10), 15, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            noviceString.setSpan(BulletSpan(20, Color.BLACK, 10), 66, noviceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.noviceInfoTv.setText(noviceString)
        }


    }

    override fun onResume() {
        super.onResume()
        // setWidthPercent(95)
        setFullScreen()
    }

    override fun initClickListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }


}