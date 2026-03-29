
public class User {
    private int userid;
    private String username;
    private String password;
    private String role;
    private String email;

    public User(int userid, String username, String password, String role, String email) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public int getUserid() {
        return userid;
    }

    public boolean login(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            return true;
        }
        return false;
    }

    public void logout() {
        
    }

    public void UpdateProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
