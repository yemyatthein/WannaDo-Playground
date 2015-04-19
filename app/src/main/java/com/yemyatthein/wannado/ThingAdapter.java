package com.yemyatthein.wannado;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ThingAdapter extends CursorAdapter {

    public static class ViewHolder {

        public final TextView name;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.list_item_thing_textview);
        }
    }

    public ThingAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_thing, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = cursor.getString(1);
        viewHolder.name.setText(name);
    }
}
