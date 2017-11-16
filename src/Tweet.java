

import java.util.ArrayList;

public class Tweet {
	
	private String tweet;
	private static ArrayList<String> positiveWords = null;
	private static ArrayList<String> negativeWords = null;
	
	/**User Tweet(String) instead*/
	public Tweet(){
		tweet = null;
		positiveWords = null;
		negativeWords = null;
	}
	
	/**Initializes the static lists for positive and negative words if null.*/
	public Tweet(String tweet){
		this.tweet = tweet;
		
		if(positiveWords==null){
			positiveWords = new ArrayList<String>();
			setPositiveWords();
		}
		
		if(negativeWords==null){
			negativeWords = new ArrayList<String>();
			setNegativeWords();
		}
	}

	/**Returns a String of the tweet*/
	public String getTweet(){
		return tweet;
	}
	
	/**Returns a String value of tweet, this is for java swing list requirement.*/
	public String toString(){
		return tweet;
	}

	/**Returns true if the tweet is positive, returns false otherwise*/
	public boolean isPositive() {
		int positiveTotal = 0;
		String[] words = tweet.split(" ");
		for(int i=0; i<words.length; i++){
			//if it contains any negative words return false
			if(negativeWords.contains(words[i].toLowerCase())){
				return false;
			}
			if(positiveWords.contains(words[i].toLowerCase())){
				positiveTotal+=1;
			}
		}
		System.out.println("\n");
		//if and only if it contains at least 1 positive word, then return true
		if(positiveTotal>0){
			return true;
		}
		return false;
	}
	
	/**Initializes some words for positive messages (tweets)*/
	private void setPositiveWords() {
		positiveWords.add("good");
		positiveWords.add("thank");
		positiveWords.add("excellent");
		positiveWords.add("like");
		positiveWords.add("great");
		positiveWords.add("thanks");
		positiveWords.add("give");
		positiveWords.add("happy");
		positiveWords.add("love");
		positiveWords.add("welcome");
		positiveWords.add("agree");
	}	
	
	/**Initializes some words for negative messages (tweets)*/
	private void setNegativeWords() {
		negativeWords.add("bad");
		negativeWords.add("sad");
		negativeWords.add("hate");
		negativeWords.add("horrible");
		negativeWords.add("sad");
	}
}
