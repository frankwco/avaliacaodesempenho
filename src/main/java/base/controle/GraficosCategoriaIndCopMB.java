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
@Named("graficosCategoriaIndCopMB")
public class GraficosCategoriaIndCopMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicialDate = new Date();
	private Date dataFinalDate = new Date();
	private CategoriaIndicador categoriaIndicador;

	private BarChartModel barraCusto;
	private BarChartModel barraCustoProcesso;

	private BarChartModel barraProdutividade;
	private BarChartModel barraQualidade;
	private BarChartModel barraTempo;
	private Processo[] listaProcesso;
	private Long[] processos;
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

	@PostConstruct
	public void init() {
		graficoCategoriaIndicadorData("Custo");
		graficoCategoriaIndicadorDataProcesso("Custo");
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
			//barraCusto.setTitle("Indicadores de " + categoriaIndicador.getDescricao());
		

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
		System.out.println("Aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		categoriaIndicador = listIndic.get(0);
		if (processos != null) {
			System.out.println("Tamanhooooo: "+processos.length);
			for (Long p : processos) {
				System.out.println("Processsooo: "+p);
			}
		}

		barraCustoProcesso = graficoCategoriaIndicadorDataProcesso();
		//barraCustoProcesso.setTitle("Indicadores de " + categoriaIndicador.getDescricao() + " e Processos");

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



}