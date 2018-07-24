package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.DescricaoDespesa;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class DescricaoDespesaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<DescricaoDespesa> dao;
	
	@Transacional
	public void inserirAlterar(DescricaoDespesa tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
