package data;

public enum ERequest {
	
	ECHO((byte)0); // ���� ��û
	// TODO: ���⿡ �ٸ� ��û �߰��ϱ� (�ĺ��ڴ� �����ؼ� ��Ģ�� ���սô�.)

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
