package com.yemyatthein.wannado;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThingAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_CURRENT = 0;
    private static final int VIEW_TYPE_OTHER = 1;

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

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_CURRENT: {
                layoutId = R.layout.list_item_thing_current;
                break;
            }
            case VIEW_TYPE_OTHER: {
                layoutId = R.layout.list_item_thing;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_CURRENT: {
                final Context ctx = context;
                ImageView btnNew = (ImageView) view.findViewById(R.id.imgBtnCourtesy);
                btnNew.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(ctx, "Thanks for the Courtesy Touch ;)", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case VIEW_TYPE_OTHER: {
                break;
            }
        }

        String name = cursor.getString(1);
        viewHolder.name.setText(name);

    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_CURRENT : VIEW_TYPE_OTHER;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
