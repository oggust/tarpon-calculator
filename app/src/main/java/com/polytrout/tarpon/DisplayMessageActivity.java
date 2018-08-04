package com.polytrout.tarpon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.Date;
import java.util.TimeZone;

public class DisplayMessageActivity extends Activity {

    private String length_str;
    private String girth_str;
    private String length_unit;
    private boolean clown;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		// Show the Up button in the action bar.
        setupActionBar();
        
        Intent intent = getIntent();
        length_str = intent.getStringExtra(MainActivity.EXTRA_LENGTH);
        girth_str = intent.getStringExtra(MainActivity.EXTRA_GIRTH);
        final double length = Double.parseDouble(length_str);
        final double girth = Double.parseDouble(girth_str);

        // Get unit preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        clown = sharedPref.getBoolean("pref_unit", false);

        // Get handles to the text views
        TextView newResult = findViewById(R.id.new_weight);
        TextView oldResult = findViewById(R.id.old_weight);

        String weight_unit;
        if (clown) {
			weight_unit = getString(R.string.lb);
            length_unit = getString(R.string.in);
        	newResult.setText(String.format(getString(R.string.result_new_formula),
					                        Formula.newFormula(length * 2.54, girth * 2.54) / 0.453592,
                    weight_unit));
        	oldResult.setText(String.format(getString(R.string.with_the_old_formula),
        		                            Formula.oldFormula(length * 2.54, girth * 2.54) / 0.453592,
                    weight_unit));
        } else {
			weight_unit = getString(R.string.kg);
            length_unit = getString(R.string.cm);
			newResult.setText(String.format(getString(R.string.result_new_formula),
					                        Formula.newFormula(length, girth), weight_unit));
			oldResult.setText(String.format(getString(R.string.with_the_old_formula),
					                        Formula.oldFormula(length, girth), weight_unit));
        }
        // Set the text view as the activity layout
       // setContentView(newResult);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_message, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            TextView newResult = findViewById(R.id.new_weight);
            TextView oldResult = findViewById(R.id.old_weight);

            shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_text),
                    newResult.getText(), oldResult.getText(),
                    length_str, length_unit, girth_str, length_unit));
            shareIntent.setType("text/plain");

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            }
        }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
        case R.id.action_about: 
            openAbout();
            return true;
        case R.id.action_settings:
            openSettings();
            return true;
        case R.id.action_tarpon_table:
            openTarponTable();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

    private void openAbout() {
    	Intent intent = new Intent(this, AboutActivity.class);
    	startActivity(intent);

    }
    private void openSettings() {
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }

    private void openTarponTable() {
    	Intent intent = new Intent(this, TarponTableActivity.class);
    	startActivity(intent);
    }


    /** Called when the user clicks the "Save this result" button */
    public void saveData(View view) {

    	// Save to database
        TarponTableDbHelper dbHelper = new TarponTableDbHelper(view.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String unit = clown?" in":" cm";
        ContentValues values = new ContentValues();
        values.put(TarponTableContract.TarponEntry.COLUMN_NAME_LENGTH, length_str + unit);
        values.put(TarponTableContract.TarponEntry.COLUMN_NAME_GIRTH, girth_str + unit);
        values.put(TarponTableContract.TarponEntry.COLUMN_NAME_CTIME,
                   System.currentTimeMillis()); // in UTC
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        values.put(TarponTableContract.TarponEntry.COLUMN_NAME_TZ,
                   tz.getDisplayName(tz.inDaylightTime(now), TimeZone.SHORT));

        db.insertOrThrow(
                TarponTableContract.TarponEntry.TABLE_NAME,
                null,
                values);

        // Ghost button and change text to "saved"
        Button b = findViewById(R.id.save_button);
        b.setText(getString(R.string.saved));
        b.setEnabled(false);
    }
}
