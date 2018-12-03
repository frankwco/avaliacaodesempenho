package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;

import base.modelo.Pessoa;
import base.modelo.Processo;
import base.modelo.Atividade;
import base.modelo.CategoriaIndicador;
import base.modelo.GrupoLancamento;
import base.modelo.Indicador;
import base.modelo.ItensLancamento;
import base.modelo.Lancamento;
import base.modelo.Ocorrencia;
import base.service.AtividadeService;
import util.ConverteStringDate;
import util.ElementosCoresAvaliacao;
import util.ExibirMensagem;
import util.FecharDialog;
import util.FuncoesMatematicas;
import util.Mensagem;
import dao.GenericDAO;

//@ViewScoped
@SessionScoped
@Named("avaliacaoMB")
public class AvaliacaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private CategoriaIndicador categoriaIndicador;

	private BarChartModel graficoBarraAvaliacaoGeral;
	private CartesianChartModel combinedModel;
	private Processo[] listaProcesso;
	private Long[] processos;
	private Long[] processosComparativos;
	private String[] processosString;

	private Integer mes;
	private Integer ano;

	Double a = new Double(Double.NaN);

	private List<Indicador> listaIndicadores;
	private List<GrupoLancamento> variaveisCalculo;

	ElementosCoresAvaliacao elementosCores = new ElementosCoresAvaliacao();

	@Inject
	private GenericDAO<Lancamento> daoLancamento; // faz as buscas
	
	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas

	@Inject
	private GenericDAO<GrupoLancamento> daoGrupoLancamentos; // faz as buscas

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicadores; // faz as
																	// buscas

	@Inject
	private GenericDAO<ItensLancamento> daoItensLancamento; // faz as buscas

	@Inject
	private FuncoesMatematicas funcoesMatematicas;

	private String opacidadeCusto = "100";
	private String opacidadeQualidade = "50";
	private String opacidadeProdutividade = "50";
	private String opacidadeTempo = "50";

	private Double custoTotalTelaInicio = 0.;
	private Double porcentagemFaturamentoTelaInicial = 0.;

	String categoria = "Custo";

//	@PostConstruct
//	public void init() {
//		graficoCategoriaIndicadorData("Custo");
//		graficoCategoriaIndicadorDataProcesso("Custo");
//		graficoCategoriaIndicadorDataProcessoComparativo("Custo");
//	}

	public void buscarValoresTelaInicio(int mes) {
		System.out.println("QUal mês "+mes);
		custoTotalTelaInicio=0.;
		porcentagemFaturamentoTelaInicial=0.;
		preencherListaVariaveisCalculo();
		for (GrupoLancamento g : variaveisCalculo) {
			if (g.getDescricao().equalsIgnoreCase("custos final com terceiros e demais despesas")) {
				if (mes == 0) {
					custoTotalTelaInicio = g.getMesAtual();
				} else if (mes == 1) {
					custoTotalTelaInicio = g.getMesMenosUm();
				} else if (mes == 2) {
					custoTotalTelaInicio = g.getMesMenosDois();
				}

			}
		}
		
//		String[] process = processos.split(";");
		List<Processo> process = daoProcesso.listaComStatus(Processo.class);
		Long[] listaProcessos = new Long[process.size()];

		for (int x = 0; x < process.size(); x++) {
			listaProcessos[x] = process.get(x).getId();
		}

		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='Custo'");
		CategoriaIndicador categoriaIndicador = listIndic.get(0);
		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessosMesAno(this.mes-mes,
				ano, categoriaIndicador.getId(), listaProcessos);

		List<Indicador> liRetorno = new ArrayList<>();
		Long ind = 9L; // ID do indicador
		for (Indicador i : li) {
			if (ind.equals(i.getId())) {
				liRetorno.add(i);
			}
		}
		porcentagemFaturamentoTelaInicial = liRetorno.get(0).getValorFinal();

	
		
		init("Custo", mes);
	}

	public void init(String categoria, int menosMes) {
		// createCombinedModel();
		Date dataIniciar = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(dataIniciar);
		mes = ((dataCal.get(Calendar.MONTH) + 1)-menosMes);
		ano = dataCal.get(Calendar.YEAR);
		System.out.println("MEs inicial: " + mes + " - " + ano);
		graficoCategoriaIndicadorDataProcessoInit();
	}

	public void preencherListaVariaveisCalculo() {
//		System.out.println("Variáveis de Cálcul2");
		if (mes != null && mes > 0) {
			System.out.println("Variáveis de Cálculo");
			variaveisCalculo = new ArrayList<>();
			List<GrupoLancamento> lg = daoGrupoLancamentos.listaComStatus(GrupoLancamento.class);
			for (GrupoLancamento g : lg) {
				Double atual = 0.;
				Double menos1 = 0.;
				Double menos2 = 0.;
				List<ItensLancamento> lilAtual = daoItensLancamento.listar(ItensLancamento.class,
						"grupoLancamento.id=" + g.getId() + " and dataLancamento BETWEEN '" + ano + "-" + mes
								+ "-01' and '" + ano + "-" + mes + "-31'");
//				List<ItensLancamento> lilAtual = daoItensLancamento.listar(ItensLancamento.class,
//				"grupoLancamento.id=" + g.getId() + " and dataLancamento BETWEEN 2018-10-01 and 2018-10-31");
//				System.out.println("TAmanho: "+lilAtual.size());

				for (ItensLancamento itens : lilAtual) {
					atual += itens.getValor();
				}

				List<ItensLancamento> lil1 = daoItensLancamento.listar(ItensLancamento.class,
						"grupoLancamento.id=" + g.getId() + " and dataLancamento BETWEEN '" + ano + "-" + (mes - 1)
								+ "-01' and '" + ano + "-" + (mes - 1) + "-31'");
				for (ItensLancamento itens : lil1) {
					menos1 += itens.getValor();
				}

				List<ItensLancamento> lil2 = daoItensLancamento.listar(ItensLancamento.class,
						"grupoLancamento.id=" + g.getId() + " and dataLancamento BETWEEN '" + ano + "-" + (mes - 2)
								+ "-01' and '" + ano + "-" + (mes - 2) + "-31'");
				for (ItensLancamento itens : lil2) {
					menos2 += itens.getValor();
				}

				GrupoLancamento gru = new GrupoLancamento();
				gru.setDescricao(g.getDescricao());
				gru.setMesAtual(atual);
				gru.setMesMenosUm(menos1);
				gru.setMesMenosDois(menos2);

				variaveisCalculo.add(gru);
			}
		}
	}

	public String chamarDetalhes(Indicador ind) {
		if (ind != null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
			session.setAttribute("INDICADOR", ind);
			session.setAttribute("REALIZADO", getRealizado(ind));
			session.setAttribute("META", getMeta(ind));

			return "avaliacaoDetalhes.jsf?faces-redirect=true&ind=" + ind.getId();
		}
		return "";
	}

	public CartesianChartModel getCombinedModel() {
		return combinedModel;
	}

	private void createCombinedModel() {
		combinedModel = new BarChartModel();

		BarChartSeries boys = new BarChartSeries();
		boys.setLabel("Alcançado");
		boys.set("indicador 1", 122);
		boys.set("indicador 2", 12);
		boys.set("indicador 3", 123);
		boys.set("indicador 4", 123);
		boys.set("indicador 5", 123);
		boys.set("indicador 6", 123);

//		BarChartSeries boys2 = new BarChartSeries();
//		boys2.setLabel("Boys meta");
//		boys2.set("1", 120);

		BarChartSeries boys3 = new BarChartSeries();
		boys3.setLabel("Meta");
		boys3.set("indicador 1", 78);
		boys3.set("indicador 2", 16);
		boys3.set("indicador 3", 162);
		boys3.set("indicador 4", 162);
		boys3.set("indicador 5", 162);
		boys3.set("indicador 6", 162);

//
//		LineChartSeries girls = new LineChartSeries();
//		girls.setLabel("Girls");
//
//		girls.set("1", 53);
//		girls.set("2", 61);
//		
//		LineChartSeries girls2 = new LineChartSeries();
//		girls2.setLabel("Girls");
//
//		girls2.set("3", 52);
//		girls2.set("4", 60);

		combinedModel.addSeries(boys);
//		combinedModel.addSeries(girls);
//		combinedModel.addSeries(boys2);
//		combinedModel.addSeries(girls2);
		combinedModel.addSeries(boys3);
//		combinedModel.addSeries(boys4);

		combinedModel.setTitle("Bar and Line");
		combinedModel.setLegendPosition("ne");
		combinedModel.setMouseoverHighlight(false);
		combinedModel.setShowDatatip(false);
		combinedModel.setShowPointLabels(true);
		Axis yAxis = combinedModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(200);
	}

	public void escolherCategoriaCusto() {
		this.categoria = "Custo";
		opacidadeCusto = "100";
		opacidadeProdutividade = "50";
		opacidadeQualidade = "50";
		opacidadeTempo = "50";
	}

	public void escolherCategoriaProdutividade() {
		this.categoria = "Produtividade";
		opacidadeCusto = "50";
		opacidadeProdutividade = "100";
		opacidadeQualidade = "50";
		opacidadeTempo = "50";
	}

	public void escolherCategoriaQualidade() {
		this.categoria = "Qualidade";
		opacidadeCusto = "50";
		opacidadeProdutividade = "50";
		opacidadeQualidade = "100";
		opacidadeTempo = "50";
	}

	public void escolherCategoriaTempo() {
		this.categoria = "Tempo";
		opacidadeCusto = "50";
		opacidadeProdutividade = "50";
		opacidadeQualidade = "50";
		opacidadeTempo = "100";
	}

	public void buscarValoresIndicadores() {
		String dataInicial = "01/05/2018";
		String dataFinal = "31/05/2018";

		List<Indicador> li = funcoesMatematicas.calcularIndicadoresTodos(ConverteStringDate.retornaData(dataInicial),
				ConverteStringDate.retornaData(dataFinal));

		for (Indicador i : li) {
			System.out
					.println(i.getDescricao() + " - " + i.getValorCalculoGrupoLancamento() + " - " + i.getValorFinal());
		}
	}

	public void chamarGraficos(int meses) {
		System.out.println("");
	}

	private List<Indicador> retornaListaIndicadores() {
		String dataInicialM6 = "01/06/2018";
		String dataFinalM6 = "30/06/2018";
		return funcoesMatematicas.calcularIndicadoresPorCategoria(ConverteStringDate.retornaData(dataInicialM6),
				ConverteStringDate.retornaData(dataFinalM6), 1L);

	}

//	private BarChartModel graficoCategoriaIndicadorDatal() {
//
//		BarChartModel model = new BarChartModel();
//
//		String dataInicial = ConverteStringDate.retornaDataddMMyyyy(dataInicialDate);
//		String dataFinal = ConverteStringDate.retornaDataddMMyyyy(dataFinalDate);
//
//		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoria(dataInicialDate, dataFinalDate,
//				categoriaIndicador.getId());
//
//		for (Indicador i : li) {
//			ChartSeries dados = new ChartSeries();
//			dados.setLabel(i.getDescricao());
//			dados.set(dataInicial + " à " + dataFinal, i.getValorFinal());
//			model.addSeries(dados);
//		}
//
//		return model;
//	}

	public void graficoCategoriaIndicadorDataProcessoInit() {
		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		categoriaIndicador = listIndic.get(0);

		graficoBarraAvaliacaoGeral = graficoCategoriaIndicadorDataProcesso();
		// barraCustoProcesso.setTitle("Indicadores de " +
		// categoriaIndicador.getDescricao() + " e Processos");

		graficoBarraAvaliacaoGeral.setLegendPosition("ne");
		graficoBarraAvaliacaoGeral.setAnimate(true);
		// graficoBarraAvaliacaoGeral.setShowPointLabels(true);
		// barModel.setLegendPosition("e");
		// barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

		// graficoBarraAvaliacaoGeral.getAxis(AxisType.X).setTickAngle(-50);

		Axis xAxis = graficoBarraAvaliacaoGeral.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = graficoBarraAvaliacaoGeral.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");

		// yAxis.setMin(0);
		// yAxis.setMax(200);

		combinedModel.setTitle("Visão Geral dos Indicadores de " + categoria+" - "+mes+"/"+ano);
		combinedModel.setAnimate(true);
		combinedModel.setLegendPosition("ne");
		combinedModel.setMouseoverHighlight(false);
		combinedModel.setShowDatatip(false);
		combinedModel.setShowPointLabels(true);
//		Axis yAxis2 = combinedModel.getAxis(AxisType.Y);
//		yAxis2.setMin(0);
//		yAxis2.setMax(200);

	}

	private BarChartModel graficoCategoriaIndicadorDataProcesso() {

		BarChartModel model = new BarChartModel();

		combinedModel = new BarChartModel();

		String dataInicial = "01-" + mes + "-" + ano;
		String dataFinal = "31-" + mes + "-" + ano;

		listaIndicadores = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessosMesAno(mes, ano,
				categoriaIndicador.getId(), processos);
//		System.out.println("QUantidade de Itens: " + listaIndicadores.size());

		BarChartSeries meta = new BarChartSeries();
		meta.setLabel("Meta");
		BarChartSeries alcancado = new BarChartSeries();
		alcancado.setLabel("Realizado");

		LineChartSeries girls = new LineChartSeries();
		for (int x = 0; x < listaIndicadores.size(); x++) {
			Indicador i = listaIndicadores.get(x);
			ChartSeries dados = new ChartSeries();
			dados.setLabel(i.getDescricao());
			dados.set(dataInicial + " à " + dataFinal, i.getValorFinal());
			model.addSeries(dados);

			meta.set(i.getAbreviacao() + " - (Ideal: " + i.getMetaMaiorMenorQue() + ")", getMeta(i));
			alcancado.set(i.getAbreviacao() + " - (Ideal: " + i.getMetaMaiorMenorQue() + ")", i.getValorFinal());

			// girls.setLabel("Meta " + mes + "-" + ano);
			// girls.set(x, getMeta(i));

		}
		combinedModel.addSeries(meta);
		combinedModel.addSeries(alcancado);

		return model;

	}

	public Double getMeta(Indicador ind) {
		switch (mes) {
		case 1:
			return ind.getMeta01();
		case 2:
			return ind.getMeta02();
		case 3:
			return ind.getMeta03();
		case 4:
			return ind.getMeta04();
		case 5:
			return ind.getMeta05();
		case 6:
			return ind.getMeta06();
		case 7:
			return ind.getMeta07();
		case 8:
			return ind.getMeta08();
		case 9:
			return ind.getMeta09();
		case 10:
			return ind.getMeta10();
		case 11:
			return ind.getMeta11();
		case 12:
			return ind.getMeta12();
		default:
			return 0.;
		}
	}

	public Double getMeta1(Indicador ind) {
		switch (mes) {
		case 1:
			return ind.getMeta12();
		case 2:
			return ind.getMeta01();
		case 3:
			return ind.getMeta02();
		case 4:
			return ind.getMeta03();
		case 5:
			return ind.getMeta04();
		case 6:
			return ind.getMeta05();
		case 7:
			return ind.getMeta06();
		case 8:
			return ind.getMeta07();
		case 9:
			return ind.getMeta08();
		case 10:
			return ind.getMeta09();
		case 11:
			return ind.getMeta10();
		case 12:
			return ind.getMeta11();
		default:
			return 0.;
		}
	}

	public Double getMeta2(Indicador ind) {
		System.out.println("Indicador:: " + ind.getDescricao() + " Mes: " + mes + " meta " + ind.getMeta07());
		switch (mes) {
		case 1:
			return ind.getMeta11();
		case 2:
			return ind.getMeta12();
		case 3:
			return ind.getMeta01();
		case 4:
			return ind.getMeta02();
		case 5:
			return ind.getMeta03();
		case 6:
			return ind.getMeta04();
		case 7:
			return ind.getMeta05();
		case 8:
			return ind.getMeta06();
		case 9:
			return ind.getMeta07();
		case 10:
			return ind.getMeta08();
		case 11:
			return ind.getMeta09();
		case 12:
			return ind.getMeta10();
		default:
			return 0.;
		}
	}

	public Double getRealizado(Indicador ind) {
		switch (mes) {
		case 1:
			return ind.getValor1();
		case 2:
			return ind.getValor2();
		case 3:
			return ind.getValor3();
		case 4:
			return ind.getValor4();
		case 5:
			return ind.getValor5();
		case 6:
			return ind.getValor6();
		case 7:
			return ind.getValor7();
		case 8:
			return ind.getValor8();
		case 9:
			return ind.getValor9();
		case 10:
			return ind.getValor10();
		case 11:
			return ind.getValor11();
		case 12:
			return ind.getValor12();
		default:
			return 0.;
		}
	}

	public Double getRealizado1(Indicador ind) {
		switch (mes) {
		case 1:
			return ind.getValor12Anterior();
		case 2:
			return ind.getValor1();
		case 3:
			return ind.getValor2();
		case 4:
			return ind.getValor3();
		case 5:
			return ind.getValor4();
		case 6:
			return ind.getValor5();
		case 7:
			return ind.getValor6();
		case 8:
			return ind.getValor7();
		case 9:
			return ind.getValor8();
		case 10:
			return ind.getValor9();
		case 11:
			return ind.getValor10();
		case 12:
			return ind.getValor11();
		default:
			return 0.;
		}
	}

	public Double getRealizado2(Indicador ind) {
		switch (mes) {
		case 1:
			return ind.getValor11Anterior();
		case 2:
			return ind.getValor12Anterior();
		case 3:
			return ind.getValor1();
		case 4:
			return ind.getValor2();
		case 5:
			return ind.getValor3();
		case 6:
			return ind.getValor4();
		case 7:
			return ind.getValor5();
		case 8:
			return ind.getValor6();
		case 9:
			return ind.getValor7();
		case 10:
			return ind.getValor8();
		case 11:
			return ind.getValor9();
		case 12:
			return ind.getValor10();
		default:
			return 0.;
		}
	}

	public CategoriaIndicador getCategoriaIndicador() {
		return categoriaIndicador;
	}

	public void setCategoriaIndicador(CategoriaIndicador categoriaIndicador) {
		this.categoriaIndicador = categoriaIndicador;
	}

	public Long[] getProcessos() {
		return processos;
	}

	public void setProcessos(Long[] processos) {
		this.processos = processos;
	}

	public Processo[] getListaProcesso() {
		return listaProcesso;
	}

	public void setListaProcesso(Processo[] listaProcesso) {
		this.listaProcesso = listaProcesso;
	}

	public String[] getProcessosString() {
		return processosString;
	}

	public void setProcessosString(String[] processosString) {
		this.processosString = processosString;
	}

	public Long[] getProcessosComparativos() {
		return processosComparativos;
	}

	public void setProcessosComparativos(Long[] processosComparativos) {
		this.processosComparativos = processosComparativos;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public BarChartModel getGraficoBarraAvaliacaoGeral() {
		return graficoBarraAvaliacaoGeral;
	}

	public void setGraficoBarraAvaliacaoGeral(BarChartModel graficoBarraAvaliacaoGeral) {
		this.graficoBarraAvaliacaoGeral = graficoBarraAvaliacaoGeral;
	}

	public List<Indicador> getListaIndicadores() {
		return listaIndicadores;
	}

	public void setListaIndicadores(List<Indicador> listaIndicadores) {
		this.listaIndicadores = listaIndicadores;
	}

	public Double getA() {
		return a;
	}

	public void setA(Double a) {
		this.a = a;
	}

	public String getOpacidadeCusto() {
		return opacidadeCusto;
	}

	public void setOpacidadeCusto(String opacidadeCusto) {
		this.opacidadeCusto = opacidadeCusto;
	}

	public String getOpacidadeQualidade() {
		return opacidadeQualidade;
	}

	public void setOpacidadeQualidade(String opacidadeQualidade) {
		this.opacidadeQualidade = opacidadeQualidade;
	}

	public String getOpacidadeProdutividade() {
		return opacidadeProdutividade;
	}

	public void setOpacidadeProdutividade(String opacidadeProdutividade) {
		this.opacidadeProdutividade = opacidadeProdutividade;
	}

	public String getOpacidadeTempo() {
		return opacidadeTempo;
	}

	public void setOpacidadeTempo(String opacidadeTempo) {
		this.opacidadeTempo = opacidadeTempo;
	}

	public ElementosCoresAvaliacao getElementosCores() {
		return elementosCores;
	}

	public void setElementosCores(ElementosCoresAvaliacao elementosCores) {
		this.elementosCores = elementosCores;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<GrupoLancamento> getVariaveisCalculo() {
		return variaveisCalculo;
	}

	public void setVariaveisCalculo(List<GrupoLancamento> variaveisCalculo) {
		this.variaveisCalculo = variaveisCalculo;
	}

	public Double getCustoTotalTelaInicio() {
		return custoTotalTelaInicio;
	}

	public void setCustoTotalTelaInicio(Double custoTotalTelaInicio) {
		this.custoTotalTelaInicio = custoTotalTelaInicio;
	}

	public Double getPorcentagemFaturamentoTelaInicial() {
		return porcentagemFaturamentoTelaInicial;
	}

	public void setPorcentagemFaturamentoTelaInicial(Double porcentagemFaturamentoTelaInicial) {
		this.porcentagemFaturamentoTelaInicial = porcentagemFaturamentoTelaInicial;
	}
	

}