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
import base.modelo.GrupoLancamento;
import base.modelo.Indicador;
import base.modelo.Processo;
import base.modelo.Smart;
import base.service.IndicadorService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidarFormula;
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
	private GrupoLancamento grupoLancamentoFormula;
	private Indicador indicadorFormula;
	String naoEe = "";
	String mensagemNaoEe = "";

	private String expressao;

	private List<Indicador> indicadoresExpressao = new ArrayList<>();

	private Smart smart = new Smart();

	@Inject
	private GenericDAO<Indicador> daoIndicador; // faz as buscas

	@Inject
	private IndicadorService indicadorService; // inserir no banco

	private List<Processo> listProcesso;

	private int posicaoInserirFormulaGrupoLancamento = 0;
	private int posicaoInserirFormulaIndicador = 0;

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

	public void onSelect() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String selectedText = (String) map.get("selectedText");
		System.out.println("Texto selecionado: " + selectedText);
	}

	public void onCursorSelecionado() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		if (map.containsKey("posicaoCursorGrupoLancamento")) {
			posicaoInserirFormulaGrupoLancamento = Integer
					.parseInt(((String) map.get("posicaoCursorGrupoLancamento")).trim());
			posicaoInserirFormulaIndicador = 0;
		}
		if (map.containsKey("posicaoCursorIndicador")) {
			posicaoInserirFormulaIndicador = Integer.parseInt(((String) map.get("posicaoCursorIndicador")).trim());
			posicaoInserirFormulaGrupoLancamento = 0;
		}
	}

	public void selecionarIndicador() {
		if (indicadorFormula != null) {
			if (posicaoInserirFormulaIndicador == 0) {
				posicaoInserirFormulaIndicador = indicador.getFormulaIndicador().length();
				System.out.println(posicaoInserirFormulaIndicador);
			}

			StringBuilder stringBuilder = new StringBuilder(indicador.getFormulaIndicador());
			stringBuilder.insert(posicaoInserirFormulaIndicador, "" + indicadorFormula.getId());

			indicador.setFormulaIndicador(stringBuilder.toString());
		}

		RequestContext.getCurrentInstance().update("frmModalSalvar");
	}

	public void selecionarGrupoLancamento() {
		if (grupoLancamentoFormula != null) {
			if (posicaoInserirFormulaGrupoLancamento == 0) {
				posicaoInserirFormulaGrupoLancamento = indicador.getFormulaGrupoLancamento().length();
			}

			StringBuilder stringBuilder = new StringBuilder(indicador.getFormulaGrupoLancamento());
			stringBuilder.insert(posicaoInserirFormulaGrupoLancamento, "" + grupoLancamentoFormula.getId());

			indicador.setFormulaGrupoLancamento(stringBuilder.toString());
		}
		RequestContext.getCurrentInstance().update("frmModalSalvar");
	}

	public void preencherLista(Indicador t) {
		this.indicador = t;

	}

	public void verificarSmart() {
		naoEe = "<ol>";
		mensagemNaoEe = "";
		if (!indicador.isAtingivel()) {
			naoEe += " <li> Não é Atingível </li>";
		}
		if (!indicador.isEspecifico()) {
			naoEe += " <li> Não é Específico </li>";
		}
		if (!indicador.isMensuravel()) {
			naoEe += " <li> Não é Mensurável </li>";
		}
		if (!indicador.isRelevante()) {
			naoEe += " <li> Não é Relevante </li>";
		}
		if (!indicador.isTemporizavel()) {
			naoEe += " <li> Não e Temporizável </li>";
		}
		naoEe += " </ol>";
		// naoEe = naoEe.trim().replace(" ", "<br/>");

		if (naoEe.length() < 15) {
			mensagemNaoEe = "Recomendamos fortemente incluí-lo na Avaliação de Desempenho, o que vocâ acha??";
			naoEe = "<ol>";
			naoEe += " <li> É AtingÍvel </li>";
			naoEe += " <li> É Específico </li>";
			naoEe += " <li> É Mensurável </li>";
			naoEe += " <li> É Relevante </li>";
			naoEe += " <li> É Temporizável </li>";
			naoEe += " </ol>";
		} else {
			mensagemNaoEe = "Recomendamos <b>NÃO</b> incluí-lo na Avaliação de Desempenho, o que você acha??";
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
			context.addMessage(null, new FacesMessage("Pronto", "Indicador Selecionado para Análise!!"));
		}
	}

	public void removerIndicador(Indicador ind) {
		indicador = ind;
		indicador.setUtilizarAnalise(false);
		indicadorService.inserirAlterar(indicador);
		criarNovoObjeto();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Pronto", "Indicador Removido da Análise!!"));
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
		boolean cadastrar = true;
		if (!ValidarFormula.validar(indicador.getFormulaGrupoLancamento())) {
			ExibirMensagem.exibirMensagem("Erro na fórmula de Grupos de Lançamentos");
			cadastrar= false;
		}else if (!ValidarFormula.validar(indicador.getFormulaIndicador())) {
			ExibirMensagem.exibirMensagem("Erro na fórmula de Indicadores");			
			cadastrar= false;
		}
		if(cadastrar) {
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
		listIndicadorTransferencia = daoIndicador.listar(Indicador.class, "processo.descricao='Transferência'");
		listIndicadorArmazenagem = daoIndicador.listar(Indicador.class, "processo.descricao='Armazenagem'");
		listIndicadorManutencao = daoIndicador.listar(Indicador.class, "processo.descricao='Manutenção'");
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

	public Indicador getIndicadorFormula() {
		return indicadorFormula;
	}

	public void setIndicadorFormula(Indicador indicadorFormula) {
		this.indicadorFormula = indicadorFormula;
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

	public List<Indicador> getIndicadoresExpressao() {
		return indicadoresExpressao;
	}

	public void setIndicadoresExpressao(List<Indicador> indicadoresExpressao) {
		this.indicadoresExpressao = indicadoresExpressao;
	}

	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

	public GrupoLancamento getGrupoLancamentoFormula() {
		return grupoLancamentoFormula;
	}

	public void setGrupoLancamentoFormula(GrupoLancamento grupoLancamentoFormula) {
		this.grupoLancamentoFormula = grupoLancamentoFormula;
	}

}
