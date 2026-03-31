public abstract class User {
    protected int userid;
    protected String username;
    protected String password;
    protected String role;
    protected String email;

    public User(int userid, String username, String password, String role, String email) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public int getUserid()       { return userid; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getRole()      { return role; }
    public String getEmail()     { return email; }

    public abstract void displayMenu();
}