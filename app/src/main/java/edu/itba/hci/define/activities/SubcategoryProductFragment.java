package edu.itba.hci.define.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Diego on 22/06/2015.
 */
public class SubcategoryProductFragment extends Fragment {
    private int age, gender;
    private ListView listView;
    private AsyncTask request;

    private View mProgressView;
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.category_product_list);
        listView.setClickable(true);

        mProgressView = view.findViewById(R.id.category_progress);

        showProgress(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        title= args.getString("categoryName");
        age = args.getInt("age");
        gender = args.getInt("gender");
        ApiProductFilter[] filters;
        if(CategoryFragment.convertToString(gender)!=null){
            filters = new ApiProductFilter[2];
            filters[1]= new ApiProductFilter(1, CategoryFragment.convertToString(gender));
        }
        else {
            filters = new ApiProductFilter[1];
        }
        filters[0]=new ApiProductFilter(2, CategoryFragment.convertToString(age));

        request= ApiManager.getProductsBySubcategoryId(args.getInt("categoryId"), 1, 500, filters, new Callback<ProductList>() {
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
                Toast.makeText(getActivity(), getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("SaleFragment", "cancelando las llamadas");
        request.cancel(true);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        NavBasicActivity activity = ((NavBasicActivity) getActivity());
        activity.setTitle(title);
        activity.setToggleDrawer(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
