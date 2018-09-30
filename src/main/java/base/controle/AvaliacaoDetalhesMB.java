package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.MeterGaugeChartModel;

import base.modelo.Pessoa;
import base.modelo.Atividade;
import base.modelo.Indicador;
import base.modelo.Ocorrencia;
import base.service.AtividadeService;
import util.ElementosCoresAvaliacao;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;

@ViewScoped
@Named("avaliacaoDetalhesMB")
public class AvaliacaoDetalhesMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private String porcentagem = "20";

	ElementosCoresAvaliacao elementosCores = new ElementosCoresAvaliacao();

	private MeterGaugeChartModel meterGaugeModel2;

	private Ocorrencia ocorrenciaVerDetalhes;

	private Indicador indicador;

	@Inject
	private GenericDAO<Atividade> daoProcesso; // faz as buscas

	@Inject
	private AtividadeService atividadeService; // inserir no banco

	private List<Ocorrencia> listaTodasOcorrencias;

	private List<Ocorrencia> listaOcorrenciasPorIndicador;

	@Inject
	private GenericDAO<Ocorrencia> daoOcorrencia; // faz as buscas

	@PostConstruct
	public void inicializar() {
		createMeterGaugeModels();
		listaTodasOcorrencias = daoOcorrencia.listaComStatus(Ocorrencia.class);
		if (indicador != null) {
			listaOcorrenciasPorIndicador = daoOcorrencia.listar(Ocorrencia.class, "indicador.id=" + indicador.getId());
		}
	}
	
	private void calcularValoresIndicador() {
		if(indicador!=null) {
			
		}
	}

	public void preencherOcorrencia(Ocorrencia t) {
		this.ocorrenciaVerDetalhes = t;

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
		System.out.println("Teste de chamada de método");
	}

	public void itemSelect(ItemSelectEvent event) {
		System.out.println("Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

	}

	public String verificarSmart() {
		String naoEe = "<ol>";
		if (indicador != null) {
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
				naoEe = "<ol>";
				naoEe += " <li> É AtingÍvel </li>";
				naoEe += " <li> É Específico </li>";
				naoEe += " <li> É Mensurável </li>";
				naoEe += " <li> É Relevante </li>";
				naoEe += " <li> É Temporizável </li>";
				naoEe += " </ol>";
			}
			return naoEe;
		} else {
			return "";
		}

	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}

	public ElementosCoresAvaliacao getElementosCores() {
		return elementosCores;
	}

	public void setElementosCores(ElementosCoresAvaliacao elementosCores) {
		this.elementosCores = elementosCores;
	}

	public Ocorrencia getOcorrenciaVerDetalhes() {
		return ocorrenciaVerDetalhes;
	}

	public void setOcorrenciaVerDetalhes(Ocorrencia ocorrenciaVerDetalhes) {
		this.ocorrenciaVerDetalhes = ocorrenciaVerDetalhes;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public List<Ocorrencia> getListaTodasOcorrencias() {
		return listaTodasOcorrencias;
	}

	public void setListaTodasOcorrencias(List<Ocorrencia> listaTodasOcorrencias) {
		this.listaTodasOcorrencias = listaTodasOcorrencias;
	}

	public List<Ocorrencia> getListaOcorrenciasPorIndicador() {
		return listaOcorrenciasPorIndicador;
	}

	public void setListaOcorrenciasPorIndicador(List<Ocorrencia> listaOcorrenciasPorIndicador) {
		this.listaOcorrenciasPorIndicador = listaOcorrenciasPorIndicador;
	}

}
