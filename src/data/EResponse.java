package data;

public enum EResponse {

	ECHO_OK(ERequest.ECHO, (byte)0); // 에코 응답 
	// TODO: 여기에 다른 응 추가하기 (식별자는 상의해서 규칙을 정합시다.)

	// 파라미터로 들어온 값에 해당하는 ERequest 객체를 반환한다.
	// 알 수 없는 요청이면 null을 반환한다.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(byte value) {
		
		for (EResponse v : EResponse.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private short value = -1; // 요청의 식별
	
	// 생성
	private EResponse(ERequest request, byte value) {
		
		this.value = (short) ((request.getValue() << 8) | value);
		
		// 0000 0000 | 0000 0000
		// 요청 ID   | Yes/No/...
	}
	
	// 이 요청의 식별자를 가져온다.
	public short getValue() {
		
		return value;
	}
	
	// 이 응답이 어떤 요청에 대한 응답인지 가져온다.
	public ERequest getRequest() {
		
		final byte requestId = (byte) (value >> 8);
		
		return ERequest.valueOf(requestId);
	}
}
