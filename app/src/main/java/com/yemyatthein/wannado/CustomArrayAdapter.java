package com.yemyatthein.wannado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private static final int CURRENT_ROW = 0;
    private static final int REMAINING_ROW = 0;

    private final Context context;
    private final String[] values;

    public CustomArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_item_todo, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) return CURRENT_ROW;
        return REMAINING_ROW;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_todo, parent, false);
        if (position == 0) {
            rowView = inflater.inflate(R.layout.list_item_todo_current, parent, false);
        }
        TextView itemText = (TextView) rowView.findViewById(R.id.list_item_text);
        itemText.setText(values[position]);
        return rowView;
    }

}
