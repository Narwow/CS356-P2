

import java.util.ArrayList;

/**
 * Implements Feed
 * For use with TwitterUser objects.
 */
public class TwitterFeed implements Feed{
	
	private ArrayList<User> observers = null;
	private ArrayList<Tweet> feed = null;
	
	public TwitterFeed() {
		observers = new ArrayList<User>();
		feed = new ArrayList<Tweet>();
	}
	
	@Override
	public void attach(User user) {
		observers.add(user);
	}

	@Override
	public void detach(User user) {
		if(observers.contains(user)){
			observers.remove(user);
		}
	}

	@Override
	public void notifyObservers(Tweet tweet) {
		for(User user: observers){
			user.update(tweet);
		}
	}

	@Override
	public ArrayList<Tweet> getFeed() {
		return feed;
	}

	@Override
	public void setFeed(ArrayList<Tweet> tweets) {
		feed.addAll(tweets);
	}
	
	public void postTweet(Tweet tweet){
		feed.add(tweet);
	}

}
