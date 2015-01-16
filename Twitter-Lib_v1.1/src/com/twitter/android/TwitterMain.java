package com.twitter.android;

import java.io.File;

import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.android.TwitterApp.TwDialogListener;
import com.twitter.lib.R;

public class TwitterMain {
	
	private TwitterApp mTwitter;
	
	TwitterSession mSession;
	
	private ProgressDialog mProgressDialog;
	
	public OnCompleteListener onCompleteListener;
	public interface OnCompleteListener {
		public void onComplete(String action);
	}	
	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}
	
	//auth
	public OnAuthCompleteListener onAuthCompleteListener;
	public interface OnAuthCompleteListener {
		public void onAuthComplete(User user);
	}	
	public void setOnAuthCompleteListener(OnAuthCompleteListener onAuthCompleteListener) {
		this.onAuthCompleteListener = onAuthCompleteListener;
	}
	
		private enum FROM {
		TWITTER_POST, TWITTER_LOGIN
	};

	private enum MESSAGE {
		SUCCESS, DUPLICATE, FAILED, CANCELLED
	};

	Activity activity;
	String imageUrl;
	String MessageString;
	
	boolean authOnly = false;
	
	boolean postWithDialog = false;


	@SuppressLint("NewApi")
	public TwitterMain(Activity activity, String consumerKey, String consumerSecret) {
		if (android.os.Build.VERSION.SDK_INT >= 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		this.activity = activity;
		mTwitter = new TwitterApp(activity, consumerKey, consumerSecret);
		mSession = new TwitterSession(activity);
		
	}
	@SuppressLint("NewApi")
	public void getAuthenticate() {
		// TODO Auto-generated method stub
		this.authOnly = true;
		if (android.os.Build.VERSION.SDK_INT >= 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		if(mSession.getAccessToken()==null){
			mTwitter.setListener(mTwLoginDialogListener);
			send();	
		}
		else {
			Toast.makeText(activity, "Already authorise!!", Toast.LENGTH_SHORT).show();
		}
		
	
	}
	
	
	public void send() {
		mTwitter.resetAccessToken();
		mTwitter.authorize();
	}
	
	public void resetTwitterToken() {
		mTwitter.resetAccessToken();
		
	}


	private void postAsToast(FROM twitterPost, MESSAGE success) {
		switch (twitterPost) {
		case TWITTER_LOGIN:
			switch (success) {
			case SUCCESS:
				Toast.makeText(activity, "Login Successful", Toast.LENGTH_LONG)
						.show();
				break;
			case FAILED:
				Toast.makeText(activity, "Login Failed", Toast.LENGTH_LONG)
						.show();
			default:
				break;
			}
			break;
		case TWITTER_POST:
			switch (success) {
			case SUCCESS:
				Toast.makeText(activity, "Posted Successfully",
						Toast.LENGTH_LONG).show();
				break;
			case FAILED:
				Toast.makeText(activity, "Posting Failed", Toast.LENGTH_LONG)
						.show();
				break;
			case DUPLICATE:
				Toast.makeText(activity,
						"Posting Failed because of duplicate message...",
						Toast.LENGTH_LONG).show();
			default:
				break;
			}
			break;
		}
	}
	


	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {
			postAsToast(FROM.TWITTER_LOGIN, MESSAGE.FAILED);
			if(authOnly){
				if(onAuthCompleteListener!=null)
				onAuthCompleteListener.onAuthComplete(null);
			}else {
				if(onCompleteListener!=null)
				onCompleteListener.onComplete("Fail");	
			}
			
		}

		public void onComplete(String value, User user) {
			// Log.e(Constant.TAG, user.toString());
			try {
				
				if (!authOnly) {
					if(postWithDialog){
						postWithDialog=false;
						callDialog();
					}else {
						
						tweetData();	
					}
					
				}else {
					authOnly=false;
					if(onAuthCompleteListener!=null)
					onAuthCompleteListener.onAuthComplete(user);
				}
			} catch (Exception e) {
				if (e.getMessage().toString().contains("duplicate")) {
					postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
				}
				e.printStackTrace();
				if(onCompleteListener!=null)
				onCompleteListener.onComplete("Fail");
			}
			//mTwitter.resetAccessToken();
			// finish();
		}


	};

	private void callDialog() {
		
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				activity);
 
			// set title
			View mTitleView=activity.getLayoutInflater().inflate(R.layout.titlebar, null);
			alertDialogBuilder.setCustomTitle(mTitleView);
			View mContentView=activity.getLayoutInflater().inflate(R.layout.dialog, null);
			alertDialogBuilder.setView(mContentView);
			  // set values for custom dialog components - text, image and button
	         final EditText editTextString = (EditText) mContentView. findViewById(R.id.editTextString);
	         final TextView textViewChar = (TextView) mContentView.findViewById(R.id.textViewChar);
	         editTextString.setText(MessageString);
	         if(MessageString.length()>140)textViewChar.setTextColor(Color.RED);
	         textViewChar.setText(MessageString.length()+"");
	         final ImageButton imageButtonSend = (ImageButton) mTitleView.findViewById(R.id.imageButtonSend);
				// create alert dialog
				final AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		 
         imageButtonSend.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
            	 
            	 
            	 try {
					mTwitter.updateStatus(editTextString.getText().toString());
					 postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 alertDialog.dismiss();
             }
         });
         
         editTextString.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s.length()>140 || s.length()==0){
					textViewChar.setTextColor(Color.RED);
					imageButtonSend.setEnabled(false);
				}else {
					textViewChar.setTextColor(Color.DKGRAY);
					imageButtonSend.setEnabled(true);
				}
				textViewChar.setText(""+s.length());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	private void showProcessDialog(){
		
		mProgressDialog=new ProgressDialog(activity);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
	}
	private void closeProcessDialog(){
		try {
			if(mProgressDialog!=null){
				if(mProgressDialog.isShowing()){
					mProgressDialog.dismiss();
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	private void tweetData() {
		// TODO Auto-generated method stub
		try {
			
			if(imageUrl.toString().length()>0){
				
				/*new Thread(new Runnable() {
					
					@Override
					public void run() {*/
						//showProcessDialog();
						try {
							mTwitter.uploadPic(new File(imageUrl),MessageString);
						} catch (Exception e) {
							e.printStackTrace();
						}
				/*		
					}
				}).start();*/
				
				
			}else {
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							mTwitter.updateStatus(MessageString);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}
				});
			}
			postAsToast(FROM.TWITTER_POST, MESSAGE.SUCCESS);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			postAsToast(FROM.TWITTER_POST, MESSAGE.DUPLICATE);
		}
	}

	
	@SuppressLint("NewApi")
	public void sendTweet(String MessageString, String imageUrl) {
		
		
		if(MessageString==null)	MessageString="";
		if(imageUrl==null)imageUrl="";
		
		if(MessageString.length()==0 && imageUrl.length()==0){
			Toast.makeText(activity, "tweet string and image url both blank", Toast.LENGTH_LONG).show();
			return;
		}
		this.MessageString=MessageString;
		this.imageUrl=imageUrl;
		
		if (android.os.Build.VERSION.SDK_INT >= 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if(mSession.getAccessToken()==null){
			mTwitter.setListener(mTwLoginDialogListener);
			send();	
		}
		else {
			tweetData();
		}
		
		
	}
	@SuppressLint("NewApi")
	public void sendTweetWithDialog(String tweetString) {
		
		if(tweetString==null)tweetString="";
		
		this.MessageString=tweetString;
		postWithDialog=true;
		if (android.os.Build.VERSION.SDK_INT >= 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		if(mSession.getAccessToken()==null){
			mTwitter.setListener(mTwLoginDialogListener);
			send();	
		}
		else {
			callDialog();
		}
		
	}


}
