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

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.models.Address;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ApiResponse;
import edu.itba.hci.define.models.Category;
import edu.itba.hci.define.models.CategoryList;
import edu.itba.hci.define.models.CreditCard;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.OrderList;
import edu.itba.hci.define.models.Product;
import edu.itba.hci.define.models.ProductList;
import edu.itba.hci.define.models.State;
import edu.itba.hci.define.models.StatesList;
import edu.itba.hci.define.models.Subcategory;
import edu.itba.hci.define.models.SubcategoryList;
import edu.itba.hci.define.models.User;

public class ApiManager {

    static private final String LOG_TAG = "ApiManager";

    static private final String BASE_URL = "http://eiffel.itba.edu.ar/hci/service3/";
    static private Gson gson;
    static private SharedPreferences preferences;
    static private DefineApplication context;


    private ApiManager() {
    }


    static public void initialize(SharedPreferences pref, DefineApplication con) {


        gson = new GsonBuilder()
                .registerTypeAdapter(Order.class, new ApiDeserializer<Order>("order"))
                .registerTypeAdapter(User.class, new ApiDeserializer<Order>("account"))
                .registerTypeAdapter(OrderList.class, new ApiDeserializer<OrderList>("orders"))
                .registerTypeAdapter(ProductList.class, new ApiDeserializer<ProductList>("products"))
                .registerTypeAdapter(Product.class, new ApiDeserializer<Product>("product"))
                .registerTypeAdapter(StatesList.class, new ApiDeserializer<StatesList>("states"))
                .registerTypeAdapter(CreditCard.class, new ApiDeserializer<CreditCard>("creditCard"))
                .registerTypeAdapter(Address.class, new ApiDeserializer<Address>("address"))
                .registerTypeAdapter(SubcategoryList.class, new ApiDeserializer<SubcategoryList>("subcategories"))
                .registerTypeAdapter(CategoryList.class, new ApiDeserializer<SubcategoryList>("categories"))
                .create();

        preferences = pref;

        context = con;
    }

    static public AsyncTask getOrderById(int id, Callback<Order> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));

        fillAuthenticationData(params);

        return makeApiCall("Order", "GetOrderById", params, callback, Order.class);

    }

    static public AsyncTask getAddressById(int id, Callback<Address> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));

        fillAuthenticationData(params);

        return makeApiCall("Account", "GetAddressById", params, callback, Address.class);

    }

    static public AsyncTask getCreditCardById(int id, Callback<CreditCard> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("id", Integer.toString(id));

        fillAuthenticationData(params);

        return makeApiCall("Account", "GetCreditCardById", params, callback, CreditCard.class);

    }

    static public AsyncTask getAllOrders(Callback<OrderList> callback) {

        Map<String, String> params = new HashMap<>(2);

        fillAuthenticationData(params);

        return makeApiCall("Order", "GetAllOrders", params, callback, OrderList.class);

    }

    static public AsyncTask getAccount(Callback<User> callback) {

        Map<String, String> params = new HashMap<>(2);

        fillAuthenticationData(params);

        return makeApiCall("Account", "GetAccount", params, callback, User.class);

    }

    static public AsyncTask getAllProducts(int page, int page_size, ApiProductFilter[] filters, Callback<ProductList> callback) {

        Map<String, String> params = new HashMap<>(3);

        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(page_size));

        if (filters != null) {
            String filterJson = gson.toJson(filters);

            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetAllProducts", params, callback, ProductList.class);

    }


    static public AsyncTask getAllProducts(int page, ApiProductFilter[] filters, Callback<ProductList> callback) {

        return getAllProducts(page, 8, filters, callback);

    }

    static public AsyncTask getAllProducts(int page, Callback<ProductList> callback) {

        return getAllProducts(page, null, callback);

    }

    static public AsyncTask getProductsByName(String name, int page, int page_size, ApiProductFilter[] filters, Callback<ProductList> callback) {

        Map<String, String> params = new HashMap<>(4);

        params.put("name", name);
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(page_size));

        if (filters != null) {
            String filterJson = gson.toJson(filters);
            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetProductsByName", params, callback, ProductList.class);

    }

    static public AsyncTask getProductsByName(String name, int page, ApiProductFilter[] filters, Callback<ProductList> callback) {
        return getProductsByName(name, page, 8, filters, callback);
    }


    static public AsyncTask getProductsByCategoryId(int id, int page, ApiProductFilter[] filters, Callback<ProductList> callback) {
        return getProductsByCategoryId(id, page, 8, filters, callback);
    }


    static public AsyncTask getProductsByCategoryId(int id, int page, int page_size, ApiProductFilter[] filters, Callback<ProductList> callback) {

        Map<String, String> params = new HashMap<>(4);

        params.put("id", String.valueOf(id));
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(page_size));

        if (filters != null) {
            String filterJson = gson.toJson(filters);
            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetProductsByCategoryId", params, callback, ProductList.class);

    }


    static public AsyncTask getProductsBySubcategoryId(int id, int page, ApiProductFilter[] filters, Callback<ProductList> callback) {
        return getProductsBySubcategoryId(id, page, 8, filters, callback);
    }


    static public AsyncTask getProductsBySubcategoryId(int id, int page, int page_size, ApiProductFilter[] filters, Callback<ProductList> callback) {

        Map<String, String> params = new HashMap<>(4);

        params.put("id", String.valueOf(id));
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(page_size));

        if (filters != null) {
            String filterJson = gson.toJson(filters);
            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetProductsBySubategoryId", params, callback, ProductList.class);

    }

    static public AsyncTask getAllSubcategories(int id, Callback<SubcategoryList> callback) {

        return getAllSubcategories(id, null, callback);

    }

    static public AsyncTask getAllSubcategories(int id, ApiProductFilter[] filters, Callback<SubcategoryList> callback) {

        Map<String, String> params = new HashMap<>(2);

        params.put("id", String.valueOf(id));

        if (filters != null) {
            String filterJson = gson.toJson(filters);

            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetAllSubcategories", params, callback, SubcategoryList.class);

    }

    static public AsyncTask getAllCategories(Callback<CategoryList> callback) {

        return getAllCategories(null, callback);
    }

    static public AsyncTask getAllCategories(ApiProductFilter[] filters, Callback<CategoryList> callback) {

        Map<String, String> params = new HashMap<>(1);

        if (filters != null) {
            String filterJson = gson.toJson(filters);

            params.put("filters", filterJson);
        }

        return makeApiCall("Catalog", "GetAllCategories", params, callback, CategoryList.class);

    }

    static public AsyncTask getAllStates(Callback<StatesList> callback) {

        return makeApiCall("Common", "GetAllStates", null, callback, StatesList.class);
    }

    static public AsyncTask login(String email, String password, Callback<User> callback) {
        Map<String, String> params = new HashMap<>(2);

        Checksum checksum = new CRC32();

        byte[] emailBytes = email.getBytes();

        checksum.update(emailBytes, 0, emailBytes.length);

        long checksumValue = checksum.getValue();

        params.put("username", String.valueOf(checksumValue));
        params.put("password", password);

        return makeApiCall("Account", "SignIn", params, callback, User.class);
    }

    static public void logout() {

        preferences.edit().clear().commit();
        context.clearCache();

        context.notifyLogOut();

    }


    static public AsyncTask getProductById(int id, Callback<Product> callback) {

        Map<String, String> params = new HashMap<>(1);

        params.put("id", String.valueOf(id));

        return makeApiCall("Catalog", "GetProductById", params, callback, Product.class);

    }

    static public <T extends ApiResponse> AsyncTask genericCall(String service, String method, Callback<T> callback, Class<T> type) {
        return makeApiCall(service, method, null, callback, type);
    }

    static public <T extends ApiResponse> AsyncTask genericCallWithAuth(String service, String method, Callback<T> callback, Class<T> type) {

        Map<String, String> params = new HashMap<>(2);

        fillAuthenticationData(params);

        return makeApiCall(service, method, params, callback, type);
    }

    static private <T extends ApiResponse> AsyncTask makeApiCall(String service, String method, Map<String, String> parameters, Callback<T> callback, Class<T> type) {

        String url = buildUrl(service, method, parameters);

        Log.d(LOG_TAG, "Calling URL: " + url);

        return new ApiCallTask<T>(callback, type).execute(url);

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

        params.put("username", preferences.getString("username", ""));
        params.put("authentication_token", preferences.getString("authentication_token", ""));

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

                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Order> orderList = new Gson().fromJson(content, listType);


                List<Order> cachedOrders = context.readFromCache("orders");

                if (cachedOrders != null) {

                    for (Order remoteOrder : orderList) {

                        for (Order cachedOrder : cachedOrders) {

                            if (cachedOrder.getId() == remoteOrder.getId()) {
//
//                                Log.v(LOG_TAG, "Cached Order: " + cachedOrder.toString());
//                                Log.v(LOG_TAG, "Remote Order: " + remoteOrder.toString());


                                remoteOrder.setNotifications(cachedOrder.hasNotifications());
                            }
                        }

                    }

                }

                try {
                    context.writeToCache("orders", orderList);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                response = (T) new OrderList(orderList);

            } else if (type == ProductList.class) {

                Log.v(LOG_TAG, "Deserializando lista de productos");
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Product> productList = new Gson().fromJson(content, listType);

                response = (T) new ProductList(productList, je.getAsJsonObject().get("total").getAsInt());

            } else if (type == StatesList.class) {

                Log.v(LOG_TAG, "Deserializando lista de estados");
                Type listType = new TypeToken<List<State>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<State> stateList = new Gson().fromJson(content, listType);

                response = (T) new StatesList(stateList);
            } else if (type == SubcategoryList.class) {

                Log.v(LOG_TAG, "Deserializando lista de subcategorias");
                Type listType = new TypeToken<List<Subcategory>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Subcategory> subcategories = new Gson().fromJson(content, listType);

                response = (T) new SubcategoryList(subcategories);
            } else if (type == CategoryList.class) {

                Log.v(LOG_TAG, "Deserializando lista de categorias");
                Type listType = new TypeToken<List<Category>>() {
                }.getType();
                // In this test code i just shove the JSON here as string.
                List<Category> categories = new Gson().fromJson(content, listType);

                response = (T) new CategoryList(categories);
            } else {

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

    static public class ApiCallTask<T extends ApiResponse> extends AsyncTask<String, Void, T> {

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

            Log.v(LOG_TAG, "Api response: " + result.toString());

            if (result.hasError()) {

                callback.onError(result.getError());

            } else {

                callback.onSuccess(result);

            }


        }

        @Override
        protected void onCancelled(T t) {
            super.onCancelled(t);

            Log.v(LOG_TAG, "Tarea cancelada.");
        }

    }

}

