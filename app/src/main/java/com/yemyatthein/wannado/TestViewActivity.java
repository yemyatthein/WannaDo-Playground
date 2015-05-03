package com.yemyatthein.wannado;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yemyatthein.wannado.data.DataContract;

public class TestViewActivity extends ActionBarActivity {

    private ContentValues createTestValueThingTable() {
        String sampleDesc = "Juve settled as Stefano Sturaro bustled into the box and saw an " +
                "effort charged down by Matias Silvestre in the 19th minute, while Fernando " +
                "Llorente blazed over ...";
        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.ThingEntry.COLUMN_NAME, "Algorithm Study");
        testValues.put(DataContract.ThingEntry.COLUMN_CREATED_DATE, 1419033600L);
        testValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, sampleDesc);
        testValues.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 0);
        testValues.put(DataContract.ThingEntry.COLUMN_CTOUCH, 0);
        return testValues;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_test);

        // Insert
        Button btnInsert = (Button) findViewById(R.id.testBtnInsert);
        final TextView txtView = (TextView) findViewById(R.id.testTxtView);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtView.setText("");
                ContentValues values = createTestValueThingTable();

                Time time = new Time();
                time.setToNow();
                String timeStamp = String.valueOf(time.toMillis(true));
                String currentName = values.getAsString(DataContract.ThingEntry.COLUMN_NAME);
                values.put(DataContract.ThingEntry.COLUMN_NAME, currentName + " " + timeStamp);

                getContentResolver().insert(
                        DataContract.ThingEntry.CONTENT_URI, values);
                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
            }
        });

        // View
        Button btnView = (Button) findViewById(R.id.testBtnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(DataContract.ThingEntry.CONTENT_URI,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(DataContract.ThingEntry.COLUMN_NAME);
                    int idIndex = cursor.getColumnIndex(DataContract.ThingEntry._ID);
                    String name = cursor.getString(nameIndex);
                    long thingId = cursor.getLong(idIndex);
                    txtView.setText(txtView.getText() + name + " (" + thingId + ")" + "\n");
                }
            }
        });

        // Clear
        Button btnClear = (Button) findViewById(R.id.testBtnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtView.setText("");
                getContentResolver().delete(
                        DataContract.ThingEntry.CONTENT_URI, null, null);
                Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
