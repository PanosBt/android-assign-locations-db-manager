package gr.hua.ictapps.android.locations_db_manager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import gr.hua.ictapps.android.locations_db_manager.R;
import gr.hua.ictapps.android.locations_db_manager.custom_adapters.CustomArrayAdapter;
import gr.hua.ictapps.android.locations_db_manager.db.DBHelper;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final TextView errorText = findViewById(R.id.search_text_view_error);
        final DBHelper dbHelper = new DBHelper(SearchActivity.this);
        final Spinner spinner = findViewById(R.id.search_spinner);

        // set OnClickListener for back button
        findViewById(R.id.search_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        errorText.setVisibility(View.GONE);

        // fill the spinner w/ all timestamps

        CustomArrayAdapter adapter = new CustomArrayAdapter(
                SearchActivity.this,
                R.layout.support_simple_spinner_dropdown_item);

        ArrayList<String> dts;

        // get all timestamps from db
        if ((dts = dbHelper.getAllDts()).isEmpty()) {
            errorText.setText(R.string.activity_search_error_msg_fetching);
            errorText.setVisibility(View.VISIBLE);
            return;
        }

        // make first element a hint
        dts.add(0, "Select a timestamp");

        // fill adapter w/ fetched timestamps
        try {
            adapter.addAll(dts);
        } catch (Exception ex) { /* Many things can go wrong here. It doesn't make sense to catch
                                    each exception separately */
            ex.printStackTrace();
            errorText.setText(R.string.activity_search_error_msg_fetching);
            errorText.setVisibility(View.VISIBLE);
            return;
        }

        spinner.setAdapter(adapter);

        // set OnClickListener for search button
        findViewById(R.id.search_button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user selections

                String userid = ((EditText) findViewById(R.id.search_edit_text_userid))
                        .getText()
                        .toString()
                        .trim();
                // if no selection was made from the spinner, make "dt" an empty String
                String dt = spinner.getSelectedItemPosition() == 0 ?
                        "" : spinner.getSelectedItem().toString();

                // if no selection was made, show err msg
                if (TextUtils.isEmpty(userid) && TextUtils.isEmpty(dt)) {
                    errorText.setText(R.string.activity_search_error_msg_empty_selection);
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                // launch results activity sending user selections
                Intent intent = new Intent();
                intent.setAction("gr.hua.ictapps.android.locations_db_manager.activities.results");
                intent.putExtra("userid", userid);
                intent.putExtra("dt", dt);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.setAction("gr.hua.ictapps.android.locations_db_manager.activities.main");
        startActivity(intent);
        finish();
    }
}
