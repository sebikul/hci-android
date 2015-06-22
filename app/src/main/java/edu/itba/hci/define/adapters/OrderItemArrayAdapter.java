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

import edu.itba.hci.define.R;
import edu.itba.hci.define.models.OrderItem;


public class OrderItemArrayAdapter extends ArrayAdapter<OrderItem> {


    private final ImageLoader imageLoader;
    private OrderItem[] items;

    public OrderItemArrayAdapter(Context context, int resource, OrderItem[] objects) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.productitem_item, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.photo_img);
        TextView name = (TextView) convertView.findViewById(R.id.title_product);
        TextView brand = (TextView) convertView.findViewById(R.id.title_brand);
        TextView price = (TextView) convertView.findViewById(R.id.product_info);

        OrderItem p = items[position];
        imageLoader.displayImage(p.getProduct().getImageUrl(), image);
        name.setText(p.getProduct().getName());
        //brand.setText(p.getBrand());
        price.setText("$ " + p.getPrice());


        return convertView;
    }
}
