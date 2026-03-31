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

    // Change own password — available to all roles
    public void changePassword(Scanner sc) {
        System.out.print("Enter current password: ");
        String current = sc.nextLine().trim();
        if (!this.password.equals(current)) {
            System.out.println("Incorrect current password.");
            return;
        }
        System.out.print("Enter new password    : ");
        String newPass = sc.nextLine().trim();
        if (newPass.isEmpty()) { System.out.println("Password cannot be empty."); return; }
        System.out.print("Confirm new password  : ");
        String confirm = sc.nextLine().trim();
        if (!newPass.equals(confirm)) { System.out.println("Passwords do not match."); return; }

        try {
            java.util.List<String> lines = new java.util.ArrayList<>();
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("users.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(this.username)) { d[1] = newPass; line = String.join(",", d); }
                lines.add(line);
            }
            br.close();
            java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            this.password = newPass;
            System.out.println("✔ Password changed successfully.");
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }
}