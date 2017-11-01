package com.ray.lib_map.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author      : leixing
 * Date        : 2017-10-20
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

@SuppressWarnings("WeakerAccess")
public class IOUtil {
    private static final String USER_AGENT = "Mozilla/5.0";

    private IOUtil() {
        throw new UnsupportedOperationException();
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String urlString) {
        return get(urlString, 10 * 1000);
    }

    public static String get(String urlString, int connectTime) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder resultBuilder = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(connectTime);
            connection.setReadTimeout(10 * 1000);
            connection.setUseCaches(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            is = connection.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                resultBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(br);
            close(isr);
            close(is);
        }

        return resultBuilder.toString();
    }
}
