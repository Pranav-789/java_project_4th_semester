import java.util.Scanner;

class AdminService {

    static UserData addUser(String id, String username, String password, String role) {
        if (!role.equals("ADMIN") && !role.equals("FACULTY") && !role.equals("STUDENT")) {
            System.out.println("Invalid role");
            return null;
        }

        if (AuthService.getUserById(id) != null || AuthService.getUserByUsername(username) != null) {
            System.out.println("User ID or Username already exists");
            return null;
        }

        if (role.equals("ADMIN") && AuthService.hasAdmin()) {
            System.out.println("An admin already exists");
            return null;
        }

        UserData user = new UserData(id, username, password, role);
        AuthService.register(user);

        return user;
    }

    static void viewAllCourses() {
        CourseData[] courses = CourseService.getAllCourses();

        for (CourseData course : courses) {
            System.out.println(course.courseId + " | " + course.courseName + " | " + course.facultyUsername);
        }
    }

    static void createCourse(String courseId, String courseName, String facultyUsername) {
        UserData faculty = AuthService.getUserByUsername(facultyUsername);
        if (faculty == null || !faculty.roleString.equals("FACULTY")) {
            System.out.println("Invalid faculty username or user is not a FACULTY");
            return;
        }

        CourseData course = new CourseData(courseId, courseName, facultyUsername);
        CourseService.addCourse(course);

        System.out.println("Course created successfully");
    }
}


public class Admin {

    static void menu(Scanner sc) {

        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Add User");
            System.out.println("2. View All Courses");
            System.out.println("3. Create Course");
            System.out.println("4. Logout");

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

                    AdminService.addUser(id, username, password, role);
                    break;

                case 2:
                    AdminService.viewAllCourses();
                    break;

                case 3:
                    System.out.print("Enter course id: ");
                    String courseId = sc.nextLine();

                    System.out.print("Enter course name: ");
                    String courseName = sc.nextLine();

                    System.out.print("Enter faculty username: ");
                    String facultyUsername = sc.nextLine();

                    AdminService.createCourse(courseId, courseName, facultyUsername);
                    break;

                case 4:
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
