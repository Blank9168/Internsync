public class User {
    private int userid;
    private String username;
    private String password;
    private String role;
    private String email;

    // Constructor
    public User(int userid, String username, String password, String role, String email) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    // Getters
    public int getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    // Login method
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Logout method (currently empty)
    public void logout() {
        // Optional: Add logout logic if needed
    }

    // Update profile
    public void UpdateProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}