package gr.hua.ictapps.android.locations_db_manager.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import gr.hua.ictapps.android.locations_db_manager.db.DBHelper;
import gr.hua.ictapps.android.locations_db_manager.db.LocationsContract;

public class LocationsProvider extends ContentProvider {

    private static LocationsContract locationsContract;
    private DBHelper dbHelper;
    private UriMatcher uriMatcher;

    public LocationsProvider() {
    }

    @Override
    public boolean onCreate() {
        this.locationsContract = new LocationsContract();
        dbHelper = new DBHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // our resources are accessed by only one uri
        uriMatcher.addURI(
                locationsContract.AUTHORITY,
                "locations",
                1
        );
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * insert a new locations resource w/ the given values
     * the values must follow LocationsContract specification
     * and not be null
     * @param uri the uri for the resource, can be foun in LocationsContract#CONTENT_URL
     * @param values the values to store, following LocationsContract specification
     * @return the Uri of the created resource upon success, or null upon failure
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String userid;
        Float longitude;
        Float latitude;
        String dt;
        Uri newUri = null;

        if (uriMatcher.match(uri) == 1)
            if ((userid = values.getAsString("userid")) != null
                    && (longitude = values.getAsFloat("longitude")) != null
                    && (latitude = values.getAsFloat("latitude")) != null
                    && (dt = values.getAsString("dt")) != null
                    ) {
                LocationsContract locationsContract = new LocationsContract(userid, longitude, latitude, dt);
                dbHelper = new DBHelper(getContext());
                long newId = dbHelper.insert(locationsContract);
                newUri = Uri.parse(locationsContract.CONTENT_URL + newId);
            }
        return newUri;

    }

    /**
     * Query a locations resource
     * LocationsContract#CONTENT_URL provides access to all locations
     * and projection and selection can be applied using LocationsContract
     * specification variables
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return a Cursor w/ the requested resources
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if (uriMatcher.match(uri) == 1) {
            cursor = dbHelper.getAllLocations(
                    projection,
                    selection,
                    selectionArgs);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
