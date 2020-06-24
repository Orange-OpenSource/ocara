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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.Equipment;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.fragment.BrowseEquipmentsFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

import static com.orange.ocara.tools.ListUtils.newArrayList;

/**
 * Bridge between the view (a {@link BrowseEquipmentsFragment}) and the data (a bunch of {@link Equipment}s)
 */
@EBean
public class RulesetEquipmentsAdapter extends ItemListAdapter<Equipment> {

    @RootContext
    Activity mActivity;

    private int maxSize = 0;

    @AfterInject
    void initAdapter() {
        maxSize = mActivity.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_height_material);
    }

    @Override
    public void update(Collection<Equipment> objects) {
        List<Equipment> items = newArrayList(objects);
        Collections.sort(items, new EquipmentComparator());

        this.objects.clear();
        this.objects.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemViewHolder viewHolder;
        final Equipment item = getItem(position);

        /*
         * initialize the view
         */
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(mActivity)
                    .inflate(R.layout.object_description_item_for_rule, parent, false);

            viewHolder = new ItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        /*
         * initialize inner parts of the view
         * - trigger the download of the URL asynchronously into the image view.
         */
        final TextView title = convertView.findViewById(R.id.object_description_title);
        title.setText(item.getName());

        final String path = mActivity.getExternalCacheDir() + File.separator + item.getIcon();
        File icon = new File(path);

        Timber.v("Message=Trying to load image;Icon=%s;Path=%s;TargetWidth=%d;TargetHeight=%d;TargetExists=%b", item.getIcon(), icon, maxSize, maxSize, icon.exists());
        Picasso
                .with(mActivity)
                .load(icon)
                .placeholder(android.R.color.black)
                .resize(maxSize, maxSize)
                .into(makeTarget(title));

        /*
         * bind the item to the view
         */
        viewHolder.bind(item);

        return convertView;
    }

    /**
     * retrieves a listener for image loading.
     *
     * @param textView a component where to put a {@link Drawable}
     * @return a {@link Target}
     */
    private Target makeTarget(TextView textView) {
        return new Target() {
            /**
             * Callback when an image has been successfully loaded.
             *
             * @param bitmap an image
             * @param from a location where the image was loaded from
             */
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                textView.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(mActivity.getResources(), bitmap), null, null, null);
            }

            /**
             * Callback indicating the image could not be successfully loaded.
             *
             * @param errorDrawable an alternative image
             */
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

                Timber.e("ErrorMessage=Image could not be loaded");
            }

            /**
             * Callback invoked right before the request is submitted.
             *
             * @param placeHolderDrawable an alternative image
             */
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // do nothing yet
            }
        };
    }

    /**
     * Retrieves the position of an {@link EquipmentEntity}
     *
     * @param objectRef an identifier for a {@link EquipmentEntity}
     * @return the position of the element in the adapter's collection. -1, if it does not exist.
     */
    public int getPositionByReference(final String objectRef) {
        if (objectRef != null) {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).getReference().equals(objectRef)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * a ViewHolder for a {@link Equipment}
     */
    private class ItemViewHolder {

        private final TextView title;

        /**
         * instantiate
         *
         * @param convertView the target view
         */
        ItemViewHolder(View convertView) {
            title = convertView.findViewById(R.id.object_description_title);
        }

        /**
         * links an element to the view
         *
         * @param item an element to bind
         */
        void bind(Equipment item) {
            String text = item.getName() + (item.isCharacteristic() ? "\n" + mActivity.getString(R.string.ruleset_equipment_attribute) : "");
            title.setText(text);
        }
    }

    /**
     * a {@link Comparator} for sorting elements in a collection
     */
    private class EquipmentComparator implements Comparator<Equipment> {

        public int compare(Equipment o1, Equipment o2) {
            return Collator
                    .getInstance()
                    .compare(o1.getName(), o2.getName());
        }
    }
}