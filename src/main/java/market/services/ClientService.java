package market.services;

import jakarta.persistence.EntityManager;
import market.model.dao.ClientDAO;
import market.model.persistence.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientService {
    public static final Logger LOG = LogManager.getLogger(ClientService.class);
    public EntityManager entityManager;
    public ClientDAO clientDAO;

    public ClientService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.clientDAO = new ClientDAO(entityManager);
    }

    public void create(Client client) {
        this.LOG.info("Preparando para criar o cliente.");
        if (client == null) {
            LOG.error("O cliente está nulo!");
            throw new RuntimeException("Null client");
        }

        String cpf = client.getCpf();
        if (!checkCpf(cpf)) {
            LOG.error("O CPF é inválido!");
            throw new RuntimeException("Invalid CPF");
        }

        try {
            beginTransaction();
            clientDAO.create(client);
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao criar o cliente, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
        LOG.info("Cliente criado com sucesso!");

    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private String formatCpf(String cpf) {
        return cpf.replaceAll("\\.|-","");
    }

    private boolean checkCpf(String cpf) {
        cpf = formatCpf(cpf);
        if (cpf.length() != 11 || cpf.equals("11111111111")
                || cpf.equals("22222222222")
                || cpf.equals("33333333333")
                || cpf.equals("44444444444")
                || cpf.equals("55555555555")
                || cpf.equals("66666666666")
                || cpf.equals("77777777777")
                || cpf.equals("88888888888")
                || cpf.equals("99999999999")
                || cpf.equals("00000000000")) {
            return false;
        }
        int digv1 = Character.getNumericValue(cpf.charAt(9));
        int digv2 = Character.getNumericValue(cpf.charAt(10));

        //verificar o primeiro dígito
        int multiplicador = 10;
        int soma = 0;
        int resto1;

        for (int i = 0; i < 9; ++i) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * multiplicador;
            --multiplicador;
        }

        if (((soma * 10) % 11) == 10) {
            resto1 = 0;
        } else {
            resto1 = ((soma * 10) % 11);
        }

        //verificar o segundo dígito
        multiplicador = 11;
        soma = 0;
        int resto2;

        for (int i = 0; i < 10; ++i) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * multiplicador;
            --multiplicador;
        }

        if (((soma * 10) % 11) == 10) {
            resto2 = 0;
        } else {
            resto2 = ((soma * 10) % 11);
        }

        return (digv1 == resto1 && digv2 == resto2);
    }
}
