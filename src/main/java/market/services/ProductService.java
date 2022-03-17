package market.services;

import jakarta.persistence.EntityManager;
import market.model.dao.ProductDAO;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductService {
    private static final Logger LOG = LogManager.getLogger(ProductService.class);
    private EntityManager entityManager;
    private ProductDAO productDAO;

    public ProductService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productDAO = new ProductDAO(entityManager);
    }

    public void create(Product product) {
        if (product == null) {
            this.LOG.error("O produto está nulo!");
            throw new RuntimeException("O produto está nulo!");
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

    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
