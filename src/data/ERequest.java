package data;

public enum ERequest {
	
	ANNOUNCE((byte)0), // 서버->클라이언트 (서버가 수신할 일 없음)
	ECHO((byte)1), // 에코 요청
	QUIT((byte)2), // 종료 선언  
	VALIDATE_UID((byte)3), // UID 중복 검사
	SIGNUP((byte)4), // 회원가입 요청 
	SIGNIN((byte)5), // 로그인 요청 
	SIGNOUT((byte)6), // 로그아웃 요청
	ASK_UID((byte)7), // 아이디 찾기 
	ASK_FRIEND((byte)8), // 친구 목록 요청 
	ADD_FRIEND((byte)9), // 친구 추가 
	WHOAMI((byte)10); //내정보

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
