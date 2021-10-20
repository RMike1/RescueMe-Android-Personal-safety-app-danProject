package app.applications;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;
import app.db.DBManager;
import app.models.Contact;
import app.models.SMS_Message;



public class RescueApp extends Application{
	
	
	
	 public void onCreate() {
	        super.onCreate();
	 
	        
	        Parse.initialize(this, "ApplicationID", "ClientKey");
	 
	        ParseUser.enableAutomaticUser();
	        ParseACL defaultACL = new ParseACL();
	 
	        
	        defaultACL.setPublicReadAccess(true);
	 
	        ParseACL.setDefaultACL(defaultACL, true);
	    }
	 
	 
	
	public DBManager dbManager = new DBManager(this);
	
	public static List<SMS_Message> messages = new ArrayList<SMS_Message>(); 
	public static List<Contact> contacts = new ArrayList<Contact>(); 
	
	
	public boolean addSMS(SMS_Message sms)
	{
		dbManager.addSMS(sms);
		return false;
	}
	
	public boolean addContact(Contact contact)
	{
		dbManager.addContact(contact);
		return false;
	}
	
	
	

}
