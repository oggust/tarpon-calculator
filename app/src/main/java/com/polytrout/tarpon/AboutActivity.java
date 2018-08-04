package com.polytrout.tarpon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView img = findViewById(R.id.tbrc_logo);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.tarponresearch.com/"));
                startActivity(intent);
            }
        });
    }
}
