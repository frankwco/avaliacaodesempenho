package base.controle;

import java.io.Serializable;
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
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

import base.modelo.CategoriaIndicador;
import base.modelo.ItensLancamento;
import base.modelo.Lancamento;
import dao.GenericDAO;

@ViewScoped
@Named("chartView")
public class ChartView implements Serializable {

	private LineChartModel lineModel1;
	private LineChartModel lineModel2;

	@Inject
	private GenericDAO<Lancamento> daoLancamento; // faz as buscas

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicadores; // faz as
																	// buscas

	@Inject
	private GenericDAO<ItensLancamento> daoItensLancamento; // faz as buscas
	
	int meses=12;

	@PostConstruct
	public void init() {
		createLineModels();
	}
	
	public void chamarGraficos(int meses){
		this.meses = meses;
		createLineModels();
		System.out.println("");
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Linear Chart");
		lineModel1.setLegendPosition("e");
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(10);

		lineModel2 = initCategoryModel();
		lineModel2.setTitle("Gráfico Comparativo dos Indicadores - Últimos 3 meses");
		lineModel2.setZoom(true);
		lineModel2.setLegendPosition("e");
		lineModel2.setShowPointLabels(true);
		lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Mês/Ano"));
		yAxis = lineModel2.getAxis(AxisType.Y);
		yAxis.setLabel("R$");
		//yAxis.setMin(0);
		//yAxis.setMax(20000);
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		series1.set(1, 2);
		series1.set(2, 1);
		series1.set(3, 3);
		series1.set(4, 6);
		series1.set(5, 8);

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		series2.set(1, 6);
		series2.set(2, 3);
		series2.set(3, 2);
		series2.set(4, 7);
		series2.set(5, 9);

		model.addSeries(series1);
		model.addSeries(series2);

		return model;
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();

		List<CategoriaIndicador> lci = daoCategoriaIndicadores.listaComStatus(CategoriaIndicador.class);
		// select * from tabelaitensservicolista where extract(month from
		// `dataServicoDate`) = 5
		for (CategoriaIndicador cat : lci) {
			Date data = new Date();
			GregorianCalendar dataCal = new GregorianCalendar();
			dataCal.setTime(data);
			// int mes = dataCal.get(Calendar.MONTH);
			// int ano = dataCal.get(Calendar.YEAR);

			ChartSeries girls = new ChartSeries();
			girls.setLabel(cat.getDescricao());

			dataCal.add(Calendar.MONTH, (meses*-1));

			for (int x = 0; x > (meses*-1); x--) {
				dataCal.add(Calendar.MONTH, 1);				
				int mes = dataCal.get(Calendar.MONTH)+1;
				int ano = dataCal.get(Calendar.YEAR);
				
				
				List<ItensLancamento> listaItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
						"lancamento.categoriaIndicador.id=" + cat.getId() + " and extract(month from dataLancamento) ="
								+ mes + " and extract(year from dataLancamento) =" + ano);

				Double valor = 0.;
				for (ItensLancamento il : listaItensLancamento) {
					valor += il.getValor();
				}
//				System.out.println(mes + "/" + ano + " - " + valor);
				girls.set(mes + "/" + String.valueOf(ano).substring(2), valor);

			}
			model.addSeries(girls);

		}

		// LineChartModel model = new LineChartModel();

		// ChartSeries boys = new ChartSeries();
		// boys.setLabel("Boys");
		// boys.set("2004", 120);
		// boys.set("2005", 100);
		// boys.set("2006", 44);
		// boys.set("2007", 150);
		// boys.set("2008", 25);
		//
		// ChartSeries girls = new ChartSeries();
		// girls.setLabel("Girls");
		// girls.set("2004", 52);
		// girls.set("2005", 60);
		// girls.set("2006", 110);
		// girls.set("2007", 90);
		// girls.set("2008", 120);
		//
		// model.addSeries(boys);
		// model.addSeries(girls);

		return model;
	}

}