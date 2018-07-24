package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Processo;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class ProcessoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Processo> dao;
	
	@Transacional
	public void inserirAlterar(Processo tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
