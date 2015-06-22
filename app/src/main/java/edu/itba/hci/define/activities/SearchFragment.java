package edu.itba.hci.define.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.adapters.ProductListAdapter;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ProductList;

public class SearchFragment extends Fragment {
    private String query = null;
    private ListView listView;
    private AsyncTask request;

    private View mProgressView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.category_product_list);
        listView.setClickable(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();

        mProgressView = view.findViewById(R.id.category_progress);


        if (args != null && args.containsKey("query")) {
            showProgress(true);
            query = args.getString("query");

            request = ApiManager.getProductsByName(query, 1, 500, new ApiProductFilter[]{}, new Callback<ProductList>() {
                @Override
                public void onSuccess(final ProductList response) {
                    ProductListAdapter adapter = new ProductListAdapter(getActivity(), R.layout.product_item, response.getProducts());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ProductActivity.class);
                            intent.putExtra("productId", response.getProducts().get(position).getId());
                            startActivity(intent);
                        }
                    });

                    showProgress(false);
                }

                @Override
                public void onError(ApiError error) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_img), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorConnection() {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_conection), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Log.v("SearchFragment", "Busqueda sin parametros, expandiendo cuadro de busqueda");
//            ((SearchableActivity) getActivity()).expandSearchView();
            showProgress(false);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (request != null) {
            request.cancel(true);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("SearchFragment", "Cargando menu del search");

        NavBasicActivity activity = ((NavBasicActivity) getActivity());

        String title;

        if (query == null) {
            title = getResources().getString(R.string.title_search);
        } else {
            title = getResources().getString(R.string.title_search) + ": \"" + query + "\"";
        }

        activity.setTitle(title);
        activity.setToggleDrawer(false);
        ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_search).setChecked(true);
        super.onCreateOptionsMenu(menu, inflater);

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
