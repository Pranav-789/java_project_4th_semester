import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class UserData {
    String id;
    String username;
    String passwordString;
    String roleString;

    UserData(String id, String username, String passwordString, String roleString) {
        this.id = id;
        this.username = username;
        this.passwordString = passwordString;
        this.roleString = roleString;
    }

    public String toFileString() {
        return roleString + "," + id + "," + username + "," + passwordString;
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

    // Login using ID or username
    static String login(String loginId, String password) {
        File file = new File(FILE);
        if (!file.exists()) return null;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length < 4) {
                    // Graceful fallback if any old entries exist
                    if (data.length >= 3) {
                       if (data[1].equals(loginId) && data[2].equals(password)) return data[0]; 
                    }
                    continue;
                }

                String role = data[0];
                String fileId = data[1];
                String fileUsername = data[2];
                String filePassword = data[3];

                if ((fileId.equals(loginId) || fileUsername.equals(loginId)) && filePassword.equals(password)) {
                    return role;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static UserData getUserById(String id) {
        File file = new File(FILE);
        if (!file.exists()) return null;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if (data.length < 4) continue;
                if (data[1].equals(id)) {
                    return new UserData(data[1], data[2], data[3], data[0]);
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
                if (data.length < 4) continue;
                if (data[2].equals(username)) {
                    return new UserData(data[1], data[2], data[3], data[0]);
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
                role = AuthService.login(id, password);
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
                UserData user = new UserData(id, username, password, role);
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
