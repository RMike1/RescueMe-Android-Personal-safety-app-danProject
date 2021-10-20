package app.activities;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.rescueMe.R;


public class LoginSignupActivity extends Activity {
	
	Button loginbutton;
	Button signup;
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;
 
	
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_loginsignup);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
 
		
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);
 
		
		loginbutton.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();
 
				
				ParseUser.logInInBackground(usernametxt, passwordtxt,
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									
									Intent intent = new Intent(
											LoginSignupActivity.this,
											AlertScreen.class);
									startActivity(intent);
									Toast.makeText(getApplicationContext(),
											"Successfully Logged in",
											Toast.LENGTH_LONG).show();
									finish();
								} else {
									username.setText("");
									password.setText("");
									Toast.makeText(
											getApplicationContext(),
											
											"No such user exist, please signup",
											Toast.LENGTH_LONG).show();
								}
							}

							
						});
			}
		});
		// Register Button Click Listener
		signup.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				// Retrieve the text entered from the EditText
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();
 
				
				if (usernametxt.equals("") && passwordtxt.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please complete the sign up form",
							Toast.LENGTH_LONG).show();
 
				} else {
					// Save new user data into Parse
					ParseUser user = new ParseUser();
					user.setUsername(usernametxt);
					user.setPassword(passwordtxt);
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Show a Toast message upon successful registration
								Toast.makeText(getApplicationContext(),
										"Successfully Signed up, please log in.",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"Sign up Error", Toast.LENGTH_LONG)
										.show();
							}
						}
					});
				}
 
			}
		});
 
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	        finish();
	}
}
