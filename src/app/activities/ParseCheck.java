package app.activities;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import app.rescueMe.R;

public class ParseCheck extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_splash);
		
		
		
		
		// check the current user is an anonymous user
				if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
					// If user is not signed in send the user to LoginSignupActivity.class
					Intent intent = new Intent(ParseCheck.this,
							LoginSignupActivity.class);
					startActivity(intent);
					finish();
				} else {
					
					ParseUser currentUser = ParseUser.getCurrentUser();
					if (currentUser != null) {
						// Send logged in users to Alertscreen
						Intent intent = new Intent(ParseCheck.this, AlertScreen.class);
						startActivity(intent);
						finish();
					} else {
						
						Intent intent = new Intent(ParseCheck.this,
								LoginSignupActivity.class);
						startActivity(intent);
						finish();
					}
				}
		 
			}
		
		/*Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
				
				Splash.this.startActivity(new Intent(Splash.this, Base.class));
			}
		},5000);*/
		
		
	//}	
	

}
