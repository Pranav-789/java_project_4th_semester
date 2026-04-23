import java.util.List;

public class StudentService {
    private AuthService authService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    public StudentService(AuthService authService, CourseService courseService) {
        this.authService = authService;
        this.courseService = courseService;
        this.enrollmentService = new EnrollmentService();
    }

    public void registerStudent(String username, String password) {
        if (authService.getUserByUsername(username) != null) {
            System.out.println("Error: Username already exists.");
            return;
        }

        String id = authService.generateId("STUDENT");
        Student newStudent = new Student(id, username, password);
        
        try {
            authService.register(newStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Student registered successfully with ID: " + id);
    }

    public Student login(String id, String password) {
        try {
            if ("STUDENT".equals(authService.login(id, password))) {
                 BaseUser data = authService.getUserById(id);
                 if (data == null) data = authService.getUserByUsername(id);
                 
                 if (data != null && "STUDENT".equals(data.getRole())) {
                     return new Student(data.getId(), data.getUsername(), data.getPassword());
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                if (e.studentId.equals(student.getId()) && e.courseId.equals(courseId)) {
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
                if (e.studentId.equals(student.getId())) {
                    CourseData c = null;
                    for (CourseData cd : courseService.getAll()) {
                        if (cd.courseId.equals(e.courseId)) {
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
