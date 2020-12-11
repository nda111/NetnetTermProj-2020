package data;

public enum EResponse {

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
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1);	// ���� 

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
