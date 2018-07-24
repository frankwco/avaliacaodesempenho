package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.DescricaoDespesa;
import base.modelo.Despesa;
import base.service.DespesaService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("despesaMB")
public class DespesaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Despesa despesa;
	private List<Despesa> descricaoDespesaBusca;
	private List<Despesa> listDespesa;

	@Inject
	private GenericDAO<Despesa> daoDespesa; // faz as buscas

	@Inject
	private DespesaService despesaService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		despesa = new Despesa();

		listDespesa = new ArrayList<>();
		listDespesa = daoDespesa.listaComStatus(Despesa.class);
		descricaoDespesaBusca = new ArrayList<>();

	}

	public void preencherLista(Despesa t) {
		this.despesa = t;

	}

	public void inativar(Despesa t) {
		t.setStatus(false);
		despesaService.inserirAlterar(t);
		// despesaService.update(" Despesa set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (despesa.getId() == null) {
				despesa.setStatus(true);
				despesaService.inserirAlterar(despesa);

			} else {
				despesa.setStatus(true);
				despesaService.inserirAlterar(despesa);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogDespesa();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<Despesa> completarDespesa(String str) {
		List<Despesa> list = daoDespesa.listaComStatus(Despesa.class);
		List<Despesa> selecionados = new ArrayList<>();
		for (Despesa cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		despesa = new Despesa();
	}

	public void carregarLista() {
		listDespesa = daoDespesa.listaComStatus(Despesa.class);
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public List<Despesa> getDescricaoDespesaBusca() {
		return descricaoDespesaBusca;
	}

	public void setDescricaoDespesaBusca(List<Despesa> descricaoDespesaBusca) {
		this.descricaoDespesaBusca = descricaoDespesaBusca;
	}

	public List<Despesa> getListDespesa() {
		return listDespesa;
	}

	public void setListDespesa(List<Despesa> listDespesa) {
		this.listDespesa = listDespesa;
	}

	
}
