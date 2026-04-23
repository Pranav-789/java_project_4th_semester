import java.util.List;

class AdminService {
    private AuthService authService;
    private CourseService courseService;

    public AdminService() {
        this.authService = new AuthService();
        this.courseService = new CourseService();
    }

    BaseUser addUser(String username, String password, String role) {
        if (!role.equals("ADMIN") && !role.equals("FACULTY") && !role.equals("STUDENT")) {
            System.out.println("Invalid role");
            return null;
        }

        if (authService.getUserByUsername(username) != null) {
            System.out.println("Username already exists");
            return null;
        }

        String id = authService.generateId(role);

        if (role.equals("ADMIN") && authService.hasAdmin()) {
            System.out.println("An admin already exists");
            return null;
        }

        BaseUser user = null;
        if (role.equals("ADMIN")) user = new AdminUser(id, username, password);
        else if (role.equals("FACULTY")) user = new Faculty(id, username, password);
        else if (role.equals("STUDENT")) user = new Student(id, username, password);
        
        authService.register(user);

        System.out.println("User registered successfully with ID: " + id);
        return user;
    }

    void viewAllCourses() {
        List<CourseData> courses = courseService.getAll();

        for (CourseData course : courses) {
            System.out.println(course.courseId + " | " + course.courseName + " | " + course.facultyId);
        }
    }

    void createCourse(String courseId, String courseName, String facultyId) {
        BaseUser faculty = authService.getUserById(facultyId);
        if (faculty == null || !faculty.getRole().equals("FACULTY")) {
            System.out.println("Invalid faculty ID or user is not a FACULTY");
            return;
        }

        CourseData course = new CourseData(courseId, courseName, facultyId);
        courseService.save(course);

        System.out.println("Course created successfully");
    }

    void changeCourseFaculty(String courseId, String newFacultyId) {
        BaseUser faculty = authService.getUserById(newFacultyId);
        if (faculty == null || !faculty.getRole().equals("FACULTY")) {
            System.out.println("Invalid faculty ID or user is not a FACULTY");
            return;
        }

        boolean success = courseService.updateCourseFaculty(courseId, newFacultyId);
        if (success) {
            System.out.println("Course faculty changed successfully");
        } else {
            System.out.println("Course not found");
        }
    }
}