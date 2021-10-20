package app.activities;

import java.util.List;

import com.parse.ParseUser;

import Location.MyLocation;
import Location.MyLocation.LocationResult;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import app.rescueMe.R;

public class Utilities extends Base{
	
	Button  tweet,gps;
	ToggleButton tgBut;
	TextView settingsText, locationStatus;
	String currentLoc = "";
	SharedPreferences recPrefs;
	final String PREF_NAME = "prefrences";
	Boolean status;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_utilities, frameLayout);
		
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		    	Log.v("Alert", "http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude());
		    	currentLoc = "Im here: http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude();
		    	
		    }
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
		
		//setContentView(R.layout.activity_utilities);
		settingsText = (TextView) findViewById(R.id.settingstext);
		tweet = (Button) findViewById(R.id.tweetOut);
		gps = (Button) findViewById(R.id.gpssettings);
		tgBut = (ToggleButton) findViewById(R.id.sendRecButton);
		settingsText.append(" "+ParseUser.getCurrentUser().getUsername());
		locationStatus 	= (TextView) findViewById(R.id.locationinfo);

	    recPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);      
	     //tgBut = (ToggleButton) findViewById(R.id.buttonFollow);        

	     status = recPrefs.getBoolean("Status", true);         
	            
	     if (status) {            
	    	 tgBut.setChecked(true);        
	     } else {          
	    	 tgBut.setChecked(false);
	     }
	     
	     checkFirstUse();
	     
	     
	     
	     
		
	}

	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		invalidateOptionsMenu();
		mDrawerList.setItemChecked(position, true);
		setTitle(listArray[position]);
		
		/*if (currentLoc.equals(""))
	     {	
			 locationStatus.append("");
	    	 locationStatus.append("Off");
	     }
	     else{
	    	 locationStatus.append("");
	    	 locationStatus.append("On");
	     }*/
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	
	public void postToTwitter(View v)
	{
		Intent tweetIntent = new Intent(Intent.ACTION_SEND);
		tweetIntent.putExtra(Intent.EXTRA_TEXT, "Just passed through a bad area. "+ currentLoc);/**This method launches an intent which uses package manager to check if the requested app exists on the phone*/
		tweetIntent.setType("text/plain");

		PackageManager packManager = getPackageManager();
		List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

		boolean resolved = false;
		for(ResolveInfo resolveInfo: resolvedInfoList){			/**Loops through resolvedinfolist to match twitter package name if the package exists launch twitter, if not toast that twitter is not installed*/
		    if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
		        tweetIntent.setClassName(
		            resolveInfo.activityInfo.packageName, 
		            resolveInfo.activityInfo.name );
		        resolved = true;
		        break;
		    }
		}
		if(resolved){
		    startActivity(tweetIntent);
		}else{
		    Toast.makeText(this, "Twitter is not installed", Toast.LENGTH_LONG).show();
		}	}
	
	
	public void onGPSClick(View v)
	{
		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}
	
	
	
	public void onToggleClicked(View view) { /**Save the status of whether to record or not to shared preference  */
		
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {

			SharedPreferences.Editor editor = recPrefs.edit();
			editor.putBoolean("Status", true);
			editor.commit();
	    } else {
	    	SharedPreferences.Editor editor = recPrefs.edit();
			editor.putBoolean("Status", false);
			editor.commit();
	    }
	};
	
	
	
	
	
	
	
	
	/*private void showDialogGPS() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setCancelable(false);
	    builder.setTitle("Enable GPS");
	    builder.setMessage("Please enable GPS");
	    builder.setInverseBackgroundForced(true);
	    builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            startActivity(
	                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	        }
	    });
	    builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	    
	    //LocationService newService = new LocationService();
	    //newService.bindService(service, conn, flags)
	}*/
	
	
	
	public void checkFirstUse()
	{	/**Displays a dialog on first use with instructions, this is only displayed when the app is first downloaded. 
		This is done by saving a boolean value to true, but after first use is set to false.*/
		SharedPreferences userPrefs = getSharedPreferences("UtilUserPrefs", 0);
		Boolean firstUse = userPrefs.getBoolean("firstUse", true);

		if(firstUse){
			
			 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			 alertDialog.setTitle("Welcome");
			 alertDialog.setMessage("To record audio after alert is sent choose here, by default recording is on. "
			 		+ "Recordings are saved to RescueMe cloud storage and are available on request");
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
		Intent home = new Intent(this, AlertScreen.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);
			
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	
	
	
	
	
	
	
	

}
