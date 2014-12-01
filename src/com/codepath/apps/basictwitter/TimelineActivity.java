package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private static final int TWEET_REQUEST = 0;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweet;
	private ListView lvTweets;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		populateTimeline();
		getVerifyCredentials();
		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweet = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweet);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.d("debug", "page=" + page);
				Log.d("debug", "totalItemsCount=" + totalItemsCount);
				
//				Tweet firstTweet = tweets.get(1);
				Tweet lastTweet = tweets.get(totalItemsCount-1);
//				Log.d("debug", "max id=" + firstTweet.getId_str());
				Log.d("debug", "since id=" + lastTweet.getId_str());
				String max_id = lastTweet.getId_str();
				
				TwitterClient client = TwitterApplication.getRestClient();
				client.getHomeTimeline(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray json) {
//						aTweet.addAll(Tweet.fromJSONArray(json));
						
						tweets.addAll(Tweet.fromJSONArray(json));
						Log.d("debug", "size of tweets is=" + tweets.size());
						aTweet.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s.toString());
					}
				}, max_id);
			}
		});
	}
	
	private void getVerifyCredentials() {
		TwitterClient client = TwitterApplication.getRestClient();
		client.getVerifyCredentials(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				user = User.fromJSON(json);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}

	public void populateTimeline() {
		TwitterClient client = TwitterApplication.getRestClient();
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				aTweet.addAll(Tweet.fromJSONArray(json));
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
	
	public void onComposeAction(MenuItem mi) {
		Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, ComposeActivity.class);
		intent.putExtra("user", user);
		Log.d("debug", user.getName());
		Log.d("debug", user.getScreenName());

		startActivityForResult(intent, TWEET_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK && requestCode == TWEET_REQUEST) {
			// refresh
	    	tweets.clear();
	    	populateTimeline();
	    }
	}
	
	
}
