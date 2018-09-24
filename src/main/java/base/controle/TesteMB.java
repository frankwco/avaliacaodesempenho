package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;

import base.modelo.Pessoa;
import base.modelo.Atividade;
import base.service.AtividadeService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("testeMB")
public class TesteMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String porcentagem = "20";

	
	
	@Inject
	private GenericDAO<Atividade> daoProcesso; // faz as buscas

	@Inject
	private AtividadeService atividadeService; // inserir no banco

	@PostConstruct
	public void inicializar() {

	}
	
	public void itemSelect(ItemSelectEvent event) 
	{  
		System.out.println("Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
		
	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}
	
	

	
}
