import java.util.ArrayList;
import java.util.List;

public class FacultyService {
    private List<Faculty> faculties;
    private AuthService authService;
    private CourseService courseService;

    public FacultyService(AuthService authService, CourseService courseService) {
        this.faculties = new ArrayList<>();
        this.authService = authService;
        this.courseService = courseService;
    }

    public Faculty login(String id, String password) {
        try {
            if (AuthService.login(id, password) != null) {
                 for (Faculty f : faculties) {
                     if (f.getFacultyId().equals(id)) {
                         return f;
                     }
                 }
                 
                 // Rehydrate if not natively stored
                 UserData data = AuthService.getUserById(id);
                 if (data == null) data = AuthService.getUserByUsername(id);
                 if (data != null && "FACULTY".equals(data.roleString)) {
                     Faculty rehydrated = new Faculty(data.id, data.username, data.passwordString);
                     faculties.add(rehydrated);
                     return rehydrated;
                 }
            }
        } catch (Exception e) {
            // Local fallback validation
        }

        for (Faculty f : faculties) {
            if (f.getFacultyId().equals(id) && f.getPassword().equals(password)) {
                return f;
            }
        }
        return null;
    }

    public void viewMyCourses(Faculty faculty) {
        System.out.println("\n--- My Assigned Courses ---");
        CourseData[] allCourses = CourseService.getAllCourses();
        boolean found = false;

        if (allCourses != null) {
            for (CourseData c : allCourses) {
                if (faculty.getName().equals(c.facultyUsername)) {
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
        for (CourseData c : CourseService.getAllCourses()) {
            if (c.courseId.equals(courseId)) {
                course = c;
                break;
            }
        }
        
        if (course == null || !faculty.getName().equals(course.facultyUsername)) {
            System.out.println("Error: Course not found or not assigned to you.");
            return;
        }

        System.out.println("\n--- Students Enrolled in " + courseId + " ---");
        EnrollmentData[] enrollments = EnrollmentService.getAllEnrollments();
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
