package gs.konick.model;

enum Role {
    CLIENT("клиент"),
    MANAGER("менеджер");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}

public class User extends Entity {
    private String login;
    private String password;

    // Статус: активный или неактивный (если пользователь был удален)
    private String status;

    // клиент или менеджер
    private String role;
    private String email;

    public User(long id, String login, String password, String status, String role, String email) {
        super(id);
        this.login = login;
        this.password = password;
        this.status = status;
        this.role = role;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
