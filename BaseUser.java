public abstract class BaseUser implements FileSerializable {
    protected String id;
    protected String username;
    protected String password;
    protected String role;

    public BaseUser(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toFileString() {
        return role + "," + id + "," + username + "," + password;
    }
}
