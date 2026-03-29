
public class Student {
    private int studentid;
    private String name;
    private String email;
    private int userId;
    private String course;
    private String resumeFile;
    private String applicationStatus;

    public Student(int studentid, String name, String email, int userId, String course, String resumeFile, String applicationStatus) {
        this.studentid = studentid;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.course = course;
        this.resumeFile = resumeFile;
        this.applicationStatus = applicationStatus;
    }

    public void browseInternships() {
        
    }

    public void applyInternship() {
        
    }

    public void viewApplicationStatus() {
        
    }
    public void uploadDocument(String resumeFile) {
        this.resumeFile = resumeFile;
    }
}
