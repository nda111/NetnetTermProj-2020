package data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import app.Server;

public final class User {
	
	public static final String UsersPath = "./users/";
	
	public static int MaxId = -1;
	
	public static User parseJsonOrNull(String jsonString) {
		
		try {
			
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject)parser.parse(jsonString);

			int id = (int) json.get("id");
			String uid = (String) json.get("uid");
			String email = (String) json.get("email");
			String password = (String) json.get("password");
			String name = (String) json.get("name");
			long birth = (long) json.get("birth");
			String personalMessage = (String) json.get("personalMessage");
			
			return new User(id, uid, email, password, name, birth, personalMessage);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static String buildFilePath(int id) {
		
		return UsersPath + id + ".json";
	}
	
	public static boolean isUidAvailable(String uid) {
		
		return Server.Users.containsKey(uid);
	}
	
	private static String hashPassword(String password) {

		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
		    md.update(password.getBytes());
		    
		    return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) { // Won't happen
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	private static String bytesToHex(byte[] bytes) {
		
	    StringBuilder builder = new StringBuilder();
	    for (byte b: bytes) {
	    
	    	builder.append(String.format("%02x", b));
	    }
	    
	    return builder.toString();
	}

	public int id = -1;
	public String uid = null;
	public String email = null;
	public String password = null;
	public String name = null;
	public long birth = -1;
	public boolean isLoggedIn = false;
	public String personalMessage = null;
	public long logoutTime = -1;
	
	public User(int id, String uid, String email, String password, String name, long birth, String personalMessage) {
		
		if (MaxId < id) {
			
			MaxId = id;
		}
		
		this.id = id;
		this.uid = uid;
		this.email = email;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.personalMessage = personalMessage;
	}
	
	public User(String uid, String email, String password, String name, long birth) {
		
		this(++MaxId, uid, email, hashPassword(password), name, birth, null);
	}
	
	public boolean matchPassword(String password) {
		
		password = hashPassword(password);
		
		return password.equals(this.password);
	}
	
	public JSONObject toJson() {
		
		JSONObject json = new JSONObject();
		
		json.put("id", id);
		json.put("uid", uid);
		json.put("email", email);
		json.put("password", password);
		json.put("name", name);
		json.put("birth", birth);
		json.put("personalMessage", personalMessage);
		
		return json;
	}
	
	public boolean tryWriteFile() {
		
		String path = buildFilePath(id);
		File file = new File(path);
		
		if (file.exists()) {
			
			return false;
		} else {
			
			try {
				
				file.createNewFile();
				PrintWriter writer = new PrintWriter(path);
				
				JSONObject json = toJson();
				json.writeJSONString(writer);
				
				writer.close();
				
				return true;
			} catch (IOException e) {
				
				e.printStackTrace();
				return false;
			}
		}
	}
}
