package com.yemyatthein.wannado;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yemyatthein.wannado.data.DataContract;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            final TextView txtName = (TextView) rootView.findViewById(R.id.txtDetailTitle);
            final TextView txtDescription = (TextView) rootView.findViewById(R.id.txtDetailDescription);
            final TextView txtWarning = (TextView) rootView.findViewById(R.id.txtDetailWarning);

            Bundle b = getActivity().getIntent().getExtras();

            final String nameString = b.getString("name", "");
            String descriptionString = b.getString("description", "");
            final long thingId = b.getLong("id", -1L);
            final int isCurrent = b.getInt("isCurrent", 0);

            txtName.setText(nameString);
            txtDescription.setText(descriptionString);


            final Button btnSwitchFocus = (Button) rootView.findViewById(R.id.btnSwitchFocus);
            btnSwitchFocus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thingId == -1L) {
                        return;
                    }

                    // Remove current focus

                    Cursor cursorOld = getActivity().getContentResolver().query(
                            DataContract.ThingEntry.CONTENT_URI,
                            null, DataContract.ThingEntry.COLUMN_IS_CURRENT + " = ?",
                            new String[] {String.valueOf(1)}, null);
                    if (cursorOld.moveToFirst()) {
                        long thingIdOld = cursorOld.getLong(0);
                        ContentValues contentValuesOld = Utils.convertCursorRowToContentValThing(cursorOld);
                        contentValuesOld.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 0);
                        contentValuesOld.put(DataContract.ThingEntry.COLUMN_CTOUCH, 0);

                        int rowsAffectedOld = getActivity().getContentResolver().update(
                                DataContract.ThingEntry.CONTENT_URI,
                                contentValuesOld,
                                DataContract.ThingEntry.TABLE_NAME + "." +
                                        DataContract.ThingEntry._ID + " = ?",
                                new String[] {String.valueOf(thingIdOld)});

                        assert(rowsAffectedOld == 1);

                        // TODO: Record the end in Express table
                    }


                    // Set new focus

                    Cursor cursor = getActivity().getContentResolver().query(
                            DataContract.ThingEntry.buildThingUri(thingId),
                            ThingFragment.THING_COLUMNS, null, null, null);
                    if (!cursor.moveToFirst()) {
                        Log.i("YMT-", "No cursor for " + thingId);
                        return;
                    }
                    ContentValues contentValues = Utils.convertCursorRowToContentValThing(cursor);
                    contentValues.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 1);

                    int rowsAffected = getActivity().getContentResolver().update(
                            DataContract.ThingEntry.CONTENT_URI,
                            contentValues,
                            DataContract.ThingEntry.TABLE_NAME + "." +
                                    DataContract.ThingEntry._ID + " = ?",
                            new String[] {String.valueOf(thingId)});

                    assert(rowsAffected == 1);

                    txtWarning.setVisibility(View.VISIBLE);
                    btnSwitchFocus.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "This has become your current focus now.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            if (isCurrent == 0) {
                txtWarning.setVisibility(View.GONE);
            }
            else {
                btnSwitchFocus.setVisibility(View.GONE);
            }

            return rootView;
        }
    }
}
