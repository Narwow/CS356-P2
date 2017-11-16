

import java.util.ArrayList;

/**
 * UserGroupComponent Interface
 * A component design interface.
 * To be used with {@link Group} and {@link User} interfaces.
 */
public interface UserGroupComponent {
	public String getName();
	public ArrayList<Integer> accept(ComponentElementVisitor visitor);
	public UserGroupComponent getParent();
	public String toString();
}
