package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.GrupoLancamento;
import dao.GenericDAO;
import util.Transacional;

public class GrupoLancamentoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<GrupoLancamento> dao;
	
	@Transacional
	public void inserirAlterar(GrupoLancamento tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
