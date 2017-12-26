package com.example.android.recyclerview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by dev on 26.12.17.
 */

public class NetworkUtils {
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);

            boolean hasInput = scanner.hasNext();
            String next = scanner.next();
            System.out.println(next);
            if (hasInput) {
                return next;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
