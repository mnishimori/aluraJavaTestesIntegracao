package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

class LanceDaoTest {

    private EntityManager em;
    private LanceDao dao;
    private Lance lance;
    private Usuario usuario;

    @BeforeEach
    void beforeEach(){
        this.em = JPAUtil.getEntityManager();
        this.dao = new LanceDao(em);
        this.em.getTransaction().begin();
    }

    @AfterEach
    void afterEach(){
        this.em.getTransaction().rollback();
        this.em.close();
    }

    @Test
    void deveriaBuscarMaiorLanceDoLeilao() {
        Leilao leilao = this.criarLeilao();
        this.criarLances(leilao);

        Lance maiorLanceDoLeilao = dao.buscarMaiorLanceDoLeilao(leilao);

        Assertions.assertEquals(maiorLanceDoLeilao.getValor(), new BigDecimal("300.00"));
    }

    private Leilao criarLeilao() {
        Usuario usuario = criarUsuario("Joao", "joao@email.com");
        Leilao leilao = new Leilao("Leilão Teste 1",
                new BigDecimal("100.00"), LocalDate.now(),
                usuario);
        return em.merge(leilao);
    }

    private void criarLances(Leilao leilao){
        usuario = this.criarUsuario("Fulano", "fulano@email.com");
        this.criarLance(leilao, usuario, new BigDecimal("100.00"));

        usuario = this.criarUsuario("Beltrano", "beltrano@email.com");
        this.criarLance(leilao, usuario, new BigDecimal("200.00"));

        usuario = this.criarUsuario("Ciclano", "ciclano@email.com");
        this.criarLance(leilao, usuario, new BigDecimal("300.00"));
    }

    private void criarLance(Leilao leilao, Usuario usuario, BigDecimal valorLance){
        lance = new Lance(leilao, usuario, valorLance);
        this.dao.salvar(lance);
    }

    private Usuario criarUsuario(String nome, String email) {
        Usuario usuario = new Usuario(nome, email, "12345678");
        em.persist(usuario);
        return usuario;
    }
}