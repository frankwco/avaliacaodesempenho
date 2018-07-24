package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Pessoa;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class PessoaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Pessoa> dao;
	
	@Transacional
	public void inserirAlterar(Pessoa tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
