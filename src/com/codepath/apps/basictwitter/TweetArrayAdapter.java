package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the data from item  for position
		Tweet tweet = getItem(position);
		View v;
		
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		
		// view initial
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvCreatedAt = (TextView) v.findViewById(R.id.tvCreatedAt);
		TextView tvName = (TextView) v.findViewById(R.id.tvName);

		
		// image
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		
		// screenName, @foo
		tvUserName.setText("@" + tweet.getUser().getScreenName());
		
		// body of message
		tvBody.setText(tweet.getBody());
		
		// name
		tvName.setText(tweet.getUser().getName());
		
		// created at
		long dateMillis = 0;
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);
		
		try {
			dateMillis = sf.parse(tweet.getCreatedAt()).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String createAt = (String) DateUtils.getRelativeDateTimeString(getContext(),
				dateMillis, DateUtils.MINUTE_IN_MILLIS,
				DateUtils.WEEK_IN_MILLIS, 0);
		String[] createAtString = createAt.split(",");
		String formattedTime =  createAtString[0].replaceAll(" hour.*", "h");
		formattedTime =  formattedTime.replaceAll(" minute.*", "m");
		formattedTime =  formattedTime.replaceAll(" second.*", "s");
		formattedTime =  formattedTime.replaceAll(" day.*", "d");
		formattedTime =  formattedTime.replaceAll("Yesterday", "1d");
		formattedTime =  formattedTime.replaceAll(" year.*", "y");
		tvCreatedAt.setText(formattedTime);

		return v;
		
	}
	
}
