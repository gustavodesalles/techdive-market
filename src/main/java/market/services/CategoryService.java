package market.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import market.model.dao.CategoryDAO;
import market.model.persistence.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryService {
    private static final Logger LOG = LogManager.getLogger(CategoryService.class);
    private EntityManager entityManager;
    private CategoryDAO categoryDAO;

    public CategoryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.categoryDAO = new CategoryDAO(entityManager);
    }

    public Category findByName(String name) {
        if (name == null || name.isEmpty()) {
            this.LOG.error("O nome não pode ser nulo!");
            throw new RuntimeException("Category name is null");
        }

        try {
            Category category = this.categoryDAO.findByName(name.toLowerCase());
            return category;
        } catch (NoResultException n) {
            this.LOG.error("Categoria não encontrada; criando nova categoria.");
            return null;
        }
    }

    public void delete(Long id) {

    }

    public Category getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("Null parameter");
        }

        Category category = this.categoryDAO.getById(id);

        if (category == null) {
            this.LOG.error("Não foi encontrada a categoria de ID " + id + ".");
            throw new EntityNotFoundException("Category not found");
        }

        return null;
    }
}
