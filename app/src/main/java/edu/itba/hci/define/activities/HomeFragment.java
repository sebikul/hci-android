package edu.itba.hci.define.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.Product;
import edu.itba.hci.define.models.ProductList;

public class HomeFragment extends Fragment {

    ImageLoader imageLoader;
    private AsyncTask saleRequest;
    private AsyncTask newRequest;
    private ListView saleListView, newListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("HomeFragment", "Creando vista del fragamento de la vista principal");
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        view.setVerticalScrollBarEnabled(true);
        setHasOptionsMenu(true);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        loadSales(view);
        loadNews(view);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v("HomeFragement", "Cancelando peticiones");
        saleRequest.cancel(true);
        newRequest.cancel(true);
    }

    private AsyncTask loadGeneric(final View view, int id, String filter, final int img1, final int img2, final int img3, final int name1, final int name2, final int name3, final int price1, final int price2, final int price3, final int brand1, final int brand2, final int brand3) {
        Log.v("HomeFragment", "Cargando imagenes de " + filter);


        return ApiManager.getAllProducts(1, 20, new ApiProductFilter[]{new ApiProductFilter(id, filter)}, new Callback<ProductList>() {

            @Override
            public void onSuccess(ProductList response) {

                if (response.getTotal() > 19) {
                    Random random = new Random();
                    int n = random.nextInt(20);
                    Product p1 = response.getProducts().get(n);
                    Product p2 = response.getProducts().get((n + 1) % 20);
                    Product p3 = response.getProducts().get((n + 2) % 20);
                    imageLoader.displayImage(p1.getImageUrl()[0], (ImageView) view.findViewById(img1));
                    ((TextView) view.findViewById(name1)).setText(p1.getName());
                    ((TextView) view.findViewById(price1)).setText("$ " + String.valueOf(p1.getPrice()));
                    ((TextView) view.findViewById(brand1)).setText(p1.getBrand());
                    imageLoader.displayImage(p2.getImageUrl()[0], (ImageView) view.findViewById(img2));
                    ((TextView) view.findViewById(name2)).setText(p2.getName());
                    ((TextView) view.findViewById(price2)).setText("$ " + p2.getPrice());
                    ((TextView) view.findViewById(brand2)).setText(p2.getBrand());
                    imageLoader.displayImage(p3.getImageUrl()[0], (ImageView) view.findViewById(img3));
                    ((TextView) view.findViewById(name3)).setText(p3.getName());
                    ((TextView) view.findViewById(price3)).setText("$ " + p3.getPrice());
                    ((TextView) view.findViewById(brand3)).setText(p3.getBrand());

                }
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
    }

    private void loadSales(final View view) {
        /*saleListView = (ListView) view.findViewById(R.id.sale_list);
        saleListView.setClickable(true);
        saleRequest=loadGenericList(saleListView, 5, "Oferta");*/

        saleRequest = loadGeneric(view, 5, "Oferta",
                R.id.sale_img_1, R.id.sale_img_2, R.id.sale_img_3,
                R.id.sale_name_1, R.id.sale_name_2, R.id.sale_name_3,
                R.id.sale_price_1, R.id.sale_price_2, R.id.sale_price_3,
                R.id.sale_brand_1, R.id.sale_brand_2, R.id.sale_brand_3);
    }


    private void loadNews(final View view) {
     /*   newListView = (ListView) view.findViewById(R.id.new_list);
        newListView.setClickable(true);
        newRequest=loadGenericList(newListView, 6, "Nuevo");*/
        newRequest = loadGeneric(view, 6, "Nuevo",
                R.id.new_img_1, R.id.new_img_2, R.id.new_img_3,
                R.id.new_name_1, R.id.new_name_2, R.id.new_name_3,
                R.id.new_price_1, R.id.new_price_2, R.id.new_price_3,
                R.id.new_brand_1, R.id.new_brand_2, R.id.new_brand_3);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("Home", "Vas a ver que esta entrando aca wacho");
        NavBasicActivity activity = ((NavBasicActivity) getActivity());
        activity.setTitle(R.string.app_name);
        activity.setToggleDrawer(true);
        ((NavigationView) activity.findViewById(R.id.nvView)).getMenu().findItem(R.id.item_home).setChecked(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
