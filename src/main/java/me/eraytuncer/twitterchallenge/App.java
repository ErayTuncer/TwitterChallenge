package me.eraytuncer.twitterchallenge;

import java.io.File;

public class App {

	public static void main(String[] args) {
		if (args.length > 0) {
			Config config = Config.create(new File(args[0]));
			
			ChallengeTracker tracker = new ChallengeTracker(config);
			tracker.start();			
		}
	}

}
