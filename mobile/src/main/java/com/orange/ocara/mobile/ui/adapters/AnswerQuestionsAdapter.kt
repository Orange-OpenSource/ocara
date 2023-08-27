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

package com.orange.ocara.mobile.ui.adapters


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.QuestionItemAdapterBinding
import com.orange.ocara.mobile.ui.getIconFromAnswer
import com.orange.ocara.utils.enums.Answer

class AnswerQuestionsAdapter(val context: Context, val questions: List<QuestionAnswerModel>
                             , val onAnswerSelected: (rule: RuleAnswerModel, answer: Answer)->Unit, val onIllustrationsClicked:(ruleRef:String)->Unit) :
        BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = questions.size

    override fun getChildrenCount(groupPosition: Int): Int = getGroup(groupPosition).ruleAnswers.size

    override fun getGroup(groupPosition: Int): QuestionAnswerModel = questions[groupPosition]


    override fun getChild(groupPosition: Int, childPosition: Int): RuleAnswerModel = getGroup(groupPosition).ruleAnswers[childPosition]


    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val questionAnswer = getGroup(groupPosition)
        val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding:QuestionItemAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.question_item_adapter, null, false)
        binding.mainLayout.setBackgroundColor(context.resources.getColor(R.color.white, null))
        binding.text.setTypeface(null, Typeface.BOLD);
        binding.text.text = questionAnswer.question.label
//        binding.icon.setImageResource(getIconFromAnswer(questionAnswer.answer))
        binding.info.visibility = View.INVISIBLE
//        setAnswerBtn( binding,questionAnswer.answer)
        binding.btnsContatiner.visibility=View.GONE
        if (isExpanded) {
            binding.icon.setImageDrawable(ContextCompat.getDrawable(parent!!.context, R.drawable.ic_arrow_drop_up))
        } else {
            binding.icon.setImageDrawable(ContextCompat.getDrawable(parent!!.context, R.drawable.ic_arrow_drop_down))
        }
        return binding.root
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val ruleAnswer = getChild(listPosition, expandedListPosition)

        val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding: QuestionItemAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.question_item_adapter, null, false)
        binding.icon.visibility = View.INVISIBLE
        binding.mainLayout.setBackgroundColor(context.resources.getColor(R.color.app_bg_grey,null))

//        binding.mainLayout.setOnClickListener {
//            onClick(ruleAnswer)
//        }
        binding.text.setTypeface(null, Typeface.BOLD);
        binding.text.text = ruleAnswer.rule.label
//        binding.icon.setImageResource(getIconFromAnswer(ruleAnswer.answer))

        setAnswerBtn(binding , ruleAnswer.answer)
        prepareIllustView(binding, ruleAnswer)
        setClickListeners(binding , ruleAnswer , ruleAnswer.answer)
        return binding.root
    }

    private fun prepareIllustView(binding: QuestionItemAdapterBinding, ruleAnswer: RuleAnswerModel) {
        binding.info.visibility = if (ruleAnswer.rule.illustrations.isEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        binding.info.contentDescription = binding.root.context.getString(R.string.content_desc_rule_info)
        binding.info.setOnClickListener {
            onIllustrationsClicked(ruleAnswer.rule.ref)
        }
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    fun setAnswerBtn ( binding: QuestionItemAdapterBinding ,answer: Answer){
//        binding.yesBtn.isSelected = false
//        binding.noBtn.isSelected = false
//        binding.nABtn.isSelected = false
//        binding.doubtBtn.isSelected = false
//        when (answer){
//            Answer.OK -> binding.yesBtn.isSelected = true
//            Answer.DOUBT -> binding.doubtBtn.isSelected = true
//            Answer.NOK -> binding.noBtn.isSelected = true
//            Answer.NO_ANSWER -> binding.nABtn.isSelected = true
//
//        }

        binding.yesBtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.white)))
        binding.noBtn.backgroundTintList =(ColorStateList.valueOf(context.getColor(R.color.white)))
        binding.nABtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.white)))
        binding.doubtBtn.backgroundTintList =(ColorStateList.valueOf(context.getColor(R.color.white)))
        when (answer){
            Answer.OK -> binding.yesBtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.ansYesColor)))
            Answer.DOUBT -> binding.doubtBtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.ansDoubtColor)))
            Answer.NOK -> binding.noBtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.ansNoColor)))
            Answer.NO_ANSWER -> binding.nABtn.backgroundTintList = (ColorStateList.valueOf(context.getColor(R.color.ansNAColor)))

        }

    }

    fun setClickListeners ( binding: QuestionItemAdapterBinding ,rule:RuleAnswerModel, answer: Answer){
        binding.yesBtn.setOnClickListener { l->
            onAnswerSelected(rule , Answer.OK)
        }
        binding.noBtn.setOnClickListener { l->
            onAnswerSelected(rule , Answer.NOK)
        }
        binding.doubtBtn.setOnClickListener { l->
            onAnswerSelected(rule , Answer.DOUBT)
        }
        binding.nABtn.setOnClickListener { l->
            onAnswerSelected(rule , Answer.NO_ANSWER)
        }
    }

}