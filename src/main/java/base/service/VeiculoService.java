package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Tipo;
import base.modelo.Veiculo;
import dao.GenericDAO;
import util.Transacional;

public class VeiculoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Veiculo> dao;
	
	@Transacional
	public void inserirAlterar(Veiculo tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
