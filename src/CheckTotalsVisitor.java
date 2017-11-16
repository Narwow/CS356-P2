

import java.util.ArrayList;

public class CheckTotalsVisitor implements ComponentElementVisitor{

	/**
	 * Returns an ArrayList<Integer> of the totals
	 * 0 is for group totals
	 * 1 is for user totals
	 * 2 is for message totals
	 * 3 is for positive message totals
	 * */ 
	public ArrayList<Integer> visitComponent(UserGroupComponent component){
		ArrayList<Integer> totals = new ArrayList<Integer>();
		
		int userTotal = countUserTotal(component);
		totals.add(0, userTotal);
		
		int groupTotal = countGroupTotal(component);
		totals.add(1, groupTotal);
		
		int tweetTotal = countTweetTotal(component);
		totals.add(2, tweetTotal);
		
		int positiveTotal = countPositiveTotal(component);
		totals.add(3, positiveTotal);

		return totals;
	}

	/**Counts all local and sub users*/
	private int countUserTotal(UserGroupComponent component){
		TwitterGroup group = null;
		
		//if user Object
		if(component.getClass().equals(new TwitterUser().getClass())){
			group = (TwitterGroup) component.getParent();
		}
		else{	//if group object
			group = (TwitterGroup) component;
		}
		return countAllSubUsers(group);
	}
	
	/**Counts all local and sub groups*/
	private int countGroupTotal(UserGroupComponent component){
		TwitterGroup group = null;
		
		//if user object
		if(component.getClass().equals(new TwitterUser().getClass())){
			//users cannot contain groups
			return 0;
		}
		else{	//if group object
			group = (TwitterGroup) component;
		}
		return countAllSubGroups(group);
	}
	
	/**Counts all local and sub messages (tweets)*/
	private int countTweetTotal(UserGroupComponent component){
		TwitterGroup group = null;
		
		//if user object
		if(component.getClass().equals(new TwitterUser().getClass())){
			//return the number of tweets the user has made
			TwitterUser user = (TwitterUser) component;
			return user.getMyTweets().size();
		}
		//if group object
		group = (TwitterGroup) component;
		return countAllSubTweets(group);
	}
	
	/**Counts all local and sub positive messages (tweets(*/
	private int countPositiveTotal(UserGroupComponent component) {
		int total = 0;
		if(component.getClass().equals(new TwitterUser().getClass())){
			//do loop for all users comments
			TwitterUser user = (TwitterUser) component;
			for(Tweet tweet: user.getMyTweets()){
				if(tweet.isPositive()){
					total+=1;
				}
			}
			return total;
		}
		//if group
		TwitterGroup group = (TwitterGroup) component;
		return countAllSubPositiveTweets(group);
	}
	
	

	/**Recursive call for counting all sub groups*/
	private int countAllSubGroups(TwitterGroup group){
		int total = 1;
		
		//for every sub group, get the group total
		for(Group subGroup: group.getGroups()){
			total += countAllSubGroups((TwitterGroup)subGroup);
		}
		return total;
	}
	
	/**Recursive call for counting all sub users*/
	private int countAllSubUsers(TwitterGroup group){
		int total = group.getUsers().size();	//counts local users
		
		//for every subGroup get the user total
		for(Group subGroup: group.getGroups()){
			total+=countAllSubUsers((TwitterGroup) subGroup);
		}
		return total;
	}	
	
	/**Recursive call for counting all sub messages (tweets)*/
	private int countAllSubTweets(TwitterGroup group) {
		int total = 0;
		
		for(User user: group.getUsers()){
			TwitterUser tUser = (TwitterUser)user;
			total+=tUser.getMyTweets().size();
		}
		
		for(Group subGroup: group.getGroups()){
			total+=countAllSubTweets((TwitterGroup) subGroup);
		}
		
		return total;
	}
	
	/**Recursive call for counting all sub positive messages (tweets)*/
	private int countAllSubPositiveTweets(TwitterGroup group){
		ArrayList<User> users = group.getUsers();
		int total = 0;
		
		for(User user: users){
			TwitterUser tUser = (TwitterUser)user;
			for(Tweet tweet: tUser.getMyTweets()){
				if(tweet.isPositive()){
					total+=1;
				}
			}
		}
		
		for(Group subGroup: group.getGroups()){
			total+=countAllSubPositiveTweets((TwitterGroup) subGroup);
		}
		
		return total;
	}
}
