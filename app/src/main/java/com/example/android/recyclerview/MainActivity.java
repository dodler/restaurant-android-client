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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GreenAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.toString();

    private GreenAdapter mAdapter;
    private RecyclerView mNumbersList;

    private Toast mToast;
    private Button submitNewItemActivity;
    private View.OnClickListener newItemActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
            startActivity(intent);
        }
    };
    private List<RestaurantItem> itemList = new ArrayList<>();
    private Button updateItemsButton;
    public final View.OnClickListener updateItems = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                URL url = new  URL(GET_ALL_ITEMS);
                Log.d(TAG, "updateItems: ");
                new NewItemActivity.AsyncRequest(new NewItemActivity.AsyncRequest.OnResponceComplete() {
                    @Override
                    public void onResponse(String response) {
                        itemList = new ArrayList<>();
                        if (response == null) {
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray itemList = jsonObject.getJSONArray("itemList");
                            for (int i = 0; i < itemList.length(); i++) {
                                JSONObject itemObj = itemList.getJSONObject(i);
                                RestaurantItem item = json2item(itemObj);
                                MainActivity.this.itemList.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new GreenAdapter(itemList, MainActivity.this);
                        mNumbersList.setAdapter(mAdapter);
                    }
                }).execute(url);
            } catch (MalformedURLException e) {
                Log.d(TAG, "updateItems: " + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    private static RestaurantItem json2item(JSONObject itemObj) throws JSONException {
        RestaurantItem result = new RestaurantItem();
        if (itemObj.has("id")) {
            System.out.println(itemObj.getString("id"));
            result.setId(itemObj.getString("id"));
        }
        if (itemObj.has("name")) {
            result.setName(itemObj.getString("name"));
        }


        if (itemObj.has("price") && !itemObj.isNull("price")) {
            result.setPrice(itemObj.getDouble("price"));
        }
//
        if (itemObj.has("quantity") && !itemObj.isNull("quantity")) {
             result.setQty(itemObj.getInt("quantity"));
        }

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateItems.onClick(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateItems.onClick(null);

        updateItemsButton = (Button) findViewById(R.id.update_items);
        updateItemsButton.setOnClickListener(updateItems);

        submitNewItemActivity = (Button) findViewById(R.id.submit_new_item);
        submitNewItemActivity.setOnClickListener(newItemActivity);
        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);

        mNumbersList.setHasFixedSize(false);

        mAdapter = new GreenAdapter(itemList, this);
        mNumbersList.setAdapter(mAdapter);
    }

    public static final String GET_ALL_ITEMS = "http://192.168.31.140:8080/api/get";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_refresh:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                mAdapter = new GreenAdapter(itemList, this);
                mNumbersList.setAdapter(mAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // COMPLETED (10) Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {@link com.example.android.recyclerview.GreenAdapter.ListItemClickListener}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
        intent.putExtra("name", itemList.get(clickedItemIndex).getName());
        intent.putExtra("price", itemList.get(clickedItemIndex).getPrice());
        intent.putExtra("qty", itemList.get(clickedItemIndex).getQty());
        intent.putExtra("id", itemList.get(clickedItemIndex).getId());
        startActivity(intent);

        mToast.show();
    }
}
