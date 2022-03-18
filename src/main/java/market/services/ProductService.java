package market.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import market.model.dao.CategoryDAO;
import market.model.dao.ProductDAO;
import market.model.persistence.Category;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

        validateIfNull(product);

        beginTransaction();
        this.productDAO.delete(product);
        commitAndCloseTransaction();
        this.LOG.info("Produto deletado com sucesso!");
    }

    public void update(Product newProduct, Long productId) {
        this.LOG.info("Preparando para atualizar o produto.");
        if (newProduct == null || productId == null) {
            this.LOG.error("Um dos parâmetros não pode ser nulo!");
            throw new RuntimeException("Parameter is null");
        }

        Product product = this.productDAO.getById(productId);
        validateIfNull(product);

        this.LOG.info("Produto encontrado no banco!");

        beginTransaction();
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setPrice(newProduct.getPrice());
        product.setCategory(this.categoryService.findByName(newProduct.getCategory().getName()));

        commitAndCloseTransaction();
        this.LOG.info("Produto atualizado com sucesso!");
    }

    public List<Product> listAll() {
        this.LOG.info("Preparando para listar os produtos.");
        List<Product> products = this.productDAO.listAll();

        if (products == null) {
            this.LOG.error("Não foram encontrados produtos!");
            return new ArrayList<Product>();
        }

        this.LOG.info("Foram encontrados " + products.size() + " produtos.");
        return products;
    }

    public List<Product> listByName(String name) {
        if (name == null || name.isEmpty()) {
            this.LOG.error("O parâmetro está nulo!");
            throw new RuntimeException("Null parameter");
        }

        this.LOG.info("Preparando para buscar os produtos de nome: " + name);
        List<Product> products = this.productDAO.listByName(name.toLowerCase());

        if (products == null) {
            this.LOG.error("Não foram encontrados produtos!");
            return new ArrayList<Product>();
        }

        this.LOG.info("Foram encontrados " + products.size() + " produtos.");
        return products;
    }

    private void validateIfNull(Product product) {
        if (product == null) {
            this.LOG.error("O produto não existe!");
            throw new EntityNotFoundException("Product not found");
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
