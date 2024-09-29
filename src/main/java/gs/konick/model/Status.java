package gs.konick.model;

public enum Status {
    NEW(1, "Новый заказ"),
    APPROVED(2, "Подтвержден"),
    IN_WORK(3, "В работе"),
    DELIVERING(4, "Доставка"),
    RECEIVED(5, "Готово");

    private final long id;
    private final String status;

    Status(long id, String status) {
        this.status = status;
        this.id = id;
    }

    public static Status getStatusById(long id) {
        for (Status s : values()) {
            if (s.id == id) {
                return s;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
