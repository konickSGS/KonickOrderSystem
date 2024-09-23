package gs.konick.model;

public class SalesUnit extends Entity {
    protected String name;

    // цена в копейках
    protected long price;

    public SalesUnit(long id, String name, long price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
