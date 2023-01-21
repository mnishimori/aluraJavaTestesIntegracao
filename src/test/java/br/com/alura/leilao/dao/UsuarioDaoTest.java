package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

class UsuarioDaoTest {

	private UsuarioDao dao;

	private Usuario usuario;

	private EntityManager em;

	@BeforeEach
	void beforEach(){
		this.em = JPAUtil.getEntityManager();
		this.dao = new UsuarioDao(em);
		this.em.getTransaction().begin();
	}

	@AfterEach
	void realizarAposCadaTeste(){
		this.em.getTransaction().rollback();
		this.em.close();
	}

	@Test
	void deveriaEncontrarUmUsuarioCadastrado() {
		criarUsuario();

		Usuario encontrado = this.dao.buscarPorUsername(usuario.getNome());
		Assert.assertNotNull(encontrado);
	}

	@Test
	void naoDeveriaEncontrarUmUsuarioNaoCadastrado() {
		criarUsuario();

		Assert.assertThrows(NoResultException.class, () -> this.dao.buscarPorUsername("Beltrano"));
	}

	private void criarUsuario() {
		this.usuario = new Usuario("fulano", "fulano@email.com", "12345678");
		em.persist(usuario);
	}
}
