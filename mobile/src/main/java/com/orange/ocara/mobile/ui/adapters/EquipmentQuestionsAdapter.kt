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
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.orange.ocara.domain.models.QuestionAnswerModel
import com.orange.ocara.domain.models.QuestionModel
import com.orange.ocara.domain.models.RuleAnswerModel
import com.orange.ocara.domain.models.RuleModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.QuestionItemAdapterBinding
import com.orange.ocara.mobile.ui.getIconFromAnswer

class EquipmentQuestionsAdapter constructor(var context: Context, var questions: List<QuestionModel>,
                                                 var onClick: (rule: RuleModel) -> Unit,
                                                 var onIllustrationsClicked: (ruleRef: String) -> Unit) :
        BaseExpandableListAdapter() {


    override fun getGroupCount(): Int = questions.size

    override fun getGroup(groupPosition: Int): QuestionModel = questions[groupPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val question = getGroup(groupPosition)
        val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding: QuestionItemAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.question_item_adapter, null, false)
        binding.mainLayout.setBackgroundColor(context.resources.getColor(R.color.white, null))
        binding.text.text = question.label
//        binding.icon.visibility = View.INVISIBLE
        if (isExpanded) {
            binding.icon.setImageDrawable(ContextCompat.getDrawable(parent!!.context, R.drawable.ic_arrow_drop_up))
        } else {
            binding.icon.setImageDrawable(ContextCompat.getDrawable(parent!!.context, R.drawable.ic_arrow_drop_down))
        }
        binding.info.visibility = View.INVISIBLE
        bindQuestion(binding , groupPosition)

        binding.root.setContentDescription(question.label + " " +
                String.format(binding.root.context.getString(R.string.content_desc_item_list_size) , groupCount)
                + " "+
                String.format(binding.root.context.getString(R.string.content_desc_item_list_pos) , groupPosition+1))

        return binding.root
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val rule = getChild(listPosition, expandedListPosition)

        val layoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding: QuestionItemAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.question_item_adapter, null, false)
        binding.mainLayout.setBackgroundColor(context.resources.getColor(R.color.app_bg_grey, null))
        binding.mainLayout.setOnClickListener {
            onClick(rule)
        }
        binding.text.setTypeface(null, Typeface.NORMAL);
        binding.text.text = rule.label
        binding.icon.visibility = View.INVISIBLE

        if (rule.isIllustrated) {
            binding.info.visibility = View.VISIBLE
            binding.info.setOnClickListener {
                onIllustrationsClicked(rule.ref)
            }
        } else {
            binding.info.visibility = View.INVISIBLE
        }
        bindRule(binding,listPosition, expandedListPosition)

        binding.root.setContentDescription(rule.label + " " +
                String.format(binding.root.context.getString(R.string.content_desc_item_sub_list_size) , getChildrenCount(listPosition))
                + " "+
                String.format(binding.root.context.getString(R.string.content_desc_item_sub_list_pos) , expandedListPosition+1))

//        prepareIllustView(binding, ruleAnswer)
        return binding.root
    }

    override fun getChildrenCount(groupPosition: Int): Int = getGroup(groupPosition).rules.size

    override fun getChild(groupPosition: Int, childPosition: Int): RuleModel = getGroup(groupPosition).rules[childPosition]


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    fun bindQuestion(binding: QuestionItemAdapterBinding, groupPosition: Int) {
        binding.btnsContatiner.visibility = View.GONE
    }

     fun bindRule(binding: QuestionItemAdapterBinding, groupPosition: Int, childPosition: Int) {
        binding.btnsContatiner.visibility = View.GONE
    }


}