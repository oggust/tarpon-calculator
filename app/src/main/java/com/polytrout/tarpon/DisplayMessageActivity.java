package com.polytrout.tarpon;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		// Show the Up button in the action bar.
        setupActionBar();
        
        Intent intent = getIntent();
        final double length = Double.parseDouble(intent.getStringExtra(MainActivity.EXTRA_LENGTH));
        final double girth = Double.parseDouble(intent.getStringExtra(MainActivity.EXTRA_GIRTH));
        final DecimalFormat df = new DecimalFormat("#.00");

        // Get unit preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean clown = sharedPref.getBoolean("pref_unit", false);

        // Get handles to the text views
        TextView newResult = (TextView) findViewById(R.id.new_weight);
        TextView oldResult = (TextView) findViewById(R.id.old_weight);

        if (clown) {
        	newResult.setText(df.format(newFormula(length * 2.54, girth * 2.54) / 0.453592) + " lb");
        	oldResult.setText("(With the old formula: " + 
        		              df.format(oldFormula(length * 2.54, girth * 2.54) / 0.453592) + " lb)");
        } else {
        	newResult.setText(df.format(newFormula(length, girth)) + " kg");
        	oldResult.setText("(With the old formula: " +
        		              df.format(oldFormula(length, girth)) + " kg)");
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
			getActionBar().setDisplayHomeAsUpEnabled(true);
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

	// Classic formula. In clown units, so convert.
	private double oldFormula(double length, double girth) {
		final double inch_length = length / 2.54;
		final double inch_girth = girth / 2.54;
		final double pounds_weight = inch_length * inch_girth * inch_girth / 800.0;
		return pounds_weight * 0.453592; //says google
	}
	
	// New and shiny (ALE) formula. (Ault and Luo, 2013)
	// ("A reliable game fish weight estimation model for Atlantic Tarpon (Megalops Atlanticus)")
	// (The odd variable names are from the paper)
	private double newFormula(double L, double G) {
	    final double b0 = 2.828;
	    final double b1 = 0.0000296;
	    final double b2 = 0.006123;
	    final double b3 = -0.008284;
	    final double b4 = 0.1845;
	    final double b5 = -0.1943;

	    final double G2 = G*G;
	    return  b0 + b1*G2*L + b2*G*L + b3*G2 + b4*G + b5*L;
	}
}
