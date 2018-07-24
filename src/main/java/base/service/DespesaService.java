package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Despesa;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class DespesaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Despesa> dao;
	
	@Transacional
	public void inserirAlterar(Despesa tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
