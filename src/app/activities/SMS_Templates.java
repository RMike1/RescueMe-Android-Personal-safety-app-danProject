package app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import app.adapters.TemplateAdapter;
import app.models.SMS_Message;
import app.rescueMe.R;

import com.google.gson.Gson;

public class SMS_Templates extends Base {

	Button btnNewSms;
	ListView smsTemplates;
	EditText smsToSave;
	EditText hideEdit;
	Button btnEditSms;
	List <SMS_Message> smsList = new ArrayList<SMS_Message>();
	Gson gson;
	TemplateAdapter adapter;
	LinearLayout editlayout;
	LinearLayout addlayout;
	LinearLayout listlayout;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_sms_templates);
		getLayoutInflater().inflate(R.layout.activity_sms_templates, frameLayout);
		
		checkFirstUse();
		btnNewSms = (Button) findViewById(R.id.buttonAddTemplate);
		smsTemplates = (ListView) findViewById(R.id.smsTemplateList);
		smsToSave = (EditText) findViewById(R.id.smsToSave);
		hideEdit = (EditText) findViewById(R.id.edit_textTHide);
		btnEditSms = (Button) findViewById(R.id.btn_editSave);
		editlayout = (LinearLayout) findViewById(R.id.editlayout);
		addlayout = (LinearLayout) findViewById(R.id.addlayout);
		listlayout = (LinearLayout) findViewById(R.id.listLayout);
		//hideEdit.setVisibility(View.GONE);;
		
		
		
	}
	
	protected void onPause() 
	{
		super.onPause();
		
		
		

		
		
	};
	
	protected void onResume() 
	{
		super.onResume();
			
			
			
			invalidateOptionsMenu();
			mDrawerList.setItemChecked(position, true);
			setTitle(listArray[position]);
			smsList = app.dbManager.getAllSMS();
			
			TemplateAdapter adapter = new  TemplateAdapter(this, smsList);
    			//set adapter to list
    	    smsTemplates.setAdapter(adapter);
    	    
    		if(editlayout.getVisibility() == View.VISIBLE){
    			listlayout.setClickable(false);
    			//if the edit message layout is open make the sms list unclickable
    		}
    			else{
    	    
    		smsTemplates.setOnItemClickListener(new OnItemClickListener() {
    					//set listener to listview item
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					SMS_Message msg = smsList.get(position);
					showAlert(msg);
						// call alert dialog of options when the item is clicked				
				}
			});
    		}
            smsTemplates.setOnItemLongClickListener(new OnItemLongClickListener() {
            	//set long click listener to listview item
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					/*for(int i =0;i<smsList.size();i++)
					{
						smsList.get(i).setChecked(false);
					}
					SMS_Message sms = smsList.get(position);
					if(sms.isChecked == false){
						//set sms that will be sent						
						sms.setChecked(true);
						
						Toast.makeText(getBaseContext(), "Rescue SMS Selected", Toast.LENGTH_SHORT).show();
					}*/
					SMS_Message sms = smsList.get(position);
					System.out.println(position);
					System.out.println(smsList.size());
					System.out.println(sms.id);
					if(sms.isChecked == false){
						//set sms that will be sent						
						app.dbManager.setEmergencySMS(sms, true);
						smsList = app.dbManager.getAllSMS();
					}
					else
					{
						app.dbManager.setEmergencySMS(sms, false);
						smsList = app.dbManager.getAllSMS();
					}
					
					
					TemplateAdapter adapter = new  TemplateAdapter(getBaseContext(), smsList);
		    		//set adapter to list view to update once sms to send is set
		    	    smsTemplates.setAdapter(adapter);
		    	    
					return true;
					
				}
			});
		
            
	}
	
	
	
	
	
	public void addNewSMS(View view)
	{
		
		
		SMS_Message sms = new SMS_Message(smsToSave.getText().toString(),false);//+"\nIm at :");
		if(!sms.message.equals(""))
		{	/**if the new message is not empty add it to the list
*/			
			app.dbManager.addSMS(sms);
			
			smsList = app.dbManager.getAllSMS();			
				
			TemplateAdapter adapter = new  TemplateAdapter(this, smsList);
			
			smsTemplates.setAdapter(adapter);
			smsToSave.clearFocus();
			//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    inputMethodManager.toggleSoftInputFromWindow(addlayout.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	        smsToSave.setText("");
	        
	        
	        Toast.makeText(this, "SMS Added", Toast.LENGTH_SHORT ).show();
			
		}
		else{
		
        Toast.makeText(this, "Enter your message template", Toast.LENGTH_SHORT ).show();
		}
        
        addlayout.setVisibility(View.GONE);
        /**remove the add sms layout once the message has been added to the list*/
        
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		if(editlayout.getVisibility() == View.VISIBLE || addlayout.getVisibility() == View.VISIBLE)
		{/**reload the current activity if the back button is pressed when the while the edit layout or add layout is open*/
			
			finish();
			startActivity(getIntent());
		}
		else{
			
			//startActivity(new Intent(this,AlertScreen.class));
			Intent home = new Intent(this, AlertScreen.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);			/**Back button is overridden for efficiency to always return to alertscreen, rather than going through whole stack of activities*/
			startActivity(home);
			
		}
		
		
		
		
	}
	
	
	
	
	
	 public void showAlert(final SMS_Message message)
	 {		//alert dialog of three options when the user clicks an item in the listview
	 
		 final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		 alertDialog.setTitle("SMS Manager");
		 alertDialog.setMessage(""+message.message);
		 		 
		 alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	/**remove message from the list set adapter and save the new list*/
	            Toast.makeText(getApplicationContext(), "Message deleted", Toast.LENGTH_SHORT).show();
	            
	            app.dbManager.deleteSMS(message);
	            smsList.remove(message);
	           	TemplateAdapter adapter = new  TemplateAdapter(getBaseContext(), app.dbManager.getAllSMS());
	    		smsTemplates.setAdapter(adapter);
	    	    //sp.saveSMSTemplates(getBaseContext(), smsList);
	            
	            }
	        });
	 
	        
	        alertDialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	           editlayout.setVisibility(View.VISIBLE);
	           btnEditSms.setVisibility(View.VISIBLE);
	           listlayout.setVisibility(View.GONE);
	           hideEdit.setText(message.message);
	           hideEdit.setSelection(hideEdit.getText().length());
	           addlayout.setVisibility(View.GONE);
	           btnEditSms.setOnClickListener(new OnClickListener(){
	               @Override
	               //On click function 
	               public void onClick(View view) {
	            	   if(!hideEdit.getText().toString().isEmpty())
	            	   {/**if the edited message is not empty change the current text to the new text and save to database*/
	                   message.message = (hideEdit.getText().toString());
	                   
	                   TemplateAdapter adapter = new  TemplateAdapter(getBaseContext(), smsList);//2 lines forgot to add
	                   smsTemplates.setAdapter(adapter);
	                   app.dbManager.editSMS(message.id, message.message);
	                   editlayout.setVisibility(View.GONE);
	                   listlayout.setVisibility(View.VISIBLE);
	            	   }
	            	   else{
	            		   Toast.makeText(getBaseContext(), "Cannot save an empty message!", Toast.LENGTH_SHORT).show();
	            	   }
	                   
	               }
	           });
	           
	            	
	            }
	        });
	        
	        
            alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                // User pressed Cancel button. 
                }
            });
	        
            if(addlayout.getVisibility()== (View.GONE)){  
            		alertDialog.show();// if the add layout is not open show alert dialog on click
            }
		
	 }
	 
	 
	 
	 
	 
	 public void showNewSMS(MenuItem item)
	 {  /**open and close add layout by showing and hiding layouts*/
		 if(addlayout.getVisibility()== (View.VISIBLE))
		 {
			 addlayout.setVisibility(View.GONE);
			 smsToSave.clearFocus();
			 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			 InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    inputMethodManager.toggleSoftInputFromWindow(addlayout.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		 }
		 else 
		 {
			 addlayout.setVisibility(View.VISIBLE);
			 smsToSave.requestFocus();
			 //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			 InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    inputMethodManager.toggleSoftInputFromWindow(addlayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		 }
		 
	 }
	 
	 
	 public void checkFirstUse()
		{	/**Displays a dialog on first use with instructions, this is only displayed when the app is first downloaded. 
			This is done by saving a boolean value to true, but after first use is set to false.*/
			SharedPreferences userPrefs = getSharedPreferences("mesUserPrefs", 0);
			Boolean firstUse = userPrefs.getBoolean("firstUse", true);

			if(firstUse){
				
				 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				 alertDialog.setTitle("Messages");
				 alertDialog.setMessage("Add new messages to be sent, you can choose one from the list to send as your emergency message."
				 		+ "To do this simply long press on the message.");
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
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
		}
	 
}
