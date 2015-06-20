package edu.itba.hci.define.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.models.Order;

public class PurchaseListAdapter extends ArrayAdapter<Order> {

    private List<Order> orderList;

    private TextDrawable.IShapeBuilder drawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public PurchaseListAdapter(Context context, int resource, List<Order> orderList) {
        super(context, resource, orderList);

        drawableBuilder = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                //.fontSize(30) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig();

        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.purchase_list_item, parent, false);

        ImageView image = (ImageView) rowView.findViewById(R.id.purchase_icon);
        TextView purchaseIdText = (TextView) rowView.findViewById(R.id.purchase_id);
        TextView purchaseDateText = (TextView) rowView.findViewById(R.id.purchase_date);

        Order order = orderList.get(position);

        String status = order.getStatus().name();

        int color;

        switch (order.getStatus()) {
            case CONFIRMED:
                color = Color.parseColor("#FFA000");
                break;

            case TRANSPORTED:
                color = Color.parseColor("#03A9F4");
                break;


            case DELIVERED:
                color = Color.parseColor("#4CAF50");
                break;

            default:
                color = Color.parseColor("#4CAF5");

        }

        TextDrawable drawable = drawableBuilder.buildRound(String.valueOf(status.charAt(0)), color);
        image.setImageDrawable(drawable);

        purchaseIdText.setText("#" + order.getId());
        purchaseDateText.setText(order.getReceivedDate());

        return rowView;
    }


}
