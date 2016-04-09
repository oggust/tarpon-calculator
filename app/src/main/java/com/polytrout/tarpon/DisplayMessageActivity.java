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
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

    private String length_str;
    private String girth_str;
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
        TextView newResult = (TextView) findViewById(R.id.new_weight);
        TextView oldResult = (TextView) findViewById(R.id.old_weight);

        if (clown) {
			String unit = getString(R.string.lb);
        	newResult.setText(String.format(getString(R.string.result_new_formula),
					                        Formula.newFormula(length * 2.54, girth * 2.54) / 0.453592,
                                            unit));
        	oldResult.setText(String.format(getString(R.string.with_the_old_formula),
        		                            Formula.oldFormula(length * 2.54, girth * 2.54) / 0.453592,
                                            unit));
        } else {
			String unit = getString(R.string.kg);
			newResult.setText(String.format(getString(R.string.result_new_formula),
					                        Formula.newFormula(length, girth), unit));
			oldResult.setText(String.format(getString(R.string.with_the_old_formula),
					                        Formula.oldFormula(length, girth), unit));
        }
        // Set the text view as the activity layout
       // setContentView(newResult);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
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
                   System.currentTimeMillis());

        db.insertOrThrow(
                TarponTableContract.TarponEntry.TABLE_NAME,
                null,
                values);

        // Ghost button and change text to "saved"
        Button b = (Button) findViewById(R.id.save_button);
        b.setText(getString(R.string.saved));
        b.setEnabled(false);
    }
}
