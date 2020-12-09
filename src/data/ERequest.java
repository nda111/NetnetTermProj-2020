package data;

public enum ERequest {
	
	ECHO((byte)0); // 에코 요청
	// TODO: 여기에 다른 요청 추가하기 (식별자는 상의해서 규칙을 정합시다.)

	// 파라미터로 들어온 값에 해당하는 ERequest 객체를 반환한다.
	// 알 수 없는 요청이면 null을 반환한다.
	// ex) value == 0 -> return ECHO
	public static ERequest valueOf(byte value) {
		
		for (ERequest v : ERequest.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private byte value = -1; // 요청의 식별
	
	// 생성
	private ERequest(byte value) {
		
		this.value = value;
	}
	
	// 이 요청의 식별자를 가져온다.
	public byte getValue() {
		
		return value;
	}
}
