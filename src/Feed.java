

import java.util.ArrayList;

/**
 * Feed interface.
 * Allows for observer pattern to be implemented.
 */
public interface Feed {
	
	public void attach(User user);	//User==observer
	public void detach(User user);	//User==observer
	public void notifyObservers(Tweet tweet);	//notify all users
	public ArrayList<Tweet> getFeed();		//getState
	public void setFeed(ArrayList<Tweet> tweets);	//setState
}
