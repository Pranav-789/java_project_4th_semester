import java.util.ArrayList;
import java.util.List;

public class FacultyService {
    private List<Faculty> faculties;
    private AuthService authService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    public FacultyService(AuthService authService, CourseService courseService) {
        this.faculties = new ArrayList<>();
        this.authService = authService;
        this.courseService = courseService;
        this.enrollmentService = new EnrollmentService();
    }

    public Faculty login(String id, String password) {
        try {
            if (authService.login(id, password) != null) {
                 for (Faculty f : faculties) {
                     if (f.getId().equals(id)) {
                         return f;
                     }
                 }
                 
                 // Rehydrate if not natively stored
                 BaseUser data = authService.getUserById(id);
                 if (data == null) data = authService.getUserByUsername(id);
                 if (data != null && "FACULTY".equals(data.getRole())) {
                     Faculty rehydrated = new Faculty(data.getId(), data.getUsername(), data.getPassword());
                     faculties.add(rehydrated);
                     return rehydrated;
                 }
            }
        } catch (Exception e) {
            // Local fallback validation
        }

        for (Faculty f : faculties) {
            if (f.getId().equals(id) && f.getPassword().equals(password)) {
                return f;
            }
        }
        return null;
    }

    public void viewMyCourses(Faculty faculty) {
        System.out.println("\n--- My Assigned Courses ---");
        List<CourseData> allCourses = courseService.getAll();
        boolean found = false;

        if (allCourses != null) {
            for (CourseData c : allCourses) {
                if (faculty.getUsername().equals(c.facultyUsername)) {
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
        
        if (course == null || !faculty.getUsername().equals(course.facultyUsername)) {
            System.out.println("Error: Course not found or not assigned to you.");
            return;
        }

        System.out.println("\n--- Students Enrolled in " + courseId + " ---");
        List<EnrollmentData> enrollments = enrollmentService.getAll();
        boolean found = false;
        
        if (enrollments != null) {
            for (EnrollmentData e : enrollments) {
                if (e.CourseId.equals(courseId)) {
                    System.out.println("Student ID: " + e.StudentUsername);
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("No students are currently enrolled in this course.");
        }
    }
}
