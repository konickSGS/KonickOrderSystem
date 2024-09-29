package gs.konick.dao;

import gs.konick.model.Category;

import java.util.List;

public interface CategoryDao {
    Category getCategoryByName(String name);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    void changeName(long id, String name);

    Category addCategory(String name);

    void deleteCategory(long id);
}
