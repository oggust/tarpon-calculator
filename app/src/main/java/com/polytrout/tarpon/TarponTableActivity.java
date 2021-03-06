package com.polytrout.tarpon;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ShareActionProvider;
import android.widget.TextView;


/**
 * Show a table of fish.
 * Basically, just dump "SELECT *" from the db into a list.
 */

public class TarponTableActivity extends ListActivity {

    private static final int MENU_SHARE = 1;
    private static final int MENU_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarpon_table);
        // Show the Up button in the action bar.
        setupActionBar();
        registerForContextMenu(getListView());
        populateList();
    }

    private Cursor getCursor(SQLiteDatabase db) {
        return db.query(
                TarponTableContract.TarponEntry.TABLE_NAME,  // The table to query
                null,                                        // The columns to return (all)
                "_id is not null",                           // The columns for the WHERE clause
                null,                                        // The values for the WHERE clause
                null,                                        // don't group the rows
                null,                                        // don't filter by row groups
                TarponTableContract.TarponEntry.COLUMN_NAME_CTIME + " DESC" // The sort order
        );
    }

    private void populateList() {
        TarponTableDbHelper dbHelper = new TarponTableDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ListAdapter adapter = new TarponTableEntryAdapter(this, getCursor(db));
        setListAdapter(adapter);
    }

    private void deleteTarpon(int id) {
        TarponTableDbHelper dbHelper = new TarponTableDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TarponTableContract.TarponEntry.TABLE_NAME,
                "_id" + " LIKE ?",
                new String[]{String.valueOf(id)});
        // And now, for the ListView to update, we need to give it a new cursor.
        db = dbHelper.getReadableDatabase();
        Cursor c = getCursor(db);
        ((TarponTableEntryAdapter) getListAdapter()).swapCursor(c);
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Toast.makeText(this, v.getTag() + " selected", Toast.LENGTH_LONG).show();
//
//    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            menu.add(Menu.NONE, MENU_SHARE, 1, "Share");
        }
        menu.add(Menu.NONE, MENU_DELETE, 2, "Delete");
    }

    private void shareTarpon(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        View v = info.targetView;
        // Fetch and store ShareActionProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ShareActionProvider mShareActionProvider = new ShareActionProvider(this);
            item.setActionProvider(mShareActionProvider);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            TextView weight = v.findViewById(R.id.WeightLine);
            TextView length = v.findViewById(R.id.LengthLine);
            TextView girth = v.findViewById(R.id.GirthLine);

            shareIntent.putExtra(Intent.EXTRA_TEXT,
                                 String.format(getString(R.string.share_text_from_table),
                                         weight.getText(), length.getText(), girth.getText()));
            shareIntent.setType("text/plain");

            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_SHARE:
                shareTarpon(item);
                return true;
            case MENU_DELETE:
                deleteTarpon((Integer) info.targetView.getTag());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    /**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)

	private void setupActionBar() {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}

