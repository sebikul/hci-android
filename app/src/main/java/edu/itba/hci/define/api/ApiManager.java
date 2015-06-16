package edu.itba.hci.define.api;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.itba.hci.define.models.ApiResponse;
import edu.itba.hci.define.models.Order;

public class ApiManager {

    static private final String LOG_TAG = "ApiManager";

    static private final String BASE_URL = "http://eiffel.itba.edu.ar/hci/service3/";
    static private Gson gson;
    static private SharedPreferences preferences;


    private ApiManager() {
    }

    static public void initialize(SharedPreferences pref) {

        Log.d(LOG_TAG, "Inicializando ApiManager");

        gson = new GsonBuilder()
                .registerTypeAdapter(Order.class, new ApiDeserializer<Order>("order"))
                .create();

        preferences = pref;
    }

    static public void getOrderById(int id, Callback<Order> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));

        fillAuthenticationData(params);

        makeApiCall("Order", "GetOrderById", params, callback, Order.class);

    }

    static public <T extends ApiResponse> void genericCall(String service, String method, Callback<T> callback, Class<T> type) {
        makeApiCall(service, method, null, callback, type);
    }

    static public <T extends ApiResponse> void genericCallWithAuth(String service, String method, Callback<T> callback, Class<T> type) {

        Map<String, String> params = new HashMap<>(2);

        fillAuthenticationData(params);

        makeApiCall(service, method, params, callback, type);
    }

    static private <T extends ApiResponse> void makeApiCall(String service, String method, Map<String, String> parameters, Callback<T> callback, Class<T> type) {

        String url = buildUrl(service, method, parameters);

        Log.d(LOG_TAG, "Calling URL: " + url);

        new ApiCallTask<T>(callback, type).execute(url);

    }

    static private String buildUrl(String service, String method, Map<String, String> parameters) {
        String url = BASE_URL + service + ".groovy?method=" + method;

        if (parameters != null) {
            for (Map.Entry<String, String> e : parameters.entrySet()) {
                url += "&" + e.getKey() + "=" + URLEncoder.encode(e.getValue());
            }
        }


        return url;

    }

    static private class ApiDeserializer<T extends ApiResponse> implements JsonDeserializer<T> {

        private String container;

        public ApiDeserializer(String container) {
            super();

            this.container = container;
        }

        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

            JsonElement content = je.getAsJsonObject().get(container);

            T response = new Gson().fromJson(content, type);

            if (response == null) {
                Class<T> tClass = (Class<T>) type;

                try {
                    response = tClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            if (je.getAsJsonObject().has("error")) {
                JsonElement errorElement = je.getAsJsonObject().get("error");
                ApiError error = new Gson().fromJson(errorElement, ApiError.class);
                response.setError(error);
            }

            return response;

        }
    }

    static private class ApiCallTask<T extends ApiResponse> extends AsyncTask<String, Void, T> {

        private Callback<T> callback;

        private Class<T> type;

        public ApiCallTask(Callback<T> callback, Class<T> type) {
            super();

            this.callback = callback;
            this.type = type;

        }

        @Override
        protected T doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                String response;

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    response = readStream(in);
                } finally {
                    urlConnection.disconnect();
                }

                return gson.fromJson(response, type);

            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(T result) {
            //textView.setText(result);

            if (result == null) {
                callback.onErrorConnection();
                return;
            }

            Log.v(LOG_TAG, result.toString());

            if (result.hasError()) {

                callback.onError(result.getError());

            } else {

                callback.onSuccess(result);

            }


        }
    }

    static private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    static private void fillAuthenticationData(Map<String, String> params) {

        params.put("username", preferences.getString("username", null));
        params.put("authentication_token", preferences.getString("authentication_token", null));

    }

}

