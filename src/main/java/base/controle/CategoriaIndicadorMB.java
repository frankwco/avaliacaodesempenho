package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.CategoriaIndicador;
import base.service.CategoriaIndicadorService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("categoriaIndicadorMB")
public class CategoriaIndicadorMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private CategoriaIndicador categoriaIndicador;
	private List<CategoriaIndicador> categoriaIndicadorBusca;
	private List<CategoriaIndicador> listCategoriaIndicador;

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicador; // faz as buscas

	@Inject
	private CategoriaIndicadorService categoriaIndicadorService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		categoriaIndicador = new CategoriaIndicador();

		listCategoriaIndicador = new ArrayList<>();
		listCategoriaIndicador = daoCategoriaIndicador.listaComStatus(CategoriaIndicador.class);
		categoriaIndicadorBusca = new ArrayList<>();

	}

	public void preencherLista(CategoriaIndicador t) {
		this.categoriaIndicador = t;

	}

	public void inativar(CategoriaIndicador t) {
		t.setStatus(false);
		categoriaIndicadorService.inserirAlterar(t);
		// categoriaIndicadorService.update(" CategoriaIndicador set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (categoriaIndicador.getId() == null) {
				categoriaIndicador.setStatus(true);
				categoriaIndicadorService.inserirAlterar(categoriaIndicador);

			} else {
				categoriaIndicador.setStatus(true);
				categoriaIndicadorService.inserirAlterar(categoriaIndicador);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCategoriaIndicador();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<CategoriaIndicador> completarCategoriaIndicador(String str) {
		List<CategoriaIndicador> listIndic = daoCategoriaIndicador.listaComStatus(CategoriaIndicador.class);
		List<CategoriaIndicador> matrizSelecionados = new ArrayList<>();
		for (CategoriaIndicador cur : listIndic) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				matrizSelecionados.add(cur);
			}
		}
		return matrizSelecionados;
	}

	public void criarNovoObjeto() {
		categoriaIndicador = new CategoriaIndicador();
	}

	public void carregarLista() {
		listCategoriaIndicador = daoCategoriaIndicador.listaComStatus(CategoriaIndicador.class);
	}

	public CategoriaIndicador getCategoriaIndicador() {
		return categoriaIndicador;
	}

	public void setCategoriaIndicador(CategoriaIndicador categoriaIndicador) {
		this.categoriaIndicador = categoriaIndicador;
	}

	public List<CategoriaIndicador> getCategoriaIndicadorBusca() {
		return categoriaIndicadorBusca;
	}

	public void setCategoriaIndicadorBusca(List<CategoriaIndicador> categoriaIndicadorBusca) {
		this.categoriaIndicadorBusca = categoriaIndicadorBusca;
	}

	public List<CategoriaIndicador> getListCategoriaIndicador() {
		return listCategoriaIndicador;
	}

	public void setListCategoriaIndicador(List<CategoriaIndicador> listCategoriaIndicador) {
		this.listCategoriaIndicador = listCategoriaIndicador;
	}


}
