package data;

public enum ERequest {
	
	ANNOUNCE((byte)0), // ����->Ŭ���̾�Ʈ (������ ������ �� ����)
	ECHO((byte)1), // ���� ��û
	QUIT((byte)2), // ���� ����  
	VALIDATE_UID((byte)3), // UID �ߺ� �˻�
	SIGNUP((byte)4), // ȸ������ ��û 
	SIGNIN((byte)5), // �α��� ��û 
	SIGNOUT((byte)6), // �α׾ƿ� ��û
	ASK_UID((byte)7), // ���̵� ã�� 
	ASK_FRIEND((byte)8), // ģ�� ��� ��û 
	ADD_FRIEND((byte)9), // ģ�� �߰� 
	ASK_CHAT((byte)10), // ��ȭ ��û 
	ACK_CHAT((byte)11), // ��ȭ ����/����
	SAY_CHAT((byte)12), // ��ȭ ��ȭ 
	END_CHAT((byte)13); // ��ȭ ���� 

	// �Ķ���ͷ� ���� ���� �ش��ϴ� ERequest ��ü�� ��ȯ�Ѵ�.
	// �� �� ���� ��û�̸� null�� ��ȯ�Ѵ�.
	// ex) value == 0 -> return ECHO
	public static ERequest valueOf(byte value) {
		
		for (ERequest v : ERequest.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private byte value = -1; // ��û�� �ĺ�
	
	// ����
	private ERequest(byte value) {
		
		this.value = value;
	}
	
	// �� ��û�� �ĺ��ڸ� �����´�.
	public byte getValue() {
		
		return value;
	}
}
