package gr.hua.ictapps.android.locations_db_manager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gr.hua.ictapps.android.locations_db_manager.R;
import gr.hua.ictapps.android.locations_db_manager.db.DBHelper;
import gr.hua.ictapps.android.locations_db_manager.db.LocationsContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView errorText = findViewById(R.id.main_text_view_error);
        final DBHelper dbHelper = new DBHelper(MainActivity.this);

        // OnClickListener for next button
        findViewById(R.id.main_button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("gr.hua.ictapps.android.locations_db_manager.activities.search");
                startActivity(intent);
                finish();
            }
        });

        // OnClickListener for save button
        findViewById(R.id.main_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorText.setVisibility(View.GONE);

                String userid = ((EditText) findViewById(R.id.main_edit_text_userid))
                        .getText()
                        .toString()
                        .trim();
                String sLon = ((EditText) findViewById(R.id.main_edit_text_longitude))
                        .getText()
                        .toString()
                        .trim();
                String sLat = ((EditText) findViewById(R.id.main_edit_text_latitude))
                        .getText()
                        .toString()
                        .trim();

                if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(sLon) || TextUtils.isEmpty(sLat)) {
                    errorText.setText(R.string.activity_main_error_msg_empty_fields);
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                float lon = 0;
                float lat = 0;

                try {
                    lon = Float.parseFloat(sLon);
                    lat = Float.parseFloat(sLat);
                } catch (NumberFormatException ex) {
                    errorText.setText(R.string.activity_main_error_non_float);
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                // get current date - time as String
                String dt = new SimpleDateFormat(
                        "dd-MM-yyyy kk:mm:ss",
                        Locale.getDefault()).format(new Date());

                //create LocationsContract Object to pass to dbHelper
                LocationsContract locationsContract = new LocationsContract(userid, lon, lat, dt);

                // insert new tupple to db and show relevant Toast msg
                String msg;
                if (dbHelper.insert(locationsContract) != -1)
                    msg = "Data saved successfully!";
                else
                    msg = "Data entry failed.";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                resetEditTexts();
            }
        });
    }

    private void resetEditTexts() {
        ((EditText) findViewById(R.id.main_edit_text_userid)).setText("");
        ((EditText) findViewById(R.id.main_edit_text_longitude)).setText("");
        ((EditText) findViewById(R.id.main_edit_text_latitude)).setText("");
    }
}
