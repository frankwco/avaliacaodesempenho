package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Pessoa;
import base.modelo.Salario;
import base.service.SalarioService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("salarioMB")
public class SalarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Salario salario;
	private List<Salario> salarioBusca;
	private List<Salario> listSalario;

	@Inject
	private GenericDAO<Salario> daoSalario; // faz as buscas

	@Inject
	private SalarioService salarioService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		salario = new Salario();

		listSalario = new ArrayList<>();
		listSalario = daoSalario.listaComStatus(Salario.class);
		salarioBusca = new ArrayList<>();

	}

	public void preencherLista(Salario t) {
		this.salario = t;

	}

	public void inativar(Salario t) {
		t.setStatus(false);
		salarioService.inserirAlterar(t);
		// salarioService.update(" Salario set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (salario.getId() == null) {
				salario.setStatus(true);
				salarioService.inserirAlterar(salario);

			} else {
				salario.setStatus(true);
				salarioService.inserirAlterar(salario);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogSalario();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<Salario> completarSalario(String str) {
		List<Salario> list = daoSalario.listaComStatus(Salario.class);
		List<Salario> selecionados = new ArrayList<>();
		for (Salario cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		salario = new Salario();
	}

	public void carregarLista() {
		listSalario = daoSalario.listaComStatus(Salario.class);
	}

	public Salario getSalario() {
		return salario;
	}

	public void setSalario(Salario salario) {
		this.salario = salario;
	}

	public List<Salario> getSalarioBusca() {
		return salarioBusca;
	}

	public void setSalarioBusca(List<Salario> salarioBusca) {
		this.salarioBusca = salarioBusca;
	}

	public List<Salario> getListSalario() {
		return listSalario;
	}

	public void setListSalario(List<Salario> listSalario) {
		this.listSalario = listSalario;
	}

	
}
