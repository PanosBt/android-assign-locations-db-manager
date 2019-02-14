package gr.hua.ictapps.android.locations_db_manager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "LOCATIONS";
    public static final int DB_VERSION = 1;
    private LocationsContract locationsContract;
//    public static final String KEY_ID = "_id";
//    public static final String KEY_USERID = "userid";
//    public static final String KEY_LONGITUDE = "longitude";
//    public static final String KEY_LATITUDE = "latitude";
//    public static final String KEY_DT = "dt";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        locationsContract = new LocationsContract();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_NAME +
                " (" +
                locationsContract.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                locationsContract.KEY_USERID + " VARCHAR(10) NOT NULL, " +
                locationsContract.KEY_LONGITUDE + " FLOAT NOT NULL, " +
                locationsContract.KEY_LATITUDE + " FLOAT NOT NULL, " +
                locationsContract.KEY_DT + " VARCHAR(20) NOT NULL" +
                ");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Inserts a new locations tupple in the db. The input data must be
     * be given as a locationsContract
     *
     * @param locationsContract the locationsContract w/ the data to insert
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(LocationsContract locationsContract) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(locationsContract.KEY_USERID, locationsContract.getUserid());
        values.put(locationsContract.KEY_LONGITUDE, locationsContract.getLongitude());
        values.put(locationsContract.KEY_LATITUDE, locationsContract.getLatitude());
        values.put(locationsContract.KEY_DT, locationsContract.getDt());

        return sqLiteDatabase.insert(this.DB_NAME, null, values);
    }

    /**
     *
     * @param projection
     * @param selection
     * @param selectionArgs
     * @return
     */
    public Cursor getAllLocations(String[] projection, String selection,
                                  String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.query(DB_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);


    }

    /**
     * Returns all saved timestamps
     *
     * @return An ArrayList of all saved timestamps (dt) as Strings
     */
    public ArrayList<String> getAllDts() {
        ArrayList<String> dts = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DB_NAME,
                new String[]{locationsContract.KEY_DT},
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                dts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return dts;
    }

    /**
     * Get a list of all locations stored w/ the given userid and/or dt
     * If userid or dt is empty or null, only the other parameter will be used
     * At least one parameter should not be empty or null
     *
     * @param userid the userid of the locations
     * @param dt     the timestamp of the locations
     * @return the fetched locations from the db or an empty ArrayList if no locations
     * where found or if both parameters where empty or null
     */
    public ArrayList<LocationsContract> getLocationsForUserIDAndDt(@Nullable String userid,
                                                                   @Nullable String dt) {
        String selection;
        String[] selectionArgs;

        if ((userid == null || TextUtils.isEmpty(userid))
                && (dt == null || TextUtils.isEmpty(dt)))
            return new ArrayList<>();

        //Generate query's where clause based on parameters given
        if (userid == null || TextUtils.isEmpty(userid)) {
            selection = locationsContract.KEY_DT + "=?";
            selectionArgs = new String[]{dt};
        } else if (dt == null || TextUtils.isEmpty(dt)) {
            selection = locationsContract.KEY_USERID + "=?";
            selectionArgs = new String[]{userid};
        } else {
            selection = locationsContract.KEY_USERID + "=? AND " + locationsContract.KEY_DT + "=?";
            selectionArgs = new String[]{userid, dt};
        }

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DB_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        return parseLocationsCursorToArrayList(cursor);
    }

    private ArrayList<LocationsContract> parseLocationsCursorToArrayList(Cursor cursor) {
        ArrayList<LocationsContract> locations = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String userid = cursor.getString(1);
                float longitude = cursor.getFloat(2);
                float latitude = cursor.getFloat(3);
                String _dt = cursor.getString(4);

                LocationsContract location = new LocationsContract(userid, longitude, latitude, _dt);
                location.setId(id);

                locations.add(location);
            } while (cursor.moveToNext());
        }
        return locations;

    }
}
