public class AdminUser extends BaseUser {
    public AdminUser(String id, String username, String password) {
        super(id, username, password, "ADMIN");
    }
}
