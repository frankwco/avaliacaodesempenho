package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Salario;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class SalarioService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Salario> dao;
	
	@Transacional
	public void inserirAlterar(Salario tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
