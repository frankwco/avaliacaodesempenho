package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Pessoa;
import base.modelo.Tipo;
import base.modelo.Usuario;
import dao.GenericDAO;
import util.Transacional;

public class UsuarioService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericDAO<base.modelo.Usuario> dao;
	
	@Transacional
	public void inserirAlterar(Usuario tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}
	
	@Transacional
	public void updateSenha(String senha, String email){
		dao.updateSenha(senha, email);
	}


}
