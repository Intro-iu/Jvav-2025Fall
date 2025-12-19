package service;

import dao.CategoryDAO;
import model.Category;
import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    public boolean add(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryDAO.add(category);
    }

    public boolean update(int id, String name) {
        Category category = new Category(id, name);
        return categoryDAO.update(category);
    }

    public boolean delete(int id) {
        return categoryDAO.delete(id);
    }
}
