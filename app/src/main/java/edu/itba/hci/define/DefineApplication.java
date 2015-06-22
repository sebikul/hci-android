package edu.itba.hci.define;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.User;

public class DefineApplication extends Application {

    static private final String PREFERENCES_BUCKET = "DEFINE_PREFERENCES";
    static private final String LOG_TAG = "DefineApplication";

    private static DefineApplication singleton;
    private SharedPreferences preferences;
    private User session;

    private DiskLruCache cache;


    public SharedPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        preferences = getSharedPreferences(PREFERENCES_BUCKET, MODE_PRIVATE);
        ApiManager.initialize(preferences, this);


        File cacheDir = getCacheDir();

        try {


            cache = DiskLruCache.open(cacheDir, 1, 1, 1024 * 1024 * 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (preferences.getString("authentication_token", null) != null) {
            Log.v(LOG_TAG, "Session encontrada");

            session = readFromCache("user");

        }

        updateUserData();

    }

    public User getSession() {
        return session;
    }

    public void setSession(final User session) {

        Log.v(LOG_TAG, "Guardando sesion en shared preferences");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", session.getUsername());
        editor.putString("authentication_token", session.getAuthToken());
        editor.commit();


        this.session = session;

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public boolean isLoggedIn() {
        return session != null;
    }

    public void updateUserData() {

        if (preferences.getString("authentication_token", null) != null) {

            ApiManager.getAccount(new Callback<User>() {
                @Override
                public void onSuccess(User response) {

                    try {

                        Log.v(LOG_TAG, "Actualizando datos locales del usuario");

                        response.setAuthToken(preferences.getString("authentication_token", null));

                        if (response.getAuthToken() == null) {
                            Log.v(LOG_TAG, "FUCK YOU");

                        }

                        setSession(response);
                        writeToCache("user", response);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ApiError error) {

                }

                @Override
                public void onErrorConnection() {

                }
            });

        }
    }

    public <T> void writeToCache(String key, T value) throws IOException {

        DiskLruCache.Editor editor = cache.edit(key);

        Log.v(LOG_TAG, "Guardando los datos del usuario: " + value.toString());

        OutputStream outputStream = editor.newOutputStream(0);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(value);

        objectOutputStream.close();
        outputStream.close();

        editor.commit();

    }

    public <T> T readFromCache(String key) {

        T obj = null;
        ObjectInputStream objectInputStream = null;
        DiskLruCache.Snapshot snapshot = null;
        InputStream inputStream = null;


        try {
            snapshot = cache.get(key);

            if (snapshot == null) {
                return null;
            }

            inputStream = snapshot.getInputStream(0);
            objectInputStream = new ObjectInputStream(inputStream);

            obj = (T) objectInputStream.readObject();

            Log.v(LOG_TAG, "Leyendo los datos del usuario: " + obj.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();

                }

                if (inputStream != null) {
                    inputStream.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (snapshot != null) {
                snapshot.close();

            }
        }


        return obj;


    }

    public void clearCache() {
        try {
            cache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyLogOut(){
        session=null;
    }

}