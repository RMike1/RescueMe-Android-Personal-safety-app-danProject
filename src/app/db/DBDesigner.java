package app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {
	public static final String 	TABLE_SMS = "table_sms";
	public static final String 	COLUMN_ID = "id";
	public static final String 	COLUMN_MESSAGE = "message";
	public static final String 	COLUMN_ISTOSEND = "isChecked";
	private static final String DATABASE_NAME = "rescueMe.db";
	private static final int 	DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE_TABLE_SMS = "create table if not exists "
			+ TABLE_SMS + "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_MESSAGE + " text not null,"
			+ COLUMN_ISTOSEND + " integer not null);";
	
	public static final String 	TABLE_CONTACTS = "table_contacts";
	public static final String 	CONTACT_ID = "id";
	public static final String 	COLUMN_NAME = "name";
	public static final String 	COLUMN_NUMBER = "number";
	public static final String COLUMN_ISCHECKED = "isChecked";
	
	private static final String DATABASE_CREATE_TABLE_CONTACTS = "create table if not exists "
			+ TABLE_CONTACTS + "( " + COLUMN_ID + " integer primary key, " 
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_NUMBER+ " text not null, "
					+  COLUMN_ISCHECKED+ " integer not null);";
		
	public DBDesigner(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_SMS);
		database.execSQL(DATABASE_CREATE_TABLE_CONTACTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBDesigner.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
		onCreate(db);
	}
}