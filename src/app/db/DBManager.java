package app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.SmallTest;
import app.models.Contact;
import app.models.SMS_Message;

public class DBManager {
	
	private SQLiteDatabase database;
	private DBDesigner dbHelper;
	
	
	public DBManager(Context context) {
		dbHelper = new DBDesigner(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}
	
	public void addSMS(SMS_Message sms) {
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_MESSAGE, sms.message);
		values.put(DBDesigner.COLUMN_ISTOSEND, sms.isChecked);

		database.insert(DBDesigner.TABLE_SMS, null, values);
	}
	
	
	public List<SMS_Message> getAllSMS() {
		List<SMS_Message> messages = new ArrayList<SMS_Message>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_SMS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SMS_Message sms = toSMS(cursor);
			messages.add(sms);
			cursor.moveToNext();
		}
		cursor.close();
		return messages;
	}
	
	
	public SMS_Message toSMS(Cursor cursor) {
		SMS_Message pojo = new SMS_Message();
		pojo.id = cursor.getInt(0);
		pojo.message = cursor.getString(1);
		pojo.isChecked = cursor.getInt(2)>0;
		return pojo;
	}
	
	public boolean deleteSMS(SMS_Message sms)
	{
		
		return database.delete(DBDesigner.TABLE_SMS, DBDesigner.COLUMN_ID  +"="+ sms.id,null)>0;
		
	}
	
	public void editSMS(int id, String newMessage)
	{
		ContentValues values = new ContentValues();
	    values.put(DBDesigner.COLUMN_MESSAGE, newMessage);
		database.update(DBDesigner.TABLE_SMS, values, DBDesigner.COLUMN_ID +"="+id, null);
		
	}
	
	public void setEmergencySMS(SMS_Message sms, boolean newChecked)
	{
		
		ContentValues bool1 = new ContentValues();
		bool1.put(DBDesigner.COLUMN_ISTOSEND, false);
		database.update(DBDesigner.TABLE_SMS,  bool1 , DBDesigner.COLUMN_ISTOSEND, null);
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_ISTOSEND, newChecked);
		database.update(DBDesigner.TABLE_SMS, values, DBDesigner.COLUMN_ID +"="+sms.id, null);
	}
	
	/*public SMS_Message getemergencySMS()
	{
		//List<SMS_Message> messages = new ArrayList<SMS_Message>();
		SMS_Message sms = null;
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_SMS+" WHERE "+DBDesigner.COLUMN_ISTOSEND+" = TRUE ", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			sms = toSMS(cursor);
			
			cursor.moveToNext();
		}
		
		
		return sms;
		
	}*/

	public void addContact(Contact contact) {
		ContentValues values = new ContentValues();
		values.put(DBDesigner.CONTACT_ID, contact.id);
		values.put(DBDesigner.COLUMN_NAME, contact.name);
		values.put(DBDesigner.COLUMN_NUMBER, contact.number);
		values.put(DBDesigner.COLUMN_ISCHECKED, contact.isChecked);

		database.insert(DBDesigner.TABLE_CONTACTS, null, values);
		
	}
	
	
	public List<Contact> getAllContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_CONTACTS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact = toContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		
		return contacts;
	}

	public Contact toContact(Cursor cursor) {
		Contact pojo = new Contact();
		pojo.id = cursor.getString(0);
		pojo.name = cursor.getString(1);
		pojo.number = cursor.getString(2);
		pojo.isChecked = cursor.getInt(3)>0;
		return pojo;
	}
	
	public boolean deleteContact(Contact contact)
	{
		
		return database.delete(DBDesigner.TABLE_CONTACTS, DBDesigner.CONTACT_ID  +"="+ contact.id,null)>0;
		
	}
	
	public void setEmergencyContact(Contact contact, boolean newChecked)
	{
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_ISCHECKED, newChecked);
		database.update(DBDesigner.TABLE_CONTACTS, values, DBDesigner.COLUMN_ID +"="+contact.id, null);
	}
	

}
