package market.model.dao;

import jakarta.persistence.EntityManager;
import market.model.persistence.Client;

import java.time.LocalDate;
import java.util.List;

public class ClientDAO {

    private EntityManager entityManager;

    public ClientDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Client client) {
        this.entityManager.persist(client);
    }

    public void delete(Client client) {
        this.entityManager.remove(client);
    }

    @SuppressWarnings("unchecked")
    public List<Client> listAll() {
        String sql = "SELECT * FROM Client";
        return this.entityManager.createNativeQuery(sql, Client.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Client> listByName(String name) {
        String sql = "SELECT * FROM Client WHERE name =:name";
        return this.entityManager.createNativeQuery(sql, Client.class)
                .setParameter("name", name).getResultList();
    }

    public Client getById(Long id) {
        return this.entityManager.find(Client.class, id);
    }
}
