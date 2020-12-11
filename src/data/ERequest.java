package data;

public enum ERequest {
	
	ECHO((byte)0), // 에코 요청
	QUIT((byte)1), // 종료 선언  
	VALIDATE_UID((byte)2), // UID 중복 검사
	SIGNUP((byte)3), // 회원가입 요청 
	SIGNIN((byte)4), // 로그인 요청 
	SIGNOUT((byte)5), // 로그아웃 요청
	ASK_UID((byte)6), // 아이디 찾기 
	ASK_FRIEND((byte)7); // 친구 목록 요청 

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
