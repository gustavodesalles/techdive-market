package market.application;

import jakarta.persistence.EntityManager;
import market.connection.JpaConnectionFactory;
import market.model.persistence.Category;
import market.model.persistence.Client;
import market.model.persistence.Product;
import market.services.CategoryService;
import market.services.ClientService;
import market.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Program {

    private static final Logger LOG = LogManager.getLogger(Program.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        ProductService productService = new ProductService(entityManager);
        CategoryService categoryService = new CategoryService(entityManager);
        ClientService clientService = new ClientService(entityManager);
        Scanner input = new Scanner(System.in);

        Product product;
        Client client;
        List<Product> products;
        List<Client> clients;
        String nome, descricao, cpf, categoria, dataNascString;
        LocalDate dataNasc;
        double preco;
        Long id;

        System.out.println("Escolha uma opção: ");
        System.out.println("1 - Criar produto");
        System.out.println("2 - Deletar produto");
        System.out.println("3 - Alterar produto");
        System.out.println("4 - Listar todos os produtos");
        System.out.println("5 - Listar produtos pelo nome");
        System.out.println("6 - Criar cliente");
        System.out.println("7 - Deletar cliente");
        System.out.println("8 - Alterar cliente");
        System.out.println("9 - Listar todos os clientes");
        System.out.println("10 - Listar clientes pelo nome");
        System.out.println("Digite qualquer outro caractere para sair.");

        int opcao = input.nextInt();
        input.nextLine();

        switch (opcao) {
            case 1:
                System.out.println("Digite o nome do produto: ");
                nome = input.nextLine();
                System.out.println("Digite a descrição do produto: ");
                descricao = input.nextLine();
                System.out.println("Digite o preço: ");
                preco = input.nextDouble();
                input.nextLine();
                System.out.println("Digite o nome da categoria: ");
                categoria = input.nextLine();

                product = new Product(nome, descricao, new BigDecimal(preco), new Category(categoria));
                productService.create(product);
                main(null);
                break;
            case 2:
                System.out.println("Digite o ID do produto: ");
                id = input.nextLong();
                input.nextLine();
                productService.delete(id);
                main(null);
                break;
            case 3:
                System.out.println("Digite o ID do produto: ");
                id = input.nextLong();
                input.nextLine();

                System.out.println("Digite o nome do produto: ");
                nome = input.nextLine();
                System.out.println("Digite a descrição do produto: ");
                descricao = input.nextLine();
                System.out.println("Digite o preço: ");
                preco = input.nextDouble();
                input.nextLine();
                System.out.println("Digite o nome da categoria: ");
                categoria = input.nextLine();

                product = new Product(nome, descricao, new BigDecimal(preco), new Category(categoria));
                productService.update(product, id);
                main(null);
                break;
            case 4:
                products = productService.listAll();
                products.stream().forEach(p -> System.out.println(p));
                main(null);
                break;
            case 5:
                System.out.println("Digite o nome do produto: ");
                nome = input.nextLine();
                products = productService.listByName(nome);
                products.stream().forEach(p -> System.out.println(p));
                main(null);
                break;
            case 6:
                System.out.println("Digite o nome do cliente: ");
                nome = input.nextLine();
                System.out.println("Digite o CPF do cliente: ");
                cpf = input.nextLine();
                System.out.println("Digite a data de nascimento (dd/MM/yyyy): ");
                dataNascString = input.nextLine();
                dataNasc = LocalDate.parse(dataNascString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                client = new Client(nome, cpf, dataNasc);
                clientService.create(client);
                main(null);
                break;
            case 7:
                System.out.println("Digite o ID do cliente: ");
                id = input.nextLong();
                input.nextLine();
                clientService.delete(id);
                main(null);
                break;
            case 8:
                System.out.println("Digite o ID do cliente: ");
                id = input.nextLong();
                input.nextLine();

                System.out.println("Digite o nome do cliente: ");
                nome = input.nextLine();
                System.out.println("Digite o CPF do cliente: ");
                cpf = input.nextLine();
                System.out.println("Digite a data de nascimento (dd/MM/yyyy): ");
                dataNascString = input.nextLine();
                dataNasc = LocalDate.parse(dataNascString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                client = new Client(nome, cpf, dataNasc);
                clientService.update(client, id);
                main(null);
                break;
            case 9:
                clients = clientService.listAll();
                clients.stream().forEach(c -> System.out.println(c));
                main(null);
                break;
            case 10:
                System.out.println("Digite o nome do cliente: ");
                nome = input.nextLine();
                clients = clientService.listByName(nome);
                clients.stream().forEach(c -> System.out.println(c));
                main(null);
                break;
            default:
                LOG.info("Encerrando.");
        }

        //Product product = new Product("Computador", "Atari ST",
        //        new BigDecimal(11599.95),
        //        new Category("Informática"));

        //Client client = new Client("Casimiro Miguel", "21535107081", LocalDate.of(1995, 7, 18));
        //clientService.create(client);
        //clientService.delete(1L);
        //clientService.update(client, 3L);
        //List<Client> clients = clientService.listByName("farid germAno fIlHo");
        //clients.stream().forEach(client -> System.out.println(client));


        //productService.create(product);
        //productService.delete(3L);
        //productService.update(product, 4L);
        //List<Product> products = productService.listByName("Computador");
        //products.stream().forEach(product1 -> System.out.println(product1));

    }
}
