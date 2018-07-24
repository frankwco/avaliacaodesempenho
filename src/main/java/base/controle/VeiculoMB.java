package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Salario;
import base.modelo.Veiculo;
import base.service.VeiculoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("veiculoMB")
public class VeiculoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Veiculo veiculo;
	private List<Veiculo> veiculoBusca;
	private List<Veiculo> listVeiculo;

	@Inject
	private GenericDAO<Veiculo> daoVeiculo; // faz as buscas

	@Inject
	private VeiculoService veiculoService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		veiculo = new Veiculo();

		listVeiculo = new ArrayList<>();
		listVeiculo = daoVeiculo.listaComStatus(Veiculo.class);
		veiculoBusca = new ArrayList<>();

	}

	public void preencherLista(Veiculo t) {
		this.veiculo = t;

	}

	public void inativar(Veiculo t) {
		t.setStatus(false);
		veiculoService.inserirAlterar(t);
		// veiculoService.update(" Veiculo set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (veiculo.getId() == null) {
				veiculo.setStatus(true);
				veiculoService.inserirAlterar(veiculo);

			} else {
				veiculo.setStatus(true);
				veiculoService.inserirAlterar(veiculo);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogVeiculo();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<Veiculo> completarVeiculo(String str) {
		List<Veiculo> list = daoVeiculo.listaComStatus(Veiculo.class);
		List<Veiculo> selecionados = new ArrayList<>();
		for (Veiculo cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		veiculo = new Veiculo();
	}

	public void carregarLista() {
		listVeiculo = daoVeiculo.listaComStatus(Veiculo.class);
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Veiculo> getVeiculoBusca() {
		return veiculoBusca;
	}

	public void setVeiculoBusca(List<Veiculo> veiculoBusca) {
		this.veiculoBusca = veiculoBusca;
	}

	public List<Veiculo> getListVeiculo() {
		return listVeiculo;
	}

	public void setListVeiculo(List<Veiculo> listVeiculo) {
		this.listVeiculo = listVeiculo;
	}

	
}
