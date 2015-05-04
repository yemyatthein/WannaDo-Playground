package com.yemyatthein.wannado;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yemyatthein.wannado.data.DataContract;

public class ThingAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_CURRENT = 0;
    private static final int VIEW_TYPE_OTHER = 1;

    public static class ViewHolder {

        public final TextView name;
        public final TextView desc;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.list_item_thing_textview);
            desc = (TextView) view.findViewById(R.id.list_item_desc);
        }
    }

    public ThingAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());

        Log.i("YMT", "From new: cursor and view type : " + cursor.getInt(0) + "-" + viewType);

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

        Log.i("YMT", "From bind: cursor and view type : " + cursor.getInt(0) + "-" + viewType);

        switch (viewType) {
            case VIEW_TYPE_CURRENT: {

                ContentValues contentValues = Utils.convertCursorRowToContentValThing(cursor);

                ImageView btnNew = (ImageView) view.findViewById(R.id.imgBtnCourtesy);
                if (btnNew != null) {
                    btnNew.setOnClickListener(new CourtesyTouchListener(context, contentValues));

                    TextView txtCtouchCount = (TextView) view.findViewById(R.id.txtCTouchCount);
                    txtCtouchCount.setText(String.valueOf(cursor.getInt(5)));

                    String desc = cursor.getString(2);
                    viewHolder.desc.setText(desc);
                }
                break;
            }
            case VIEW_TYPE_OTHER: {
                TextView txtCtouchCountInactive = (TextView) view.findViewById(
                        R.id.txtCTouchCountInactive);
                txtCtouchCountInactive.setText(String.valueOf(cursor.getInt(5)));
                break;
            }
        }

        String name = cursor.getString(1);
        viewHolder.name.setText(name);

    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0)? VIEW_TYPE_CURRENT: VIEW_TYPE_OTHER;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    private class CourtesyTouchListener implements View.OnClickListener {

        private int currentCount;
        private Context context;
        private ContentValues contentValues;

        CourtesyTouchListener(Context context, ContentValues contentValues) {
            this.contentValues = contentValues;
            this.context = context;
            this.currentCount = contentValues.getAsInteger(DataContract.ThingEntry.COLUMN_CTOUCH);
        }

        @Override
        public void onClick(View v) {

            // Update ctouch count
            int thingId = contentValues.getAsInteger(DataContract.ThingEntry._ID);
            contentValues.put(DataContract.ThingEntry.COLUMN_CTOUCH, currentCount + 1);
            currentCount += 1;

            int rowsAffected = context.getContentResolver().update(DataContract.ThingEntry.CONTENT_URI,
                    contentValues,
                    DataContract.ThingEntry.TABLE_NAME + "." +
                            DataContract.ThingEntry._ID + " = ?",
                    new String[] {String.valueOf(thingId)});

            assert(rowsAffected == 1);

            /*
            TODO: Decide if necessary
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_ctouch);
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Number of Courtesy Touch: " + currentCount);

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            */
        }
    }
}
