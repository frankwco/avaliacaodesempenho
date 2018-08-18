package base.controle;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
import dao.GenericDAO;
import util.ConverteStringDate;
import util.FuncoesMatematicas;

@ViewScoped
@Named("graficosCategoriaIndicadoresMB")
public class GraficosCategoriaIndicadoresMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private BarChartModel barraCusto;
	
	
	private BarChartModel barraProdutividade;
	private BarChartModel barraQualidade;
	private BarChartModel barraTempo;

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
		//createBarModels();
	}
	

	public void buscarValoresIndicadores() {
		String dataInicial = "01/06/2018";
		String dataFinal = "30/06/2018";

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

	private BarChartModel initBarModel() {

		BarChartModel model = new BarChartModel();

		String dataInicial = "01/08/2018";
		String dataFinal = "30/08/2018";

		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoria(
				ConverteStringDate.retornaData(dataInicial), ConverteStringDate.retornaData(dataFinal), 1L);

		for (Indicador i : li) {
			System.out
					.println(i.getDescricao() + " - " + i.getValorCalculoGrupoLancamento() + " - " + i.getValorFinal());
			ChartSeries dados = new ChartSeries();
			dados.setLabel(i.getDescricao());
			dados.set("01/06/2018 à 30/06/2018", i.getValorFinal());
			model.addSeries(dados);
		}

		return model;
	}

	private void createBarModels() {
		createBarModel();

	}

	private void createBarModel() {
		barraCusto = initBarModel();

		barraCusto.setTitle(" ");
		barraCusto.setLegendPosition("ne");
		// barModel.setLegendPosition("e");
		// barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

		Axis xAxis = barraCusto.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = barraCusto.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		// yAxis.setMin(0);
		// yAxis.setMax(200);
	}

	public BarChartModel getBarraCusto() {
		return barraCusto;
	}

}