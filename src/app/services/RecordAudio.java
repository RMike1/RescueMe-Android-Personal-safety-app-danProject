package app.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class RecordAudio extends Service{
	
	
	
	final MediaRecorder recorder = new MediaRecorder();
	  String path;
	  Context context;
	  MediaPlayer myPlayer = null;
	  String filePath;
	  
	    
	  /*public RecordAudio(Context context String path) {
		this.context = context;
	    this.path = sanitizePath();
	    
	  }*/
	  
	  public void onCreate() {
		  super.onCreate();
		  
		    this.path = sanitizePath();
		    
		    Toast.makeText(this, "The Recording Service was Created", Toast.LENGTH_LONG).show();

		  
	  };
	  
	  @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		  Toast.makeText(this, "Recording Service Started", Toast.LENGTH_LONG).show();

		  try {
				start();					/**Recording starts here*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    countDowntimer.start();
		    Toast.makeText(this, "Recording Started", Toast.LENGTH_LONG).show();
		    
		return super.onStartCommand(intent, flags, startId);
		
		
	    
	}
	  
	  
	  @Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
	  
	  @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		  Toast.makeText(this, "Recording Service Destroyed", Toast.LENGTH_LONG).show();
		  saveToParse();     /**before the service is destroyed the file is uploaded to parse*/
		super.onDestroy();
		
		
		
	}
	  
	  private String getRandom()
	  {
		  	int min = 0;
			int max = 1000;

			Random r = new Random();
			int i1 = r.nextInt(max - min + 1) + min;
			String filePath = "RescueMeRecording"+i1;
			setFileName(filePath);
			return filePath;
	  }
	  
	  public void setFileName(String s)					/** These 2 methods set the file name first gets a random number and second adds needed characters and sets path to correct format*/
	  {
		  this.filePath = s;
	  }

	  private String sanitizePath(/*String path*/) 
	  {
		  String path = getRandom();
	    if (!path.startsWith("/")) {
	      path = "/" + path;
	    }
	    if (!path.contains(".")) {
	      path += ".MP4";
	    }
	    return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	  }

	  /**
	   * Starts a new recording.
	   */
	  public void start() throws IOException {
	    String state = android.os.Environment.getExternalStorageState();
	    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
	        throw new IOException("SD Card is not mounted.  It is " + state + ".");
	    }

	    // make sure the directory we plan to store the recording in exists
	    File directory = new File(path).getParentFile();
	    if (!directory.exists() && !directory.mkdirs()) {
	      throw new IOException("Path to file could not be created.");		/** Uses Media recorder to record audio in mp4 format and sets the output file for recorded file */
	    }
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
	    
	    /*recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);*/
	    recorder.setOutputFile(path);
	    recorder.prepare();
	    recorder.start();
	   
	  }

	  
	  CountDownTimer countDowntimer = new CountDownTimer(10000, 1000) { 	/** Countdown timer to record for set amount of time then calls stop and release on the media recorder object */
          public void onTick(long millisUntilFinished) {
          }

          public void onFinish() {
              try {
                 // Toast.makeText(getBaseContext(), "Stop recording Automatically ", Toast.LENGTH_LONG).show();	
                  stop();
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              
              
              Toast.makeText(RecordAudio.this, "Recording Finished ", Toast.LENGTH_SHORT).show();
              
          }};
	  
	  
	  public void stop() throws IOException {
	    recorder.stop();
	    recorder.release();
	    this.stopSelf();  /** Service stops itself after recording is finished*/
	    
	  }
	  
	  
	  public void saveToParse() {
		  
		  
		  
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(path).getAbsoluteFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}															/** Recorded audio file is uploaded to Parse*/

		  byte[] buf = new byte[1024];
		  int n;
		  try {
			while (-1 != (n = fis.read(buf)))
			      baos.write(buf, 0, n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  byte[] audioBytes = baos.toByteArray(); 
		  
		  
	       ParseUser currentUser = ParseUser.getCurrentUser();
	       
	       ParseFile file = new ParseFile(filePath+".MP4", audioBytes);
	       file.saveInBackground(new SaveCallback() {
	    	   public void done(ParseException e) {
	    		     if (e == null) {
	    		    	 Toast.makeText(RecordAudio.this, "File success ", Toast.LENGTH_SHORT).show();
	    		     } else {
	    		    	 Toast.makeText(RecordAudio.this, "File fail ", Toast.LENGTH_SHORT).show();
	    		    	 e.printStackTrace();
	    		     }
	    		   }
	    		 });
	       
	       ParseObject audioRecording = new ParseObject("AudioRecording");
	       audioRecording.put("user", currentUser);
		   audioRecording.put("appName", "RescueMe");
			  //audioRecording.saveInBackground();
		   audioRecording.put("RescueMeAudio", file);
		   audioRecording.saveInBackground(new SaveCallback() {
			   public void done(ParseException e) {
				     if (e == null) {
				    	 Toast.makeText(RecordAudio.this, "Object sucess ", Toast.LENGTH_SHORT).show();
				    	 
				    	 } else {
				    		 Toast.makeText(RecordAudio.this, "Object Failed ", Toast.LENGTH_SHORT).show();
				    		 e.printStackTrace();
				     }
				   }
				 });
			  
	       
		  
		   
			   
				 
				  
				 
	   }

	
	  


	
	  
	  


}

