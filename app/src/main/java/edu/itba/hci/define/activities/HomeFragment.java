package edu.itba.hci.define.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Random;

import edu.itba.hci.define.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("HomeFragment", "Creando vista del fragamento de la vista principal");
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        view.setVerticalScrollBarEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.sale_img_1);
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

    private AsyncTask loadGeneric(final View view, int id, String filter, final int img1, final int img2, final int img3, final int name1, final int name2, final int name3, final int price1, final int price2, final int price3) {
        Log.v("HomeFragment", "Cargando imagenes de " + filter);
        return ApiManager.getAllProducts(1,20, new ApiProductFilter(id, filter), new Callback<ProductList>() {

            @Override
            public void onSuccess(ProductList response) {
                if (response.getTotal() > 19) {
                    Random random = new Random();
                    int n=random.nextInt(20);
                    Product p1=response.getProducts().get(n);
                    Product p2=response.getProducts().get((n+1)%20);
                    Product p3=response.getProducts().get((n+2)%20);
                    imageLoader.displayImage(p1.getImageUrl()[0], (ImageView) view.findViewById(img1));
                    ((TextView) view.findViewById(name1)).setText(p1.getName());
                    ((TextView) view.findViewById(price1)).setText("$ "+String.valueOf(p1.getPrice()));
                    imageLoader.displayImage(p2.getImageUrl()[0], (ImageView) view.findViewById(img2));
                    ((TextView) view.findViewById(name2)).setText(p2.getName());
                    ((TextView) view.findViewById(price2)).setText("$ "+p2.getPrice());
                    imageLoader.displayImage(p3.getImageUrl()[0], (ImageView) view.findViewById(img3));
                    ((TextView) view.findViewById(name3)).setText(p3.getName());
                    ((TextView) view.findViewById(price3)).setText("$ "+p3.getPrice());

                }
            }


            @Override
            public void onError(ApiError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_img) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorConnection() {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_conection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSales(final View view) {
        saleRequest = loadGeneric(view, 5, "Oferta",
                R.id.sale_img_1, R.id.sale_img_2, R.id.sale_img_3, 
                R.id.sale_name_1, R.id.sale_name_2, R.id.sale_name_3,
                R.id.sale_price_1, R.id.sale_price_2, R.id.sale_price_3);
    }

    private void loadNews(final View view) {
        newRequest = loadGeneric(view, 6, "Nuevo",
                R.id.new_img_1, R.id.new_img_2, R.id.new_img_3,
                R.id.new_name_1, R.id.new_name_2, R.id.new_name_3,
                R.id.new_price_1, R.id.new_price_2, R.id.new_price_3);
    }
}
