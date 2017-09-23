package me.eraytuncer.twitterchallenge;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ChallengeTracker extends Thread implements StatusListener{

	private Config config;
	private Twitter twitter;
	private TwitterStream twitterStream;
	
	public ChallengeTracker(Config config) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setOAuthConsumerKey(config.getConsumerKey())
			.setOAuthConsumerSecret(config.getConsumerSecret())
			.setOAuthAccessToken(config.getAccessToken())
			.setOAuthAccessTokenSecret(config.getAccessTokenSecret());
		
		Configuration twitterConfig = cb.build();

		this.twitter = new TwitterFactory(twitterConfig).getInstance();
		this.twitterStream = new TwitterStreamFactory(twitterConfig).getInstance();
		this.config = config;
	}

	@Override
	public void run() {
		super.run();
		
		FilterQuery filterQuery = new FilterQuery();
		filterQuery.track(config.getHashTag());
		
		twitterStream.addListener(this);
		twitterStream.filter(filterQuery);
	}
	
	@Override
	public void onException(Exception arg0) {}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {}

	@Override
	public void onScrubGeo(long arg0, long arg1) {}

	@Override
	public void onStallWarning(StallWarning arg0) {}
	
	@Override
	public void onTrackLimitationNotice(int arg0) {}

	@Override
	public void onStatus(Status status) {
		if (status.getUserMentionEntities().length > 0) {
			Status inviteStatus = inviteChallenge(status);
			scheduleTimer(inviteStatus);			
		}
	}

	private Status inviteChallenge(Status status) {
		String replyMessage = "You have "+ TimeUnit.SECONDS.toHours(config.getTimerInSeconds()) + " hours to accomplish!";

		long replyID = status.getId();
		StatusUpdate statusUpdate = new StatusUpdate(replyMessage);
		statusUpdate.setInReplyToStatusId(replyID);

		try {
			return twitter.updateStatus(statusUpdate);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private void scheduleTimer(Status inviteStatus) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.schedule(new Runnable() {			
			@Override
			public void run() {
				StatusUpdate statusUpdate = new StatusUpdate("Time is up!");
				statusUpdate.setInReplyToStatusId(inviteStatus.getId());
				try {
					twitter.updateStatus(statusUpdate);
				} catch (TwitterException e) {
					e.printStackTrace();
				}		
			}
		}, config.getTimerInSeconds(), TimeUnit.SECONDS);
	}
	
}
