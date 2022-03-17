package market.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import market.model.dao.CategoryDAO;
import market.model.dao.ProductDAO;
import market.model.persistence.Category;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductService {
    private static final Logger LOG = LogManager.getLogger(ProductService.class);
    private EntityManager entityManager;
    private ProductDAO productDAO;
    private CategoryService categoryService;

    public ProductService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productDAO = new ProductDAO(entityManager);
        this.categoryService = new CategoryService(entityManager);
    }

    public void create(Product product) {
        this.LOG.info("Preparando para criar um produto.");

        if (product == null) {
            this.LOG.error("O produto está nulo!");
            throw new RuntimeException("Null product");
        }
        String categoryName = product.getCategory().getName();
        this.LOG.info("Buscando categoria " + categoryName);
        Category category = categoryService.findByName(categoryName);
        if (category != null) {
            product.setCategory(category);
        }

        try {
            product.setName(product.getName().toLowerCase());
            product.setDescription(product.getDescription().toLowerCase());
            product.getCategory().setName(product.getCategory().getName().toLowerCase());

            beginTransaction();
            this.productDAO.create(product);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar o produto, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Produto criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para encontrar o produto.");
        if (id == null) {
            this.LOG.error("O ID do produto informado está nulo!");
            throw new RuntimeException("Null ID");
        }

        Product product = this.productDAO.getById(id);

        if (product == null) {
            this.LOG.error("O produto não existe!");
            throw new EntityNotFoundException("Product not found");
        }

        beginTransaction();
        this.productDAO.delete(product);
        commitAndCloseTransaction();
        this.LOG.info("Produto deletado com sucesso!");
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
