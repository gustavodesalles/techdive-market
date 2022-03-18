package market.model.dao;

import jakarta.persistence.EntityManager;
import market.model.persistence.Category;

public class CategoryDAO {

    private EntityManager entityManager;

    public CategoryDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Category category) {
        this.entityManager.persist(category);
    }

    public Category findByName(String name) {
        String sql = "SELECT * FROM Category WHERE name =:name";
        return (Category) this.entityManager.createNativeQuery(sql, Category.class).setParameter("name",name).getSingleResult();
    }

    public Category getById(Long id) {
        return this.entityManager.find(Category.class, id);
    }

    public void delete(Category category) {
        this.entityManager.remove(category);
    }
}
