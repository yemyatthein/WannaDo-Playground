package com.yemyatthein.wannado;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yemyatthein.wannado.data.DataContract;

import java.text.SimpleDateFormat;

public class TestViewActivity extends ActionBarActivity {

    private ContentValues createTestValueThingTable() {
        String sampleDesc = "Juve settled as Stefano Sturaro bustled into the box and saw an " +
                "effort charged down by Matias Silvestre in the 19th minute, while Fernando " +
                "Llorente blazed over ...";

        Time time = new Time();
        time.setToNow();

        Log.i("YMT-Inserted", String.valueOf(time.toMillis(true)));

        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.ThingEntry.COLUMN_NAME, "Algorithm Study");
        testValues.put(DataContract.ThingEntry.COLUMN_CREATED_DATE, time.toMillis(true));
        testValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, sampleDesc);
        testValues.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 1);
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
        final TextView txtViewExpress = (TextView) findViewById(R.id.testTxtViewExpress);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtView.setText("");

                // Shift  focus
                Cursor cursorOld = getContentResolver().query(
                        DataContract.ThingEntry.CONTENT_URI,
                        null, DataContract.ThingEntry.COLUMN_IS_CURRENT + " = ?",
                        new String[] {String.valueOf(1)}, null);
                if (cursorOld.moveToFirst()) {
                    long thingIdOld = cursorOld.getLong(0);
                    ContentValues contentValuesOld = Utils.convertCursorRowToContentValThing(
                            cursorOld);
                    contentValuesOld.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, 0);

                    int rowsAffectedOld = getContentResolver().update(
                            DataContract.ThingEntry.CONTENT_URI,
                            contentValuesOld,
                            DataContract.ThingEntry.TABLE_NAME + "." +
                                    DataContract.ThingEntry._ID + " = ?",
                            new String[] {String.valueOf(thingIdOld)});

                    assert(rowsAffectedOld == 1);

                    Log.i("TestView-YMT", "Shifted focus.");

                }

                // TODO: Switch focus record here

                ContentValues values = createTestValueThingTable();

                Time time = new Time();
                time.setToNow();

                SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd MMMM yyyy");
                String monthDayString = monthDayFormat.format(time.toMillis(true));

                Log.i("YMT", "time is " + monthDayString);

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
                txtView.setText("");
                txtViewExpress.setText("");

                Cursor cursor = getContentResolver().query(DataContract.ThingEntry.CONTENT_URI,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(DataContract.ThingEntry.COLUMN_NAME);
                    int idIndex = cursor.getColumnIndex(DataContract.ThingEntry._ID);
                    int active = cursor.getColumnIndex(DataContract.ThingEntry.COLUMN_IS_CURRENT);

                    String name = cursor.getString(nameIndex);
                    long thingId = cursor.getLong(idIndex);
                    int current = cursor.getInt(active);

                    txtView.setText(txtView.getText() + name + " (" + thingId + ", "
                            + current + ")" + "\n");
                }

                Cursor cursorExpress = getContentResolver().query(
                        DataContract.ExpressEntry.CONTENT_URI,
                        null, null, null, null);
                while (cursorExpress.moveToNext()) {
                    int eThingId = cursorExpress.getColumnIndex(
                            DataContract.ExpressEntry.COLUMN_THING_ID);
                    int eType = cursorExpress.getColumnIndex(
                            DataContract.ExpressEntry.COLUMN_TYPE);
                    int eCTouchAtm = cursorExpress.getColumnIndex(
                            DataContract.ExpressEntry.COLUMN_CTOUCH_ATM);

                    int eThingVal = cursorExpress.getInt(eThingId);
                    int eTypeVal = cursorExpress.getInt(eType);
                    int eCTouchAtmVal = cursorExpress.getInt(eCTouchAtm);

                    txtViewExpress.setText(txtViewExpress.getText().toString() + eThingVal +
                            " (" + eTypeVal + ", " + eCTouchAtmVal + ")" + "\n");
                }

                // TODO: Cursor close handles!!
            }
        });

        // Clear
        Button btnClear = (Button) findViewById(R.id.testBtnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtView.setText("");
                txtViewExpress.setText("");

                getContentResolver().delete(
                        DataContract.ThingEntry.CONTENT_URI, null, null);

                getContentResolver().delete(
                        DataContract.ExpressEntry.CONTENT_URI, null, null);

                Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
