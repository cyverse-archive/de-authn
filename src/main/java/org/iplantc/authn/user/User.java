package org.iplantc.authn.user;

import net.sf.json.JSONObject;

/**
 * Represents a Discovery Environment User.
 * @author Donald A. Barre
 */
public class User {

	private String username;
	private String password;
    private String email;
    private String shortUsername;

	public User() {

	}

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShortUsername() {
        return shortUsername;
    }

    public void setShortUsername(String shortUsername) {
        this.shortUsername = shortUsername;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("email", email);
        json.put("short_username", shortUsername);
        return json;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public static User fromJson(JSONObject json) {
        User user = new User();
        user.setUsername(json.getString("username"));
        user.setEmail(json.getString("email"));
        user.setShortUsername(json.getString("short_username"));
        return user;
    }

    public static User fromString(String str) {
        return fromJson(JSONObject.fromObject(str));
    }
}
