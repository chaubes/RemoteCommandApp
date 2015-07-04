package com.remote.command.websocket.domain;

/**
 * Created by saurabhchaubey on 15/06/15.
 */
public class User {
    private final String email;
    private final String password;
    private final UserType type;
    private final String sessionid;

    public User(String email, String password, UserType type, String sessionId) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.sessionid = sessionId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    public String getSessionid() {
        return sessionid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (sessionid != null ? !sessionid.equals(user.sessionid) : user.sessionid != null) return false;
        if (type != user.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (sessionid != null ? sessionid.hashCode() : 0);
        return result;
    }
}
