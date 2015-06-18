package edu.itba.hci.define.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.models.Order;

public class PurchaseListAdapter extends ArrayAdapter<Order> {

    private List<Order> orderList;

    public PurchaseListAdapter(Context context, int resource, List<Order> orderList) {
        super(context, resource, orderList);
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.purchase_list_item, parent, false);

        TextView textView1 = (TextView) rowView.findViewById(R.id.name);
        textView1.setText(orderList.get(position).getId());

        TextView textView2 = (TextView) rowView.findViewById(R.id.receivedDate);
        textView2.setText(orderList.get(position).getStatus());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.mipmap.ic_launcher);

        return rowView;
    }
}
