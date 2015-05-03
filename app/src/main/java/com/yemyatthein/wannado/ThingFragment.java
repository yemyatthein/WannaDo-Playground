package com.yemyatthein.wannado;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yemyatthein.wannado.data.DataContract;


public class ThingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int THING_LOADER = 0;
    ListView listView;

    public static final String[] THING_COLUMNS = {
            DataContract.ThingEntry.TABLE_NAME + "." + DataContract.ThingEntry._ID,
            DataContract.ThingEntry.COLUMN_NAME,
            DataContract.ThingEntry.COLUMN_DESCRIPTION,
            DataContract.ThingEntry.COLUMN_CREATED_DATE,
            DataContract.ThingEntry.COLUMN_IS_CURRENT,
            DataContract.ThingEntry.COLUMN_CTOUCH,
    };

    private ThingAdapter thingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_debug) {
            Intent intent = new Intent(getActivity().getApplicationContext(), TestViewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_new) {
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thingAdapter = new ThingAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_thing, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview_thing);
        listView.setAdapter(thingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        DetailActivity.class);

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                long thingId = cursor.getLong(0);
                String nameString = cursor.getString(1);
                String descriptionString = cursor.getString(2);
                int isCurrent = cursor.getInt(4);

                Bundle bundle = new Bundle();
                bundle.putLong("id", thingId);
                bundle.putString("name", nameString);
                bundle.putString("description", descriptionString);
                bundle.putInt("isCurrent", isCurrent);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(THING_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri allThingUri = DataContract.ThingEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),
                allThingUri,
                THING_COLUMNS,
                null,
                null,
                DataContract.ThingEntry.COLUMN_IS_CURRENT + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        thingAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        thingAdapter.swapCursor(null);
    }

}
