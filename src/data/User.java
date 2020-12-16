package data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import app.Server;

public final class User {
	
	public interface ValueChangeListener {
		
		void valueChanged(String name, String message);
	}
	
	public static final String UsersPath = "./users/";
	
	public static User parseJsonOrNull(String jsonString) {
		
		try {
			
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject)parser.parse(jsonString);

			String uid = (String) json.get("uid");
			String email = (String) json.get("email");
			String password = (String) json.get("password");
			String name = (String) json.get("name");
			long birth = (long) json.get("birth");
			String personalMessage = (String) json.get("personalMessage");
			long signOutTime = (long) json.get("signOutTime");

			HashSet<String> friends;
			JSONArray friendArray = (JSONArray)json.get("friends");
			if (friendArray == null) {
				
				friends = null;
			} else {
				
				friends = new HashSet<>();
				
				ListIterator iterator = friendArray.listIterator();
				while (iterator.hasNext()) {
					
					String friend = (String)iterator.next();
					friends.add(friend);
				}
			}
			
			User user = new User(uid, email, password, name, birth, personalMessage, friends);
			user.signOutTime = signOutTime;
			return user;
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	// set up the location to store user information
	public static String buildFilePath(String uid) {
		
		return UsersPath + uid + ".json";
	}
	
	// check if UID is available	
	public static boolean isUidAvailable(String uid) {
		
		return !Server.Users.containsKey(uid);
	}
	
	// Encrypts the password
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
	
	// Change bytes to hex
	private static String bytesToHex(byte[] bytes) {
		
	    StringBuilder builder = new StringBuilder();
	    for (byte b: bytes) {
	    
	    	builder.append(String.format("%02x", b));
	    }
	    
	    return builder.toString();
	}

	public String uid = null;
	public String email = null;
	public String password = null;
	public String name = null;
	public long birth = -1;
	public SocketAddress userAddress = null;
	public String personalMessage = "";
	public long signOutTime = -1;
	public HashSet<String> friends = null;
	
	public ValueChangeListener valueChangeListener = null;
	
	public User(String uid, String email, String password, String name, long birth, String personalMessage, HashSet<String> friends) {
		
		this.uid = uid;
		this.email = email;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.personalMessage = personalMessage;
		this.friends = friends;
	}
	
	public User(String uid, String email, String password, String name, long birth) {
		
		this(uid, email, hashPassword(password), name, birth, "", new HashSet<>());
	}
	
	public boolean isSignedIn() {
		
		return userAddress != null;
	}
	
	public void signInBy(SocketAddress address) {
		
		userAddress = address;
	}
	
	public void signOutNow() {
		
		userAddress = null;
		signOutTime = System.currentTimeMillis();
	}
	
	public boolean certificateByAddress(SocketAddress address) {
		
		return this.userAddress.toString().equals(address.toString());
	}
	
	public boolean matchPassword(String password) {
		
		password = hashPassword(password);
		
		return password.equals(this.password);
	}
	
	public boolean isFriendOf(User user) {
		
		return friends.contains(user.uid);
	}
	
	public JSONObject toJson() {
		
		JSONObject json = new JSONObject();
		JSONArray friendArray = new JSONArray();
		
		for (String friend : friends) {
			
			friendArray.add(friend);
		}
		
		json.put("uid", uid);
		json.put("email", email);
		json.put("password", password);
		json.put("name", name);
		json.put("birth", birth);
		json.put("personalMessage", personalMessage);
		json.put("signOutTime", signOutTime);
		json.put("friends", friendArray);
		
		return json;
	}
	
	// Sotre the user's information in a json file in the specified path
	public boolean tryWriteFile() {
		
		File dir = new File(User.UsersPath);
		if (!dir.exists()) {
			
			dir.mkdir();
		}
		
		String path = buildFilePath(uid);
		File file = new File(path);
		if (!file.exists()) {

			try {
				
				file.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
				return false;
			}
		} 

		try {
			
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
	
	public void updateValue(String name, String message) {
		
		this.name = name;
		this.personalMessage = message;
		
		if (this.valueChangeListener != null) {
			
			this.valueChangeListener.valueChanged(name, message);
		}
	}
}
