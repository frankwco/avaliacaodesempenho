package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.MeterGaugeChartModel;

import base.modelo.Pessoa;
import base.modelo.Atividade;
import base.service.AtividadeService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("testeMB")
public class TesteMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private String porcentagem = "20";

	private String statusVermelho = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:red\"></i>";
	private String statusVerde = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:green\"></i>";
	private String statusLaranja = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:orange\"></i>";

	private MeterGaugeChartModel meterGaugeModel2;

	@Inject
	private GenericDAO<Atividade> daoProcesso; // faz as buscas

	@Inject
	private AtividadeService atividadeService; // inserir no banco

	@PostConstruct
	public void inicializar() {
		createMeterGaugeModels();
	}

	public MeterGaugeChartModel getMeterGaugeModel2() {
		return meterGaugeModel2;
	}

	private MeterGaugeChartModel initMeterGaugeModel() {
		List<Number> intervals = new ArrayList<Number>() {
			{
				add(20);
				add(50);
				add(120);
				add(220);
			}
		};

		return new MeterGaugeChartModel(140, intervals);
	}

	private void createMeterGaugeModels() {

		meterGaugeModel2 = initMeterGaugeModel();
		meterGaugeModel2.setTitle("Custom Options");
		meterGaugeModel2.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
		meterGaugeModel2.setGaugeLabel("km/h");
		meterGaugeModel2.setGaugeLabelPosition("bottom");
		meterGaugeModel2.setShowTickLabels(false);
		meterGaugeModel2.setLabelHeightAdjust(110);
		meterGaugeModel2.setIntervalOuterRadius(100);
	}

	public void chamada() {
		System.out.println("Teste de chamada de m�todo");
	}

	public void itemSelect(ItemSelectEvent event) {
		System.out.println("Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}

	public String getStatusVermelho() {
		return statusVermelho;
	}

	public void setStatusVermelho(String statusVermelho) {
		this.statusVermelho = statusVermelho;
	}

	public String getStatusVerde() {
		return statusVerde;
	}

	public void setStatusVerde(String statusVerde) {
		this.statusVerde = statusVerde;
	}

	public String getStatusLaranja() {
		return statusLaranja;
	}

	public void setStatusLaranja(String statusLaranja) {
		this.statusLaranja = statusLaranja;
	}

}
