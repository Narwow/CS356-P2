/*
 * Daniel Avetyan
 * CS 356 Assignment 2
 * Due November 8, 2016
 */

import java.util.ArrayList;

public interface ComponentElementVisitor {
	
	/**Observer pattern to count totals*/
	public ArrayList<Integer> visitComponent(UserGroupComponent component);
}
