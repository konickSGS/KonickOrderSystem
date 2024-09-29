package gs.konick.model;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        Category category = new Category();

        public Category getCategory() {
            return category;
        }

        public Builder setId(long id) {
            category.id = id;
            return this;
        }

        public Builder setName(String name) {
            category.name = name;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
