package com.polytrout.tarpon;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListAdapter;

/**
 * Show a table of fish.
 * Basically, just dump "SELECT *" from the db into a list.
 */
public class TarponTableActivity extends ListActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarpon_table);
        // Show the Up button in the action bar.
        setupActionBar();
        populateList();
    }

    private void populateList() {
        TarponTableDbHelper dbHelper = new TarponTableDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                TarponTableContract.TarponEntry.TABLE_NAME,  // The table to query
                null,                                        // The columns to return (all)
                "_id is not null",                           // The columns for the WHERE clause
                null,                                        // The values for the WHERE clause
                null,                                        // don't group the rows
                null,                                        // don't filter by row groups
                TarponTableContract.TarponEntry.COLUMN_NAME_CTIME + " DESC" // The sort order
        );

        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.tarpon_table_entry,
                c,
                new String[] {
                        TarponTableContract.TarponEntry.COLUMN_NAME_CTIME,
                        TarponTableContract.TarponEntry.COLUMN_NAME_LENGTH,
                        TarponTableContract.TarponEntry.COLUMN_NAME_GIRTH },
                // Parallel array of which template objects to bind to those
                // columns.
                new int[] { R.id.DateLine, R.id.LengthLine, R.id.GirthLine },
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        setListAdapter(adapter);

    }


    	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)

	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
		}
	}

}

