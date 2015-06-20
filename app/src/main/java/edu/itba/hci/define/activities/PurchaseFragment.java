package edu.itba.hci.define.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.itba.hci.define.R;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Order;

public class PurchaseFragment extends Fragment {

    static private final String LOG_TAG = "PurchaseFragment";

    private View mProgressView;
    private View orderView;

    private TextView mTotal;

    private TextView mAddress1;
    private TextView mAddress2;
    private TextView mPhone;

    private TextView mCreditCard;
    private TextView mInstallments;


    private int orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchase_fragment, container, false);

        mProgressView = view.findViewById(R.id.purchase_progress);
        orderView = view.findViewById(R.id.purchase_view);

        mTotal = (TextView) view.findViewById(R.id.purchase_total);

        mAddress1 = (TextView) view.findViewById(R.id.purchase_address1);
        mAddress2 = (TextView) view.findViewById(R.id.purchase_address2);
        mPhone = (TextView) view.findViewById(R.id.purchase_address_phone);

        mCreditCard = (TextView) view.findViewById(R.id.purchase_payment_card);
        mInstallments = (TextView) view.findViewById(R.id.purchase_payment_installments);

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

    private void loadOrderDetails() {

        showProgress(true);

        ApiManager.getOrderById(orderId, new OrderResponseCallback());

    }

    private void setViewFromOrder(Order order) {

        mTotal.setText("$" + order.getSubtotal());

        mPhone.setText("" + order.getAddress().getName());

        mCreditCard.setText(order.getCreditCard().getNumber());

    }

    private class OrderResponseCallback implements Callback<Order> {

        @Override
        public void onSuccess(Order response) {

            showProgress(false);

            setViewFromOrder(response);

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
    public void showProgress(final boolean show) {
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
}
