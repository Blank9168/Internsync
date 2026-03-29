public class Admin {
    private int adminid;
    private int userId;

    public Admin(int adminid, int userId) {
        this.adminid = adminid;
        this.userId = userId;
    }

    public void manageUsers() {
        
    }

    public void manageCompanies() {
        
    }

    public void manageInternships() {
        
    }

    public void changeUserRole(User user, String newRole) {
        user.UpdateProfile(user.getUserid(), user.getUsername(), user.getPassword(), user.getEmail());
    }


}
