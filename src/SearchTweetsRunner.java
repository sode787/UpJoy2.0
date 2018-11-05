//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.IOException;
import java.net.URISyntaxException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchTweetsRunner {
    SearchTweets user;

    public SearchTweetsRunner(String user, int limit, String fileLocation) throws TwitterException, URISyntaxException, IOException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("set your personal key here").setOAuthConsumerSecret("set your personal consumer key here").setOAuthAccessToken("set your personal token here").setOAuthAccessTokenSecret("set your personal token here");
        TwitterFactory tf = new TwitterFactory(configurationBuilder.build());
        Twitter twitter = tf.getInstance();
        this.user = new SearchTweets(twitter, user, limit, true, fileLocation);
        this.user.searchUserAccount();
    }

    public String getTweets() {
        return this.user.getTweet();
    }
}
