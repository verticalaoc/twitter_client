package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	private TextView tvName;
	private TextView tvScreenName;
	private EditText etTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		User user = (User) getIntent().getSerializableExtra("user");
		
		// initial view
		tvName = (TextView) findViewById(R.id.tvCName);
		tvScreenName = (TextView) findViewById(R.id.tvCScreenName);
		etTweet = (EditText) findViewById(R.id.etInput);

		// set value
		tvName.setText(user.getName());
		tvScreenName.setText("@" + user.getScreenName());

	}
	
	public void onClickTweet(MenuItem mi) {
		String tweet = etTweet.getText().toString();
		TwitterClient client = TwitterApplication.getRestClient();
		client.updateStatus(tweet, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				Toast.makeText(ComposeActivity.this, "tweeted", Toast.LENGTH_SHORT).show();
				View view;
				view = null;
				onSubmit(view);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
				
		});
	}
	
	public void onSubmit(View v) {
	  // Prepare data intent 
	  Intent data = new Intent();
	  // Activity finished ok, return the data
	  setResult(RESULT_OK, data); // set result code and bundle data for response
	  finish(); // closes the activity, pass data to parent
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
