package edu.itba.hci.define.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.itba.hci.define.R;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.ApiProductFilter;
import edu.itba.hci.define.models.ProductList;

public class HomeFragment extends Fragment {

    ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("HomeFragment", "Creando vista del fragamento de la vista principal");
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.sale1);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        loadSales(view, R.id.sale1, R.id.sale2, R.id.sale3);
        loadNews(view, R.id.new1, R.id.new2, R.id.new3);
        return view;
    }

    private void loadGeneric(final View view, int id, String filter, final int img1, final int img2, final int img3) {
        Log.v("HomeFragment", "Cargando imagenes de "+filter);
        ApiManager.getAllProducts(1, new ApiProductFilter(id, filter), new Callback<ProductList>() {

            @Override
            public void onSuccess(ProductList response) {
                if (response.getTotal() > 2) {
                    imageLoader.displayImage(response.getProducts().get(0).getImageUrl()[0], (ImageView) view.findViewById(img1));
                    imageLoader.displayImage(response.getProducts().get(1).getImageUrl()[0], (ImageView) view.findViewById(img2));
                    imageLoader.displayImage(response.getProducts().get(2).getImageUrl()[0], (ImageView) view.findViewById(img3));
                }
            }


            @Override
            public void onError(ApiError error) {
                Toast.makeText(getActivity(), "Hubo un error al cargar las imagenes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorConnection() {
                Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSales(final View view, int img1, int img2, int img3) {
        loadGeneric(view, 5, "Oferta", img1, img2, img3);
    }

    private void loadNews(final View view, int img1, int img2, int img3) {
        loadGeneric(view, 6, "Nuevo", img1, img2, img3);
    }
}
