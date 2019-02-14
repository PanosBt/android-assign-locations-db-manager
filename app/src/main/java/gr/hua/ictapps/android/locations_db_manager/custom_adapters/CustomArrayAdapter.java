package gr.hua.ictapps.android.locations_db_manager.custom_adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// Inspired by https://stackoverflow.com/questions/37019941/how-to-add-a-hint-in-spinner-in-xml
public class CustomArrayAdapter extends ArrayAdapter {

    public CustomArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    // Altered to mark disabled the first element of the spinner
    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }

    // Altered to gray out the first element of the spinner
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);

        if (position == 0)
            // Set the hint text color gray
            textView.setTextColor(Color.GRAY);
        else
            textView.setTextColor(Color.BLACK);

        return textView;
    }
}
