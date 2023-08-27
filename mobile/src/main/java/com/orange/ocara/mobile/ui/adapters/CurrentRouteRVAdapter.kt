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
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.orange.ocara.data.source.ImageSource
import com.orange.ocara.domain.models.AuditEquipmentForCurrentRouteModel
import com.orange.ocara.domain.models.AuditEquipmentModel
import com.orange.ocara.mobile.R
import com.orange.ocara.utils.enums.Answer
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.File
import javax.inject.Inject

class CurrentRouteRVAdapter @Inject constructor(
    @ActivityContext val context: Context,
    val imageCache: ImageSource.ImageCache,

    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var deleteMode: Boolean = false
    private var editMode: Boolean = false


    val viewTypeTitle: Int = 0
    val viewTypeTested: Int = 1
    val viewTypeNotTested: Int = 2

    lateinit var data: List<AuditEquipmentModel>
    lateinit var auditName: String
//    var selectedObjectsIds = ArrayList<Int>()

    var clickListener: ((id: Int) -> Unit)? = null
    var onItemUp: ((pos: Int) -> Unit)? = null
    var onItemDown: ((pos: Int) -> Unit)? = null

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val containerCard: MaterialCardView = v.findViewById(R.id.container_card)
        val containerConst: ConstraintLayout = v.findViewById(R.id.container_const)
        val icon: ImageView = v.findViewById(R.id.equipmentIcon)
        val number: TextView = v.findViewById(R.id.equipmentNumberTv)
        val name: TextView = v.findViewById(R.id.equipmentName)
        val deleteCb: MaterialCheckBox = v.findViewById(R.id.delete_cb)
        val upArrow: ImageView = v.findViewById(R.id.upArrow)
        val downArrow: ImageView = v.findViewById(R.id.downArrow)
        val ansIcon: ImageView = v.findViewById(R.id.ansIcon)

    }

    class TitleViewHolder(v: View, auditName: String) : RecyclerView.ViewHolder(v) {
        val txt: TextView = v.findViewById(R.id.objectsLabel)

        init {
            txt.text = auditName
        }
    }

    /*
        this method returns 2 if object not tested and 1 if tested
     */
    override fun getItemViewType(position: Int): Int {
//        if (position == 0) return viewTypeTitle
        return viewTypeNotTested
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(getLayout(viewType), parent, false)
        if (viewType == viewTypeTitle) {
            return TitleViewHolder(view, auditName)
        }
        return ViewHolder(view)
    }

    private fun getLayout(viewType: Int): Int {
//        return if (viewType == viewTypeNotTested) R.layout.current_route_not_tested_list_item
//        else
        if (viewType == viewTypeTitle) {
            return R.layout.current_route_title_list_item
        }
        return R.layout.current_route_list_item
    }

    override fun onBindViewHolder(vHolder: RecyclerView.ViewHolder, pos: Int) {
        if (getItemViewType(pos) == viewTypeTitle) {
            return
        }
//        val position = pos - 1
        val position = pos
        val holder = vHolder as ViewHolder
        val auditEq = data[position]

        setViewAccordingToStatus(holder, auditEq , pos)

        holder.name.text = auditEq.name
        holder.number.text = (position + 1).toString() + '.'
        holder.itemView.setOnClickListener {
            clickListener?.invoke(auditEq.id)
        }
        setIconInImageView(auditEq.equipment.icon, holder.icon)
        if (deleteMode) {
            holder.deleteCb.visibility = View.VISIBLE
//            holder.deleteCb.isChecked = selectedObjectsIds.contains(data[position].id)
            holder.deleteCb.setOnCheckedChangeListener(null)
            holder.deleteCb.isChecked =
                (data[position] as AuditEquipmentForCurrentRouteModel).isSelected
            holder.deleteCb.setOnCheckedChangeListener { buttonView, isChecked ->
                (data[position] as AuditEquipmentForCurrentRouteModel).isSelected = isChecked
            }
        } else {
            holder.deleteCb.visibility = View.GONE
            holder.deleteCb.setOnCheckedChangeListener(null)
        }
        if (position == 0) {
            holder.upArrow.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            holder.upArrow.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO

        } else {
            holder.upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            holder.upArrow.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
        if (position == data.size - 1) {
            holder.downArrow.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            holder.downArrow.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        } else {
            holder.downArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            holder.downArrow.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
        if (editMode) {
            holder.upArrow.visibility = View.VISIBLE
            holder.upArrow.setOnClickListener { onItemUp?.invoke(position) }
            holder.downArrow.visibility = View.VISIBLE
            holder.downArrow.setOnClickListener { onItemDown?.invoke(position) }

        } else {
            holder.upArrow.visibility = View.GONE
            holder.upArrow.setOnClickListener(null)
            holder.downArrow.visibility = View.GONE
            holder.downArrow.setOnClickListener(null)

        }
    }

    private fun setViewAccordingToStatus(holder: ViewHolder, eq: AuditEquipmentModel , position: Int) {
        when (eq.answer) {
            Answer.OK -> {

                holder.containerCard.setStrokeColor(Color.parseColor("#32c832"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#1932c832"))
                setIconInAnsImage(R.drawable.ic_ans_ok, holder.ansIcon)
                holder.itemView.contentDescription = eq.name + " " +holder.itemView.context.getString(R.string.content_desc_compliant)+
                        " "+ String.format(context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                        String.format(context.getString(R.string.content_desc_item_list_pos) , position+1)
                //+ " "+  holder.itemView.context.getString(R.string.content_desc_in_list)
            }
            Answer.NOK -> {
                holder.containerCard.setStrokeColor(Color.parseColor("#e21b1b"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#19e21b1b"))
                setIconInAnsImage(R.drawable.no_answer, holder.ansIcon)
                holder.itemView.contentDescription = eq.name + " " +holder.itemView.context.getString(R.string.content_desc_improper)+" "+ String.format(context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                        String.format(context.getString(R.string.content_desc_item_list_pos) , position+1)
                //+ " "+  holder.itemView.context.getString(R.string.content_desc_in_list)

            }
            Answer.DOUBT -> {
                holder.containerCard.setStrokeColor(Color.parseColor("#ffcc00"))
                holder.containerConst.setBackgroundColor(Color.parseColor("#19ffcc00"))
                setIconInAnsImage(R.drawable.ic_ans_doubt, holder.ansIcon)
                holder.itemView.contentDescription = eq.name + " " +holder.itemView.context.getString(R.string.content_desc_uncertain)+" "+ String.format(context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                        String.format(context.getString(R.string.content_desc_item_list_pos) , position+1)
                //+ " "+  holder.itemView.context.getString(R.string.content_desc_in_list)

            }
            else -> {
                holder.containerCard.setStrokeColor(context.getColor(R.color.grey))
                holder.containerConst.setBackgroundColor(Color.TRANSPARENT)
                setIconInAnsImage(R.drawable.na_answer, holder.ansIcon)
                holder.itemView.contentDescription = eq.name + " " +holder.itemView.context.getString(R.string.content_desc_unaudited)+" "+ String.format(context.getString(R.string.content_desc_item_list_size) , itemCount) + " "+
                        String.format(context.getString(R.string.content_desc_item_list_pos) , position+1)
                //+ " "+  holder.itemView.context.getString(R.string.content_desc_in_list)

            }

//            holder.containerCard.contentDescription = auditEq.name + " "+

        }

    }

    private fun setIconInAnsImage(id: Int, ansIcon: ImageView) {
        ansIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                id,
                context.theme
            )
        )
    }

    private fun setIconInImageView(icon: String, imageView: ImageView) {
        val iconFile: File? = imageCache.get(icon)
        Picasso.with(context).load(iconFile).placeholder(android.R.color.black).into(imageView)
    }

    override fun getItemCount() = data.size
    fun isEditMode() = editMode
    fun isDeleteMode() = deleteMode
    fun openDeleteMode() {
        deleteMode = true
//        selectedObjectsIds.clear()
        notifyDataSetChanged()
    }

    fun exitDeleteMode() {
        deleteMode = false
//        selectedObjectsIds.clear()
        notifyDataSetChanged()
    }

    fun openEditMode() {
        editMode = true
        notifyDataSetChanged()
    }

    fun exitEditMode() {
        editMode = false
        notifyDataSetChanged()
    }
}