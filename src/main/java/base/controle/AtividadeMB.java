package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Pessoa;
import base.modelo.Atividade;
import base.service.AtividadeService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("atividadeMB")
public class AtividadeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Atividade atividade;
	private List<Atividade> atividadeBusca;
	private List<Atividade> listAtividade;
	
	@Inject
	private GenericDAO<Atividade> daoProcesso; // faz as buscas

	@Inject
	private AtividadeService atividadeService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		atividade = new Atividade();

		listAtividade = new ArrayList<>();
		listAtividade = daoProcesso.listaComStatus(Atividade.class);
		atividadeBusca = new ArrayList<>();

	}

	public void preencherLista(Atividade t) {
		this.atividade = t;

	}

	public void inativar(Atividade t) {
		t.setStatus(false);
		atividadeService.inserirAlterar(t);
		// atividadeService.update(" Atividade set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (atividade.getId() == null) {
				atividade.setStatus(true);
				atividadeService.inserirAlterar(atividade);

			} else {
				atividade.setStatus(true);
				atividadeService.inserirAlterar(atividade);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogAtividade();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}
	
	public List<Atividade> completarAtividade(String str) {
		List<Atividade> list = daoProcesso.listaComStatus(Atividade.class);
		List<Atividade> selecionados = new ArrayList<>();
		for (Atividade cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		atividade = new Atividade();
	}

	public void carregarLista() {
		listAtividade = daoProcesso.listaComStatus(Atividade.class);
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public List<Atividade> getAtividadeBusca() {
		return atividadeBusca;
	}

	public void setAtividadeBusca(List<Atividade> atividadeBusca) {
		this.atividadeBusca = atividadeBusca;
	}

	public List<Atividade> getListAtividade() {
		return listAtividade;
	}

	public void setListAtividade(List<Atividade> listAtividade) {
		this.listAtividade = listAtividade;
	}

	
	
}
