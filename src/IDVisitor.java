import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IDVisitor implements ComponentElementVisitor{
	
    Set<String> seenUserIDs = new HashSet<String>();
    Set<String> invalidUserIDs = new HashSet<String>();
    Set<String> seenUserGroupIDs = new HashSet<String>();
    Set<String> invalidUserGroupIDs = new HashSet<String>();
    
    
	@Override
	public ArrayList<Integer> visitComponent(UserGroupComponent component) {
		// TODO Auto-generated method stub
		return null;
	}
}
