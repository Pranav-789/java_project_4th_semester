import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


class AuthService implements DataService<BaseUser> {

    public static final String FILE = "Users.txt";

    @Override
    public void save(BaseUser entity) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(entity.toFileString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BaseUser> getAll() {
        List<BaseUser> entities = new ArrayList<>();
        File file = new File(FILE);
        
        if (!file.exists()) {
            return entities;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                if (data.length < 4) {
                    if (data.length >= 3) {
                        entities.add(new Student(data[1], data[1], data[2])); 
                    }
                } else {
                    String role = data[0];
                    String id = data[1];
                    String username = data[2];
                    String password = data[3];

                    if (role.equals("STUDENT")) entities.add(new Student(id, username, password));
                    else if (role.equals("FACULTY")) entities.add(new Faculty(id, username, password));
                    else if (role.equals("ADMIN")) entities.add(new AdminUser(id, username, password));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
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

    public String generateId(String role) {
        String prefix = "";
        if (role.toUpperCase().equals("ADMIN")) prefix = "ADM";
        else if (role.toUpperCase().equals("FACULTY")) prefix = "FAC";
        else if (role.toUpperCase().equals("STUDENT")) prefix = "STD";
        else return "UNKNOWN";

        int max = 0;
        for (BaseUser u : getAll()) {
            if (u.getId().startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(u.getId().substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException e) {
                    // ignore legacy or malformed IDs
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }
}