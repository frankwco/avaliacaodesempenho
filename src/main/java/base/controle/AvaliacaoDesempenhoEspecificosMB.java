package base.controle;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.procedure.ProcedureCall;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartSeries;

import base.modelo.CategoriaIndicador;
import base.modelo.Indicador;
import base.modelo.ItensLancamento;
import base.modelo.Lancamento;
import base.modelo.Processo;
import dao.GenericDAO;
import util.ConverteStringDate;
import util.FuncoesMatematicas;

@ViewScoped
@Named("avaliacaoDesempenhoEspecificosMB")
public class AvaliacaoDesempenhoEspecificosMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicialDate = new Date();
	private Date dataFinalDate = new Date();

	private Date dataInicialDateC1 = new Date();
	private Date dataFinalDateC1 = new Date();

	private Date dataInicialDateC2 = new Date();
	private Date dataFinalDateC2 = new Date();

	private Date dataInicialDateC3 = new Date();
	private Date dataFinalDateC3 = new Date();

	private CategoriaIndicador categoriaIndicador;

	private BarChartModel barraCusto;
	private BarChartModel barraCustoProcesso;
	private BarChartModel barraCustoComparativo;

	private BarChartModel barraProdutividade;
	private BarChartModel barraQualidade;
	private BarChartModel barraTempo;
	private Processo[] listaProcesso;
	private Long[] processos;
	private Long[] processosComparativos;
	private String[] processosString;

	@Inject
	private GenericDAO<Lancamento> daoLancamento; // faz as buscas

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicadores; // faz as
																	// buscas

	@Inject
	private GenericDAO<ItensLancamento> daoItensLancamento; // faz as buscas

	@Inject
	private FuncoesMatematicas funcoesMatematicas;

//	@PostConstruct
//	public void init() {
//		graficoCategoriaIndicadorData("Custo");
//		graficoCategoriaIndicadorDataProcesso("Custo");
//		graficoCategoriaIndicadorDataProcessoComparativo("Custo");
//	}
	
	public void init(String categoria) {
		graficoCategoriaIndicadorData(categoria);
		graficoCategoriaIndicadorDataProcesso(categoria);
		graficoCategoriaIndicadorDataProcessoComparativo(categoria);
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

	private BarChartModel initBarModel_Comparativo() {

		BarChartModel model = new BarChartModel();

		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoria(dataInicialDate, dataFinalDate,
				categoriaIndicador.getId());

		List<Indicador> liM6 = retornaListaIndicadores();
		List<Indicador> lista = new ArrayList<>();
		for (int x = 0; x < li.size(); x++) {
			li.get(x).setObservacao("aaad");
			// System.out.println("Mes 05 - "+li.get(x).getValorFinal() + " Mes 06 -
			// "+liM6.get(x).getValorFinal());

			lista.add(li.get(x));
			lista.add(liM6.get(x));
		}

		for (Indicador i : lista) {

			System.out
					.println(i.getDescricao() + " - " + i.getValorCalculoGrupoLancamento() + " - " + i.getValorFinal());
			ChartSeries dados = new ChartSeries();
			if (i.getObservacao().equals("aaad")) {
				dados.setLabel(i.getDescricao() + "-Mês 05");
			} else {
				dados.setLabel(i.getDescricao() + "-Mês 06");
			}

			dados.set("01/05/2018 à 31/05/2018 - 01/06/2018 à 30/06/2018", i.getValorFinal());
			model.addSeries(dados);
		}

		return model;
	}

	private List<Indicador> retornaListaIndicadores() {
		String dataInicialM6 = "01/06/2018";
		String dataFinalM6 = "30/06/2018";
		return funcoesMatematicas.calcularIndicadoresPorCategoria(ConverteStringDate.retornaData(dataInicialM6),
				ConverteStringDate.retornaData(dataFinalM6), 1L);

	}

	public void graficoCategoriaIndicadorData(String categoria) {
		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		categoriaIndicador = listIndic.get(0);
		if (categoriaIndicador == null) {

		}

		barraCusto = graficoCategoriaIndicadorDatal();
		// barraCusto.setTitle("Indicadores de " + categoriaIndicador.getDescricao());

		barraCusto.setLegendPosition("ne");
		barraCusto.setAnimate(true);
		// barraCusto.setShowPointLabels(true);
		// barModel.setLegendPosition("e");
		// barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

		Axis xAxis = barraCusto.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = barraCusto.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		// yAxis.setMin(0);
		// yAxis.setMax(200);

	}

	private BarChartModel graficoCategoriaIndicadorDatal() {

		BarChartModel model = new BarChartModel();

		String dataInicial = ConverteStringDate.retornaDataddMMyyyy(dataInicialDate);
		String dataFinal = ConverteStringDate.retornaDataddMMyyyy(dataFinalDate);

		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoria(dataInicialDate, dataFinalDate,
				categoriaIndicador.getId());

		for (Indicador i : li) {
			ChartSeries dados = new ChartSeries();
			dados.setLabel(i.getDescricao());
			dados.set(dataInicial + " à " + dataFinal, i.getValorFinal());
			model.addSeries(dados);
		}

		return model;
	}

	public void graficoCategoriaIndicadorDataProcesso(String categoria) {
		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		categoriaIndicador = listIndic.get(0);
		
		barraCustoProcesso = graficoCategoriaIndicadorDataProcesso();
		// barraCustoProcesso.setTitle("Indicadores de " +
		// categoriaIndicador.getDescricao() + " e Processos");

		barraCustoProcesso.setLegendPosition("ne");
		barraCustoProcesso.setAnimate(true);
		// barraCusto.setShowPointLabels(true);
		// barModel.setLegendPosition("e");
		// barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

		Axis xAxis = barraCustoProcesso.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = barraCustoProcesso.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		// yAxis.setMin(0);
		// yAxis.setMax(200);

	}

	private BarChartModel graficoCategoriaIndicadorDataProcesso() {

		BarChartModel model = new BarChartModel();

		String dataInicial = ConverteStringDate.retornaDataddMMyyyy(dataInicialDate);
		String dataFinal = ConverteStringDate.retornaDataddMMyyyy(dataFinalDate);

		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessos(dataInicialDate, dataFinalDate,
				categoriaIndicador.getId(), processos);

		for (Indicador i : li) {
			ChartSeries dados = new ChartSeries();
			dados.setLabel(i.getDescricao());
			dados.set(dataInicial + " à " + dataFinal, i.getValorFinal());
			model.addSeries(dados);
		}

		return model;
	}

	public void graficoCategoriaIndicadorDataProcessoComparativo(String categoria) {

		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		categoriaIndicador = listIndic.get(0);

		barraCustoComparativo = graficoCategoriaIndicadorDataProcessoComparativo();
		// barraCustoProcesso.setTitle("Indicadores de " +
		// categoriaIndicador.getDescricao() + " e Processos");

		barraCustoComparativo.setLegendPosition("ne");
		barraCustoComparativo.setAnimate(true);
		// barraCusto.setShowPointLabels(true);
		// barModel.setLegendPosition("e");
		// barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

		Axis xAxis = barraCustoComparativo.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = barraCustoComparativo.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		// yAxis.setMin(0);
		// yAxis.setMax(200);

	}

	private BarChartModel graficoCategoriaIndicadorDataProcessoComparativo() {

		BarChartModel model = new BarChartModel();

		String dataInicial1 = ConverteStringDate.retornaDataddMMyyyy(dataInicialDateC1);
		String dataFinal1 = ConverteStringDate.retornaDataddMMyyyy(dataFinalDateC1);
		String dataInicial2 = ConverteStringDate.retornaDataddMMyyyy(dataInicialDateC2);
		String dataFinal2 = ConverteStringDate.retornaDataddMMyyyy(dataFinalDateC2);
		String dataInicial3 = ConverteStringDate.retornaDataddMMyyyy(dataInicialDateC3);
		String dataFinal3 = ConverteStringDate.retornaDataddMMyyyy(dataFinalDateC3);

		List<Indicador> l1 = new ArrayList<>();
		List<Indicador> l2 = new ArrayList<>();
		List<Indicador> l3 = new ArrayList<>();
		int contador = 0;

		if (!dataInicial1.equals("") && !dataFinal1.equals("")) {
			contador++;
			l1 = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessos(dataInicialDateC1, dataFinalDateC1,
					categoriaIndicador.getId(), processosComparativos);
			if (!dataInicial2.equals("") && !dataFinal2.equals("")) {
				contador++;
				l2 = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessos(dataInicialDateC2, dataFinalDateC2,
						categoriaIndicador.getId(), processosComparativos);
				if (!dataInicial3.equals("") && !dataFinal3.equals("")) {
					contador++;
					l3 = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessos(dataInicialDateC3, dataFinalDateC3,
							categoriaIndicador.getId(), processosComparativos);
				}
			}
		}
		if (contador == 1) {
			for (int x = 0; x < l1.size(); x++) {
				ChartSeries dados = new ChartSeries();
				dados.setLabel(l1.get(x).getDescricao());
				dados.set(dataInicial1 + " à " + dataFinal1, l1.get(x).getValorFinal());
				model.addSeries(dados);
			}
		}
		if (contador == 2) {
			for (int x = 0; x < l1.size(); x++) {
				ChartSeries dados = new ChartSeries();
				dados.setLabel(l1.get(x).getDescricao());
				dados.set(dataInicial1 + " à " + dataFinal1, l1.get(x).getValorFinal());
				dados.set(dataInicial2 + " à " + dataFinal2, l2.get(x).getValorFinal());
				model.addSeries(dados);
			}
		}
		if (contador == 3) {
			for (int x = 0; x < l1.size(); x++) {
				ChartSeries dados = new ChartSeries();
				dados.setLabel(l1.get(x).getDescricao());
				dados.set(dataInicial1 + " à " + dataFinal1, l1.get(x).getValorFinal());
				dados.set(dataInicial2 + " à " + dataFinal2, l2.get(x).getValorFinal());
				dados.set(dataInicial3 + " à " + dataFinal3, l3.get(x).getValorFinal());
				model.addSeries(dados);
			}
		}

		return model;
	}

	public BarChartModel getBarraCusto() {
		return barraCusto;
	}

	public Date getDataInicialDate() {
		return dataInicialDate;
	}

	public void setDataInicialDate(Date dataInicialDate) {
		this.dataInicialDate = dataInicialDate;
	}

	public Date getDataFinalDate() {
		return dataFinalDate;
	}

	public void setDataFinalDate(Date dataFinalDate) {
		this.dataFinalDate = dataFinalDate;
	}

	public CategoriaIndicador getCategoriaIndicador() {
		return categoriaIndicador;
	}

	public void setCategoriaIndicador(CategoriaIndicador categoriaIndicador) {
		this.categoriaIndicador = categoriaIndicador;
	}

	public BarChartModel getBarraCustoProcesso() {
		return barraCustoProcesso;
	}

	public void setBarraCustoProcesso(BarChartModel barraCustoProcesso) {
		this.barraCustoProcesso = barraCustoProcesso;
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

	public BarChartModel getBarraCustoComparativo() {
		return barraCustoComparativo;
	}

	public void setBarraCustoComparativo(BarChartModel barraCustoComparativo) {
		this.barraCustoComparativo = barraCustoComparativo;
	}

	public Date getDataInicialDateC1() {
		return dataInicialDateC1;
	}

	public void setDataInicialDateC1(Date dataInicialDateC1) {
		this.dataInicialDateC1 = dataInicialDateC1;
	}

	public Date getDataFinalDateC1() {
		return dataFinalDateC1;
	}

	public void setDataFinalDateC1(Date dataFinalDateC1) {
		this.dataFinalDateC1 = dataFinalDateC1;
	}

	public Date getDataInicialDateC2() {
		return dataInicialDateC2;
	}

	public void setDataInicialDateC2(Date dataInicialDateC2) {
		this.dataInicialDateC2 = dataInicialDateC2;
	}

	public Date getDataFinalDateC2() {
		return dataFinalDateC2;
	}

	public void setDataFinalDateC2(Date dataFinalDateC2) {
		this.dataFinalDateC2 = dataFinalDateC2;
	}

	public Date getDataInicialDateC3() {
		return dataInicialDateC3;
	}

	public void setDataInicialDateC3(Date dataInicialDateC3) {
		this.dataInicialDateC3 = dataInicialDateC3;
	}

	public Date getDataFinalDateC3() {
		return dataFinalDateC3;
	}

	public void setDataFinalDateC3(Date dataFinalDateC3) {
		this.dataFinalDateC3 = dataFinalDateC3;
	}

	public Long[] getProcessosComparativos() {
		return processosComparativos;
	}

	public void setProcessosComparativos(Long[] processosComparativos) {
		this.processosComparativos = processosComparativos;
	}

}