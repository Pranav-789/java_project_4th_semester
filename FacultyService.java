import java.util.List;

public class FacultyService {
    private AuthService authService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    public FacultyService(AuthService authService, CourseService courseService) {
        this.authService = authService;
        this.courseService = courseService;
        this.enrollmentService = new EnrollmentService();
    }

    public Faculty login(String id, String password) {
        try {
            if ("FACULTY".equals(authService.login(id, password))) {
                BaseUser data = authService.getUserById(id);
                if (data == null) data = authService.getUserByUsername(id);
                if (data != null && "FACULTY".equals(data.getRole())) {
                    return new Faculty(data.getId(), data.getUsername(), data.getPassword());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void viewMyCourses(Faculty faculty) {
        System.out.println("\n--- My Assigned Courses ---");
        List<CourseData> allCourses = courseService.getAll();
        boolean found = false;

        if (allCourses != null) {
            for (CourseData c : allCourses) {
                if (faculty.getId().equals(c.facultyId)) {
                    System.out.println(c.courseId + " | " + c.courseName);
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("You have no assigned courses at this time.");
        }
    }

    public void viewEnrolledStudents(Faculty faculty, String courseId) {
        CourseData course = null;
        for (CourseData c : courseService.getAll()) {
            if (c.courseId.equals(courseId)) {
                course = c;
                break;
            }
        }
        
        if (course == null || !faculty.getId().equals(course.facultyId)) {
            System.out.println("Error: Course not found or not assigned to you.");
            return;
        }

        System.out.println("\n--- Students Enrolled in " + courseId + " ---");
        List<EnrollmentData> enrollments = enrollmentService.getAll();
        boolean found = false;
        
        if (enrollments != null) {
            for (EnrollmentData e : enrollments) {
                if (e.courseId.equals(courseId)) {
                    System.out.println("Student ID: " + e.studentId);
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("No students are currently enrolled in this course.");
        }
    }
}
