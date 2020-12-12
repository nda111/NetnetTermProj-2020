package data;

public enum EResponse {

	ANNOUNCE_ADD_FRIEND(ERequest.ANNOUNCE, (byte)0), // ģ�� �߰� 
	ANNOUNCE_FRIEND_IN(ERequest.ANNOUNCE, (byte)1),  // ģ�� ���� 
	ANNOUNCE_FRIEND_OUT(ERequest.ANNOUNCE, (byte)2), // ģ�� ���� ���� 
	
	ANNOUNCE_ASK_CHAT(ERequest.ANNOUNCE, (byte)3), // ��ȭ ��û ���� 
	ANNOUNCE_ACK_CHAT(ERequest.ANNOUNCE, (byte)4), // ��ȭ ��û ��� ���� 
	ANNOUNCE_SAY_CHAT(ERequest.ANNOUNCE, (byte)5), // ��ȭ ���� ���� 
	ANNOUNCE_END_CHAT(ERequest.ANNOUNCE, (byte)6), // ��ȭ ���� ���� 

	ECHO_OK(ERequest.ECHO, (byte)0), // ���� ����
	
	QUIT_OK(ERequest.QUIT, (byte)0), // ���� ���� 
	
	VALIDATE_UID_OK(ERequest.VALIDATE_UID, (byte)0), // UID ��� ���� 
	VALIDATE_UID_NO(ERequest.VALIDATE_UID, (byte)1), // UID ��� �Ұ���
	
	SIGNUP_OK(ERequest.SIGNUP, (byte)0),	// ���� ���� 
	SIGNUP_ERR(ERequest.SIGNUP, (byte)1), 	// ���� ���� 
	
	SIGNIN_OK(ERequest.SIGNIN, (byte)0),			// �α��� ���� 
	SIGNIN_ERR_PW(ERequest.SIGNIN, (byte)1),		// �α��� ��й�ȣ Ʋ
	SIGNIN_ERR_MULTI(ERequest.SIGNIN, (byte)2),		// �α��� ���� ��� 
	SIGNIN_ERR_NO_UID(ERequest.SIGNIN, (byte)3),	// �α��� ���� ����
	
	SIGNOUT_OK(ERequest.SIGNOUT, (byte)0),			// �α׾ƿ� ���� 
	SIGNOUT_ERR_NOT_IN(ERequest.SIGNOUT, (byte)1), 	// �α׾ƿ� ���� (�α��� �� �� ����)
	SIGNOUT_ERR(ERequest.SIGNOUT, (byte)2),			// �α׾ƿ� ���� (�� �� ����)

	ASK_UID_OK(ERequest.ASK_UID, (byte)0),	// ����
	ASK_UID_NO(ERequest.ASK_UID, (byte)1),	// ���� 
	
	ASK_FRIEND_OK(ERequest.ASK_FRIEND, (byte)0),	// ���� 
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1),	// ���� 
	
	ADD_FRIEND_OK(ERequest.ADD_FRIEND, (byte)0),			// ���� 
	ADD_FRIEND_ERR_UID(ERequest.ADD_FRIEND, (byte)1),		// �׷� �� ���� 
	ADD_FRIEND_ERR_ALREADY(ERequest.ADD_FRIEND, (byte)2),	// �̹� �߰��� 
	ADD_FRIEND_ERR_YOU(ERequest.ADD_FRIEND, (byte)3),		// ���� ģ�� �߰� 
	ADD_FRIEND_ERR(ERequest.ADD_FRIEND, (byte)4),			// ���� 

	ASK_CHAT_OK(ERequest.ASK_CHAT, (byte)0), 		// ���� ����� 
	ASK_CHAT_OFFLINE(ERequest.ASK_CHAT, (byte)1), 	// �� ���������̶� �� �� 
	ASK_CHAT_ERR(ERequest.ASK_CHAT, (byte)2), 		// ���� �����ϱ� �ٽ� �غ� 

	ACK_CHAT_OK(ERequest.ASK_CHAT, (byte)0),		// ���� �����Ұ� 
	ACK_CHAT_LATE(ERequest.ASK_CHAT, (byte)1),		// �ʾ��� 
	
	SAY_CHAT_OK(ERequest.SAY_CHAT, (byte)0),		// ���ϱ� ���� 
	SAY_CHAT_NO_ROOM(ERequest.SAY_CHAT, (byte)1), 	// �� ������ 
	SAY_CHAT_ERR(ERequest.SAY_CHAT, (byte)2),		// ���� 

	END_CHAT_OK(ERequest.END_CHAT, (byte)0),	// ���� 
	END_CHAT_ERR(ERequest.END_CHAT, (byte)1);	// ���� 

	// �Ķ���ͷ� ���� ���� �ش��ϴ� ERequest ��ü�� ��ȯ�Ѵ�.
	// �� �� ���� ��û�̸� null�� ��ȯ�Ѵ�.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(short value) {
		
		for (EResponse v : EResponse.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private short value = -1; // ��û�� �ĺ�
	
	// ����
	private EResponse(ERequest request, byte value) {
		
		this.value = (short) ((request.getValue() << 8) | value);
		
		// 0000 0000 | 0000 0000
		// ��û ID   | Yes/No/...
	}
	
	// �� ��û�� �ĺ��ڸ� �����´�.
	public short getValue() {
		
		return value;
	}
	
	// �� ������ � ��û�� ���� �������� �����´�.
	public ERequest getRequest() {
		
		final byte requestId = (byte) (value >> 8);
		
		return ERequest.valueOf(requestId);
	}
	
	// ��û�� ���� � �������� �����´�.
	public byte getResponse() {
		
		return (byte) (value & 0xFFFFFF);
	}
}
