package app.activities;



import com.parse.ParseUser;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import app.applications.RescueApp;
import app.rescueMe.R;


public class Base extends Activity {
	
	//This is the base class that is inherited by all other activities, 
	public RescueApp app;
	public int AlertTime;
	protected FrameLayout frameLayout;//
	protected ListView mDrawerList;//
	protected String[] listArray = { "Alert", "Conacts", "Messages","Utilities","Logout" };//
	protected static int position;//
	private static boolean isLaunch = true;//
	 DrawerLayout mDrawerLayout;//
	 ActionBarDrawerToggle actionBarDrawerToggle;//
	 
	
	
	 protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    setContentView(R.layout.navigation_drawer_base_layout);//
	    frameLayout = (FrameLayout)findViewById(R.id.content_frame);//
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//
		mDrawerList = (ListView) findViewById(R.id.left_drawer);//
	    
	    app = (RescueApp) getApplication();
	    
	    app.dbManager.open();
	    
	    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));//
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				openActivity(position);
			}
		});//
		
		// enable ActionBar app icon to toggle nav drawer
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
	    
	   
	    
	    
	    actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,						
				mDrawerLayout, 				
				R.drawable.ic_action_save,     
				R.string.open_drawer,       
				R.string.close_drawer)       
		{ 
			@Override
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); 
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
			}
		};
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
		
		
		if(isLaunch){
			 /**
			  Setting this value to  false so it will only launch alertscreen activity on app first launch, base activity is always called when any child activity launches.
			   
			  
			  */
			isLaunch = false;
			openActivity(0);
		}
		
	    
	  }
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		app.dbManager.open();
		
		
	}

	  protected void onDestroy() {
	    super.onDestroy();
	    app.dbManager.close();
	  }
	  
	  
		protected void openActivity(int position) {
			
			
//			mDrawerList.setItemChecked(position, true);
//			setTitle(listArray[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			Base.position = position; //Setting currently selected position in this field so that it will be available in child activities. 
			
			switch (position) {
			case 0:
				
					
				startActivity(new Intent(this, AlertScreen.class));
				break;
			case 1:
				startActivity(new Intent(this, Contacts.class));
				break;
			case 2:
				startActivity(new Intent(this, SMS_Templates.class));
				break;
			case 3:
				startActivity(new Intent(this, Utilities.class));
				break;
			case 4:
				ParseUser.logOut();
				isLaunch = false;
				Intent intent = new Intent(getApplicationContext(), LoginSignupActivity.class);  /**Clear the back stack when logout is called so that no other activities will open when user presses back button*/
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				//startActivity(new Intent(this, LoginSignupActivity.class));
				//finish();
			

			default:
				break;
			}
			
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.rescue_me, menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			// The action bar home/up action should open or close the drawer. 
			
			if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
			
			switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		/* Called whenever we call invalidateOptionsMenu() */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // If the nav drawer is open, hide action items related to the content view
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	        hideMenuItems(menu, !drawerOpen);
	        
	        
		     MenuItem newContact = menu.findItem(R.id.action_add_contact);
		     MenuItem newSMS = menu.findItem(R.id.action_add);
		     
	        
	        if(this instanceof AlertScreen){
	        		//position = 0;
		          newContact.setVisible(false);
		          newSMS.setVisible(false);
		        }
		      else if(this instanceof Contacts){
		    	 // position = 2;
		          newContact.setVisible(true);
		          newSMS.setVisible(false);
		        }
		      else if(this instanceof Utilities){
		    	  //position = 3;
		          newContact.setVisible(false);
		          newSMS.setVisible(false);
		        }
		      else if(this instanceof SMS_Templates){ 
		    	  //position = 2;
	          	newContact.setVisible(false);
	          	newSMS.setVisible(true);
		      }
			
			
	        return super.onPrepareOptionsMenu(menu);
	    }
	    
	    
	    
	    
		/*@Override
		public void onBackPressed() {
			if(mDrawerLayout.isDrawerOpen(mDrawerList)){
				mDrawerLayout.closeDrawer(mDrawerList);
			}else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}*/
		
		private void hideMenuItems(Menu menu, boolean visible)
		{

		    for(int i = 0; i < menu.size(); i++)
		    {

		        menu.getItem(i).setVisible(visible);

		    }
		}
	
	  
	  
	  /////////////////////////////////////////////////////
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.rescue_me, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		
		super.onPrepareOptionsMenu(menu);
		 MenuItem contacts = menu.findItem(R.id.contacts);
	     MenuItem alert = menu.findItem(R.id.alert);
	     MenuItem sms = menu.findItem(R.id.newSMS);
	     MenuItem newContact = menu.findItem(R.id.action_add_contact);
	     MenuItem newSMS = menu.findItem(R.id.action_add);
	     MenuItem utilities = menu.findItem(R.id.utilities);
	     
	      
	      
	   
	      if(this instanceof AlertScreen){
	          alert.setVisible(false);
	          contacts.setVisible(true);
	          sms.setVisible(true);
	          newContact.setVisible(false);
	          newSMS.setVisible(false);
	        }
	      else if(this instanceof Contacts){
	    	  alert.setVisible(true);
	          newContact.setVisible(true);
	          sms.setVisible(true);
	          newSMS.setVisible(false);
	          contacts.setVisible(false);
	        }
	      else if(this instanceof Utilities){
	    	  utilities.setVisible(false);
	    	  alert.setVisible(true);
	          contacts.setVisible(true);
	          sms.setVisible(true);
	          newContact.setVisible(false);
	          newSMS.setVisible(false);
	        }
	      else{ 
	    	alert.setVisible(true);
          	newContact.setVisible(false);
          	newSMS.setVisible(true);
          	contacts.setVisible(true);
          	sms.setVisible(false);
	      }
		
		return true;
		
		
	}*/
	
	
	    
	public void settings(MenuItem item){
		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}
	
//	public void sms(MenuItem item){
//		startActivity(new Intent(this, SMS_Templates.class));
//	}
//	
//	public void contacts(MenuItem item){
//		startActivity(new Intent(this, Contacts.class));
//		
//	}
//	public void alert(MenuItem item){
//		startActivity(new Intent(this, AlertScreen.class));
//		
//	}
//	public void utilities(MenuItem item){
//		startActivity(new Intent(this, Utilities.class));
//	}
	public void chooseContact(MenuItem item)
	{
		Contacts c = new Contacts();//create an instance of contacts in order to gain access to chooseConatact() method
		c.chooseContact(item);
	}
	public void showNewSMS(MenuItem item)
	{
		SMS_Templates s = new SMS_Templates();//instance of SMS_templates to access showNewSMS() method
		s.showNewSMS(item);
	}


}
