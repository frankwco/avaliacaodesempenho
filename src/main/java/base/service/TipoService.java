package base.service;

import java.io.Serializable;

import javax.inject.Inject;


import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class TipoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Tipo> daoTipo;
	
	@Transacional
	public void inserirAlterar(Tipo tipo){
		if(tipo.getId()==null){
			daoTipo.inserir(tipo);
		}else{
			daoTipo.alterar(tipo);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoTipo.update(valor);
	}

}
