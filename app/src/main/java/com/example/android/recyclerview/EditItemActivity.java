package com.example.android.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dev on 25.12.17.
 */

public class EditItemActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPrice;
    private EditText etQty;
    private Button submitEdit;
    private View.OnClickListener submitEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                submitUpdate();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            finish();
        }
    };
    private String editItemId;
    private Button deleteButton;
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                new NewItemActivity.AsyncRequest(new NewItemActivity.AsyncRequest.OnResponceComplete() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "delete status:" + response, Toast.LENGTH_SHORT).show();
                        System.out.println("del response:" + response);
                    }
                }).execute(new URL("http://192.168.31.140:8080/api/delete/" + editItemId));
                finish();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    };

    private void submitUpdate() throws MalformedURLException {
        new NewItemActivity.AsyncRequest(new NewItemActivity.AsyncRequest.OnResponceComplete() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),
                        "update status:" + response, Toast.LENGTH_SHORT).show();
            }
        }).execute(new URL(
                "http://192.168.31.140:8080/api/update/"
                        + editItemId + "/" + etName.getText().toString()
                        + "/" + etPrice.getText().toString() + "/" +
                        etQty.getText().toString()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 1.0);
        int qty = intent.getIntExtra("qty", 1);
        editItemId = intent.getStringExtra("id");

        etName = (EditText) findViewById(R.id.edit_item_name);
        etPrice = (EditText) findViewById(R.id.edit_item_price);
        etQty = (EditText) findViewById(R.id.edit_item_qty);

        etName.setText(name);
        etPrice.setText(String.valueOf(price));
        etQty.setText(String.valueOf(qty));

        submitEdit = (Button) findViewById(R.id.edit_item_submit_btn);
        submitEdit.setOnClickListener(submitEditClickListener);

        deleteButton = (Button) findViewById(R.id.delete_item);
        deleteButton.setOnClickListener(deleteClickListener);
    }
}
