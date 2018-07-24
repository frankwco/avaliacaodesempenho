package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.context.RequestContext;

import base.modelo.Despesa;
import base.modelo.Indicador;
import base.modelo.Processo;
import base.modelo.Smart;
import base.service.IndicadorService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("indicadorMB")
public class IndicadorMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Indicador indicador;
	private List<Indicador> indicadorBusca;
	private List<Indicador> listIndicador;
	private List<Indicador> listIndicadorTransferencia;
	private List<Indicador> listIndicadorArmazenagem;
	private List<Indicador> listIndicadorColeta;
	private List<Indicador> listIndicadorManutencao;
	private Indicador indicadorSelecionadoExpressao;
	String naoEe = "";
	String mensagemNaoEe = "";

	private Smart smart = new Smart();

	@Inject
	private GenericDAO<Indicador> daoIndicador; // faz as buscas

	@Inject
	private IndicadorService indicadorService; // inserir no banco

	private List<Processo> listProcesso;

	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas

	@PostConstruct
	public void inicializar() {

		indicador = new Indicador();

		listIndicador = new ArrayList<>();
		listIndicador = daoIndicador.listaComStatus(Indicador.class);
		indicadorBusca = new ArrayList<>();
		carregarLista();

		listProcesso = new ArrayList<>();
		listProcesso = daoProcesso.listaComStatus(Processo.class);
	}

	public void selecionarIndicador() {
		// ($1:@Valor com Ped�gio;+$2:@Gastos com Combust�vel;+3)/2
		if (indicador.getParametros().trim().equals("+")) {
			indicador.setParametros("");
		}
		indicador.setParametros(indicador.getParametros() + "$" + indicadorSelecionadoExpressao.getId() + ":@"
				+ indicadorSelecionadoExpressao.getDescricao() + ";");

		RequestContext.getCurrentInstance().update("frmModalSalvar");
	}

	public void preencherLista(Indicador t) {
		this.indicador = t;

	}

	public void verificarSmart() {
		naoEe = "<ol>";
		mensagemNaoEe = "";
		if (!smart.isAtingivel()) {
			naoEe += " <li> N�o � Ating�vel </li>";
		}
		if (!smart.isEspecifico()) {
			naoEe += " <li> N�o � Espec�fico </li>";
		}
		if (!smart.isMensuravel()) {
			naoEe += " <li> N�o � Mensur�vel </li>";
		}
		if (!smart.isRelevante()) {
			naoEe += " <li> N�o � Relevante </li>";
		}
		if (!smart.isTemporizavel()) {
			naoEe += " <li> N�o � Temporiz�vel </li>";
		}
		naoEe += " </ol>";
		// naoEe = naoEe.trim().replace(" ", "<br/>");

		if (naoEe.length() < 15) {
			mensagemNaoEe = "Recomendamos fortemente inclu�-lo na Avalia��o de Desempenho, o que voc� acha??";
			naoEe = "<ol>";
			naoEe += " <li> � Ating�vel </li>";
			naoEe += " <li> � Espec�fico </li>";
			naoEe += " <li> � Mensur�vel </li>";
			naoEe += " <li> � Relevante </li>";
			naoEe += " <li> � Temporiz�vel </li>";
			naoEe += " </ol>";
		} else {
			mensagemNaoEe = "Recomendamos <b>N�O</b> inclu�-lo na Avalia��o de Desempenho, o que voc� acha??";
		}
		// O indicador ** foi classificado como naoE, deseja inclu�-lo na
		// avalia��o de desempenho??
		// RequestContext.getCurrentInstance().update("frmDlgIncluirIndicador");
		// RequestContext.getCurrentInstance().execute("PF('dlgIncluirIndicador').show();");
		// RequestContext.getCurrentInstance().update("frmDlgIncluirIndicador");
		// PrimeFaces.current().dialog().openDynamic("dlgIncluirIndicador");
		PrimeFaces.current().executeScript("PF('dlgIncluirIndicador').show();");
		// Map<String,Object> options = new HashMap<String, Object>();
		// options.put("modal", true);
		// options.put("width", 640);
		// options.put("height", 340);
		// options.put("contentWidth", "100%");
		// options.put("contentHeight", "100%");
		// options.put("headerElement", "customheader");
		// System.out.println("Antes de chamar o diuaglo");
		// PrimeFaces.current().dialog().openDynamic("confirmarIndicador",
		// options, null);
		// System.out.println("Depois de chamar o diuaglo");

	}

	public void confirmarInclusaoIndicador(String confirmar) {
		if (confirmar.equals("sim")) {
			indicador.setUtilizarAnalise(true);
			indicadorService.inserirAlterar(indicador);
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage("Pronto",  "Indicador Selecionado para An�lise!!") );
		}
	}

	public void removerIndicador(Indicador ind) {
		indicador = ind;
		indicador.setUtilizarAnalise(false);
		indicadorService.inserirAlterar(indicador);
		criarNovoObjeto();
		FacesContext context = FacesContext.getCurrentInstance();	         
        context.addMessage(null, new FacesMessage("Pronto",  "Indicador Removido da An�lise!!") );
	}

	public void inativar(Indicador t) {
		t.setStatus(false);
		indicadorService.inserirAlterar(t);
		// indicadorService.update(" Indicador set status = false where id = " +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();

	}

	public void salvar() {
		if (indicador.getParametros().trim().equals("")) {
			indicador.setParametros("+");
		}
		try {
			if (indicador.getId() == null) {
				indicador.setStatus(true);
				indicadorService.inserirAlterar(indicador);

			} else {
				indicador.setStatus(true);
				indicadorService.inserirAlterar(indicador);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogIndicador();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public List<Indicador> completarIndicador(String str) {
		List<Indicador> list = daoIndicador.listaComStatus(Indicador.class);
		List<Indicador> selecionados = new ArrayList<>();
		for (Indicador cur : list) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				selecionados.add(cur);
			}
		}
		return selecionados;
	}

	public void criarNovoObjeto() {
		indicador = new Indicador();
	}

	public void carregarLista() {
		listIndicador = daoIndicador.listaComStatus(Indicador.class);
		listIndicadorTransferencia = daoIndicador.listar(Indicador.class, "processo.descricao='Transfer�ncia'");
		listIndicadorArmazenagem = daoIndicador.listar(Indicador.class, "processo.descricao='Armazenagem'");
		listIndicadorManutencao = daoIndicador.listar(Indicador.class, "processo.descricao='Manuten��o'");
		listIndicadorColeta = daoIndicador.listar(Indicador.class, "processo.descricao='Coleta'");

	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public List<Indicador> getIndicadorBusca() {
		return indicadorBusca;
	}

	public void setIndicadorBusca(List<Indicador> indicadorBusca) {
		this.indicadorBusca = indicadorBusca;
	}

	public List<Indicador> getListIndicador() {
		return listIndicador;
	}

	public void setListIndicador(List<Indicador> listIndicador) {
		this.listIndicador = listIndicador;
	}

	public Indicador getIndicadorSelecionadoExpressao() {
		return indicadorSelecionadoExpressao;
	}

	public void setIndicadorSelecionadoExpressao(Indicador indicadorSelecionadoExpressao) {
		this.indicadorSelecionadoExpressao = indicadorSelecionadoExpressao;
	}

	public List<Indicador> getListIndicadorTransferencia() {
		return listIndicadorTransferencia;
	}

	public void setListIndicadorTransferencia(List<Indicador> listIndicadorTransferencia) {
		this.listIndicadorTransferencia = listIndicadorTransferencia;
	}

	public List<Indicador> getListIndicadorArmazenagem() {
		return listIndicadorArmazenagem;
	}

	public void setListIndicadorArmazenagem(List<Indicador> listIndicadorArmazenagem) {
		this.listIndicadorArmazenagem = listIndicadorArmazenagem;
	}

	public List<Indicador> getListIndicadorColeta() {
		return listIndicadorColeta;
	}

	public void setListIndicadorColeta(List<Indicador> listIndicadorColeta) {
		this.listIndicadorColeta = listIndicadorColeta;
	}

	public List<Indicador> getListIndicadorManutencao() {
		return listIndicadorManutencao;
	}

	public void setListIndicadorManutencao(List<Indicador> listIndicadorManutencao) {
		this.listIndicadorManutencao = listIndicadorManutencao;
	}

	public Smart getSmart() {
		return smart;
	}

	public void setSmart(Smart smart) {
		this.smart = smart;
	}

	public String getNaoEe() {
		return naoEe;
	}

	public void setNaoEe(String naoEe) {
		this.naoEe = naoEe;
	}

	public List<Processo> getListProcesso() {
		return listProcesso;
	}

	public void setListProcesso(List<Processo> listProcesso) {
		this.listProcesso = listProcesso;
	}

	public String getMensagemNaoEe() {
		return mensagemNaoEe;
	}

	public void setMensagemNaoEe(String mensagemNaoEe) {
		this.mensagemNaoEe = mensagemNaoEe;
	}

}
