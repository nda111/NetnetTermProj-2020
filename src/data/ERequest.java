package data;

public enum ERequest {
	
	ANNOUNCE((byte)0), // server->client(server does not receive anything
	ECHO((byte)1), // Request echo
	QUIT((byte)2), // Announce termination 
	VALIDATE_UID((byte)3), // UID duplicate check
	SIGNUP((byte)4), // Request sign-up
	SIGNIN((byte)5), // Request sign-in
	SIGNOUT((byte)6), // Request sign-out
	ASK_UID((byte)7), // Find ID
	ASK_FRIEND((byte)8), // Request friend list 
	ADD_FRIEND((byte)9), // Add friend
	ASK_CHAT((byte)10), // Request chat
	ACK_CHAT((byte)11), // Accept/reject conversation
	SAY_CHAT((byte)12), // Conversation ignition
	END_CHAT((byte)13), // End conversation
	WHOAMI((byte)14), // Internal information
	EDIT_INF((byte)15); // Edit sentence

	// Returns the ERequest object corresponding to the value entered into the parameter
	// Entered into the parameter
	// EX) value == 0 -> return ECHO
	public static ERequest valueOf(byte value) {
		
		for (ERequest v : ERequest.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private byte value = -1; // Identification of request
	
	// Generating
	private ERequest(byte value) {
		
		this.value = value;
	}
	
	// Gets the identifier od this request
	public byte getValue() {
		
		return value;
	}
}
