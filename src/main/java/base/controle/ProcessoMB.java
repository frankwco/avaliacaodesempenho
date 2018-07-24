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
import base.modelo.Processo;
import base.service.ProcessoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("processoMB")
public class ProcessoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Processo processo;
	private List<Processo> processoBusca;
	private List<Processo> listProcesso;

	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas

	@Inject
	private ProcessoService processoService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		processo = new Processo();

		listProcesso = new ArrayList<>();
		listProcesso = daoProcesso.listaComStatus(Processo.class);
		processoBusca = new ArrayList<>();
		
		for(Processo p:listProcesso){
			System.out.println("-"+p.getDescricao());
			for(Indicador i:p.getIndicadores()){
				System.out.println("---"+i.getDescricao());
			}
		}

	}

	public void preencherLista(Processo t) {
		this.processo = t;

	}

	public void inativar(Processo t) {
		t.setStatus(false);
		processoService.inserirAlterar(t);
		// processoService.update(" Processo set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (processo.getId() == null) {
				processo.setStatus(true);
				processoService.inserirAlterar(processo);

			} else {
				processo.setStatus(true);
				processoService.inserirAlterar(processo);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogProcesso();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}
	
	public List<Processo> completarProcesso(String str) {
		List<Processo> list = daoProcesso.listaComStatus(Processo.class);
		List<Processo> selecionados = new ArrayList<>();
		for (Processo cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		processo = new Processo();
	}

	public void carregarLista() {
		listProcesso = daoProcesso.listaComStatus(Processo.class);
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public List<Processo> getProcessoBusca() {
		return processoBusca;
	}

	public void setProcessoBusca(List<Processo> processoBusca) {
		this.processoBusca = processoBusca;
	}

	public List<Processo> getListProcesso() {
		return listProcesso;
	}

	public void setListProcesso(List<Processo> listProcesso) {
		this.listProcesso = listProcesso;
	}

	

	


	

	
}
