

import java.util.ArrayList;

public class TwitterGroup implements Group {
	
	private static ArrayList<Group> allGroups = new ArrayList<Group>();
	private ArrayList<UserGroupComponent> componentsInGroup = new ArrayList<UserGroupComponent>();
	private String groupName;
	private UserGroupComponent parent;
	
	public TwitterGroup(){
		groupName = null;
		parent = null;
	}
	
	/**initializes a TwitterGroup object, adds itself to the static list of allGroups*/
	public TwitterGroup(String groupName, UserGroupComponent parent) {
		this.groupName = groupName;
		this.parent = parent;
		allGroups.add(this);
	}
	
	/**Returns a list of all users in this group.*/
	@Override
	public ArrayList<User> getUsers() {
		TwitterUser tempUser = new TwitterUser();
		ArrayList<User> users = new ArrayList<User>();
		for(UserGroupComponent component: componentsInGroup){
			if(component.getClass().equals(tempUser.getClass())){
				users.add((User) component);
			}
		}
		return users;
	}
	
	/**
	 * Returns Group object with the same name as the argument
	 * return null if such a group does not exist.
	 * */
	public static Group getGroupById(String name){
		for(Group group: allGroups){
			if(group.getName().equals(name)){
				return group;
			}
		}
		return null;
	}

	/**Returns all sub groups in this group*/
	@Override
	public ArrayList<Group> getGroups() {
		//System.out.println("In getGroups()");
		ArrayList<Group> groups = new ArrayList<Group>();
		for(UserGroupComponent component: componentsInGroup){
			if(component.getClass().equals(new TwitterGroup().getClass())){
				groups.add((Group) component);
			}
		}
		return groups;
	}

	@Override
	public void add(UserGroupComponent component) {
		componentsInGroup.add(component);
	}
	
	@Override
	public String getName() {
		return groupName;
	}
	
	@Override
	public ArrayList<Integer> accept(ComponentElementVisitor visitor){
		return visitor.visitComponent(this);
	}

	/**Returns parent of this group, if this group is the root group then it will return null*/
	@Override
	public UserGroupComponent getParent() {
		return parent;
	}	
	
	@Override
	public String toString(){
		return groupName;
	}
}
