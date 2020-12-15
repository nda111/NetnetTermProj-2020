package data;

public enum EResponse {

	ANNOUNCE_ADD_FRIEND(ERequest.ANNOUNCE, (byte)0), // 燁살뮄�럡 �빊遺쏙옙 
	ANNOUNCE_FRIEND_IN(ERequest.ANNOUNCE, (byte)1),  // 燁살뮄�럡 占쎌젔占쎈꺗 
	ANNOUNCE_FRIEND_OUT(ERequest.ANNOUNCE, (byte)2), // 燁살뮄�럡 占쎈염野껓옙 占쎈퉸占쎌젫 
	
	ANNOUNCE_ASK_CHAT(ERequest.ANNOUNCE, (byte)3), 	// 占쏙옙占쎌넅 占쎈뻿筌ｏ옙 占쎌읈占쎈뼎 
	ANNOUNCE_ACK_CHAT(ERequest.ANNOUNCE, (byte)4), 	// 占쏙옙占쎌넅 占쎈뻿筌ｏ옙 野껉퀗�궢 占쎌읈占쎈뼎 
	ANNOUNCE_SAY_CHAT(ERequest.ANNOUNCE, (byte)5), 	// 占쏙옙占쎌넅 占쎄땀占쎌뒠 占쎌읈占쎈뼎 
	ANNOUNCE_CALC_CHAT(ERequest.ANNOUNCE, (byte)6), // �④쑴沅� 野껉퀗�궢 占쎌읈占쎈뼎 
	ANNOUNCE_END_CHAT(ERequest.ANNOUNCE, (byte)7),	// 占쏙옙占쎌넅 �넫�굝利� 占쎌읈占쎈뼎 
	
	ANNOUNCE_EDIT_INF(ERequest.ANNOUNCE, (byte)8),

	ECHO_OK(ERequest.ECHO, (byte)0), // 占쎈퓠�굜占� 占쎌벓占쎈뼗
	
	QUIT_OK(ERequest.QUIT, (byte)0), // �넫�굝利� 占쎈땾占쎌뵭 
	
	VALIDATE_UID_OK(ERequest.VALIDATE_UID, (byte)0), // UID 占쎄텢占쎌뒠 揶쏉옙占쎈뮟 
	VALIDATE_UID_NO(ERequest.VALIDATE_UID, (byte)1), // UID 占쎄텢占쎌뒠 �겫�뜃占쏙옙�뮟
	
	SIGNUP_OK(ERequest.SIGNUP, (byte)0),	// 揶쏉옙占쎌뿯 占쎄쉐�⑨옙 
	SIGNUP_ERR(ERequest.SIGNUP, (byte)1), 	// 揶쏉옙占쎌뿯 占쎈뼄占쎈솭 
	
	SIGNIN_OK(ERequest.SIGNIN, (byte)0),			// 嚥≪뮄�젃占쎌뵥 占쎄쉐�⑨옙 
	SIGNIN_ERR_PW(ERequest.SIGNIN, (byte)1),		// 嚥≪뮄�젃占쎌뵥 �뜮袁⑨옙甕곕뜇�깈 占쏙옙
	SIGNIN_ERR_MULTI(ERequest.SIGNIN, (byte)2),		// 嚥≪뮄�젃占쎌뵥 占쎈연占쎌쑎 疫꿸퀗由� 
	SIGNIN_ERR_NO_UID(ERequest.SIGNIN, (byte)3),	// 嚥≪뮄�젃占쎌뵥 占쎈씨占쎈뮉 �④쑴�젟
	
	SIGNOUT_OK(ERequest.SIGNOUT, (byte)0),			// 嚥≪뮄�젃占쎈툡占쎌뜍 占쎄쉐�⑨옙 
	SIGNOUT_ERR_NOT_IN(ERequest.SIGNOUT, (byte)1), 	// 嚥≪뮄�젃占쎈툡占쎌뜍 占쎈뼄占쎈솭 (嚥≪뮄�젃占쎌뵥 占쎈립 占쎌읅 占쎈씨占쎌벉)
	SIGNOUT_ERR(ERequest.SIGNOUT, (byte)2),			// 嚥≪뮄�젃占쎈툡占쎌뜍 占쎈뼄占쎈솭 (域뱄옙 占쎌뇚 占쎌뵠占쎌�)

	ASK_UID_OK(ERequest.ASK_UID, (byte)0),	// 占쎌뿳占쎌벉
	ASK_UID_NO(ERequest.ASK_UID, (byte)1),	// 占쎈씨占쎌벉 
	
	ASK_FRIEND_OK(ERequest.ASK_FRIEND, (byte)0),	// 占쎈�뗰옙�� 
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1),	// 占쎄쉘占쎄쉘 
	
	ADD_FRIEND_OK(ERequest.ADD_FRIEND, (byte)0),			// 占쎄쉐�⑨옙 
	ADD_FRIEND_ERR_UID(ERequest.ADD_FRIEND, (byte)1),		// 域밸챶�쑕 占쎈막 占쎈씨占쎌벉 
	ADD_FRIEND_ERR_ALREADY(ERequest.ADD_FRIEND, (byte)2),	// 占쎌뵠沃섓옙 �빊遺쏙옙占쎈맙 
	ADD_FRIEND_ERR_YOU(ERequest.ADD_FRIEND, (byte)3),		// 癰귣챷�뵥 燁살뮄�럡 �빊遺쏙옙 
	ADD_FRIEND_ERR(ERequest.ADD_FRIEND, (byte)4),			// 占쎈퓠占쎌쑎 

	ASK_CHAT_OK(ERequest.ASK_CHAT, (byte)0), 		// 占쎈�뗰옙�� �눧�눘堉김퉪�눊苡� 
	ASK_CHAT_OFFLINE(ERequest.ASK_CHAT, (byte)1), 	// 椰꾬옙 占쎌궎占쎈늄占쎌뵬占쎌뵥占쎌뵠占쎌뵬 占쎈툧 占쎈쭡 
	ASK_CHAT_ERR(ERequest.ASK_CHAT, (byte)2), 		// 占쎈퓠占쎌쑎 占쎄텢占쎌몵占쎈빍繹먲옙 占쎈뼄占쎈뻻 占쎈퉸�걡占� 

	ACK_CHAT_OK(ERequest.ACK_CHAT, (byte)0),		// 占쎈�뗰옙�� 占쎌읈占쎈뼎占쎈막野껓옙 
	ACK_CHAT_LATE(ERequest.ACK_CHAT, (byte)1),		// 占쎈뮡占쎈�占쎈선 
	
	SAY_CHAT_OK(ERequest.SAY_CHAT, (byte)0),		// 筌띾�곕릭疫뀐옙 占쎈�뗰옙�� 
	SAY_CHAT_NO_ROOM(ERequest.SAY_CHAT, (byte)1), 	// 獄쏉옙 占쎈씨占쎈선筌욑옙 
	SAY_CHAT_ERR(ERequest.SAY_CHAT, (byte)2),		// 占쎈퓠占쎌쑎 

	END_CHAT_OK(ERequest.END_CHAT, (byte)0),	// 占쎈�뗰옙�� 
	END_CHAT_ERR(ERequest.END_CHAT, (byte)1),	// 占쎈퓠占쎌쑎 
	
	WHO_AM_I_OK(ERequest.WHOAMI, (byte)0), //占쎄땀占쎌젟癰귨옙 占쎄퐫疫뀐옙 占쎄쉐�⑨옙
	WHO_AM_I_NO(ERequest.WHOAMI, (byte)1), //占쎄땀占쎌젟癰귨옙 占쎄퐫疫뀐옙 占쎈뼄占쎈솭

	EDIT_INF_OK(ERequest.EDIT_INF, (byte)0),
	EDIT_INF_ERR(ERequest.EDIT_INF, (byte)1);

	// 占쎈솁占쎌뵬沃섎챸苑ｆ에占� 占쎈굶占쎈선占쎌궔 揶쏅�る퓠 占쎈퉸占쎈뼣占쎈릭占쎈뮉 ERequest 揶쏆빘猿쒐몴占� 獄쏆꼹�넎占쎈립占쎈뼄.
	// 占쎈르 占쎈땾 占쎈씨占쎈뮉 占쎌뒄筌ｏ옙占쎌뵠筌롳옙 null占쎌뱽 獄쏆꼹�넎占쎈립占쎈뼄.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(short value) {
		
		for (EResponse v : EResponse.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private short value = -1; // 占쎌뒄筌ｏ옙占쎌벥 占쎈뻼癰귨옙
	
	// 占쎄문占쎄쉐
	private EResponse(ERequest request, byte value) {
		
		this.value = (short) ((request.getValue() << 8) | value);
		
		// 0000 0000 | 0000 0000
		// 占쎌뒄筌ｏ옙 ID   | Yes/No/...
	}
	
	// 占쎌뵠 占쎌뒄筌ｏ옙占쎌벥 占쎈뻼癰귢쑴�쁽�몴占� 揶쏉옙占쎌죬占쎌궔占쎈뼄.
	public short getValue() {
		
		return value;
	}
	
	// 占쎌뵠 占쎌벓占쎈뼗占쎌뵠 占쎈선占쎈샥 占쎌뒄筌ｏ옙占쎈퓠 占쏙옙占쎈립 占쎌벓占쎈뼗占쎌뵥筌욑옙 揶쏉옙占쎌죬占쎌궔占쎈뼄.
	public ERequest getRequest() {
		
		final byte requestId = (byte) (value >> 8);
		
		return ERequest.valueOf(requestId);
	}
	
	// 占쎌뒄筌ｏ옙占쎈퓠 占쏙옙占쎈퉸 占쎈선占쎈샥 占쎌벓占쎈뼗占쎌뵥筌욑옙 揶쏉옙占쎌죬占쎌궔占쎈뼄.
	public byte getResponse() {
		
		return (byte) (value & 0xFFFFFF);
	}
}
