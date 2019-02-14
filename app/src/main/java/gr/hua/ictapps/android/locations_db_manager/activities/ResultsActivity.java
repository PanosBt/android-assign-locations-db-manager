package gr.hua.ictapps.android.locations_db_manager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import gr.hua.ictapps.android.locations_db_manager.R;
import gr.hua.ictapps.android.locations_db_manager.db.DBHelper;
import gr.hua.ictapps.android.locations_db_manager.db.LocationsContract;

public class ResultsActivity extends AppCompatActivity {

    private static final int PADDING = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final DBHelper dbHelper = new DBHelper(ResultsActivity.this);

        Intent intent = getIntent();

        // Set OnClickListener for back button
        findViewById(R.id.results_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        TableLayout table = findViewById(R.id.results_table);
        table.setVisibility(View.GONE);

        // fetch requested locations from db
        ArrayList<LocationsContract> locations = dbHelper.getLocationsForUserIDAndDt(
                intent.getStringExtra("userid"),
                intent.getStringExtra("dt"));

        // if no locations where found show err msg
        if (locations.isEmpty()) {
            ((TextView) findViewById(R.id.results_text_view_error))
                    .setText(R.string.activity_results_error_msg);
            return;
        } else
            table.setVisibility(View.VISIBLE);

        for (LocationsContract location : locations) {
            // Create a new row
            TableRow row = new TableRow(ResultsActivity.this);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);
            row.setGravity(Gravity.CENTER);
            row.setPadding(3, 3, 3, 3);
            row.setBackgroundResource(R.drawable.border);

            // add location's attributes to each row as a TextView
            genFormatAddTextView(Integer.toString(location.getId()), row);
            genFormatAddTextView(location.getUserid(), row);
            genFormatAddTextView(Float.toString(location.getLongitude()), row);
            genFormatAddTextView(Float.toString(location.getLatitude()), row);
            genFormatAddTextView(location.getDt(), row);

            table.addView(row);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    // Generates and formats a TextView w/ the given text and adds it to the given row
    private void genFormatAddTextView(String text, TableRow row) {
        TextView textView = new TextView(ResultsActivity.this);
        textView.setText(text);

        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setBackgroundResource(R.drawable.border);
        textView.setGravity(Gravity.CENTER);

        row.addView(textView);
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.setAction("gr.hua.ictapps.android.locations_db_manager.activities.search");
        startActivity(intent);
        finish();
    }
}
