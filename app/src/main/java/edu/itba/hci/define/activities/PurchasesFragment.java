package edu.itba.hci.define.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.adapters.PurchaseListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.broadcasts.AlarmReceiver;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.OrderList;


public class PurchasesFragment extends Fragment {

    static private final String LOG_TAG = "PurchasesFragment";

    private ListView listView;
    private View mProgressView;

    private AsyncTask loaderTask;

    private SwipeRefreshLayout swipeContainer;

    private DefineApplication context;

    BroadcastReceiver broadcastReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchases_fragment, container, false);

        context = (DefineApplication) getActivity().getApplicationContext();

        listView = (ListView) view.findViewById(R.id.purchases_list);
        mProgressView = view.findViewById(R.id.purchases_progress);

        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Order order = (Order) listView.getItemAtPosition(position);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("order");

                Fragment fragment = new PurchaseFragment();

                Bundle args = new Bundle();
                args.putInt("orderId", order.getId());
                fragment.setArguments(args);

                transaction.replace(R.id.content, fragment, "order");

                transaction.commit();

            }
        });

        showProgress(true);

        updateOrderList(false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateOrderList(true);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        setHasOptionsMenu(true);

        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(LOG_TAG, "Actualizando lista de ordenes");
                swipeContainer.setRefreshing(true);
                updateOrderList(true);
                abortBroadcast();
            }
        };

        Log.v(LOG_TAG, "Registrando broadcast");
        IntentFilter intentFilter = new IntentFilter(AlarmReceiver.NOTIFICATION_BROADCAST);
        intentFilter.setPriority(3);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(loaderTask.getStatus()!= AsyncTask.Status.FINISHED){
            Log.v(LOG_TAG, "Cancelando la llamada a la api.");

            loaderTask.cancel(true);
        }

        Log.v(LOG_TAG, "Sacando el registro a refresh alarm");
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    void updateOrderList(final boolean showToast) {

        loaderTask = ApiManager.getAllOrders(new Callback<OrderList>() {
            @Override
            public void onSuccess(OrderList response) {

                PurchaseListAdapter adapter = new PurchaseListAdapter(getActivity(), R.layout.purchase_list_item, response.getOrders());

                listView.setAdapter(adapter);

                if (showToast) {
                    Toast.makeText(PurchasesFragment.this.getActivity(), getResources().getString(R.string.purchases_updated), Toast.LENGTH_SHORT).show();
                }

                showProgress(false);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onError(ApiError error) {

            }

            @Override
            public void onErrorConnection() {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("asd", "11111) ----------------------------------------------");

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.v("asd", "---------------------------------------------");
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavBasicActivity activity = ((NavBasicActivity) getActivity());
        activity.setTitle(R.string.title_purchases);
        activity.setToggleDrawer(false);
        ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_purchases).setChecked(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
