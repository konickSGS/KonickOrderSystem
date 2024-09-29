package gs.konick.model;

/**
 * Единица заказа. Например: пицца или вода.
 */
public class SaleUnit {
    private long id;
    private String name;
    private long categoryId;
    // Цена в копейках
    private long price;
    private String description;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        SaleUnit saleUnit = new SaleUnit();

        public SaleUnit build() {
            return saleUnit;
        }

        public Builder setId(long id) {
            saleUnit.id = id;
            return this;
        }

        public Builder setName(String name) {
            saleUnit.name = name;
            return this;
        }

        public Builder setCategoryId(long categoryId) {
            saleUnit.categoryId = categoryId;
            return this;
        }

        public Builder setPrice(long price) {
            saleUnit.price = price;
            return this;
        }

        public Builder setDescription(String description) {
            saleUnit.description = description;
            return this;
        }
    }
}
