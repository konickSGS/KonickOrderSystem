package gs.konick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable {
    private long id;
    private String login;
    private String hashedPassword;
    private Role role;
    private String email;
    private String address;
    private Date createDate;

    private User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && role == user.role && Objects.equals(email, user.email) && Objects.equals(address, user.address) && Objects.equals(createDate, user.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, role, email, address, createDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public static class Builder {
        User user = new User();

        public Builder setId(long id) {
            user.id = id;
            return this;
        }

        public Builder setLogin(String login) {
            user.login = login;
            return this;
        }

        public Builder setHashedPassword(String password) {
            user.hashedPassword = password;
            return this;
        }

        public Builder setRole(int roleId) {
            user.role = Role.getRoleById(roleId);
            return this;
        }

        public Builder setEmail(String email) {
            user.email = email;
            return this;
        }

        public Builder setAddress(String address) {
            user.address = address;
            return this;
        }

        public Builder setCreateDate(Date createDate) {
            user.createDate = createDate;
            return this;
        }

        public User build() {
            return user;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
