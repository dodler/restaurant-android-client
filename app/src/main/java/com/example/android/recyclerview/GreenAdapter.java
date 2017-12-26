/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {

    private static final String TAG = GreenAdapter.class.getSimpleName();

    private List<RestaurantItem> itemList;
    // COMPLETED (3) Create a final private ListItemClickListener called mOnClickListener
    final private ListItemClickListener mOnClickListener;

    private static int viewHolderCount;

    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    // COMPLETED (4) Add a ListItemClickListener as a parameter to the constructor and store it in mOnClickListener
    public GreenAdapter(List<RestaurantItem> itemList, ListItemClickListener listener) {
        this.itemList = itemList;
        mOnClickListener = listener;
        viewHolderCount = 0;
        mNumberItems = itemList.size();
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.number_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        // COMPLETED (12) Set the text of viewHolderIndex to "ViewHolder index: " + viewHolderCount
        viewHolder.priceTvView.setText(String.valueOf(itemList.get(viewHolderCount).getPrice()));
        viewHolder.nameTvView.setText(String.valueOf(itemList.get(viewHolderCount).getName()));
        viewHolder.qtyTvView.setText(String.valueOf(itemList.get(viewHolderCount).getQty()));

        // COMPLETED (13) Use ColorUtils.getViewHolderBackgroundColorFromInstance and pass in a Context and the viewHolderCount
        int backgroundColorForViewHolder = ColorUtils
                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
        // COMPLETED (14) Set the background color of viewHolder.itemView with the color from above
        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);

        // COMPLETED (15) Increment viewHolderCount and log its value
        viewHolderCount++;
        System.out.println("count:" + viewHolderCount);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    // COMPLETED (5) Implement OnClickListener in the NumberViewHolder class

    class NumberViewHolder extends RecyclerView.ViewHolder
        implements OnClickListener {

        TextView nameTvView;
        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView priceTvView;
        // Will display which ViewHolder is displaying this data
        TextView qtyTvView;

        int pos;


        public NumberViewHolder(View itemView) {
            super(itemView);

            priceTvView = (TextView) itemView.findViewById(R.id.tv_price);
            qtyTvView = (TextView) itemView.findViewById(R.id.tv_qty);
            nameTvView = (TextView) itemView.findViewById(R.id.tv_name);

            // COMPLETED (7) Call setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }

        void bind(int pos) {
            System.out.println("binding " + pos);
            this.pos = pos;
//            priceTvView.setText(itemList.get(pos).getName());
        }


        // COMPLETED (6) Override onClick, passing the clicked item's position (getAdapterPosition()) to mOnClickListener via its onListItemClick method
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
