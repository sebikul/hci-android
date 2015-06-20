package edu.itba.hci.define.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import edu.itba.hci.define.R;
import edu.itba.hci.define.adapters.PurchaseListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.OrderList;


public class PurchasesFragment extends Fragment {

    static private final String LOG_TAG = "PurchasesFragment";

    private ListView listView;
    private View mProgressView;

    private AsyncTask loaderTask;

    private SwipeRefreshLayout swipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchases_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.purchases_list);
        mProgressView = view.findViewById(R.id.purchases_progress);

        listView.setClickable(true);

        showProgress(true);

        updateOrderList(false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.v(LOG_TAG, "Tenes que actualizar las ordenes wacho");
                updateOrderList(true);


                //fixme
                //todo
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.v(LOG_TAG, "Cancelando la llamada a la api.");

        loaderTask.cancel(true);

    }

    void updateOrderList(final boolean showToast) {

        loaderTask = ApiManager.getAllOrders(new Callback<OrderList>() {
            @Override
            public void onSuccess(OrderList response) {
                showProgress(false);

                PurchaseListAdapter adapter = new PurchaseListAdapter(getActivity(), R.layout.purchase_list_item, response.getOrders());
                listView.setClickable(true);

                listView.setAdapter(adapter);

                swipeContainer.setRefreshing(false);

                if (showToast) {
                    Toast.makeText(PurchasesFragment.this.getActivity(), getResources().getString(R.string.purchases_updated), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(ApiError error) {

            }

            @Override
            public void onErrorConnection() {

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.v(LOG_TAG,"Cancelando la llamada a la api.");

        loaderTask.cancel(true);

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

            listView.setVisibility(show ? View.GONE : View.VISIBLE);
            listView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            listView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
