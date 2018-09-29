package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.CategoriaIndicador;
import base.modelo.Ocorrencia;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class OcorrenciaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Ocorrencia> dao;
	
	@Transacional
	public void inserirAlterar(Ocorrencia tipo){
		if(tipo.getId()==null){
			dao.inserir(tipo);
		}else{
			dao.alterar(tipo);
		}
	}


}
