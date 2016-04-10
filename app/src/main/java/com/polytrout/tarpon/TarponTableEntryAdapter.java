package com.polytrout.tarpon;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * This fills out the table using data from the database.
 */
class TarponTableEntryAdapter extends CursorAdapter {
    private final Context context;
    private final LayoutInflater cursorInflater;

    public TarponTableEntryAdapter(Context context, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(
                     Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.tarpon_table_entry, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView weight_tv = (TextView) view.findViewById(R.id.WeightLine);
        TextView length_tv = (TextView) view.findViewById(R.id.LengthLine);
        TextView girth_tv = (TextView) view.findViewById(R.id.GirthLine);
        TextView date_tv = (TextView) view.findViewById(R.id.DateLine);
        double length_val, girth_val;
        boolean used_usc = false;

        String length = cursor.getString(cursor.getColumnIndex(TarponTableContract.TarponEntry.COLUMN_NAME_LENGTH));
        String girth = cursor.getString(cursor.getColumnIndex(TarponTableContract.TarponEntry.COLUMN_NAME_GIRTH));
        length_tv.setText(length);
        girth_tv.setText(girth);

        // Set the database _id here, so I can get it from the onclick handler.
        int db_id = cursor.getInt(cursor.getColumnIndex("_id"));
        view.setTag(db_id);

        long ctime = cursor.getLong(cursor.getColumnIndex(TarponTableContract.TarponEntry.COLUMN_NAME_CTIME));
        String date_string  = DateFormat.getDateTimeInstance().format(new Date(ctime));
        date_tv.setText(date_string);

        length_val = Double.parseDouble(length.split(" ")[0]);
        girth_val = Double.parseDouble(girth.split(" ")[0]);

        if(length.endsWith("in")) {
            used_usc = true;
            length_val /= 2.54;
            girth_val /= 2.54;
        }

        double result = Formula.newFormula(length_val, girth_val);
        if (used_usc) {
            weight_tv.setText(String.format(context.getString(R.string.result_lb), result / 0.453592));
        } else {
            weight_tv.setText(String.format(context.getString(R.string.result_kg), result));
        }
    }
}

