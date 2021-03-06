package com.polytrout.tarpon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_LENGTH = "com.polytrout.tarpon.LENGTH";
	public final static String EXTRA_GIRTH = "com.polytrout.tarpon.GIRTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get unit preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean clown = sharedPref.getBoolean("pref_unit", false);
        if (clown) {
        	setContentView(R.layout.activity_main_clown);
        }else{
        	setContentView(R.layout.activity_main);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the "Do the math" button */
    public void sendMessage(View view) {
    	EditText lengthText = findViewById(R.id.edit_message_length);
    	EditText girthText = findViewById(R.id.edit_message_girth);
    	String length = lengthText.getText().toString();
    	String girth = girthText.getText().toString();

    	if(length.length() != 0 && girth.length() != 0) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
    	    intent.putExtra(EXTRA_LENGTH, length);
    	    intent.putExtra(EXTRA_GIRTH, girth);
    	    startActivity(intent);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_about: 
                openAbout();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_tarpon_table:
                openTarponTable();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
