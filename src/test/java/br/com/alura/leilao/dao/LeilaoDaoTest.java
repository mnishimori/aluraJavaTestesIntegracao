package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LeilaoDaoTest {

    private EntityManager em;
    private LeilaoDao dao;

    @BeforeEach
    void beforEach(){
        this.em = JPAUtil.getEntityManager();
        this.dao = new LeilaoDao(em);
        this.em.getTransaction().begin();
    }

    @AfterEach
    void realizarAposCadaTeste(){
        this.em.getTransaction().rollback();
        this.em.close();
    }

    @Test
    void deveriaCadastrarUmLeilao() {
        Usuario usuario = this.criarUsuario();
        Leilao leilao = new Leilao("Leilão Teste 1", new BigDecimal("10.00"), LocalDate.now(),
                usuario);

        leilao = dao.salvar(leilao);

        Leilao leilaoSalvo = dao.buscarPorId(leilao.getId());
        assertNotNull(leilaoSalvo);
    }

    @Test
    void deveriaAtualizarUmLeilao() {
        Usuario usuario = this.criarUsuario();
        Leilao leilao = new Leilao("Leilão Teste 1", new BigDecimal("10.00"), LocalDate.now(),
                usuario);
        leilao = dao.salvar(leilao);
        leilao.setNome("Leilão Teste 2");
        leilao.setValorInicial(new BigDecimal("50.00"));

        leilao = dao.salvar(leilao);

        Leilao leilaoSalvo = dao.buscarPorId(leilao.getId());
        assertEquals("Leilão Teste 2", leilaoSalvo.getNome());
        assertEquals(new BigDecimal("50.00"), leilaoSalvo.getValorInicial());
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
        em.persist(usuario);
        return usuario;
    }
}