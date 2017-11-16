

public class MainDrive {
	public static void main(String[] args) {
		
		//Create a root group to pass into admin view to initialize JTree
		TwitterGroup rootGroup = new TwitterGroup("Root", null);
		
		//get an instance of AdminView
		AdminView admin = AdminView.getInstance(rootGroup);
		
		//Initialize some Groups and Users to start off with
		rootGroup.add(new TwitterUser("Alice", rootGroup));
		rootGroup.add(new TwitterGroup("CS356", rootGroup));
		TwitterGroup.getGroupById("CS356").add(new TwitterUser("Bob", TwitterGroup.getGroupById("CS356")));
		TwitterGroup.getGroupById("CS356").add(new TwitterUser("Carl", TwitterGroup.getGroupById("CS356")));
		TwitterGroup.getGroupById("CS356").add(new TwitterUser("Joe", TwitterGroup.getGroupById("CS356")));
		TwitterGroup.getGroupById("CS356").add(new TwitterUser("Alex", TwitterGroup.getGroupById("CS356")));
		
		//creates an Admin window
		admin.newScreen(rootGroup);
	}
}
