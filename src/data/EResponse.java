package data;

public enum EResponse {

	ANNOUNCE_ADD_FRIEND(ERequest.ANNOUNCE, (byte)0), // Add friend
	ANNOUNCE_FRIEND_IN(ERequest.ANNOUNCE, (byte)1),  // Friend connect
	ANNOUNCE_FRIEND_OUT(ERequest.ANNOUNCE, (byte)2), // Friend disconnect
	
	ANNOUNCE_ASK_CHAT(ERequest.ANNOUNCE, (byte)3), 	// Request friend to chat 
	ANNOUNCE_ACK_CHAT(ERequest.ANNOUNCE, (byte)4), 	// 占쏙옙占쎌넅 占쎈뻿筌ｏ옙 野껉퀗�궢 占쎌읈占쎈뼎 
	ANNOUNCE_SAY_CHAT(ERequest.ANNOUNCE, (byte)5), 	// 占쏙옙占쎌넅 占쎄땀占쎌뒠 占쎌읈占쎈뼎 
	ANNOUNCE_CALC_CHAT(ERequest.ANNOUNCE, (byte)6), // �④쑴沅� 野껉퀗�궢 占쎌읈占쎈뼎 
	ANNOUNCE_END_CHAT(ERequest.ANNOUNCE, (byte)7),	// 占쏙옙占쎌넅 �넫�굝利� 占쎌읈占쎈뼎 

	ECHO_OK(ERequest.ECHO, (byte)0), // Echo response
	
	QUIT_OK(ERequest.QUIT, (byte)0), // Acceptance of termination
	
	VALIDATE_UID_OK(ERequest.VALIDATE_UID, (byte)0), // UID usage enabled
	VALIDATE_UID_NO(ERequest.VALIDATE_UID, (byte)1), // UID usage disabled
	
	SIGNUP_OK(ERequest.SIGNUP, (byte)0),	// Success sign-up
	SIGNUP_ERR(ERequest.SIGNUP, (byte)1), 	// Fail sign-up
	
	SIGNIN_OK(ERequest.SIGNIN, (byte)0),			// Success log-in
	SIGNIN_ERR_PW(ERequest.SIGNIN, (byte)1),		// Enter wrong password
	SIGNIN_ERR_MULTI(ERequest.SIGNIN, (byte)2),		// Log-in on multiple devices
	SIGNIN_ERR_NO_UID(ERequest.SIGNIN, (byte)3),	// Non-existent account
	
	SIGNOUT_OK(ERequest.SIGNOUT, (byte)0),			// Success log-out
	SIGNOUT_ERR_NOT_IN(ERequest.SIGNOUT, (byte)1), 	// Fail log-out (never logged in)
	SIGNOUT_ERR(ERequest.SIGNOUT, (byte)2),			// Fail log-out (another reason)

	ASK_UID_OK(ERequest.ASK_UID, (byte)0),	// UID Existence
	ASK_UID_NO(ERequest.ASK_UID, (byte)1),	// UID None
	
	ASK_FRIEND_OK(ERequest.ASK_FRIEND, (byte)0),	// Friends existence
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1),	// No friend
	
	ADD_FRIEND_OK(ERequest.ADD_FRIEND, (byte)0),			// Successfully add friends
	ADD_FRIEND_ERR_UID(ERequest.ADD_FRIEND, (byte)1),		// Failed(non-exist user)
	ADD_FRIEND_ERR_ALREADY(ERequest.ADD_FRIEND, (byte)2),	// Failed(Already friend)
	ADD_FRIEND_ERR_YOU(ERequest.ADD_FRIEND, (byte)3),		// Failed(add yourself as a friend)
	ADD_FRIEND_ERR(ERequest.ADD_FRIEND, (byte)4),			// Fail

	ASK_CHAT_OK(ERequest.ASK_CHAT, (byte)0), 		// 占쎈�뗰옙�� �눧�눘堉김퉪�눊苡� 
	ASK_CHAT_OFFLINE(ERequest.ASK_CHAT, (byte)1), 	// 椰꾬옙 占쎌궎占쎈늄占쎌뵬占쎌뵥占쎌뵠占쎌뵬 占쎈툧 占쎈쭡 
	ASK_CHAT_ERR(ERequest.ASK_CHAT, (byte)2), 		// 占쎈퓠占쎌쑎 占쎄텢占쎌몵占쎈빍繹먲옙 占쎈뼄占쎈뻻 占쎈퉸�걡占� 

	ACK_CHAT_OK(ERequest.ACK_CHAT, (byte)0),		// 占쎈�뗰옙�� 占쎌읈占쎈뼎占쎈막野껓옙 
	ACK_CHAT_LATE(ERequest.ACK_CHAT, (byte)1),		// 占쎈뮡占쎈�占쎈선 
	
	SAY_CHAT_OK(ERequest.SAY_CHAT, (byte)0),		// 筌띾�곕릭疫뀐옙 占쎈�뗰옙�� 
	SAY_CHAT_NO_ROOM(ERequest.SAY_CHAT, (byte)1), 	// 獄쏉옙 占쎈씨占쎈선筌욑옙 
	SAY_CHAT_ERR(ERequest.SAY_CHAT, (byte)2),		// 占쎈퓠占쎌쑎 

	END_CHAT_OK(ERequest.END_CHAT, (byte)0),	// 占쎈�뗰옙�� 
	END_CHAT_ERR(ERequest.END_CHAT, (byte)1),	// 占쎈퓠占쎌쑎 
	
	WHO_AM_I_OK(ERequest.WHOAMI, (byte)0), // Success to get User name
	WHO_AM_I_NO(ERequest.WHOAMI, (byte)1); // Fail to get User name

	// Returns the ERequest object corresponding to the value entered into the parameter
	// Returns null if an unknown request is made
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(short value) {
		
		for (EResponse v : EResponse.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private short value = -1; // Identification of request
	
	// Generating
	private EResponse(ERequest request, byte value) {
		
		this.value = (short) ((request.getValue() << 8) | value);
		
		// 0000 0000 | 0000 0000
		// Requeat ID   | Yes/No/...
	}
	
	// Get the identifier of thie request
	public short getValue() {
		
		return value;
	}
	
	// Gets which response this response is to which request
	public ERequest getRequest() {
		
		final byte requestId = (byte) (value >> 8);
		
		return ERequest.valueOf(requestId);
	}
	
	// Get what kind of response to the request
	public byte getResponse() {
		
		return (byte) (value & 0xFFFFFF);
	}
}
