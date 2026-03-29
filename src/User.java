public class User {
    protected int userid;
    protected String username;
    protected String password;
    protected String role;
    protected String email;
     private String name;
    private String course;

    public User(int userid, String username, String password, String role, String email, String name, String course) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.name = name;
        this.course = course;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getCourse() { return course; }

    public void displayMenu() {
        System.out.println("User Menu");
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}