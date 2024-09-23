package gs.konick.model;

public class Pizza extends SalesUnit {
    private String pizzaSize;

    public Pizza(int id, String name, long price, String pizzaSize) {
        super(id, name, price);
        this.pizzaSize = pizzaSize;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", pizzaSize='" + pizzaSize + '\'' +
                '}';
    }
}
