package edu.itba.hci.define.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.itba.hci.define.R;

public class PurchaseListAdapter extends ArrayAdapter<Map<String, String>> {
    private List<Map<String,String>> values;

    public PurchaseListAdapter(Context context, int resource, List<Map<String,String>> values) {
        super(context, resource, values);
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.purchase_list_item, parent, false);

        TextView textView1 = (TextView) rowView.findViewById(R.id.name);
        textView1.setText(values.get(position).get("nombre"));

        TextView textView2 = (TextView) rowView.findViewById(R.id.receivedDate);
        textView2.setText(values.get(position).get("descripcion"));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setImageResource(R.mipmap.ic_launcher);

        return rowView;
    }
}
