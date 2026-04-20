import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Instantiate core components 
        AuthService authService = new AuthService();
        CourseService courseService = new CourseService();
        StudentService studentService = new StudentService(authService, courseService);
        FacultyService facultyService = new FacultyService(authService, courseService);
        
        while (true) {
            System.out.println("\n==================================");
            System.out.println(" COURSE ENROLLMENT SYSTEM (CLI)");
            System.out.println("==================================");
            System.out.println("1. Student Login / Register");
            System.out.println("2. Faculty Login");
            System.out.println("3. Exit System");
            System.out.print("Select Menu Option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    handleStudentMenu(scanner, studentService);
                    break;
                case "2":
                    handleFacultyMenu(scanner, facultyService);
                    break;
                case "3":
                    System.out.println("Shutting down the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private static void handleStudentMenu(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Student Access ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Back to Main Menu");
        System.out.print("Select Option: ");
        
        String option = scanner.nextLine().trim();
        
        if (option.equals("1")) {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();
            
            Student student = studentService.login(id, password);
            if (student != null) {
                System.out.println("Login Successful.");
                studentDashboard(scanner, studentService, student);
            } else {
                System.out.println("Error: Invalid ID or Password.");
            }
            
        } else if (option.equals("2")) {
            System.out.print("Enter New ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String pass = scanner.nextLine().trim();
            
            if (id.isEmpty() || name.isEmpty() || pass.isEmpty()) {
                System.out.println("Error: All fields are required to register.");
            } else {
                studentService.registerStudent(id, name, pass);
            }
        }
    }

    private static void studentDashboard(Scanner scanner, StudentService studentService, Student student) {
        while (true) {
            System.out.println("\n--- Student Dashboard | " + student.getUsername() + " ---");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. View My Courses");
            System.out.println("5. Logout");
            System.out.print("Select Option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    studentService.viewAvailableCourses();
                    break;
                case "2":
                    System.out.print("Enter the Course ID to Enroll: ");
                    String enrollId = scanner.nextLine().trim();
                    studentService.enrollInCourse(student, enrollId);
                    break;
                case "3":
                    System.out.print("Enter the Course ID to Drop: ");
                    String dropId = scanner.nextLine().trim();
                    studentService.dropCourse(student, dropId);
                    break;
                case "4":
                    studentService.viewMyCourses(student);
                    break;
                case "5":
                    System.out.println("Logging off...");
                    return;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private static void handleFacultyMenu(Scanner scanner, FacultyService facultyService) {
        System.out.println("\n--- Faculty Access ---");
        System.out.print("Enter Faculty ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        Faculty faculty = facultyService.login(id, password);
        if (faculty != null) {
            System.out.println("Login Successful.");
            facultyDashboard(scanner, facultyService, faculty);
        } else {
            System.out.println("Error: Invalid ID or Password.");
        }
    }

    private static void facultyDashboard(Scanner scanner, FacultyService facultyService, Faculty faculty) {
        while (true) {
            System.out.println("\n--- Faculty Dashboard | " + faculty.getUsername() + " ---");
            System.out.println("1. View My Assigned Courses");
            System.out.println("2. View Enrolled Students for a Course");
            System.out.println("3. Logout");
            System.out.print("Select Option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    facultyService.viewMyCourses(faculty);
                    break;
                case "2":
                    System.out.print("Enter Course ID to view student roster: ");
                    String courseId = scanner.nextLine().trim();
                    facultyService.viewEnrolledStudents(faculty, courseId);
                    break;
                case "3":
                    System.out.println("Logging off...");
                    return;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
    }
}
