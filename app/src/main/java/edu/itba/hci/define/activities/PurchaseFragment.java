package edu.itba.hci.define.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.adapters.OrderItemArrayAdapter;
import edu.itba.hci.define.adapters.ProductListAdapter;
import edu.itba.hci.define.adapters.PurchaseListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Address;
import edu.itba.hci.define.models.CreditCard;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.ProductItem;
import edu.itba.hci.define.models.State;
import edu.itba.hci.define.models.StatesList;

public class PurchaseFragment extends Fragment {

    static private final String LOG_TAG = "PurchaseFragment";

    private View mProgressView;

    private View mProgressViewAddress;
    private View mProgressViewPayment;

    private View orderView;
    private View addressView;
    private View paymentView;

    private TextView mTotal;
    private TextView mStatus;

    private TextView mAddress1;
    private TextView mAddress2;
    private TextView mPhone;

    private TextView mCreditCard;
    private TextView mExpiration;

    private AsyncTask globalLoader = null;
    private AsyncTask addressLoader = null;
    private AsyncTask paymentLoader = null;

    private ListView productList;

    private int orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchase_fragment, container, false);

        mProgressView = view.findViewById(R.id.purchase_progress);
        mProgressViewAddress = view.findViewById(R.id.purchase_progress_address);
        mProgressViewPayment = view.findViewById(R.id.purchase_progress_payment);


        orderView = view.findViewById(R.id.purchase_view);

        addressView = view.findViewById(R.id.card_view_address);
        paymentView = view.findViewById(R.id.card_view_payment);

        mTotal = (TextView) view.findViewById(R.id.purchase_total);
        mStatus = (TextView) view.findViewById(R.id.purchase_status);


        mAddress1 = (TextView) view.findViewById(R.id.purchase_address1);
        mAddress2 = (TextView) view.findViewById(R.id.purchase_address2);
        mPhone = (TextView) view.findViewById(R.id.purchase_address_phone);

        mCreditCard = (TextView) view.findViewById(R.id.purchase_payment_card);
        mExpiration = (TextView) view.findViewById(R.id.purchase_payment_expiration);

        productList = (ListView) view.findViewById(R.id.product_list);

        Bundle args = getArguments();
        orderId = args.getInt("orderId");
        Log.v(LOG_TAG, "ID de orden: " + orderId);

        setHasOptionsMenu(true);

        loadOrderDetails();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getActivity().setTitle(String.format(getActivity().getResources().getString(R.string.purchase_title), orderId));

    }

    @Override
    public void onPause() {
        super.onPause();

        if (globalLoader != null) {
            globalLoader.cancel(true);
        }

        if (addressLoader != null) {
            addressLoader.cancel(true);
        }

        if (paymentLoader != null) {
            paymentLoader.cancel(true);
        }

    }

    private void loadOrderDetails() {

        showGlobalProgress(true);

        globalLoader = ApiManager.getOrderById(orderId, new OrderResponseCallback());

    }

    private void setSummaryFromOrder(Order order) {

        mTotal.setText("$" + order.getSubtotal());

        mPhone.setText("" + order.getAddress().getName());

        mStatus.setText(order.getStatus().toString());

    }

    private void setAddress(Address address) {

        mAddress1.setText(String.format("%s %s %s %s", address.getStreet(), address.getNumber(), address.getFloor(), address.getGate()));

        mPhone.setText(address.getPhoneNumber());

    }


    private void setPaymentView(CreditCard creditCard) {


        mCreditCard.setText(creditCard.getNumber());

        mExpiration.setText(creditCard.getExpirationDate());


    }

    private class OrderResponseCallback implements Callback<Order> {

        @Override
        public void onSuccess(Order response) {

            showGlobalProgress(false);

            showAddressProgress(true);
            showPaymentProgress(true);

            setSummaryFromOrder(response);

            addressLoader = ApiManager.getAddressById(response.getAddress().getId(), new AddressCallback());

            paymentLoader = ApiManager.getCreditCardById(response.getCreditCard().getId(), new CreditCardCallback());

            OrderItemArrayAdapter adapter = new OrderItemArrayAdapter(getActivity(), R.layout.product_item, response.getItems());

            productList.setAdapter(adapter);

        }

        @Override
        public void onError(ApiError error) {

        }

        @Override
        public void onErrorConnection() {

        }
    }

    private class CreditCardCallback implements Callback<CreditCard> {

        @Override
        public void onSuccess(CreditCard response) {
            showPaymentProgress(false);

            setPaymentView(response);
        }

        @Override
        public void onError(ApiError error) {

        }

        @Override
        public void onErrorConnection() {

        }
    }

    private class AddressCallback implements Callback<Address> {

        @Override
        public void onSuccess(Address response) {

            final Address address = response;


            ApiManager.getAllStates(new Callback<StatesList>() {
                @Override
                public void onSuccess(StatesList response) {

                    List<State> list = response.getStates();

                    String stateString = null;

                    for (State state : list) {

                        if (state.getStateId() == address.getProvince()) {
                            stateString = state.getName();
                        }

                    }

                    mAddress2.setText(String.format("%s, %s %s", address.getCity(), stateString, address.getZipCode()));

                    showAddressProgress(false);


                }

                @Override
                public void onError(ApiError error) {

                }

                @Override
                public void onErrorConnection() {

                }
            });

            showAddressProgress(false);

            setAddress(response);
        }

        @Override
        public void onError(ApiError error) {

        }

        @Override
        public void onErrorConnection() {

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showGlobalProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            orderView.setVisibility(show ? View.GONE : View.VISIBLE);
            orderView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    orderView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            orderView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showAddressProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            addressView.setVisibility(show ? View.GONE : View.VISIBLE);
            addressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    addressView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewAddress.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewAddress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressViewAddress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewAddress.setVisibility(show ? View.VISIBLE : View.GONE);
            addressView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showPaymentProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            paymentView.setVisibility(show ? View.GONE : View.VISIBLE);
            paymentView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    paymentView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressViewPayment.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressViewPayment.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressViewPayment.setVisibility(show ? View.VISIBLE : View.GONE);
            paymentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
