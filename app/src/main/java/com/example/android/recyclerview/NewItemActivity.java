package com.example.android.recyclerview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dev on 25.12.17.
 */

public class NewItemActivity extends AppCompatActivity {

    private static final String TAG = "NewItemActivity";
    private Button submitButton;
    private View.OnClickListener sendNewItemRequest = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                sendRequestForNewItem(etPrice.getText().toString(),
                        qty.getText().toString(),
                        name.getText().toString());
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "onClick: " +e.getMessage());
            }
        }
    };

    public static final class AsyncRequest extends AsyncTask<URL, Void, String>{

        private OnResponceComplete onResponceComplete;

        public interface OnResponceComplete{
            void onResponse(String response);
        }

        public AsyncRequest(){
            onResponceComplete = null;
        }

        public AsyncRequest(OnResponceComplete onResponceComplete){
            this.onResponceComplete = onResponceComplete;
        }

        @Override
        protected String doInBackground(URL... urls) {
            Log.d(TAG, "doInBackground: " +  urls[0]);
            assert urls.length == 1;
            try {
                String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                System.out.println(responseFromHttpUrl);
                return responseFromHttpUrl;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: " + e.getMessage());
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("on post execute " + s);
            if (onResponceComplete != null && s != null) {
                onResponceComplete.onResponse(s);
            }
        }
    }

    private void sendRequestForNewItem(String etPrice, String qty, String name) throws IOException {
        String urlStr = "http://192.168.31.140:8080/api/create/" + name + "/" + etPrice + "/" + qty;
        Log.d(TAG, "sendRequestForNewItem: url:" + urlStr);
        URL url = new URL(urlStr);
        new AsyncRequest(new AsyncRequest.OnResponceComplete() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "create status:" + response, Toast.LENGTH_SHORT).show();
            }
        }).execute(url);
    }

    private EditText etPrice;
    private EditText qty;
    private EditText name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        Log.i(TAG, "onCreaten: new item on create");

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(sendNewItemRequest);

        etPrice = (EditText) findViewById(R.id.price);
        qty = (EditText) findViewById(R.id.qty);
        name = (EditText) findViewById(R.id.name);

    }
}
