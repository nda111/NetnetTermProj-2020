package data;

public enum EResponse {

	ECHO_OK(ERequest.ECHO, (byte)0), // 에코 응답
	
	QUIT_OK(ERequest.QUIT, (byte)0), // 종료 수락 
	
	VALIDATE_UID_OK(ERequest.VALIDATE_UID, (byte)0), // UID 사용 가능 
	VALIDATE_UID_NO(ERequest.VALIDATE_UID, (byte)1), // UID 사용 불가능
	
	SIGNUP_OK(ERequest.SIGNUP, (byte)0),	// 가입 성공 
	SIGNUP_ERR(ERequest.SIGNUP, (byte)1), 	// 가입 실패 
	
	SIGNIN_OK(ERequest.SIGNIN, (byte)0),			// 로그인 성공 
	SIGNIN_ERR_PW(ERequest.SIGNIN, (byte)1),		// 로그인 비밀번호 틀
	SIGNIN_ERR_MULTI(ERequest.SIGNIN, (byte)2),		// 로그인 여러 기기 
	SIGNIN_ERR_NO_UID(ERequest.SIGNIN, (byte)3),	// 로그인 없는 계정
	
	SIGNOUT_OK(ERequest.SIGNOUT, (byte)0),			// 로그아웃 성공 
	SIGNOUT_ERR_NOT_IN(ERequest.SIGNOUT, (byte)1), 	// 로그아웃 실패 (로그인 한 적 없음)
	SIGNOUT_ERR(ERequest.SIGNOUT, (byte)2),			// 로그아웃 실패 (그 외 이유)

	ASK_UID_OK(ERequest.ASK_UID, (byte)0),	// 있음
	ASK_UID_NO(ERequest.ASK_UID, (byte)1),	// 없음 
	
	ASK_FRIEND_OK(ERequest.ASK_FRIEND, (byte)0),	// ㅇㅋ 
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1);	// ㄴㄴ 

	// 파라미터로 들어온 값에 해당하는 ERequest 객체를 반환한다.
	// 알 수 없는 요청이면 null을 반환한다.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(short value) {
		
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
	
	// 요청에 대해 어떤 응답인지 가져온다.
	public byte getResponse() {
		
		return (byte) (value & 0xFFFFFF);
	}
}
