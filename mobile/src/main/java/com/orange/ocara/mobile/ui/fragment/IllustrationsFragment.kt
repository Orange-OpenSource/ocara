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

package com.orange.ocara.mobile.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.orange.ocara.domain.models.IllustrationModel
import com.orange.ocara.mobile.R
import com.orange.ocara.mobile.databinding.FragmentIllustrationsBinding
import com.orange.ocara.mobile.ui.viewmodel.ExplanationsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class IllustrationsFragment : OcaraFragment() {
    private val args: IllustrationsFragmentArgs by navArgs()
    lateinit var binding: FragmentIllustrationsBinding
    lateinit var viewModel: ExplanationsViewModel

    var transformation: Transformation = object : Transformation {
        override fun transform(source: Bitmap): Bitmap? {
            val targetWidth: Int = binding.image.getWidth()
            val aspectRatio = source.height.toDouble() / source.width.toDouble()
            val targetHeight = (targetWidth * aspectRatio).toInt()
            val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle()
            }
            return result
        }

       override fun key(): String? {
            return "transformation" + " desiredWidth"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_illustrations, container, false)
        viewModel = ViewModelProvider(this).get(ExplanationsViewModel::class.java)
        initLiveData()
        viewModel.loadIllustrations(args.ruleRef, args.ruleSetRef, args.ruleSetVer)
        initClickListeners()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as AppCompatActivity).supportActionBar?.title = ""

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.illustrations_title)

        inflater.inflate(R.menu.default_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initClickListeners() {
        binding.right.setOnClickListener {
            viewModel.getNextIll()
        }
        binding.left.setOnClickListener {
            viewModel.getPreIll()
        }
    }

    private fun initLiveData() {
        viewModel.currentIllustInd.observe(viewLifecycleOwner) {
            setIllust(viewModel.allIllust[it])
        }
        viewModel.isPreAvailable.observe(viewLifecycleOwner) {
            preLiveData(it)
        }
        viewModel.isNextAvailable.observe(viewLifecycleOwner) {
            nextLiveData(it)
        }
    }

    private fun nextLiveData(it: Boolean) {
        Timber.d("nnnn nextLiveData = $it")
        if (!it) {
//            binding.right.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
            binding.right.visibility = View.GONE

        } else {
//            binding.right.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_orange_24)
            binding.right.visibility = View.VISIBLE
            binding.right.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
        }
    }

    private fun preLiveData(it: Boolean) {
        if (!it) {
//            binding.left.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            binding.left.visibility = View.GONE

        } else {
//            binding.left.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_orange_24)
            binding.left.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            binding.left.visibility = View.VISIBLE

        }
    }

    private fun setIllust(illust: IllustrationModel) {
        if (illust.image.isEmpty()) {
            binding.image.visibility = View.GONE
        }
        val path = requireContext().externalCacheDir.toString() + File.separator + illust.image
        val icon = File(path)
//        Picasso
//                .with(context)
//                .load(icon)
//                .error(android.R.color.transparent)
//                .into(binding.image)

        Picasso.with(context)
                .load(icon)
                .error(android.R.color.black)
                .transform(transformation)
                .into(binding.image)
//        , object : Callback {
//            override fun onSuccess() {
//                holder.progressBar_picture.setVisibility(View.GONE)
//            }
//
//            override  fun onError() {
//                Log.e(LOGTAG, "error")
//                holder.progressBar_picture.setVisibility(View.GONE)
//            }
//        }
        if (illust.comment.isEmpty()) {
            binding.contentCard.visibility = View.GONE
        } else
            binding.comment.text = illust.comment
    }
}