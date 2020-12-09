package data;

public enum EResponse {

	ECHO_OK(ERequest.ECHO, (byte)0); // ���� ���� 
	// TODO: ���⿡ �ٸ� �� �߰��ϱ� (�ĺ��ڴ� �����ؼ� ��Ģ�� ���սô�.)

	// �Ķ���ͷ� ���� ���� �ش��ϴ� ERequest ��ü�� ��ȯ�Ѵ�.
	// �� �� ���� ��û�̸� null�� ��ȯ�Ѵ�.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(byte value) {
		
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
}
