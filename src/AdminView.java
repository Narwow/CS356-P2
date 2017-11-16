

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTree;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AdminView {
	
	private static AdminView INSTANCE = null;
	
	private TwitterGroup rootGroup;
	
	private JTree tree;
	private JButton btnAddUser;
	private JButton btnAddGroup;
	private JButton btnOpenUserView;
	private JButton btnMessageTotal;
	private JButton btnUserTotal;
	private JButton btnGroupTotal;
	private JButton btnPositive;
	private JEditorPane dtrpnUserId;
	private JEditorPane dtrpnGroupId; 
	private DefaultMutableTreeNode top;
	private DefaultMutableTreeNode selectedElement;
	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public void newScreen(TwitterGroup rootGroup) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminView window = new AdminView(rootGroup);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Singleton pattern
	public static AdminView getInstance(TwitterGroup rootGroup){
		if(INSTANCE==null){
			INSTANCE = new AdminView(rootGroup);
		}
		return INSTANCE;
	}

	/**
	 * Create the application.
	 */
	private AdminView(TwitterGroup rootGroup) {
		this.rootGroup = rootGroup;
		frame = new JFrame();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame.setTitle("Admin Console");
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//ADD USER BUTTON
		btnAddUser = new JButton("Add User");
		btnAddUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName = dtrpnUserId.getText();
				
				//if a valid user name
				if(userName!=null && !userName.equals("")){
					
					//if user already exists return, do nothing
					if(TwitterUser.getUserById(userName)!=null){
						return;
					}
					
					selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					
					//only add users to groups, cannot add users to users
					TwitterGroup selectedGroup;
					if(selectedElement==null){
						selectedGroup = rootGroup;
					}//else if selected object is a user, go to parent group
					else if(selectedElement.getUserObject().getClass().equals(new TwitterUser().getClass())){
						selectedElement = (DefaultMutableTreeNode) selectedElement.getParent();
						//if parent group is null
						if(selectedElement==null){
							selectedGroup = rootGroup;
						}
						else{
							selectedGroup = (TwitterGroup) selectedElement.getUserObject();
						}
					}
					else{
						selectedGroup = (TwitterGroup) selectedElement.getUserObject();
					}

					//add user to tree and redraw
					selectedGroup.add(new TwitterUser(userName, selectedGroup));
					reDraw();
				}
			}
		});
		btnAddUser.setBounds(556, 19, 157, 29);
		frame.getContentPane().add(btnAddUser);
		
		//ADD GROUP
		btnAddGroup = new JButton("Add Group");
		btnAddGroup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String groupName = dtrpnGroupId.getText();
				//if a valid group name
				if(groupName!=null && !groupName.equals("")){
					
					//if group already exits return, do nothing
					if(TwitterGroup.getGroupById(groupName)!=null){
						return;
					}
					
					//select parent group to add to
					selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					TwitterGroup selectedGroup;
					if(selectedElement==null){
						selectedGroup = rootGroup;
					}//else if selected object is a user, go to parent group
					else if(selectedElement.getUserObject().getClass().equals(new TwitterUser().getClass())){
						selectedElement = (DefaultMutableTreeNode) selectedElement.getParent();
						if(selectedElement==null){
							selectedGroup = rootGroup;
						}
						else{
							selectedGroup = (TwitterGroup) selectedElement.getUserObject();
						}
					}
					else{
						selectedGroup = (TwitterGroup) selectedElement.getUserObject();
					}
					
					//add group to tree and redraw
					selectedGroup.add(new TwitterGroup(groupName, selectedGroup));
					reDraw();
				}
			}
		});
		btnAddGroup.setBounds(556, 58, 157, 29);
		frame.getContentPane().add(btnAddGroup);
		
		
		//TEXT BOX USER ID
		dtrpnUserId = new JEditorPane();
		dtrpnUserId.setBounds(350, 19, 157, 26);
		frame.getContentPane().add(dtrpnUserId);
		
		
		//TEXT BOX GROUP ID
		dtrpnGroupId = new JEditorPane();
		//dtrpnGroupId.setText("Group ID");
		dtrpnGroupId.setBounds(350, 58, 157, 26);
		frame.getContentPane().add(dtrpnGroupId);
		
		
		//BUTTON OPEN USER VIEW
		btnOpenUserView = new JButton("Open User View");
		btnOpenUserView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedElement==null){
					return;
				}
				//if not a user object, return
				if(!selectedElement.getUserObject().getClass().equals(new TwitterUser().getClass())){
					return;
				}
				
				TwitterUser selectedUser = (TwitterUser) selectedElement.getUserObject();
				@SuppressWarnings("deprecation")
				Point p = frame.location();
				
				//create a user view window 
				UserView.newScreen(selectedUser, p.getX(), p.getY());
			}
		});
		btnOpenUserView.setBounds(350, 103, 363, 29);
		frame.getContentPane().add(btnOpenUserView);
		
		
		//BUTTON SHOW MESSAGE TOTAL
		btnMessageTotal = new JButton("Show Message Total");
		btnMessageTotal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedElement==null){
					return;
				}
				UserGroupComponent component = (UserGroupComponent) selectedElement.getUserObject();
				ArrayList<Integer> totals = component.accept(new CheckTotalsVisitor());
				int tweetTotal = totals.get(2);	//2 is for tweet totals
				
				JOptionPane.showMessageDialog(null, String.valueOf(tweetTotal));
			}
		});
		btnMessageTotal.setBounds(350, 383, 177, 29);
		frame.getContentPane().add(btnMessageTotal);
		
		
		//BUTTON SHOW USER TOTAL
		btnUserTotal = new JButton("Show User Total");
		btnUserTotal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//accept returns 
				selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedElement==null){
					return;
				}
				UserGroupComponent component = (UserGroupComponent) selectedElement.getUserObject();
				ArrayList<Integer> totals = component.accept(new CheckTotalsVisitor());
				int userTotal = totals.get(0);	//0 is for user totals
				
				JOptionPane.showMessageDialog(null, String.valueOf(userTotal));
			}
		});
		btnUserTotal.setBounds(350, 312, 177, 29);
		frame.getContentPane().add(btnUserTotal);
		
		
		//BUTTON SHOW GROUP TOTAL
		btnGroupTotal = new JButton("Show Group Total");
		btnGroupTotal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedElement==null){
					return;
				}
				UserGroupComponent component = (UserGroupComponent) selectedElement.getUserObject();
				ArrayList<Integer> totals = component.accept(new CheckTotalsVisitor());
				int groupTotal = totals.get(1);	//1 is for group total 
				
				JOptionPane.showMessageDialog(null, String.valueOf(groupTotal));
			}
		});
		btnGroupTotal.setBounds(542, 312, 171, 29);
		frame.getContentPane().add(btnGroupTotal);
		
		
		//BUTTON SHOW POSITIVE %
		btnPositive = new JButton("Show Positive %");
		btnPositive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectedElement = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedElement==null){
					return;
				}
				UserGroupComponent component = (UserGroupComponent) selectedElement.getUserObject();
				ArrayList<Integer> totals = component.accept(new CheckTotalsVisitor());
				int positiveTotal = totals.get(3);	//3 is for positive tweet total 
				int tweetTotal = totals.get(2);
				double percentPositive = 0.0;
				if(tweetTotal==0){
					percentPositive=0.0;
				}
				else{
					percentPositive = 100.0 * ((double)positiveTotal / (double)tweetTotal);
				}
				
				JOptionPane.showMessageDialog(null, String.valueOf(positiveTotal)+"/"+String.valueOf(tweetTotal) 
														+ " = " + String.format("%.2f",percentPositive) + "%");
			}
		});
		btnPositive.setBounds(542, 383, 171, 29);
		frame.getContentPane().add(btnPositive);
		
		
		//TREE USER GROUP
		top = new DefaultMutableTreeNode(rootGroup);
		createNodes(top, rootGroup);
		tree = new JTree(top);
		tree.setBounds(0, 0, 335, 428);
		frame.getContentPane().add(tree);
	}
	
	
	/**Recursively creates all the nodes for the JTree*/
	private void createNodes(DefaultMutableTreeNode top, TwitterGroup localRootGroup){
		DefaultMutableTreeNode group = null;
		DefaultMutableTreeNode user = null;
		
		//iterate through rootGroup if it contains a group recursively call this method
		ArrayList<Group> topGroups = null;
		if(localRootGroup==null){
			System.out.println("Error");
		}
		
		//recursively call createNodes() on any sub groups
		topGroups = localRootGroup.getGroups();
		for(Group localGroup: topGroups){
			group = new DefaultMutableTreeNode(localGroup);
			//System.out.println("Adding group = "+localGroup.getName());
			top.add(group);
			createNodes(group, (TwitterGroup) localGroup);
		}
		
		//for students just add it to the current top node
		ArrayList<User> topUsers = localRootGroup.getUsers();
		for(User localUser: topUsers){
			user = new DefaultMutableTreeNode(localUser);
			top.add(user);
		}
	}
	
	/**Improper but effective way of doing a screen refresh*/
	private void reDraw(){
		frame.dispose();
		newScreen(rootGroup);
	}
}
