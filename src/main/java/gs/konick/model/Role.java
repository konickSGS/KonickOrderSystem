package gs.konick.model;

public enum Role {
    CLIENT(1, "client"),
    MANAGER(2, "manager");

    private long id;
    private String role;

    public static Role getRoleById(long id) {
        for (Role r: values()) {
            if (r.id == id) return r;
        }
        throw new IllegalArgumentException("Вашего id " + id + " нет в доступных для role");
    }

    Role(long id, String role) {
        this.id = id;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
