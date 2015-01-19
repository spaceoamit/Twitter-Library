# Twitter-Library
 Step 1 : 	Get Twitter CONSUMER_KEY and CONSUMER_SECRET from Twitter Developer 	Account 	https://dev.twitter.com/
 Twitter Library project implimentation:

Step 2 : 
	Import Library project “Twitter-Lib_v1.1”
Step 3 : 
	Configer Android >> Library in your project
Step 4: 
	initalize twitter main Object with key and secret:
	i.e. In Oncreate method of class.
	TwitterMain mTwitterMain;
	mTwitterMain=new TwitterMain(this,CONSUMER_KEY,CONSUMER_SECRET);

ForLogin:

mTwitterMain.getAuthenticate();
mTwitterMain.setOnAuthCompleteListener(new OnAuthCompleteListener() {
				@Override
				public void onAuthComplete(User user) {
					if(user!=null){
						user.getName();
						user.getId();
					}
				}
			});
For Tweet: 

mTwitterMain.sendTweet(tweetString,imageUrl);

tweetString= String that you want to tweet if you don't want to tweet text 			 string  then pass it blank or null

imageUrl  = Pass image path from sdcard if don't want to pass image pass it 	   		 blank or null
  
Note:  tweetString must be less then 140 char
	 one parameter is compulsory you can not pass both null or blank.


Text Tweeting with Dialog: 

mTwitterMain.sendTweetWithDialog(tweetString);
tweetString= string that you pass you can see on dialog you can make edit it after.
		
Note : if you want to reset access token and authorise ap agin call below method.
 
mTwitterMain.resetTwitterToken();
