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

        Product product = new Product("Doritos", "Cool Ranch",
                new BigDecimal(14.95),
                new Category("Alimentos"));

        //productService.create(product);
        productService.delete(1L);
    }
}
