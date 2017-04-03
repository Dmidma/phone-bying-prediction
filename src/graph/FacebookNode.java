package graph;

import java.util.HashMap;

/**
 * This class is used to store previous and current smart phones of a certain node.
 * It will also contain the number of each available smart phone.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class FacebookNode {
	
	
	
	// the name of the previous smart phone
	private String previousPhone;
	
	// the name of the current smart phone
	private String currentPhone;
	
	// Map that will contain the phone's name of his friend
	private HashMap<Integer, String> currentFriendPhone;
	
	// A map that will contain the number of each phone between friends
	private HashMap<String, Integer> phoneTypes;
	
	
	
	// since we have only two smart phone to compare
	// I am going to store the probability of buying only one of them
	// the sum of the 2 probabilities = 1
	


	public FacebookNode() {
		this.currentPhone = "N/A";
		this.previousPhone = "N/A";
		this.currentFriendPhone = new HashMap<Integer, String>();
		this.phoneTypes = new HashMap<String, Integer>();
	}
	

	public FacebookNode(String currentPhone, String previousPhone) {
		this();
		this.currentPhone = currentPhone;
		this.previousPhone = previousPhone;
	}
	
	/**
	 * This method will associate the name of the current phone to its node friend.
	 * It will also increment the number of the given phone type.
	 * @param nodeId the id of friend node.
	 * @param phone the name of its current phone.
	 */
	public void addPhoneToFriend(int nodeId, String phone) {
		if (!currentFriendPhone.containsKey(nodeId)) {
			currentFriendPhone.put(nodeId, phone);
			
			// check if the current phone is mapped
			// if it's mapped, increment it's number
			if (!phoneTypes.containsKey(phone)) {
				phoneTypes.put(phone, 1);
			}
			else {
				phoneTypes.replace(phone, phoneTypes.get(phone) + 1);
			}
		}
	}
	
	/**
	 * This method will reset the the two maps of the current node.
	 */
	public void resetMapping() {
		this.currentFriendPhone = new HashMap<Integer, String>();
		this.phoneTypes = new HashMap<String, Integer>();
	}
	
	/**
	 * This method will return the number of friend owners of the given phone.
	 * @param phone the name of the phone.
	 * @return the number of its friend owners, 0 if none of them has this type.
	 */
	public int getNumberOfPhone(String phone) {
		
		if (!phoneTypes.containsKey(phone)) {
			return 0;
		}
		
		return phoneTypes.get(phone);
	}
	

	// Getters and Setters
	
	public String getPreviousPhone() {
		return new String(previousPhone);
	}
	
	public String getCurrentPhone() {
		return new String(currentPhone);
	}
	
	public void setPreviousPhone(String previousPhone) {
		this.previousPhone = previousPhone;
	}
	
	public void setCurrentPhone(String currentPhone) {
		this.currentPhone = currentPhone;
	}
	
	
}
