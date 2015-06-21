package edu.itba.hci.define.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.models.Product;

/**
 * Created by Diego on 21/06/2015.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {


    private final ImageLoader imageLoader;
    private List<Product> productList;

    public ProductListAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.productList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View singleView = inflater.inflate(R.layout.product_item, parent, false);

        ImageView image = (ImageView) singleView.findViewById(R.id.photo_img);
        TextView name = (TextView) singleView.findViewById(R.id.title_product);
        TextView brand = (TextView) singleView.findViewById(R.id.title_brand);
        TextView price = (TextView) singleView.findViewById(R.id.title_price);

        Product p = productList.get(position);
        imageLoader.displayImage(p.getImageUrl()[0], image);
        name.setText(p.getName());
        //brand.setText(p.getBrand());
        price.setText("$ "+p.getPrice());


        return singleView;
    }
}
