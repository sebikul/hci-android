package edu.itba.hci.define.api;

/**
 * Created by sebikul on 6/9/15.
 */
public interface Callback<T> {

    void onSuccess(T response);

    void onError(ApiError error);

    void onErrorConnection();

}
