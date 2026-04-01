import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class UserData {
    String username;
    String passwordString;
    String roleString;

    UserData(String username, String passwordString, String roleString) {
        this.username = username;
        this.passwordString = passwordString;
        this.roleString = roleString;
    }

    public String toFileString() {
        return roleString + "," + username + "," + passwordString;
    }
}

class AuthService {

    private static final String FILE = "Users.txt";

    static void register(UserData user) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(user.toFileString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login
    static String login(String username, String password) {
        File file = new File(FILE);
        if (!file.exists()) return null;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length < 3)
                    continue;

                String role = data[0];
                String fileUsername = data[1];
                String filePassword = data[2];

                if (fileUsername.equals(username) && filePassword.equals(password)) {
                    return role;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static UserData getUserByUsername(String username) {
        File file = new File(FILE);
        if (!file.exists()) return null;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if (data.length < 3) continue;
                if (data[1].equals(username)) {
                    return new UserData(data[1], data[2], data[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean hasAdmin() {
        File file = new File(FILE);
        if (!file.exists()) return false;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if (data.length < 3) continue;
                if ("ADMIN".equals(data[0])) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

public class User {
    static void menu(Scanner sc) {
        System.out.println("Welcome to AuthService");
        System.out.println("Press 1. login");
        System.out.println("Press 2. Register");
        System.out.println("Press 3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        String username;
        System.out.print("Enter your username: ");
        username = sc.next();
        String password;
        System.out.print("Enter your password: ");
        password = sc.next();
        String role;
        switch (choice) {
            case 1:
                role = AuthService.login(username, password);
                if (role != null) {
                    System.out.println("Login successful");
                } else {
                    System.out.println("Login failed");
                }
                break;
            case 2:
                System.out.print("Enter your role: ");
                role = sc.next();
                UserData user = new UserData(username, password, role);
                AuthService.register(user);
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
