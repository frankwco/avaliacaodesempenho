package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Pessoa;
import base.modelo.Processo;
import base.modelo.ProcessoIndicador;
import base.modelo.Atividade;
import base.modelo.CategoriaIndicador;
import base.service.AtividadeService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("selecionarIndicadoresMB")
public class SelecionarIndicadoresMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProcessoIndicador> listaProcessoIndicador;

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicador; // faz as
																	// buscas

	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas

	@Inject
	private AtividadeService atividadeService; // inserir no banco

	@PostConstruct
	public void inicializar() {
		listaProcessoIndicador = new ArrayList<>();
		construirLista();
	}

	private void construirLista() {
		List<Processo> lp = daoProcesso.listaComStatus(Processo.class);
		List<CategoriaIndicador> lci = daoCategoriaIndicador.listaComStatus(CategoriaIndicador.class);

		for (Processo p : lp) {
//			for (CategoriaIndicador ci : lci) {
				ProcessoIndicador pi = new ProcessoIndicador();
//				pi.setCategoriaIndicador(ci);
				pi.setProcesso(p);
				pi.setStatus(true);
				listaProcessoIndicador.add(pi);
//			}
		}
	}

	public List<ProcessoIndicador> getListaProcessoIndicador() {
		return listaProcessoIndicador;
	}

	public void setListaProcessoIndicador(List<ProcessoIndicador> listaProcessoIndicador) {
		this.listaProcessoIndicador = listaProcessoIndicador;
	}
	
	

}
