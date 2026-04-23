import java.util.Scanner;
import java.util.List;

class AdminService {
    private AuthService authService;
    private CourseService courseService;

    public AdminService() {
        this.authService = new AuthService();
        this.courseService = new CourseService();
    }

    BaseUser addUser(String id, String username, String password, String role) {
        if (!role.equals("ADMIN") && !role.equals("FACULTY") && !role.equals("STUDENT")) {
            System.out.println("Invalid role");
            return null;
        }

        if (authService.getUserById(id) != null || authService.getUserByUsername(username) != null) {
            System.out.println("User ID or Username already exists");
            return null;
        }

        if (role.equals("ADMIN") && authService.hasAdmin()) {
            System.out.println("An admin already exists");
            return null;
        }

        BaseUser user = null;
        if (role.equals("ADMIN")) user = new AdminUser(id, username, password);
        else if (role.equals("FACULTY")) user = new Faculty(id, username, password);
        else if (role.equals("STUDENT")) user = new Student(id, username, password);
        
        authService.register(user);

        return user;
    }

    void viewAllCourses() {
        List<CourseData> courses = courseService.getAll();

        for (CourseData course : courses) {
            System.out.println(course.courseId + " | " + course.courseName + " | " + course.facultyUsername);
        }
    }

    void createCourse(String courseId, String courseName, String facultyUsername) {
        BaseUser faculty = authService.getUserByUsername(facultyUsername);
        if (faculty == null || !faculty.getRole().equals("FACULTY")) {
            System.out.println("Invalid faculty username or user is not a FACULTY");
            return;
        }

        CourseData course = new CourseData(courseId, courseName, facultyUsername);
        courseService.save(course);

        System.out.println("Course created successfully");
    }

    void changeCourseFaculty(String courseId, String newFacultyUsername) {
        BaseUser faculty = authService.getUserByUsername(newFacultyUsername);
        if (faculty == null || !faculty.getRole().equals("FACULTY")) {
            System.out.println("Invalid faculty username or user is not a FACULTY");
            return;
        }

        boolean success = courseService.updateCourseFaculty(courseId, newFacultyUsername);
        if (success) {
            System.out.println("Course faculty changed successfully");
        } else {
            System.out.println("Course not found");
        }
    }
}

public class Admin {

    static void menu(Scanner sc) {
        AdminService adminService = new AdminService();

        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Add User");
            System.out.println("2. View All Courses");
            System.out.println("3. Create Course");
            System.out.println("4. Change Course Faculty");
            System.out.println("5. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter username: ");
                    String username = sc.nextLine();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    System.out.print("Enter role (ADMIN/FACULTY/STUDENT): ");
                    String role = sc.nextLine();

                    adminService.addUser(id, username, password, role);
                    break;

                case 2:
                    adminService.viewAllCourses();
                    break;

                case 3:
                    System.out.print("Enter course id: ");
                    String courseId = sc.nextLine();

                    System.out.print("Enter course name: ");
                    String courseName = sc.nextLine();

                    System.out.print("Enter faculty username: ");
                    String facultyUsername = sc.nextLine();

                    adminService.createCourse(courseId, courseName, facultyUsername);
                    break;

                case 4:
                    System.out.print("Enter course id: ");
                    String editCourseId = sc.nextLine();

                    System.out.print("Enter new faculty username: ");
                    String newFacultyUsername = sc.nextLine();

                    adminService.changeCourseFaculty(editCourseId, newFacultyUsername);
                    break;

                case 5:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        menu(sc);
    }
}
