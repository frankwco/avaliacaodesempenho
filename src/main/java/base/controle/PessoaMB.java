package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Indicador;
import base.modelo.Pessoa;
import base.service.PessoaService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("pessoaMB")
public class PessoaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa;
	private List<Pessoa> pessoaBusca;
	private List<Pessoa> listPessoa;

	@Inject
	private GenericDAO<Pessoa> daoPessoa; // faz as buscas

	@Inject
	private PessoaService pessoaService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		pessoa = new Pessoa();

		listPessoa = new ArrayList<>();
		listPessoa = daoPessoa.listaComStatus(Pessoa.class);
		pessoaBusca = new ArrayList<>();

	}

	public void preencherLista(Pessoa t) {
		this.pessoa = t;

	}

	public void inativar(Pessoa t) {
		t.setStatus(false);
		pessoaService.inserirAlterar(t);
		// pessoaService.update(" Pessoa set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (pessoa.getId() == null) {
				pessoa.setStatus(true);
				pessoaService.inserirAlterar(pessoa);

			} else {
				pessoa.setStatus(true);
				pessoaService.inserirAlterar(pessoa);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogPessoa();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<Pessoa> completarPessoa(String str) {
		List<Pessoa> list = daoPessoa.listaComStatus(Pessoa.class);
		List<Pessoa> selecionados = new ArrayList<>();
		for (Pessoa cur : list) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		pessoa = new Pessoa();
	}

	public void carregarLista() {
		listPessoa = daoPessoa.listaComStatus(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoaBusca() {
		return pessoaBusca;
	}

	public void setPessoaBusca(List<Pessoa> pessoaBusca) {
		this.pessoaBusca = pessoaBusca;
	}

	public List<Pessoa> getListPessoa() {
		return listPessoa;
	}

	public void setListPessoa(List<Pessoa> listPessoa) {
		this.listPessoa = listPessoa;
	}


}
