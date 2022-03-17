package market.application;

import jakarta.persistence.EntityManager;
import market.connection.JpaConnectionFactory;
import market.model.persistence.Category;
import market.model.persistence.Product;
import market.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class Program {

    private static final Logger LOG = LogManager.getLogger(Program.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        ProductService productService = new ProductService(entityManager);

        Product product = new Product("Notebook", "Dell Inspiron 3501",
                new BigDecimal(3719.99),
                new Category("Informática"));

        productService.create(product);
    }
}
