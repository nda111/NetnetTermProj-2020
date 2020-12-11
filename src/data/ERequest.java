package data;

public enum ERequest {
	
	ECHO((byte)0), // ���� ��û
	QUIT((byte)1), // ���� ����  
	VALIDATE_UID((byte)2), // UID �ߺ� �˻�
	SIGNUP((byte)3), // ȸ������ ��û 
	SIGNIN((byte)4), // �α��� ��û 
	SIGNOUT((byte)5), // �α׾ƿ� ��û
	ASK_UID((byte)6), // ���̵� ã�� 
	ASK_FRIEND((byte)7); // ģ�� ��� ��û 

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
