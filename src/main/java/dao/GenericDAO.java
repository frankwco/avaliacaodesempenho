package dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public class GenericDAO<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public void alterar(T objeto) {
		manager.merge(objeto);
	}
	
	public void remover(T objeto) {
		Object  o = manager.merge(objeto);
		manager.remove(o);
	}
	
	public void inserir(T objeto) {
		manager.persist(objeto);
	}

	public T buscarPorId(Class<T> classe, Long id) {		
		return manager.find(classe, id);
	}
	
	public T buscarCondicao(Class classeEntidade, String condicao) {
		return (T) manager.createQuery("from "+classeEntidade.getSimpleName()+" where "+condicao).getSingleResult();
	}
	
	public List<T> listaComStatus(Class classeEntidade) {
		return manager.createQuery("from "+classeEntidade.getSimpleName()+" where status is true order by id").getResultList();
	}
	
	public List<T> listar(Class classeEntidade, String condicao) {
		Query query = null;
		try {
			query = manager.createQuery("from " + classeEntidade.getSimpleName() + " where status is true and " + condicao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}
	
	public List<T> listarCodicaoLivre(Class classeEntidade, String condicao) {
		Query query = null;
		try {
			query = manager.createQuery("from " + classeEntidade.getSimpleName() + " where " + condicao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}
	
	
	public List listarCadastro(Class classe, String condicao) {
		Query query = null;
		try {
			query = manager
					.createQuery("from " + classe.getSimpleName() + " where " + condicao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

//	
//	
//	
//	public void update(String alteracao) {
//		String sql = "";
//		
//			sql = ("update " + alteracao);
//			int update = manager.createQuery(sql).executeUpdate();
//			
//		
//	}
	
	
	public void updateSenha(String senha, String email) {
		String sql = "";
	  
			sql = ("update Pessoa set senha = '" + senha + "' where usuario like '" + email + "'");
			int update = manager.createQuery(sql).executeUpdate();
	 
	}
	
	
	
	

}
