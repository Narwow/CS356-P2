

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User View java swing window
 */
public class UserView {
	
	private TwitterUser user;

	private static HashMap<String, UserView> userViewsOpen = new HashMap<String, UserView>();
	private JFrame frame;
	private JTextField txtUserId;
	private JTextField txtTweetMessage;
	private JButton btnPostTweet;
	private JButton btnAddUser;
	private JList<TwitterUser> usersFollowedList;
	private JList<Tweet> feedList;
	private int xFrame=0;
	private int yFrame=0;

	/**
	 * Launch the application. 
	 */
	public static void newScreen(TwitterUser user, double x, double y) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserView window = new UserView(user, x, y);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * x and y are used for the position of the frame.
	 */
	public UserView(TwitterUser user, double x, double y) {
		xFrame = (int)x;
		yFrame = (int)y;
		this.user = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Add this object to the list of open views
		userViewsOpen.put(user.getName(), this);	

		frame = new JFrame();
		frame.setTitle(user.getName());
		frame.setBounds(xFrame, yFrame, 500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Remove users from the open window list when manually closed.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event){
				userViewsOpen.remove(user.getName());
			}
		});
		
		//TEXT FIELD USER ID
		txtUserId = new JTextField();
		txtUserId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtUserId.setBounds(15, 16, 197, 46);
		frame.getContentPane().add(txtUserId);
		txtUserId.setColumns(10);
		
		
		//BUTTON FOLLOW USER
		btnAddUser = new JButton("Follow User");
		btnAddUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean redraw = true;
				String userIdString = txtUserId.getText();
				if(userIdString!=null && (userIdString.length()>0)){	//text field valid
					TwitterUser followThisUser = (TwitterUser) TwitterUser.getUserById(userIdString);
					if(followThisUser!=null){	//if user exists
						followThisUser.setListener(user);	//follow user
						user.addToMyFollowList(followThisUser);
					}else{
						JOptionPane.showMessageDialog(null, "Cannot add user that does not exist.");
						redraw = false;
					}
				}else{
					JOptionPane.showMessageDialog(null, "User ID field invalid.");
					redraw = false;
				}
				if(redraw){
					reDraw();
				}
			}
		});
		btnAddUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnAddUser.setBounds(266, 15, 197, 46);
		frame.getContentPane().add(btnAddUser);
		
		
		//LIST OF USERS FOLLOWED
		DefaultListModel<TwitterUser> usersFollowedListModel = new DefaultListModel<TwitterUser>();
		ArrayList<User> followedUsers = user.getUsersIFollow();
		for(User user: followedUsers){
			usersFollowedListModel.addElement((TwitterUser) user);
		}
		usersFollowedList = new JList<TwitterUser>(usersFollowedListModel);
		usersFollowedList.setBounds(15, 91, 448, 126);
		frame.getContentPane().add(usersFollowedList);
		
		
		//LIST OF FEED TWEETS
		DefaultListModel<Tweet> feedListModel = new DefaultListModel<Tweet>();
		ArrayList<Tweet> followedTweets = user.getFeed();
		for(Tweet tweet: followedTweets){
			feedListModel.addElement(tweet);
		}
		feedList = new JList<Tweet>(feedListModel);
		feedList.setBounds(15, 302, 448, 126);
		frame.getContentPane().add(feedList);
		
		
		//TWEET TEXT BOX
		txtTweetMessage = new JTextField();
		txtTweetMessage.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtTweetMessage.setBounds(25, 233, 187, 46);
		frame.getContentPane().add(txtTweetMessage);
		txtTweetMessage.setColumns(10);
		
		
		//POST TWEET BUTTON
		btnPostTweet = new JButton("Post Tweet");
		btnPostTweet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String tweetMessage = txtTweetMessage.getText();
				
				//if a valid tweet
				if(tweetMessage!=null && tweetMessage.length()>0){
					tweetMessage = "@"+user.getName()+" "+tweetMessage;
					user.postTweet(new Tweet(tweetMessage));
				}
				txtTweetMessage.setText("");
				
				for(String key: userViewsOpen.keySet()){
					UserView view = userViewsOpen.get(key);
					view.reDraw();
				}
			}
		});
		btnPostTweet.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPostTweet.setBounds(266, 232, 197, 46);
		frame.getContentPane().add(btnPostTweet);
	}
	
	/**Improper but effective way of doing a screen refresh*/
	@SuppressWarnings("deprecation")
	private void reDraw(){
		frame.dispose();
		Point p = frame.location();
		newScreen(user, p.getX(), p.getY());
	}
}
