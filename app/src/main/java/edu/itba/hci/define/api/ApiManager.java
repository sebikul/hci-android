package edu.itba.hci.define.api;

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

import edu.itba.hci.define.models.Order;

public class ApiManager {

    static private final String LOG_TAG = "ApiManager";

    static private final String BASE_URL = "http://eiffel.itba.edu.ar/hci/service3/";
    static private ApiManager obj;
    static private Gson gson;


    private ApiManager() {
    }

    static public ApiManager getService() {

        if (obj == null) {
            obj = new ApiManager();

            gson = new GsonBuilder()
                    .registerTypeAdapter(Order.class, new ApiDeserializer<Order>("order"))
                    .create();

        }

        return obj;

    }

    public void getOrderById(int id, Callback<Order> callback) {

        HashMap<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));
        params.put("username", "janedoe");//fixme
        params.put("authentication_token", "a8c0d2a9d332574951a8e4a0af7d516f");//fixme

        makeApiCall("Order", "GetOrderById", params, callback, Order.class);

    }

    private <T> void makeApiCall(String service, String method, HashMap<String, String> parameters, Callback<T> callback, Class<T> type) {

        String url = buildUrl(service, method, parameters);

        Log.d(LOG_TAG, "Calling URL: " + url);

        new ApiCallTask<T>(callback, type).execute(url);

    }

    private String buildUrl(String service, String method, HashMap<String, String> parameters) {
        String url = BASE_URL + service + ".groovy?method=" + method;

        for (Map.Entry<String, String> e : parameters.entrySet()) {
            url += "&" + e.getKey() + "=" + URLEncoder.encode(e.getValue());
        }

        return url;

    }

    static private class ApiDeserializer<T> implements JsonDeserializer<T> {

        private String container;

        public ApiDeserializer(String container) {
            super();

            this.container = container;
        }

        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            // Get the "content" element from the parsed JSON
            JsonElement content = je.getAsJsonObject().get(container);

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(content, type);

        }
    }

    private class ApiCallTask<T> extends AsyncTask<String, Void, T> {

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


                Log.v(LOG_TAG, response);

                return gson.fromJson(response, type);

            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(T result) {
            //textView.setText(result);


            callback.onSuccess(result);
        }
    }

    private String readStream(InputStream is) {
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

}

