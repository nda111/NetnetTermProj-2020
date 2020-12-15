package data;

public enum ERequest {
	
	ANNOUNCE((byte)0), // �꽌踰�->�겢�씪�씠�뼵�듃 (�꽌踰꾧� �닔�떊�븷 �씪 �뾾�쓬)
	ECHO((byte)1), // �뿉肄� �슂泥�
	QUIT((byte)2), // 醫낅즺 �꽑�뼵  
	VALIDATE_UID((byte)3), // UID 以묐났 寃��궗
	SIGNUP((byte)4), // �쉶�썝媛��엯 �슂泥� 
	SIGNIN((byte)5), // 濡쒓렇�씤 �슂泥� 
	SIGNOUT((byte)6), // 濡쒓렇�븘�썐 �슂泥�
	ASK_UID((byte)7), // �븘�씠�뵒 李얘린 
	ASK_FRIEND((byte)8), // 移쒓뎄 紐⑸줉 �슂泥� 
	ADD_FRIEND((byte)9), // 移쒓뎄 異붽� 
	ASK_CHAT((byte)10), // ���솕 �슂泥� 
	ACK_CHAT((byte)11), // ���솕 �닔�씫/嫄곗젅
	SAY_CHAT((byte)12), // ���솕 諛쒗솕 
	END_CHAT((byte)13), // ���솕 醫낅즺 
	WHOAMI((byte)14), //�궡�젙蹂�
	EDIT_INF((byte)15); // 내 정보 수정 

	// �뙆�씪誘명꽣濡� �뱾�뼱�삩 媛믪뿉 �빐�떦�븯�뒗 ERequest 媛앹껜瑜� 諛섑솚�븳�떎.
	// �븣 �닔 �뾾�뒗 �슂泥��씠硫� null�쓣 諛섑솚�븳�떎.
	// ex) value == 0 -> return ECHO
	public static ERequest valueOf(byte value) {
		
		for (ERequest v : ERequest.values()) {
			
			if (v.getValue() == value) {
				
				return v;
			}
		}
		
		return null;
	}
	
	private byte value = -1; // �슂泥��쓽 �떇蹂�
	
	// �깮�꽦
	private ERequest(byte value) {
		
		this.value = value;
	}
	
	// �씠 �슂泥��쓽 �떇蹂꾩옄瑜� 媛��졇�삩�떎.
	public byte getValue() {
		
		return value;
	}
}
