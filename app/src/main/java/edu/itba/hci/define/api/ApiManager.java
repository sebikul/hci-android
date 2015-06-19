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
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ApiResponse;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.OrderList;
import edu.itba.hci.define.models.Product;
import edu.itba.hci.define.models.ProductList;
import edu.itba.hci.define.models.User;

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
                .registerTypeAdapter(User.class, new ApiDeserializer<Order>("account"))
                .registerTypeAdapter(OrderList.class, new ApiDeserializer<OrderList>("orders"))
                .registerTypeAdapter(ProductList.class, new ApiDeserializer<ProductList>("products"))
                .registerTypeAdapter(Product.class,new ApiDeserializer<Product>("products"))
                .create();

        preferences = pref;
    }

    static public void getOrderById(int id, Callback<Order> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));

        fillAuthenticationData(params);

        makeApiCall("Order", "GetOrderById", params, callback, Order.class);

    }

    static public void getAllOrders(Callback<OrderList> callback) {

        Map<String, String> params = new HashMap<>(2);

        fillAuthenticationData(params);

        makeApiCall("Order", "GetAllOrders", params, callback, OrderList.class);

    }

    static public void getAllProducts(int page, ApiProductFilter filter, Callback<ProductList> callback) {

        Map<String, String> params = new HashMap<>(2);

        params.put("page", String.valueOf(page));

        if (filter != null) {
            String filterJson = gson.toJson(filter);

            params.put("filter", "[" + filterJson + "]");
        }

        makeApiCall("Catalog", "GetAllProducts", params, callback, ProductList.class);

    }

    static public void getAllProducts(int page, Callback<ProductList> callback) {

        getAllProducts(page, null, callback);

    }

    // TODO: getAllProducts (que reciba filtros, el id no es necesario), getProductById


    static public void login(String email, String password, Callback<User> callback) {
        Map<String, String> params = new HashMap<>(2);

        Checksum checksum = new CRC32();

        byte[] emailBytes = email.getBytes();

        checksum.update(emailBytes, 0, emailBytes.length);

        long checksumValue = checksum.getValue();

        params.put("username", String.valueOf(checksumValue));
        params.put("password", password);

        makeApiCall("Account", "SignIn", params, callback, User.class);
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

    static private class ApiDeserializer<T extends ApiResponse> implements JsonDeserializer<T> {

        private String container;

        public ApiDeserializer(String container) {
            super();

            this.container = container;
        }

        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

            JsonElement content = je.getAsJsonObject().get(container);

            T response;

            if (type == OrderList.class) {

                Log.v(LOG_TAG, "Deserializando lista de ordenes");
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Order> orderList = new Gson().fromJson(content, listType);

                response = (T) new OrderList(orderList);

            }else if(type == ProductList.class){

                Log.v(LOG_TAG, "Deserializando lista de productos");
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Product> productList = new Gson().fromJson(content, listType);

                response = (T) new ProductList(productList);

            } else {

                if (type == Product.class) {
                    content = content.getAsJsonArray().get(0);
                }

                response = new Gson().fromJson(content, type);

                if (response == null) {
                    Class<T> tClass = (Class<T>) type;

                    try {
                        response = tClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (type == User.class) {

                        JsonElement tokenElement = je.getAsJsonObject().get("authenticationToken");

                        String token = new Gson().fromJson(tokenElement, String.class);

                        ((User) response).setAuthToken(token);
                    }
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

}

