package data;

public enum EResponse {

	ANNOUNCE_ADD_FRIEND(ERequest.ANNOUNCE, (byte)0), // 移쒓뎄 異붽� 
	ANNOUNCE_FRIEND_IN(ERequest.ANNOUNCE, (byte)1),  // 移쒓뎄 �젒�냽 
	ANNOUNCE_FRIEND_OUT(ERequest.ANNOUNCE, (byte)2), // 移쒓뎄 �뿰寃� �빐�젣 
	
	ANNOUNCE_ASK_CHAT(ERequest.ANNOUNCE, (byte)3), 	// ���솕 �떊泥� �쟾�떖 
	ANNOUNCE_ACK_CHAT(ERequest.ANNOUNCE, (byte)4), 	// ���솕 �떊泥� 寃곌낵 �쟾�떖 
	ANNOUNCE_SAY_CHAT(ERequest.ANNOUNCE, (byte)5), 	// ���솕 �궡�슜 �쟾�떖 
	ANNOUNCE_CALC_CHAT(ERequest.ANNOUNCE, (byte)6), // 怨꾩궛 寃곌낵 �쟾�떖 
	ANNOUNCE_END_CHAT(ERequest.ANNOUNCE, (byte)7),	// ���솕 醫낅즺 �쟾�떖 

	ECHO_OK(ERequest.ECHO, (byte)0), // �뿉肄� �쓳�떟
	
	QUIT_OK(ERequest.QUIT, (byte)0), // 醫낅즺 �닔�씫 
	
	VALIDATE_UID_OK(ERequest.VALIDATE_UID, (byte)0), // UID �궗�슜 媛��뒫 
	VALIDATE_UID_NO(ERequest.VALIDATE_UID, (byte)1), // UID �궗�슜 遺덇��뒫
	
	SIGNUP_OK(ERequest.SIGNUP, (byte)0),	// 媛��엯 �꽦怨� 
	SIGNUP_ERR(ERequest.SIGNUP, (byte)1), 	// 媛��엯 �떎�뙣 
	
	SIGNIN_OK(ERequest.SIGNIN, (byte)0),			// 濡쒓렇�씤 �꽦怨� 
	SIGNIN_ERR_PW(ERequest.SIGNIN, (byte)1),		// 濡쒓렇�씤 鍮꾨�踰덊샇 ��
	SIGNIN_ERR_MULTI(ERequest.SIGNIN, (byte)2),		// 濡쒓렇�씤 �뿬�윭 湲곌린 
	SIGNIN_ERR_NO_UID(ERequest.SIGNIN, (byte)3),	// 濡쒓렇�씤 �뾾�뒗 怨꾩젙
	
	SIGNOUT_OK(ERequest.SIGNOUT, (byte)0),			// 濡쒓렇�븘�썐 �꽦怨� 
	SIGNOUT_ERR_NOT_IN(ERequest.SIGNOUT, (byte)1), 	// 濡쒓렇�븘�썐 �떎�뙣 (濡쒓렇�씤 �븳 �쟻 �뾾�쓬)
	SIGNOUT_ERR(ERequest.SIGNOUT, (byte)2),			// 濡쒓렇�븘�썐 �떎�뙣 (洹� �쇅 �씠�쑀)

	ASK_UID_OK(ERequest.ASK_UID, (byte)0),	// �엳�쓬
	ASK_UID_NO(ERequest.ASK_UID, (byte)1),	// �뾾�쓬 
	
	ASK_FRIEND_OK(ERequest.ASK_FRIEND, (byte)0),	// �뀋�뀑 
	ASK_FRIEND_NO(ERequest.ASK_FRIEND, (byte)1),	// �꽩�꽩 
	
	ADD_FRIEND_OK(ERequest.ADD_FRIEND, (byte)0),			// �꽦怨� 
	ADD_FRIEND_ERR_UID(ERequest.ADD_FRIEND, (byte)1),		// 洹몃윴 �븷 �뾾�쓬 
	ADD_FRIEND_ERR_ALREADY(ERequest.ADD_FRIEND, (byte)2),	// �씠誘� 異붽��븿 
	ADD_FRIEND_ERR_YOU(ERequest.ADD_FRIEND, (byte)3),		// 蹂몄씤 移쒓뎄 異붽� 
	ADD_FRIEND_ERR(ERequest.ADD_FRIEND, (byte)4),			// �뿉�윭 

	ASK_CHAT_OK(ERequest.ASK_CHAT, (byte)0), 		// �뀋�뀑 臾쇱뼱蹂쇨쾶 
	ASK_CHAT_OFFLINE(ERequest.ASK_CHAT, (byte)1), 	// 嫄� �삤�봽�씪�씤�씠�씪 �븞 �맖 
	ASK_CHAT_ERR(ERequest.ASK_CHAT, (byte)2), 		// �뿉�윭 �궗�쑝�땲源� �떎�떆 �빐遊� 

	ACK_CHAT_OK(ERequest.ACK_CHAT, (byte)0),		// �뀋�뀑 �쟾�떖�븷寃� 
	ACK_CHAT_LATE(ERequest.ACK_CHAT, (byte)1),		// �뒭�뿀�뼱 
	
	SAY_CHAT_OK(ERequest.SAY_CHAT, (byte)0),		// 留먰븯湲� �뀋�뀑 
	SAY_CHAT_NO_ROOM(ERequest.SAY_CHAT, (byte)1), 	// 諛� �뾾�뼱吏� 
	SAY_CHAT_ERR(ERequest.SAY_CHAT, (byte)2),		// �뿉�윭 

	END_CHAT_OK(ERequest.END_CHAT, (byte)0),	// �뀋�뀑 
	END_CHAT_ERR(ERequest.END_CHAT, (byte)1),	// �뿉�윭 
	
	WHO_AM_I_OK(ERequest.WHOAMI, (byte)0), //�궡�젙蹂� �꽔湲� �꽦怨�
	WHO_AM_I_NO(ERequest.WHOAMI, (byte)1); //�궡�젙蹂� �꽔湲� �떎�뙣

	// �뙆�씪誘명꽣濡� �뱾�뼱�삩 媛믪뿉 �빐�떦�븯�뒗 ERequest 媛앹껜瑜� 諛섑솚�븳�떎.
	// �븣 �닔 �뾾�뒗 �슂泥��씠硫� null�쓣 諛섑솚�븳�떎.
	// ex) value == 0 -> return ECHO
	public static EResponse valueOf(short value) {
		
		for (EResponse v : EResponse.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private short value = -1; // �슂泥��쓽 �떇蹂�
	
	// �깮�꽦
	private EResponse(ERequest request, byte value) {
		
		this.value = (short) ((request.getValue() << 8) | value);
		
		// 0000 0000 | 0000 0000
		// �슂泥� ID   | Yes/No/...
	}
	
	// �씠 �슂泥��쓽 �떇蹂꾩옄瑜� 媛��졇�삩�떎.
	public short getValue() {
		
		return value;
	}
	
	// �씠 �쓳�떟�씠 �뼱�뼡 �슂泥��뿉 ���븳 �쓳�떟�씤吏� 媛��졇�삩�떎.
	public ERequest getRequest() {
		
		final byte requestId = (byte) (value >> 8);
		
		return ERequest.valueOf(requestId);
	}
	
	// �슂泥��뿉 ���빐 �뼱�뼡 �쓳�떟�씤吏� 媛��졇�삩�떎.
	public byte getResponse() {
		
		return (byte) (value & 0xFFFFFF);
	}
}
