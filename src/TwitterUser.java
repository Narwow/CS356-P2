

import java.util.ArrayList;

public class TwitterUser implements User {
	
	private static ArrayList<User> allUsers = new ArrayList<User>();
	private String name;
	private ArrayList<Tweet> everyonesTweets;	//observer state
	private ArrayList<User> usersIFollow;
	private TwitterFeed myTweets;
	private UserGroupComponent parent;
	
	public TwitterUser(){
		name = null;
		everyonesTweets = null;
		myTweets = null;
	}
	
	/**Initializes a TwitterUser object, the parent group should be passes as the parent argument*/
	public TwitterUser(String name, UserGroupComponent parent) {
		this.name = name;
		this.parent = parent;
		everyonesTweets = new ArrayList<Tweet>();
		usersIFollow = new ArrayList<User>();
		myTweets = new TwitterFeed();
		allUsers.add(this);
	}
	
	/**
	 * A static method to return a User object with the same
	 * name as the argument name passed. Returns null if
	 * no such User object exists.
	 */
	public static User getUserById(String name){
		for(User user: allUsers){
			if(user.getName().equals(name)){
				return user;
			}
		}
		return null;
	}
	
	/**Returns an ArrayList<Tweet> of all tweets made by this user.*/
	public ArrayList<Tweet> getMyTweets(){
		return myTweets.getFeed();
	}
	
	/**Returns an ArrayList<Tweet> of all user tweets this user is observing.*/
	public ArrayList<Tweet> getFeed(){
		return everyonesTweets;
	}
	
	public void addToMyFollowList(User user){
		usersIFollow.add(user);
	}
	
	/**Returns an ArrayList<User> of all the users that this user object follows.*/
	public ArrayList<User> getUsersIFollow(){
		return usersIFollow;
	}
	
	/**Accepts a tweet to update, uses observer pattern*/
	@Override
	public void update(Tweet tweet) {
		everyonesTweets.add(tweet);
	}

	@Override
	public String getName() {
		return name;
	}

	/**Posts a tweet and notifies all observers*/
	public void postTweet(Tweet tweet){
		//to see own tweets
		everyonesTweets.add(tweet);
		
		//to post to everyone that follows
		myTweets.postTweet(tweet);
		myTweets.notifyObservers(tweet);
	}
	
	/**Adds a listener to this users tweets*/
	public void setListener(User user){
		myTweets.attach(user);
	}
	
	@Override
	public String toString(){
		return name;
	}

	/**Accepts a visitor type.*/
	@Override
	public ArrayList<Integer> accept(ComponentElementVisitor visitor) {
		return visitor.visitComponent(this);
	}

	/**Returns the parent group of this user object, cannot return null.*/
	@Override
	public UserGroupComponent getParent() {
		return parent;
	}
	
}
