package alistar.app.brain;
import android.database.sqlite.*;
import android.content.*;
import com.readystatesoftware.notificationlog.*;

import java.io.File;
import java.util.*;
import android.database.*;

import alistar.app.Utils;
import alistar.app.map.*;
import com.google.android.gms.maps.model.*;
import alistar.app.timeline.*;

public class Memory extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "saraMemory.db";
    public static final int DATABASE_VERSION = 12;
    public static final String TABLE_DATA = "data";
    public static final String TABLE_PLACES = "places";
    public static final String TABLE_MOMENTS = "moments";
    public static final String TABLE_LOCATION_HISTORY = "location_history";
    public static final String TABLE_EMOTIONS = "emotions";
    public static final String TABLE_SIMCARDS = "simcards";
    public static final String TABLE_ALI_EMOTIONS = "ali_emotions";
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_VALUE = "value";
    public static final String KEY_NOTE = "note";
    public static final String KEY_FEELING ="feeling";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_STAR = "star";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ALART_DISTANCE = "alart_distance";
    public static final String KEY_ALART = "alart";
    public static final String KEY_MAP_MARKER_ID = "map_marker_id";
    public static final String KEY_ACCURACY = "accuracy";
    public static final String KEY_EMOJI = "emoji";
    public static final String KEY_SERIAL = "serial";


	@Override
	public void onCreate ( SQLiteDatabase db )
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
			+ KEY_PHONE_NUMBER + " TEXT" + ")";

		String CREATE_MOMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOMENTS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_EMOJI + " TEXT,"
			+ KEY_NOTE + " TEXT,"
			+ KEY_DATE + " TEXT" + ")";

		String CREATE_ALI_EMOTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALI_EMOTIONS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_FEELING + " TEXT,"
			+ KEY_DATE + " TEXT,"
			+ KEY_NOTE + " TEXT,"
			+ KEY_EMOJI + " TEXT" + ")";

		String CREATE_PLACES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLACES + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT,"
			+ KEY_LATITUDE + " TEXT,"
			+ KEY_LONGITUDE + " TEXT,"
			+ KEY_DESCRIPTION + " TEXT,"
			+ KEY_ALART_DISTANCE + " TEXT,"
			+ KEY_ALART + " TEXT,"
			+ KEY_STAR + " TEXT" + ")";

		String CREATE_LOCATION_HISORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION_HISTORY + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_MAP_MARKER_ID + " TEXT,"
			+ KEY_LATITUDE + " TEXT,"
			+ KEY_LONGITUDE + " TEXT,"
			+ KEY_ACCURACY + " TEXT,"
			+ KEY_DATE + " TEXT" + ")";

		String CREATE_SIMCARDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SIMCARDS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_SERIAL + " TEXT" + ")";

		db.execSQL ( CREATE_CONTACTS_TABLE );
		db.execSQL ( CREATE_ALI_EMOTIONS_TABLE );
		db.execSQL ( CREATE_PLACES_TABLE );
		db.execSQL ( CREATE_LOCATION_HISORY_TABLE );
		db.execSQL ( CREATE_MOMENTS_TABLE );
		db.execSQL ( CREATE_SIMCARDS_TABLE );
	}

	@Override
	public void onUpgrade ( SQLiteDatabase db, int p2, int p3 )
	{
		// TODO: Implement this method
		//db.execSQL("ALTER TABLE " + TABLE_LOCATION_HISTORY + " ADD " + KEY_ACCURACY + " TEXT");
		onCreate ( db );
	}

	public Memory ( Context context )
	{
		super ( context, Utils.MEMORY_FOLDER
			   + File.separator + DATABASE_NAME, null, DATABASE_VERSION );

		SQLiteDatabase.openOrCreateDatabase(Utils.MEMORY_FOLDER
				+ File.separator + DATABASE_NAME, null);
    }

	public void saveAliFeeling ( int feeling, String note, long date, String emoji )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		ContentValues values = new ContentValues ( );
		values.put ( KEY_FEELING, String.valueOf ( feeling ) );
		if ( note != null )
			values.put ( KEY_NOTE, note );
		values.put ( KEY_DATE, String.valueOf ( date ) );
		values.put ( KEY_EMOJI, String.valueOf ( emoji ) );
		db.insert ( TABLE_ALI_EMOTIONS, null, values );
		db.close ( );
		Log.d ( "[Memory] Emotions", "Ali emotion saved, value:" + String.valueOf ( feeling ) );
	}

	public List<Emotion> getEmotions(int count)
	{
		List<Emotion> data = new ArrayList<Emotion> ( );
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_ALI_EMOTIONS + " ORDER BY " + KEY_ID + " DESC LIMIT " + count, null );

		if ( !cursor.moveToFirst ( ) )
			return null;

		for ( int i=0; i < cursor.getCount ( ); i++ )
		{
			cursor.moveToPosition ( ( cursor.getCount ( ) - 1 ) - i );
			data.add ( new Emotion( Integer.valueOf ( cursor.getString ( 0 ) )
					, Integer.valueOf ( cursor.getString ( 1 ) ), Long.valueOf ( cursor.getString ( 2 ) )
					, cursor.getString ( 3 ), cursor.getString ( 4 ) ) );
		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public List<Emotion> getEmotionsByTimeRange(long start, long end)
	{
		List<Emotion> data = new ArrayList<> ( );
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_ALI_EMOTIONS + " WHERE " + KEY_DATE + " >= " + start + " AND " + KEY_DATE + " <= " + end + " ORDER BY " + KEY_ID + " DESC ", null );

		if ( !cursor.moveToFirst ( ) )
			return data;

		for ( int i=0; i < cursor.getCount ( ); i++ )
		{
			cursor.moveToPosition ( ( cursor.getCount ( ) - 1 ) - i );
			data.add ( new Emotion( Integer.valueOf ( cursor.getString ( 0 ) )
					, Integer.valueOf ( cursor.getString ( 1 ) ), Long.valueOf ( cursor.getString ( 2 ) )
					, cursor.getString ( 3 ), cursor.getString ( 4 ) ) );
		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public List<Emotion> getAllAliEmotions ( )
	{
		List<Emotion> data = new ArrayList<Emotion> ( );
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_ALI_EMOTIONS + " ORDER BY " + KEY_ID, null );

		if ( !cursor.moveToFirst ( ) )
			return null;

		for ( int i=0; i < cursor.getCount ( ); i++ )
		{
			cursor.moveToPosition ( ( cursor.getCount ( ) - 1 ) - i );
			data.add ( new Emotion( Integer.valueOf ( cursor.getString ( 0 ) )
									   , Integer.valueOf ( cursor.getString ( 1 ) ), Long.valueOf ( cursor.getString ( 2 ) )
									   , cursor.getString ( 3 ), cursor.getString ( 4 ) ) );
			/*if(cursor.getString(3) == null)
			 {
			 Log.d("memory", "null");
			 }
			 else
			 {
			 Log.d("memory", cursor.getString(3));
			 }*/
		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public MapMarker savePlace ( String name, double la, double lo, String description, double distance, boolean alart, boolean star )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		ContentValues values = new ContentValues ( );
		values.put ( KEY_NAME, name );
		values.put ( KEY_LONGITUDE, String.valueOf ( lo ) );
		values.put ( KEY_LATITUDE, String.valueOf ( la ) );
		values.put ( KEY_DESCRIPTION, description );
		values.put ( KEY_ALART_DISTANCE, String.valueOf ( distance ) );
		if ( alart )
		{
			values.put ( KEY_ALART, "1" );
		}
		else
		{
			values.put ( KEY_ALART, "0" );
		}
		if ( star )
		{
			values.put ( KEY_STAR, "1" );
		}
		else
		{
			values.put ( KEY_STAR, "0" );
		}
		db.insert ( TABLE_PLACES, null, values );
		db.close ( );
		Log.d ( "new place", "place saved: " + name + " " + lo + " " + la + " " + description + distance + " " + alart + " " + star );
		return getLastSavedPlace ( );
	}

	public List<MapMarker> getPlaces ( )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		List<MapMarker> data = new ArrayList<MapMarker> ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_PLACES, null );
		if ( cursor != null )
		{
			cursor.moveToFirst ( );
			for ( int i=0; i < cursor.getCount ( ); i++ )
			{
				MapMarker marker = new MapMarker ( Integer.valueOf ( cursor.getString ( 0 ) ), cursor.getString ( 1 ), new LatLng ( Double.valueOf ( cursor.getString ( 2 ) ), Double.valueOf ( cursor.getString ( 3 ) ) ), cursor.getString ( 4 ), Double.valueOf ( cursor.getString ( 5 ) ), cursor.getString ( 6 ).equals ( "1" ), cursor.getString ( 7 ).equals ( "1" ) );
				//Log.d("marker", marker.getName() + " " + marker.getLatLng().longitude + " " + marker.getLatLng().latitude);
				data.add ( marker );
				cursor.moveToNext ( );
			}
		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public MapMarker getLastSavedPlace ( )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_PLACES + " ORDER BY " + KEY_ID + " DESC LIMIT 1", null );
		Log.d ( "memory", "getLastSavedPlace cursorCount =" + cursor.getCount ( ) );
		if ( cursor != null )
		{
			cursor.moveToFirst ( );
			for ( int i=0; i < cursor.getCount ( ); i++ )
			{
				MapMarker marker = new MapMarker ( Integer.valueOf ( cursor.getString ( 0 ) ), cursor.getString ( 1 ), new LatLng ( Double.valueOf ( cursor.getString ( 2 ) ), Double.valueOf ( cursor.getString ( 3 ) ) ), cursor.getString ( 4 ), Double.valueOf ( cursor.getString ( 5 ) ), cursor.getString ( 6 ).equals ( "1" ), cursor.getString ( 7 ).equals ( "1" ) );
				Log.d ( "marker", marker.getName ( ) );
				cursor.close ( );
				db.close ( );
				return marker;
			}

		}
		cursor.close ( );
		db.close ( );
		return null;
	}

	public void deletePlace ( int id )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		db.execSQL ( "DELETE FROM " + TABLE_PLACES + " WHERE " + KEY_ID + "=" + id );
		db.close ( );
	}

	public void updatePlace ( MapMarker mapMarker )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		ContentValues values = new ContentValues ( );
		values.put ( KEY_NAME, mapMarker.getName ( ) );
		values.put ( KEY_LONGITUDE, String.valueOf ( mapMarker.getLatLng ( ).longitude ) );
		values.put ( KEY_LATITUDE, String.valueOf ( mapMarker.getLatLng ( ).latitude ) );
		values.put ( KEY_DESCRIPTION, mapMarker.getDescription ( ) );
		values.put ( KEY_ALART_DISTANCE, String.valueOf ( mapMarker.getDistance ( ) ) );
		if ( mapMarker.getAlart ( ) )
		{
			values.put ( KEY_ALART, "1" );
		}
		else
		{
			values.put ( KEY_ALART, "0" );
		}
		if ( mapMarker.isStared ( ) )
		{
			values.put ( KEY_STAR, "1" );
		}
		else
		{
			values.put ( KEY_STAR, "0" );
		}
		Log.d ( "memory", "update" + mapMarker.getLatLng ( ).longitude + " " + mapMarker.getLatLng ( ).latitude );
		db.update ( TABLE_PLACES, values, KEY_ID + "=" + mapMarker.getId ( ), null );
		db.close ( );
	}

	public void saveCurrentLocation ( MapMarker marker )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		ContentValues values = new ContentValues ( );
		if ( marker.getId ( ) != -1 )
		{
			values.put ( KEY_MAP_MARKER_ID, String.valueOf ( marker.getId ( ) ) );
		}
		else
		{
			values.put ( KEY_LONGITUDE, String.valueOf ( marker.getLatLng ( ).longitude ) );
			values.put ( KEY_LATITUDE, String.valueOf ( marker.getLatLng ( ).latitude ) );
			values.put ( KEY_ACCURACY, String.valueOf ( marker.getAccuracy ( ) ) );
		}

		values.put ( KEY_DATE, String.valueOf ( System.currentTimeMillis ( ) ) );
		db.insert ( TABLE_LOCATION_HISTORY, null, values );
		Log.d ( "location history", "location added to history: " + marker.getName ( ) );
	}

	public List<LocationHistory> getLocationHistoryList ( )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_LOCATION_HISTORY + " ORDER BY " + KEY_ID + " DESC", null );
		List<LocationHistory> data = new ArrayList<LocationHistory> ( );
		//Log.d("memory", "getLastSavedPlace cursorCount =" + cursor.getCount());
		if ( cursor != null )
		{
			cursor.moveToFirst ( );
			for ( int i=0; i < cursor.getCount ( ); i++ )
			{
				double latitude = -1000;
				double longtude = -1000;
				if ( cursor.getString ( 2 ) != null )
				{
					latitude = Double.valueOf ( cursor.getString ( 2 ) );
				}
				if ( cursor.getString ( 3 ) != null )
				{
					latitude = Double.valueOf ( cursor.getString ( 3 ) );
				}
				int mapMarkerId = -1;
				if ( cursor.getString ( 1 ) != null )
				{
					mapMarkerId = Integer.valueOf ( cursor.getString ( 1 ) );
				}
				LocationHistory lh = new LocationHistory ( Integer.valueOf ( cursor.getString ( 0 ) ), mapMarkerId, latitude, longtude, Long.valueOf ( cursor.getString ( 4 ) ) );
				data.add ( lh );
				//Log.d("location history item", cursor.getString(4));
				cursor.moveToNext ( );
			}

		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public List<LocationHistory> getLocationHistoryList ( int limit )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_LOCATION_HISTORY + " ORDER BY " + KEY_ID + " DESC LIMIT " + limit, null );
		List<LocationHistory> data = new ArrayList<LocationHistory> ( );
		//Log.d("memory", "getLastSavedPlace cursorCount =" + cursor.getCount());
		if ( cursor != null )
		{
			cursor.moveToFirst ( );
			for ( int i=0; i < cursor.getCount ( ); i++ )
			{
				double latitude = -1000;
				double longtude = -1000;
				if ( cursor.getString ( 2 ) != null )
				{
					latitude = Double.valueOf ( cursor.getString ( 2 ) );
				}
				if ( cursor.getString ( 3 ) != null )
				{
					latitude = Double.valueOf ( cursor.getString ( 3 ) );
				}
				int mapMarkerId = -1;
				if ( cursor.getString ( 1 ) != null )
				{
					mapMarkerId = Integer.valueOf ( cursor.getString ( 1 ) );
				}
				LocationHistory lh = new LocationHistory ( Integer.valueOf ( cursor.getString ( 0 ) ), mapMarkerId, latitude, longtude, Long.valueOf ( cursor.getString ( 4 ) ) );
				data.add ( lh );
				//Log.d("location history item", cursor.getString(4));
				cursor.moveToNext ( );
			}

		}
		cursor.close ( );
		db.close ( );
		return data;
	}

	public MapMarker findMapMarkerById ( int id )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor cursor = db.rawQuery ( "SELECT * FROM " + TABLE_PLACES + " WHERE " + KEY_ID + "=" + id, null );
		if ( cursor != null )
		{
			cursor.moveToFirst ( );
			for ( int i=0; i < cursor.getCount ( ); i++ )
			{
				MapMarker marker = new MapMarker ( Integer.valueOf ( cursor.getString ( 0 ) ), cursor.getString ( 1 ), new LatLng ( Double.valueOf ( cursor.getString ( 2 ) ), Double.valueOf ( cursor.getString ( 3 ) ) ), cursor.getString ( 4 ), Double.valueOf ( cursor.getString ( 5 ) ), cursor.getString ( 6 ).equals ( "1" ), cursor.getString ( 7 ).equals ( "1" ) );
				//Log.d("marker", marker.getName());
				cursor.close ( );
				db.close ( );
				return marker;
			}

		}
		cursor.close ( );
		db.close ( );
		return null;
	}

	public void saveMoment ( String emoji, String note )
	{
		SQLiteDatabase database = this.getWritableDatabase ( );
		ContentValues cv = new ContentValues ( );
		cv.put ( KEY_EMOJI, emoji );
		cv.put ( KEY_NOTE, note );
		cv.put ( KEY_DATE, String.valueOf ( System.currentTimeMillis ( ) ) );
		database.insert ( TABLE_MOMENTS, null, cv );
		database.close ( );
	}

	public List<Moment> getAllMoments ( )
	{
		List<Moment> data = new ArrayList<Moment> ( );
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor c = db.rawQuery ( "SELECT * FROM " + TABLE_MOMENTS + " ORDER BY " + KEY_ID + " DESC", null );
		c.moveToFirst ( );
		for ( int i=0; i < c.getCount ( ); i++ )
		{
			Moment m = new Moment ( Integer.valueOf ( c.getString ( 0 ) ), c.getString ( 2 ), c.getString ( 1 ), Long.valueOf ( c.getString ( 3 ) ) );
			data.add ( m );
			m = null;
			c.moveToNext ( );
		}
		return data;
	}

	public Moment getLastSavedMoment ( )
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		Cursor c = db.rawQuery ( "SELECT * FROM " + TABLE_MOMENTS + " ORDER BY " + KEY_ID + " DESC LIMIT 1", null );
		Moment moment = null;
		if ( c.moveToFirst ( ) )
		{
			moment = new Moment ( Integer.valueOf ( c.getString ( 0 ) ), c.getString ( 2 ), c.getString ( 1 ), Long.valueOf ( c.getString ( 3 ) ) );
		}
		c.close();
		db.close();
		return moment;
	}

	public void saveSimSerial ( String sim )
	{
		SQLiteDatabase database = this.getWritableDatabase ( );
		ContentValues cv = new ContentValues ( );
		cv.put ( KEY_SERIAL, sim );
		database.insert ( TABLE_SIMCARDS, null, cv );
		database.close ( );
	}

	public boolean isSimcardSaved ( String serial )
	{
		SQLiteDatabase database = this.getWritableDatabase ( );
        Cursor c = database.rawQuery ( "SELECT * FROM " + TABLE_SIMCARDS, null );
        c.moveToFirst ( );
        for ( int i=0; i < c.getCount ( ); i++ )
        {
            if ( c.getString ( 1 ).equals ( serial ) )
            {
                c.close ( );
                database.close ( );
                return true;
            }
        }
		return false;
	}

    public boolean hasRecords ( String table )
    {
        SQLiteDatabase database = this.getWritableDatabase ( );
        Cursor c = database.rawQuery ( "SELECT * FROM " + table, null );
        return c.getCount ( ) > 0;
    }


}
