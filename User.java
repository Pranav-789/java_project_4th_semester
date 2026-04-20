import java.util.Scanner;


class AuthService extends AbstractFileService<BaseUser> {

    public static final String FILE = "Users.txt";
    
    public AuthService() {
        super(FILE);
    }

    @Override
    protected BaseUser parseLine(String[] data) {
        if (data.length < 4) {
            if (data.length >= 3) { // Legacy fallback
                // Not ideal but preserves old format handling somewhat
                return new Student(data[1], data[1], data[2]); 
            }
            return null;
        }
        String role = data[0];
        String id = data[1];
        String username = data[2];
        String password = data[3];

        if (role.equals("STUDENT")) return new Student(id, username, password);
        if (role.equals("FACULTY")) return new Faculty(id, username, password);
        if (role.equals("ADMIN")) return new AdminUser(id, username, password);
        
        return null;
    }

    public void register(BaseUser user) {
        save(user);
    }

    // Login using ID or username
    public String login(String loginId, String password) {
        for (BaseUser u : getAll()) {
            if ((u.getId().equals(loginId) || u.getUsername().equals(loginId)) && u.getPassword().equals(password)) {
                return u.getRole();
            }
        }
        return null;
    }

    public BaseUser getUserById(String id) {
        for (BaseUser u : getAll()) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    public BaseUser getUserByUsername(String username) {
        for (BaseUser u : getAll()) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public boolean hasAdmin() {
        for (BaseUser u : getAll()) {
            if ("ADMIN".equals(u.getRole())) return true;
        }
        return false;
    }
}

public class User {
    static void menu(Scanner sc) {
        AuthService authService = new AuthService();
        System.out.println("Welcome to AuthService");
        System.out.println("Press 1. login");
        System.out.println("Press 2. Register");
        System.out.println("Press 3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        String id;
        String username;
        String password;
        String role;
        switch (choice) {
            case 1:
                System.out.print("Enter your ID/username: ");
                id = sc.next();
                System.out.print("Enter your password: ");
                password = sc.next();
                role = authService.login(id, password);
                if (role != null) {
                    System.out.println("Login successful");
                } else {
                    System.out.println("Login failed");
                }
                break;
            case 2:
                System.out.print("Enter your ID: ");
                id = sc.next();
                System.out.print("Enter your username: ");
                username = sc.next();
                System.out.print("Enter your password: ");
                password = sc.next();
                System.out.print("Enter your role: ");
                role = sc.next();
                BaseUser user = null;
                if (role.toUpperCase().equals("ADMIN")) user = new AdminUser(id, username, password);
                else if (role.toUpperCase().equals("FACULTY")) user = new Faculty(id, username, password);
                else if (role.toUpperCase().equals("STUDENT")) user = new Student(id, username, password);
                
                if (user != null) {
                    authService.register(user);
                } else {
                    System.out.println("Invalid role");
                }
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        menu(sc);
    }
}
