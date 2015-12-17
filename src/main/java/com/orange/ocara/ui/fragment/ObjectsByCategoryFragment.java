/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.ui.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orange.ocara.R;
import com.orange.ocara.modelStatic.Category;
import com.orange.ocara.modelStatic.ObjectDescription;
import com.orange.ocara.ui.activity.DetailsActivityInGrid_;
import com.orange.ocara.ui.activity.ListRulesActivity_;
import com.orange.ocara.ui.adapter.ItemListAdapter;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

@EFragment(resName="fragment_object_list")
public class ObjectsByCategoryFragment extends BaseFragment {

    @Setter
    @Getter
    private Category category;

    @Setter
    private String ruleSetId;

    @Setter
    private Long auditId;

    @Inject
    Picasso picasso;

    @ViewById(resName="objects_gridview")
    GridView objectsGridView;

    private CategoryObjectsAdapter categoryObjectsAdapter;
    private OnObjectSelectionListener listener = null;

    public interface OnObjectSelectionListener {
        void onObjectSelected(ObjectDescription objectDescription, Category category);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnObjectSelectionListener) {
            listener = (OnObjectSelectionListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // As Grid
        final int firstVisiblePosition = objectsGridView.getFirstVisiblePosition();
        objectsGridView.setNumColumns(getResources().getInteger(R.integer.object_columns_count));
        objectsGridView.setSelection(firstVisiblePosition);
    }

    @AfterViews
    void setUpObjectsGridView() {
        categoryObjectsAdapter = new CategoryObjectsAdapter(category);
        objectsGridView.setAdapter(categoryObjectsAdapter);

        registerForContextMenu(objectsGridView);
    }

    @ItemClick(resName="objects_gridview")
    void selectObject(ObjectDescription objectDescription) {
        if (listener != null) {
            listener.onObjectSelected(objectDescription, category);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.objects_context_menu, menu);


        // Workaround to force fragment menu item to call local onContextItemSelected not one from activity
        //TODO find another way
        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };

        for (int i = 0, n = menu.size(); i < n; i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (!getUserVisibleHint()) {
            return false;
        }

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final ObjectDescription objectDescription = categoryObjectsAdapter.getItem(info.position);

        int i = item.getItemId();
        if (i == R.id.action_object_grid_detail) {
            showDetail(objectDescription);
            return true;
        } else if (i == R.id.action_object_add) {
            selectObject(objectDescription);
            return true;
        } else if (i == R.id.action_object_ruleset) {
            showRuleSet(objectDescription);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }

    }

    private void showDetail(ObjectDescription objectDescription) {
        DetailsActivityInGrid_.intent(this).auditId(auditId)
                .objectDescriptionId(objectDescription.getName())
                .start();

    }



    private void showDefinition(ObjectDescription objectDescription) {
        Toast.makeText(getActivity(), "show definition", Toast.LENGTH_SHORT).show();
    }


    private void showRuleSet(ObjectDescription objectDescription) {
        String objectDescriptionId = objectDescription.getName();
        ListRulesActivity_.intent(this).ruleSetId(ruleSetId).objectDescriptionId(objectDescriptionId).start();
    }

    /**
     * BaseAdapter.
     */
    private class CategoryObjectsAdapter extends ItemListAdapter<ObjectDescription> {

        /**
         * Constructor.
         *
         * @param category Category
         */
        protected CategoryObjectsAdapter(Category category) {
            update(category.getObjects());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ObjectViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.object_description_item, parent, false);

                viewHolder = new ObjectViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ObjectViewHolder) convertView.getTag();
            }

            final ObjectDescription objectDescription = getItem(position);

            // Trigger the download of the URL asynchronously into the image view.
            picasso.load(Uri.parse(objectDescription.getIcon().toString())).placeholder(android.R.color.black).into(viewHolder.image);
            viewHolder.title.setText(objectDescription.getDescription());

            return convertView;
        }
    }

    private class ObjectViewHolder {
        final ImageView image;
        final TextView title;

        ObjectViewHolder(View convertView) {
            this.image = (ImageView) convertView.findViewById(com.orange.ocara.R.id.object_description_image);
            this.title = (TextView) convertView.findViewById(com.orange.ocara.R.id.object_description_title);
        }
    }

}
