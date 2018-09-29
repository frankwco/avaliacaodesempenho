package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import base.modelo.Pessoa;
import base.modelo.Ocorrencia;
import base.service.OcorrenciaService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("ocorrenciaMB")
public class OcorrenciaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Ocorrencia ocorrencia;
	private List<Ocorrencia> ocorrenciaBusca;
	private List<Ocorrencia> listOcorrencia;

	@Inject
	private GenericDAO<Ocorrencia> daoOcorrencia; // faz as buscas

	@Inject
	private OcorrenciaService ocorrenciaService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		ocorrencia = new Ocorrencia();

		listOcorrencia = new ArrayList<>();
		listOcorrencia = daoOcorrencia.listaComStatus(Ocorrencia.class);
		ocorrenciaBusca = new ArrayList<>();

	}

	public void preencherLista(Ocorrencia t) {
		this.ocorrencia = t;

	}

	public void inativar(Ocorrencia t) {
		t.setStatus(false);
		ocorrenciaService.inserirAlterar(t);
		// ocorrenciaService.update(" Ocorrencia set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (ocorrencia.getId() == null) {
				ocorrencia.setStatus(true);
				ocorrenciaService.inserirAlterar(ocorrencia);

			} else {
				ocorrencia.setStatus(true);
				ocorrenciaService.inserirAlterar(ocorrencia);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogOcorrencia();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public List<Ocorrencia> completarAtividade(String str) {
		List<Ocorrencia> list = daoOcorrencia.listaComStatus(Ocorrencia.class);
		List<Ocorrencia> selecionados = new ArrayList<>();
		for (Ocorrencia cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		ocorrencia = new Ocorrencia();
	}

	public void carregarLista() {
		listOcorrencia = daoOcorrencia.listaComStatus(Ocorrencia.class);
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	public List<Ocorrencia> getOcorrenciaBusca() {
		return ocorrenciaBusca;
	}

	public void setOcorrenciaBusca(List<Ocorrencia> ocorrenciaBusca) {
		this.ocorrenciaBusca = ocorrenciaBusca;
	}

	public List<Ocorrencia> getListOcorrencia() {
		return listOcorrencia;
	}

	public void setListOcorrencia(List<Ocorrencia> listOcorrencia) {
		this.listOcorrencia = listOcorrencia;
	}

}
