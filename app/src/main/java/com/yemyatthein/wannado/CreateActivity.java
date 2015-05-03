package com.yemyatthein.wannado;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yemyatthein.wannado.data.DataContract;


public class CreateActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_create, container, false);
            Button btnAddCreate = (Button) rootView.findViewById(R.id.btnAddCreate);
            btnAddCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText txtName = (EditText) rootView.findViewById(R.id.txtNameCreate);
                    EditText txtDesc = (EditText) rootView.findViewById(R.id.txtDescCreate);
                    String name = txtName.getText().toString();
                    String desc = txtDesc.getText().toString();
                    if (!name.isEmpty() &&
                            !desc.isEmpty()) {
                        Time time = new Time();
                        time.setToNow();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataContract.ThingEntry.COLUMN_NAME, name);
                        contentValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, desc);
                        contentValues.put(DataContract.ThingEntry.COLUMN_CREATED_DATE,
                                time.toMillis(true));
                        contentValues.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 0);
                        contentValues.put(DataContract.ThingEntry.COLUMN_CTOUCH, 0);

                        Uri inserted = getActivity().getContentResolver().insert(
                                DataContract.ThingEntry.CONTENT_URI, contentValues);
                        Cursor cursor = getActivity().getContentResolver().query(inserted,
                                ThingFragment.THING_COLUMNS, null, null, null);
                        contentValues = Utils.convertCursorRowToContentValThing(cursor);
                        long thingId = contentValues.getAsInteger(DataContract.ThingEntry._ID);
                        int isCurrent = contentValues.getAsInteger(
                                DataContract.ThingEntry.COLUMN_IS_CURRENT);

                        Intent intent = new Intent(getActivity().getApplicationContext(),
                                DetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", thingId);
                        bundle.putString("name", name);
                        bundle.putString("description", desc);
                        bundle.putInt("isCurrent", isCurrent);

                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }
            });
            return rootView;
        }
    }
}
