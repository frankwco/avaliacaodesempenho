package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.ItensLancamento;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class ItensLancamentoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<ItensLancamento> dao;
	
	@Transacional
	public void inserirAlterar(ItensLancamento tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
