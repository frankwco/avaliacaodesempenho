package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.CategoriaIndicador;
import base.modelo.DescricaoDespesa;
import base.service.DescricaoDespesaService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("descricaoDespesaMB")
public class DescricaoDespesaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private DescricaoDespesa descricaoDespesa;
	private List<DescricaoDespesa> descricaoDespesaBusca;
	private List<DescricaoDespesa> listDescricaoDespesa;

	@Inject
	private GenericDAO<DescricaoDespesa> daoDescricaoDespesa; // faz as buscas

	@Inject
	private DescricaoDespesaService descricaoDespesaService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		descricaoDespesa = new DescricaoDespesa();

		listDescricaoDespesa = new ArrayList<>();
		listDescricaoDespesa = daoDescricaoDespesa.listaComStatus(DescricaoDespesa.class);
		descricaoDespesaBusca = new ArrayList<>();

	}

	public void preencherLista(DescricaoDespesa t) {
		this.descricaoDespesa = t;

	}

	public void inativar(DescricaoDespesa t) {
		t.setStatus(false);
		descricaoDespesaService.inserirAlterar(t);
		// descricaoDespesaService.update(" DescricaoDespesa set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (descricaoDespesa.getId() == null) {
				descricaoDespesa.setStatus(true);
				descricaoDespesaService.inserirAlterar(descricaoDespesa);

			} else {
				descricaoDespesa.setStatus(true);
				descricaoDespesaService.inserirAlterar(descricaoDespesa);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogDescricaoDespesa();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<DescricaoDespesa> completarDescricaoDespesa(String str) {
		List<DescricaoDespesa> list = daoDescricaoDespesa.listaComStatus(DescricaoDespesa.class);
		List<DescricaoDespesa> selecionados = new ArrayList<>();
		for (DescricaoDespesa cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		descricaoDespesa = new DescricaoDespesa();
	}

	public void carregarLista() {
		listDescricaoDespesa = daoDescricaoDespesa.listaComStatus(DescricaoDespesa.class);
	}

	public DescricaoDespesa getDescricaoDespesa() {
		return descricaoDespesa;
	}

	public void setDescricaoDespesa(DescricaoDespesa descricaoDespesa) {
		this.descricaoDespesa = descricaoDespesa;
	}

	public List<DescricaoDespesa> getDescricaoDespesaBusca() {
		return descricaoDespesaBusca;
	}

	public void setDescricaoDespesaBusca(List<DescricaoDespesa> descricaoDespesaBusca) {
		this.descricaoDespesaBusca = descricaoDespesaBusca;
	}

	public List<DescricaoDespesa> getListDescricaoDespesa() {
		return listDescricaoDespesa;
	}

	public void setListDescricaoDespesa(List<DescricaoDespesa> listDescricaoDespesa) {
		this.listDescricaoDespesa = listDescricaoDespesa;
	}

	
}
