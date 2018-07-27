package util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import base.modelo.CategoriaIndicador;
import base.modelo.GrupoLancamento;
import base.modelo.Indicador;
import base.modelo.ItensLancamento;
import base.modelo.Lancamento;
import base.service.IndicadorService;
import base.service.ItensLancamentoService;
import base.service.LancamentoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;
import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@Named
public class FuncoesMatematicas implements Serializable {

	private static final long serialVersionUID = 1L;

	private Lancamento lancamento;
	private List<Lancamento> lancamentoBusca;
	private List<Lancamento> listLancamento;
	private List<ItensLancamento> listItensLancamento;
	private GrupoLancamento grupoLancamentoSelecionado;
	private ItensLancamento itensLancamento;

	private List<Indicador> listaIndicadores;

	@Inject
	private GenericDAO<Lancamento> daoLancamento; // faz as buscas

	@Inject
	private GenericDAO<GrupoLancamento> daoGrupoLancamentos; // faz as buscas

	@Inject
	private GenericDAO<ItensLancamento> daoItensLancamento; // faz as buscas

	@Inject
	private LancamentoService lancamentoService; // inserir no banco

	@Inject
	private ItensLancamentoService itensLancamentoService; // inserir no banco

	@Inject
	private GenericDAO<Indicador> daoIndicador; // faz as buscas

	@PostConstruct
	public void inicializar() {

		lancamento = new Lancamento();
		itensLancamento = new ItensLancamento();
		listLancamento = new ArrayList<>();
		listLancamento = daoLancamento.listaComStatus(Lancamento.class);
		lancamentoBusca = new ArrayList<>();
		listItensLancamento = new ArrayList<>();
		listaIndicadores = daoIndicador.listar(Indicador.class, "utilizarAnalise is true");
	}

	public List<Indicador> calcularTodosIndicadores(Date dataInicial, Date dataFinal) {
	
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal);
		listaIndicadores = calcularValorFinalIndicador();
		
		return listaIndicadores;
	}

	private List<Indicador> calcularValorGruposLancamentos(Date dataInicial, Date dataFinal) {
		for (Indicador in : listaIndicadores) {
			Double valor = 0.;
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String foo = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(), dataInicial, dataFinal);
			System.out.println("FooGL: " + foo);
			try {
				if (engine.eval(foo) != null) {
					valor = Double.parseDouble(engine.eval(foo).toString());
				}

			} catch (Exception e) {
				System.out.println("erro na equação");

			}
			in.setValorCalculoGrupoLancamento(valor);
			in.setValorFinal(valor);
		}
		return listaIndicadores;
	}

	private List<Indicador> calcularValorFinalIndicador() {
		for (Indicador in : listaIndicadores) {
			Double valor = 0.;
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String foo = montarExpressaoIndicador(in.getFormulaIndicador());
			System.out.println("FooInd: " + foo);
			try {
				if (engine.eval(foo) != null) {
					valor = Double.parseDouble(engine.eval(foo).toString());
				}

			} catch (Exception e) {
				System.out.println("erro na equação");

			}
			in.setValorFinal(valor+in.getValorFinal());
		}
		return listaIndicadores;
	}

	private Double buscarValorItensLancamento(Long idGrupoLancamento, Date dataInicial, Date dataFinal) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("DATA Formatada: " + format.format(dataInicial));
		listItensLancamento = new ArrayList<>();
		listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
				"grupoLancamento.id=" + idGrupoLancamento + " and dataLancamento between '" + format.format(dataInicial)
						+ "' and '" + format.format(dataFinal) + "'");
		Double valor = 0.;
		for (ItensLancamento it : listItensLancamento) {
			valor += it.getValor();
		}
		return valor;
	}

	private Double buscarValorIndicador(Long idIndicador) {
		Double valor = 0.;
		for (Indicador i : listaIndicadores) {
			if (i.getId()==idIndicador) {
				System.out.println("No somar:::-----: "+i.getValorFinal());
				valor += i.getValorFinal();
			}
		}
		return valor;
	}

	private int buscarQuantidadeItensLancamento(Long idGrupoLancamento, Date dataInicial, Date dataFinal) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		listItensLancamento = new ArrayList<>();
		listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
				"grupoLancamento.id=" + idGrupoLancamento + " and dataLancamento between '" + format.format(dataInicial)
						+ "' and '" + format.format(dataFinal) + "'");

		return listItensLancamento.size();
	}

	private String montarExpressaoIndicador(String formula) {

		if (formula.trim().equals("")) {
			return "";
		}
		// Cada elemento ficará em uma posição, dá para FAZER VALIDAÇÕES
		String[] result = formula.split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])");
		System.out.println(Arrays.toString(result));
		String equacao = "";
		for (int x = 0; x < result.length; x++) {
			String posicao = result[x];
			if (posicao.equals("somar")) {
				equacao += String
						.valueOf(buscarValorIndicador(Long.parseLong(result[x + 2])));
				x = x + 3;
			} else if (posicao.equals("contar")) {
				equacao += String.valueOf(buscarValorIndicador(Long.parseLong(result[x + 2])));
				x = x + 3;
			} else {
				equacao += posicao;
			}
		}

		return equacao;
	}

	private String montarExpressaoGrupoLancamentos(String formula, Date dataInicial, Date dataFinal) {

		if (formula.trim().equals("")) {
			return "";
		}
		// Cada elemento ficará em uma posição, dá para FAZER VALIDAÇÕES
		String[] result = formula.split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])");
		System.out.println(Arrays.toString(result));
		String equacao = "";
		for (int x = 0; x < result.length; x++) {
			String posicao = result[x];
			if (posicao.equals("somar")) {
				equacao += String
						.valueOf(buscarValorItensLancamento(Long.parseLong(result[x + 2]), dataInicial, dataFinal));
				x = x + 3;
			} else if (posicao.equals("contar")) {
				equacao += String.valueOf(
						buscarQuantidadeItensLancamento(Long.parseLong(result[x + 2]), dataInicial, dataFinal));
				x = x + 3;
			} else {
				equacao += posicao;
			}
		}

		return equacao;
	}

	private Double buscaValorIndicador(Long id) {
		Double valor = 0.;
		for (ItensLancamento it : listItensLancamento) {
			if (it.getIndicador().getId().equals(id)) {
				valor = it.getValor();
			}
		}
		return valor;
	}

	private GrupoLancamento retornaGrupoLancamentoDescricao(String descricao) {
		GrupoLancamento grupo = null;
		List<GrupoLancamento> listaGrupol = daoGrupoLancamentos.listar(GrupoLancamento.class,
				"descricao='" + descricao.trim() + "'");
		if (listaGrupol.size() > 0) {
			grupo = listaGrupol.get(0);
		}

		return grupo;
	}

	private void buscarIndicadores(Long id, Date dataInicial, Date dataFinal) {

		listItensLancamento = new ArrayList<>();
		if (lancamento.getCategoriaIndicador() != null && lancamento.getProcesso() != null) {
			if (lancamento.getId() == null) {
				System.out.println("CAT: " + lancamento.getCategoriaIndicador().getId());
				List<Indicador> indicadores = daoIndicador.listar(Indicador.class,
						"categoriaIndicador.id=" + lancamento.getCategoriaIndicador().getId());
				for (Indicador ind : indicadores) {
					System.out.println("FOR");
					ItensLancamento it = new ItensLancamento();
					it.setDataLancamento(new Date());
					it.setIndicador(ind);
					it.setLancamento(lancamento);
					it.setStatus(true);
					listItensLancamento.add(it);
				}
			} else {
				System.out.println("Buscando os Itens do Lançamento");
				listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
						"lancamento.id=" + lancamento.getId());
			}
		}
	}

	private void carregarLista() {
		listLancamento = daoLancamento.listaComStatus(Lancamento.class);
	}

	private void carregarListaItens() {
		listItensLancamento = daoItensLancamento.listaComStatus(ItensLancamento.class);
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public List<Lancamento> getLancamentoBusca() {
		return lancamentoBusca;
	}

	public void setLancamentoBusca(List<Lancamento> lancamentoBusca) {
		this.lancamentoBusca = lancamentoBusca;
	}

	public List<Lancamento> getListLancamento() {
		return listLancamento;
	}

	public void setListLancamento(List<Lancamento> listLancamento) {
		this.listLancamento = listLancamento;
	}

	public List<ItensLancamento> getListItensLancamento() {
		return listItensLancamento;
	}

	public void setListItensLancamento(List<ItensLancamento> listItensLancamento) {
		this.listItensLancamento = listItensLancamento;
	}

	public GrupoLancamento getGrupoLancamentoSelecionado() {
		return grupoLancamentoSelecionado;
	}

	public void setGrupoLancamentoSelecionado(GrupoLancamento grupoLancamentoSelecionado) {
		this.grupoLancamentoSelecionado = grupoLancamentoSelecionado;
	}

	public ItensLancamento getItensLancamento() {
		return itensLancamento;
	}

	public void setItensLancamento(ItensLancamento itensLancamento) {
		this.itensLancamento = itensLancamento;
	}

}
