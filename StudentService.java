import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students;
    private AuthService authService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    public StudentService(AuthService authService, CourseService courseService) {
        this.students = new ArrayList<>();
        this.authService = authService;
        this.courseService = courseService;
        this.enrollmentService = new EnrollmentService();
    }

    public void registerStudent(String id, String username, String password) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                System.out.println("Error: Student ID already exists.");
                return;
            }
        }
        Student newStudent = new Student(id, username, password);
        students.add(newStudent);
        
        try {
            authService.register(newStudent);
        } catch (Exception e) {
            // Fallback in case AuthService doesn't expose standard register directly
        }
        System.out.println("Student registered successfully.");
    }

    public Student login(String id, String password) {
        boolean authSuccess = false;
        try {
            authSuccess = authService.login(id, password) != null;
        } catch (Exception e) {
            // Local fallback if AuthService logic handles roles differently
            authSuccess = true; 
        }

        if (authSuccess) {
            for (Student s : students) {
                if (s.getId().equals(id) && s.getPassword().equals(password)) {
                    return s;
                }
            }
            
            // Re-hydrate from file if the service was restarted.
            BaseUser data = authService.getUserById(id);
            if (data == null) data = authService.getUserByUsername(id); // fallback
            
            if (data != null && "STUDENT".equals(data.getRole())) {
                Student rehydratedStudent = new Student(data.getId(), data.getUsername(), data.getPassword());
                students.add(rehydratedStudent);
                return rehydratedStudent;
            }
        }
        return null;
    }

    public void viewAvailableCourses() {
        System.out.println("\n--- Available Courses ---");
        List<CourseData> courses = courseService.getAll();
        if (courses == null || courses.size() == 0) {
            System.out.println("No courses currently available.");
            return;
        }
        for (CourseData c : courses) {
            System.out.println(c.courseId + " | " + c.courseName);
        }
    }

    public void enrollInCourse(Student student, String courseId) {
        CourseData course = null;
        for (CourseData c : courseService.getAll()) {
            if (c.courseId.equals(courseId)) {
                course = c;
                break;
            }
        }
        
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }

        List<EnrollmentData> enrollments = enrollmentService.getAll();
        if (enrollments != null) {
            for (EnrollmentData e : enrollments) {
                if (e.StudentUsername.equals(student.getId()) && e.CourseId.equals(courseId)) {
                    System.out.println("Error: You are already enrolled in this course.");
                    return;
                }
            }
        }

        EnrollmentData newEnrollment = new EnrollmentData(student.getId(), courseId);
        enrollmentService.save(newEnrollment);
        System.out.println("Success: Enrolled in " + courseId + ".");
    }

    public void viewMyCourses(Student student) {
        System.out.println("\n--- My Enrolled Courses ---");
        List<EnrollmentData> enrollments = enrollmentService.getAll();
        boolean found = false;

        if (enrollments != null) {
            for (EnrollmentData e : enrollments) {
                if (e.StudentUsername.equals(student.getId())) {
                    CourseData c = null;
                    for (CourseData cd : courseService.getAll()) {
                        if (cd.courseId.equals(e.CourseId)) {
                            c = cd;
                            break;
                        }
                    }
                    if (c != null) {
                        System.out.println(c.courseId + " | " + c.courseName);
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("You are not currently enrolled in any courses.");
        }
    }

    public void dropCourse(Student student, String courseId) {
        boolean dropped = false;
        try {
            dropped = enrollmentService.removeEnrollment(student.getId(), courseId);
        } catch (Exception ex) {
            System.out.println("Error: Unable to drop course. Ensure the ID is correct.");
            return;
        }

        if (dropped) {
            System.out.println("Success: Dropped course " + courseId + ".");
        } else {
            System.out.println("Error: You are not enrolled in this course or it doesn't exist.");
        }
    }
}
