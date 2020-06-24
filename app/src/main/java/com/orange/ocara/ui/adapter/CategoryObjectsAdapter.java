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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.ocara.R;
import com.orange.ocara.data.net.model.EquipmentEntity;
import com.orange.ocara.ui.fragment.BrowseEquipmentsByCategoryFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.orange.ocara.tools.ListUtils.newArrayList;
import static java.util.Collections.sort;

/**
 * Implementation of {@link ItemListAdapter} for {@link EquipmentEntity}s
 */
public class CategoryObjectsAdapter extends ItemListAdapter<EquipmentEntity> {

    /**
     * the current {@link Context}
     */
    private Context context;

    /**
     * Constructor.
     *
     * @param context a {@link Context} to populate
     */
    public CategoryObjectsAdapter(final Context context) {
        super();
        this.context = context;
    }

    @Override
    public void update(Collection<EquipmentEntity> objects) {
        List<EquipmentEntity> items = newArrayList(objects);
        sort(items, new ObjectDescriptionComparator());
        super.update(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ObjectViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.object_description_item, parent, false);

            viewHolder = new ObjectViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ObjectViewHolder) convertView.getTag();
        }

        final EquipmentEntity objectDescription = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        if (objectDescription != null) {
            final String path = context.getExternalCacheDir() + File.separator + objectDescription.getIcon();
            File icon = new File(path);
            Picasso
                    .with(context)
                    .load(icon)
                    .error(android.R.color.black)
                    .into(viewHolder.image);

            viewHolder.bind(objectDescription);
        }

        return convertView;
    }

    /**
     * a ViewHolder for an {@link EquipmentEntity}
     */
    private class ObjectViewHolder {
        final ImageView image;
        final TextView title;

        ObjectViewHolder(View convertView) {
            this.image = convertView.findViewById(R.id.object_description_image);
            this.title = convertView.findViewById(R.id.object_description_title);
        }

        void bind(EquipmentEntity content) {
            title.setText(content.getName());
        }
    }

    /**
     * an implementation of {@link Comparator} that helps on sorting elements in alphabetical order
     */
    private static class ObjectDescriptionComparator implements Comparator<EquipmentEntity>, Serializable {

        private static final long serialVersionUID = 1;

        @Override
        public int compare(EquipmentEntity o1, EquipmentEntity o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            if (o1.getName().equals(o2.getName())) {
                return 0;
            }
            return o1.getName().compareTo(o2.getName());
        }
    }
}
