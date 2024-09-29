package gs.konick.dao.impl;

import gs.konick.dao.CategoryDao;
import gs.konick.db.ConnectionPool;
import gs.konick.model.Category;
import gs.konick.sql.CategorySqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private static CategoryDao INSTANCE;

    private CategoryDaoImpl() {
    }

    public static synchronized CategoryDao getInstance() {
        if (INSTANCE == null) {
            return new CategoryDaoImpl();
        }
        return INSTANCE;
    }

    private static Category makeCategory(ResultSet resultSet) {
        try {
            int nameColumn = resultSet.findColumn("name");
            int idColumn = resultSet.findColumn("id");
            return new Category.Builder()
                    .setId(resultSet.getLong(idColumn))
                    .setName(resultSet.getString(nameColumn))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("В таблице нет колонки с таким именем", e);
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.FIND_CATEGORY_BY_NAME)) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                return makeCategory(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category getCategoryById(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.FIND_CATEGORY_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                return makeCategory(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.GET_ALL_CATEGORIES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(makeCategory(resultSet));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeName(long id, String name) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.CHANGE_NAME)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось изменить имя категории");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category addCategory(String name) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.ADD_CATEGORY)) {
            preparedStatement.setString(1, name);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось добавить категорию");
            }

            return getCategoryByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategory(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CategorySqlQuery.DELETE_CATEGORY)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось удалить category");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
