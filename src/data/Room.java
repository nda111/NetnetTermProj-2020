package data;

public final class Room {

	public String uid1, uid2;
	
	// Two users can chat in the chat room
	public Room(String uid1, String uid2) {
		
		this.uid1 = uid1;
		this.uid2 = uid2;
	}
	
	public boolean isIn(String uid) {
		
		return uid.equals(uid1) || uid.equals(uid2);
	}
	
	public String opponentOf(String uid) {
		
		if (uid.equals(uid1)) {
			
			return uid2;
		} else if (uid.equals(uid2)) {
			
			return uid1;
		} else {
			
			return null;
		}
	}
}
