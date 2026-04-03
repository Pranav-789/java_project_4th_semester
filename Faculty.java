public class Faculty {
    private String facultyId;
    private String name;
    private String password;

    public Faculty(String facultyId, String name, String password) {
        this.facultyId = facultyId;
        this.name = name;
        this.password = password;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
