package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.GrupoLancamento;
import base.service.GrupoLancamentoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("grupoLancamentoMB")
public class GrupoLancamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private GrupoLancamento grupoLancamento;
	private List<GrupoLancamento> grupoLancamentoBusca;
	private List<GrupoLancamento> listGrupoLancamento;

	@Inject
	private GenericDAO<GrupoLancamento> daoGrupoLancamento; // faz as buscas

	@Inject
	private GrupoLancamentoService grupoLancamentoService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		grupoLancamento = new GrupoLancamento();

		listGrupoLancamento = new ArrayList<>();
		listGrupoLancamento = daoGrupoLancamento.listaComStatus(GrupoLancamento.class);
		grupoLancamentoBusca = new ArrayList<>();

	}

	public void preencherLista(GrupoLancamento t) {
		this.grupoLancamento = t;

	}

	public void inativar(GrupoLancamento t) {
		t.setStatus(false);
		grupoLancamentoService.inserirAlterar(t);
		// grupoLancamentoService.update(" TipoLancamento set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (grupoLancamento.getId() == null) {
				grupoLancamento.setStatus(true);
				grupoLancamentoService.inserirAlterar(grupoLancamento);

			} else {
				grupoLancamento.setStatus(true);
				grupoLancamentoService.inserirAlterar(grupoLancamento);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogGrupoLancamento();;
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<GrupoLancamento> completarGrupoLancamento(String str) {
		List<GrupoLancamento> listIndic = daoGrupoLancamento.listaComStatus(GrupoLancamento.class);
		List<GrupoLancamento> matrizSelecionados = new ArrayList<>();
		for (GrupoLancamento cur : listIndic) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				matrizSelecionados.add(cur);
			}
		}
		return matrizSelecionados;
	}
	
	public List<GrupoLancamento> completarGrupoLancamentoParenteses(String str) {
		List<GrupoLancamento> listIndic = daoGrupoLancamento.listaComStatus(GrupoLancamento.class);
		List<GrupoLancamento> matrizSelecionados = new ArrayList<>();
		for (GrupoLancamento cur : listIndic) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				matrizSelecionados.add(cur);
			}
		}
		return matrizSelecionados;
	}

	public void criarNovoObjeto() {
		grupoLancamento = new GrupoLancamento();
	}

	public void carregarLista() {
		listGrupoLancamento = daoGrupoLancamento.listaComStatus(GrupoLancamento.class);
	}

	public GrupoLancamento getGrupoLancamento() {
		return grupoLancamento;
	}

	public void setGrupoLancamento(GrupoLancamento grupoLancamento) {
		this.grupoLancamento = grupoLancamento;
	}

	public List<GrupoLancamento> getGrupoLancamentoBusca() {
		return grupoLancamentoBusca;
	}

	public void setGrupoLancamentoBusca(List<GrupoLancamento> grupoLancamentoBusca) {
		this.grupoLancamentoBusca = grupoLancamentoBusca;
	}

	public List<GrupoLancamento> getListGrupoLancamento() {
		return listGrupoLancamento;
	}

	public void setListGrupoLancamento(List<GrupoLancamento> listGrupoLancamento) {
		this.listGrupoLancamento = listGrupoLancamento;
	}

	


}
