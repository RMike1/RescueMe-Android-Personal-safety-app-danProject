package app.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Location.MyLocation;
import Location.MyLocation.LocationResult;
import app.rescueMe.R;
import app.services.RecordAudio;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.models.Contact;
import app.models.SMS_Message;


public class AlertScreen extends Base  {
	
	
	Button alertButton;
	Button btnCancelAlert;
	EditText message;
	EditText phoneNum;
	LocationManager manager;
	String messageToSend;
	boolean isItCanceled = false, isRecording;
	Log v;
	String numToSend;
	String locToSend = "";
	Runnable timeRunnable;
	Handler mHandler=new Handler();
	List<Contact>ContactList = new ArrayList<Contact>();
    List<SMS_Message> SMSlist = new ArrayList<SMS_Message>();
    List<String> numbersList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_alert_screen);
		getLayoutInflater().inflate(R.layout.activity_alert_screen, frameLayout);
		
		
		checkFirstUse();
		final Animation animScale = AnimationUtils.loadAnimation(this,
			   R.anim.anim_scale);
		alertButton = (Button) findViewById(R.id.alertButton);
		btnCancelAlert = (Button) findViewById(R.id.btnCancelAlert);
		
		
//		LocationResult locationResult = new LocationResult(){
//		    @Override
//		    public void gotLocation(Location location){
//		    	Log.v("Alert", "http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude());
//		    	locToSend = "Im here: http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude();
//		    	
//		    }
//		};
//		MyLocation myLocation = new MyLocation();
//		myLocation.getLocation(this, locationResult);
		
		btnCancelAlert.setOnClickListener(new OnClickListener() {
			/**set a listener to the cancel button which will cancel the alert */
			@Override
			public void onClick(View v) {
				isItCanceled = true;
				btnCancelAlert.setVisibility(View.GONE);
				mHandler.post(timeRunnable); /**this prepares the runnable to be executed on click.*/
				
				
			}
		});
		
		alertButton.setOnTouchListener(new View.OnTouchListener() {        
		    @Override	/**set an on touch listener to the alert button which acts like a deadman switch where the alert is launched on button released, allowing the user to hold their thumb on the button
		    			and if an incident occurs all they need to do is simply release their thumb to launch alert messages
*/		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		                Toast.makeText(getBaseContext(), "Armed", Toast.LENGTH_SHORT).show();
		                
		                isItCanceled = false;
		                return true; 
		            case MotionEvent.ACTION_UP:
		            	/**set the cancel button to visible for 3 seconds should the user press by mistake or need to cancel alert*/
		            	Toast.makeText(getBaseContext(), "Cancel within in 3 seconds!", Toast.LENGTH_SHORT).show();
		            	mHandler.postDelayed(timeRunnable, 3000);
		            	btnCancelAlert.setVisibility(View.VISIBLE);
		            	
		                return true;
		                
		        }
		        
		        return true;
		    }
		});
		
		timeRunnable=new Runnable(){
            @Override
            public void run() {
            	if(isItCanceled == false)
            	{	/**if cancel is not pressed run method is launched after 3 seconds and send the messages. A scale animation is also added to the button just for visual effect that alert is launched*/
	            	alertButton.startAnimation(animScale);
	            	
	            	sendMessage();
	            	if(isRecording){startService(new Intent(AlertScreen.this,RecordAudio.class));} /** If recording is set start record audio service*/
	            	
	            	btnCancelAlert.setVisibility(View.GONE); /**hide cancel button at this stage*/
	            	
            	}
            	
            	mHandler.removeCallbacks(timeRunnable); /**remove all pending posts of runnable*/
            }
            
        };
  	}
	
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		invalidateOptionsMenu();
		mDrawerList.setItemChecked(position = 0, true);
		setTitle(listArray[position]);
		getRecordStatus();
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		    	Log.v("Alert", "http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude());
		    	locToSend = "Im here: http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude();
		    	
		    }
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
				
		ContactList = app.dbManager.getAllContacts(); /**retrieve contacts and message to send when the activity resumes*/
		if(ContactList != null){
			getNumbersToSend();
		}
		else{
			Toast.makeText(this, "No contacts added", Toast.LENGTH_SHORT).show();
		}
		
		SMSlist = app.dbManager.getAllSMS();
		
		if(SMSlist!= null){
			getSOSMessage();
			//Log.v("Alert", messageToSend);
			
		}

		
	}

	
	
	public void sendMessage() 
	{
		//String num = "+353861949405"/*"0894620552"*/;
		if(locToSend == null)
		{							
			locToSend = ""; 
		}						
		
		try{  /**this method sends the sms messages from chosen numbers and message*/
			if(numbersList.isEmpty() || messageToSend == null)
			{
				throw new NoDetailsAvailableException("No details available");
			}
			for (int i = 0; i < numbersList.size(); i++) 
		     {
		             String message = messageToSend.toString();
		             String tempMobileNumber = numbersList.get(i).toString();
		             SmsManager sms = SmsManager.getDefault();
		             sms.sendTextMessage(tempMobileNumber, null, message+" "+locToSend+"\nSent from Rescue Me Personal Safety App!", null, null);
		             
		             
		      }
			
			Toast.makeText(this, "Help is on the way", Toast.LENGTH_SHORT).show();
			
		}catch(NoDetailsAvailableException e){
			Toast.makeText(getApplicationContext(),
			         "SMS failed, make sure you added SOS Contacts and SMS",
			         Toast.LENGTH_LONG).show();
			         e.printStackTrace();
		}
		
		
	}
	
	
	
	
	public void getSOSMessage()
	{	/**get the message that has been selected
*/		boolean sos;
		for(int i=0; i<SMSlist.size();i++)
		{
			sos = SMSlist.get(i).isChecked;
			if(sos == true){
				messageToSend = SMSlist.get(i).message;
				
			}
		}
		
	}
	
	public void getNumbersToSend()
	{	/**get the numbers to send from looping arraylist and getting checked booleans and add to new arraylist*/
		boolean contact;
		for(int i=0; i<ContactList.size();i++)
		{
			contact = ContactList.get(i).isChecked;
			if(contact == true)
			{
				//numToSend = ContactList.get(i).number;
				numbersList.add(ContactList.get(i).number);
				Log.v("Alert", ContactList.get(i).number);
			}
		}
	}
	
	public void getRecordStatus()
	{
		SharedPreferences recPrefs = getSharedPreferences("prefrences", 0);
		 
		Boolean str = recPrefs.getBoolean("Status", true); 
		if (str) {    
		     Log.v("Alert", "Im recording");
		     isRecording = true;
		} else {    
			Log.v("Alert", "Im not recording");
			isRecording = false;
		}
	}
	
	public void checkFirstUse()
	{	/**Displays a dialog on first use with instructions, this is only displayed when the app is first downloaded. 
		This is done by saving a boolean value to true, but after first use is set to false.*/
		SharedPreferences userPrefs = getSharedPreferences("UserPrefs", 0);
		Boolean firstUse = userPrefs.getBoolean("firstUse", true);

		if(firstUse){
			
			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			 alertDialog.setTitle("Welcome");
			 alertDialog.setMessage("Welcome to Rescue me. To get started add some contacts and some messsage templates. "
			 		+ "When added simply press ALERT button and release to send your emergency SMS to chosen contacts.");
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
	
	
	class NoDetailsAvailableException extends Exception{
		//Exception to catch error of no numbers numbers selected
		public NoDetailsAvailableException(String message)
		{
			super(message);
		}
		
		public String getMessage()
		{
			return super.getMessage();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	 


	
    	
    }
    
    
    

	
	


    

