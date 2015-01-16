package com.twitter.lib;

import twitter4j.User;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.android.TwitterMain;
import com.twitter.android.TwitterMain.OnAuthCompleteListener;
import com.twitter.android.TwitterMain.OnCompleteListener;

public class MainActivity extends Activity implements OnClickListener {

	private Button buttonLogin;
	private Button buttonTweet;
	private Button buttonTweetDialog;
	private Button buttonTweetImg;
	private Button buttonTweetImgTxt;

	private ImageView imageViewUserImg;
	private TextView textViewEmail;
	private TextView textViewName;
	
	private static final String CONSUMER_KEY = "iNM6E7344sGFBJIVwAfwzcGoS";
	private static final String CONSUMER_SECRET = "mfHIwvf7sHwbVGEB2V8BUxIdNBLWZc82hmG68e39dLzri4BNux";

	TwitterMain mTwitterMain;
	
	boolean authOnly=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initControls();
		
		//set key and secret for Twitter
		mTwitterMain=new TwitterMain(this,CONSUMER_KEY,CONSUMER_SECRET);
		
	}

	private void initControls() {

		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonTweet = (Button) findViewById(R.id.buttonTweet);
		buttonTweetDialog = (Button) findViewById(R.id.buttonTweetDialog);
		buttonTweetImg = (Button) findViewById(R.id.buttonTweetImg);
		buttonTweetImgTxt = (Button) findViewById(R.id.buttonTweetImgTxt);

		imageViewUserImg = (ImageView) findViewById(R.id.imageViewUserImg);
		textViewEmail = (TextView) findViewById(R.id.textViewEmail);
		textViewName = (TextView) findViewById(R.id.textViewName);

		buttonLogin.setOnClickListener(this);
		buttonTweet.setOnClickListener(this);
		buttonTweetDialog.setOnClickListener(this);
		buttonTweetImg.setOnClickListener(this);
		buttonTweetImgTxt.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == buttonLogin) {
			
			mTwitterMain.getAuthenticate();
			mTwitterMain.setOnAuthCompleteListener(new OnAuthCompleteListener() {
				@Override
				public void onAuthComplete(User user) {
					if(user!=null){
						textViewName.setText(user.getName());
						textViewEmail.setText("UserID : "+user.getId()+"");
					}
				}
			});
		} else if (v == buttonTweet) {
			
			//any test only 140 char
			String tweetString="Test Tweet in Lib :"+System.currentTimeMillis();
			
			//this must be in sdcard not from resource
			String imageUrl="";
			
			mTwitterMain.sendTweet(tweetString,imageUrl);
		
		} else if (v == buttonTweetDialog) {
			
			
			//any test only 140 char
			String tweetString="Test Tweet in Lib : Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :Test Tweet in Lib :"+System.currentTimeMillis();
			
			mTwitterMain.sendTweetWithDialog(tweetString);
			

		} else if (v == buttonTweetImg) {
			
			//any test only 140 char
			String tweetString="";
			
			//this must be in sdcard not from resource
			String imageUrl="sdCardPath";
			
			mTwitterMain.sendTweet(tweetString,imageUrl);
	

		} else if (v == buttonTweetImgTxt) {
			
			//any test only 140 char
			String tweetString="Test Tweet in Lib :"+System.currentTimeMillis();
			//this must be in sdcard not from resource
			String imageUrl="sdCardPath";
			mTwitterMain.sendTweet(tweetString,imageUrl);
		

		}
	}
}
