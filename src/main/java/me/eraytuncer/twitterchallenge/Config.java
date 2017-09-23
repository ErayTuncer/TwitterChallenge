package me.eraytuncer.twitterchallenge;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	
	private Long timerInSeconds;
	private String hashTag;

	public static Config create(File configFile) {
		FileReader reader;
		Config config = new Config();
		try {
			reader = new FileReader(configFile);
			JSONObject rootObject = (JSONObject) new JSONParser().parse(reader);
			config.consumerKey       = (String)  rootObject.get("consumerKey");
			config.consumerSecret    = (String)  rootObject.get("consumerSecret");
			config.accessToken       = (String)  rootObject.get("accessToken");
			config.accessTokenSecret = (String)  rootObject.get("accessTokenSecret");
			config.timerInSeconds    = (Long)    rootObject.get("timerInSeconds");
			config.hashTag           = (String)  rootObject.get("hashTag");		
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return config;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public Long getTimerInSeconds() {
		return timerInSeconds;
	}

	public String getHashTag() {
		return hashTag;
	}
	
}
