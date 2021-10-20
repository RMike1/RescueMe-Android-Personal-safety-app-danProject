package app.activities;

import java.util.ArrayList;
import java.util.List;

import app.rescueMe.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.provider.ContactsContract;
import app.adapters.ContactAdapter;
import app.models.Contact;

public class Contacts extends Base {

	
	
	boolean toDelete = false;
	boolean numberExists = false;
	static final int PICK_CONTACT=1;
	ListView savedContacts;
	List <Contact> localContactslist  = new ArrayList <Contact>();
	List <Contact> newContactsList = new ArrayList<Contact>();
	public Activity activity;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_contacts_screen);
		getLayoutInflater().inflate(R.layout.activity_contacts_screen, frameLayout);
		
		checkFirstUse();
		savedContacts = (ListView) findViewById(R.id.listViewContacts);
		localContactslist = app.dbManager.getAllContacts();
		ContactAdapter adapter = new  ContactAdapter(this, localContactslist);
		savedContacts.setAdapter(adapter);
		
		
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		
		
		
		
	}
	
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		invalidateOptionsMenu();
		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);
		
		localContactslist = app.dbManager.getAllContacts();
		
		ContactAdapter adapter = new  ContactAdapter(this, localContactslist);
		savedContacts.setAdapter(adapter);
		
		
		savedContacts.setOnItemClickListener(new OnItemClickListener() {
			/**set listener on item click to launch alertdialog for deleting a contact*/

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contact item = localContactslist.get(position);
				showAlert(item);
				Toast.makeText(getBaseContext(), item.id, Toast.LENGTH_SHORT).show();
								
			}
		});
		
		
		savedContacts.setOnItemLongClickListener(new OnItemLongClickListener() {
			/**set long click listener to add selected contact to emergency contact which will be the group of numbers that SMS will be sent to*/
		@Override
		public boolean onItemLongClick(AdapterView<?> parent,
				View view, int position, long id) {
			Contact item = localContactslist.get(position);
			if(item.isChecked){
				
				app.dbManager.setEmergencyContact(item, false);
				Toast.makeText(getBaseContext(), "Removed from emergency contacts", Toast.LENGTH_SHORT).show();
			}
			else
			{
			
				app.dbManager.setEmergencyContact(item, true);
			Toast.makeText(getBaseContext(), "Added to emergency contacts", Toast.LENGTH_SHORT).show();
			}															/**adapter needs to be set here again to update the list of contacts*/
			localContactslist = app.dbManager.getAllContacts();
			ContactAdapter adapter = new  ContactAdapter(getBaseContext(), localContactslist);
			savedContacts.setAdapter(adapter);
			return true;
		}
	});
		
	}
	
		
	public  void chooseContact(MenuItem item){
		/**launches native contacts app and uses action pick to bring result back of whats chosen*/
		  Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		  startActivityForResult(intent, PICK_CONTACT);
	}
		  
		  @Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		 super.onActivityResult(reqCode, resultCode, data);

		 String cNumber = null;
		 
		 switch (reqCode) {
		 case (PICK_CONTACT) :
		   if (resultCode == Activity.RESULT_OK) {

		     Uri contactData = data.getData();
		     Cursor c =  getContentResolver().query(contactData, null, null, null, null);
		     if (c.moveToFirst()) {

		         String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

		         String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		           if (hasPhone.equalsIgnoreCase("1")) {
		          Cursor phones = getContentResolver().query( 
		                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
		                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
		                       null, null);
		             phones.moveToFirst();
		               cNumber = phones.getString(phones.getColumnIndex("data1"));
		             System.out.println("number is:"+cNumber);
		           }
		         String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		         Boolean isChecked = false;		         
		         Contact newContact = new Contact(id.toString(),name.toString(), cNumber.toString(),isChecked);
		         checkContactExists(newContact);
		         if(!numberExists)
		         {
		  
		        	 app.dbManager.addContact(newContact);
		        	 Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
		        	 

		         }
		         else{
		        	 Toast.makeText(this, "This contact is already saved", Toast.LENGTH_SHORT).show();
		         }
		         		        		 
		         	numberExists = false;
		         }
		 	    
		   }
		   break;
		   }
		  
		 }
		  
		  
	public void checkContactExists(Contact contact)
	{		/**check if contact already exits in the list*/
		//localContactslist = app.dbManager.getAllContacts();
		
		for(int i=0; i<localContactslist.size();i++)
		{
			 
			if(localContactslist.get(i).number.equals(contact.number))
			{
				numberExists = true;
					 
			}
			
		}
  }
		  
		  

		 
		 
		 
		 
		 
		
		 
		 

		 public void showAlert(final Contact contact)
		 {	//alert dialog for deleting contact
		 
			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			 alertDialog.setTitle("Delete Contact");
			 alertDialog.setMessage("Delete contact : "+ contact.name);
			 
			 
			 alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		 
		            
		            Toast.makeText(getApplicationContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
		            app.dbManager.deleteContact(contact);
		            //localContactslist.remove(contact);
		            localContactslist = app.dbManager.getAllContacts();
		            ContactAdapter adapter = new ContactAdapter(getBaseContext(), localContactslist);			 
		    					//set adapter again to update list and save updated arraylist to sharedpreference
		    			         savedContacts.setAdapter(adapter);
		    			         
		            
		            }
		        });
		 
		        
		        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            
		            
		            dialog.cancel();
		            }
		        });
		        

			 alertDialog.show();
			
			
		 }
		 
		 
		 public void checkFirstUse()
			{	/**Displays a dialog on first use with instructions, this is only displayed when the app is first downloaded. 
				This is done by saving a boolean value to true, but after first use is set to false.*/
				SharedPreferences userPrefs = getSharedPreferences("ConuserPrefs", 0);
				Boolean firstUse = userPrefs.getBoolean("firstUse", true);

				if(firstUse){
					
					 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
					 alertDialog.setTitle("Contacts");
					 alertDialog.setMessage("After you have added contacts long press to add this contact to your emergency contacts list."
					 		+ "You can add as many you like but be aware of the cost of sending multiple messages");
					 alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog,int which) {
				 
				            	dialog.cancel();
				            
				            }
				        });
				alertDialog.show();
				
				SharedPreferences.Editor editor = userPrefs.edit();
				editor.putBoolean("firstUse", false);
				editor.commit();
				}
				
			}
		 
		 
		 @Override
			public void onBackPressed() {
				
				super.onBackPressed();
					
					//startActivity(new Intent(this,AlertScreen.class));
				Intent home = new Intent(this, AlertScreen.class);  /**Back button is overridden for efficiency to always return to alertscreen, rather than going through whole stack of activities*/
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);
					
			}
		 
		 

		 @Override
			protected void onDestroy() {
				// TODO Auto-generated method stub
				super.onDestroy();
				
			}


		



		
	
	

}
