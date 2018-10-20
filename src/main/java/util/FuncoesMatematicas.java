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
import base.modelo.Processo;
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
	// private List<ItensLancamento> listItensLancamento;
	private GrupoLancamento grupoLancamentoSelecionado;
	private ItensLancamento itensLancamento;

	// private List<Indicador> listaIndicadores;

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
		// listItensLancamento = new ArrayList<>();
		// listaIndicadores = daoIndicador.listar(Indicador.class, "utilizarAnalise is
		// true");
	}

	public List<Indicador> calcularIndicadoresTodos(Date dataInicial, Date dataFinal) {
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class, "utilizarAnalise is true");
		;
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal, listaIndicadores, null);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	public List<Indicador> calcularIndicadoresPorIndicador(Date dataInicial, Date dataFinal, Long id) {
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class, "utilizarAnalise is true and id=" + id);
		;
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal, listaIndicadores, null);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	public List<Indicador> calcularIndicadoresPorProcesso(Date dataInicial, Date dataFinal, Long id) {
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class,
				"utilizarAnalise is true and processo.id=" + id);
		;
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal, listaIndicadores, null);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	public List<Indicador> calcularIndicadoresPorCategoria(Date dataInicial, Date dataFinal, Long id) {
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class,
				"utilizarAnalise is true and categoriaIndicador.id=" + id);
		;
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal, listaIndicadores, null);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	public Indicador calcularIndicadorPorIdAno(Integer mes, Integer ano, Long idIndicador) {
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class,
				"utilizarAnalise is true and id=" + idIndicador);
		listaIndicadores = calcularValorGruposLancamentos(ConverteStringDate.retornaData("01/" + mes + "/" + ano),
				ConverteStringDate.retornaData("31/" + mes + "/" + ano), listaIndicadores, ano);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores.get(0);
	}

	public List<Indicador> calcularIndicadoresPorCategoriaProcessosMesAno(Integer mes, Integer ano, Long idCategoria,
			Long[] processos) {
		String qp = "";
		if (processos != null) {
			for (Long p : processos) {
				qp += " processo.id=" + p;
			}
			qp = qp.trim().replace(" ", " or ");
			qp = " and (" + qp + ")";
		}
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class,
				"utilizarAnalise is true and categoriaIndicador.id=" + idCategoria + qp);

		listaIndicadores = calcularValorGruposLancamentos(ConverteStringDate.retornaData("01/" + mes + "/" + ano),
				ConverteStringDate.retornaData("31/" + mes + "/" + ano), listaIndicadores, ano);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	public List<Indicador> calcularIndicadoresPorCategoriaProcessos(Date dataInicial, Date dataFinal, Long idCategoria,
			Long[] processos) {
		String qp = "";
		if (processos != null) {
			for (Long p : processos) {
				qp += " processo.id=" + p;
			}
			qp = qp.trim().replace(" ", " or ");
			qp = " and (" + qp + ")";
		}
		List<Indicador> listaIndicadores = daoIndicador.listar(Indicador.class,
				"utilizarAnalise is true and categoriaIndicador.id=" + idCategoria + qp);
		listaIndicadores = calcularValorGruposLancamentos(dataInicial, dataFinal, listaIndicadores, null);
		// listaIndicadores = calcularValorFinalIndicador();
		return listaIndicadores;
	}

	private List<Indicador> calcularValorGruposLancamentos(Date dataInicial, Date dataFinal,
			List<Indicador> listaIndicadores, Integer ano) {
		List<Indicador> lr = new ArrayList<>();
		for (Indicador in : listaIndicadores) {
			Indicador indicador = new Indicador();
			indicador.setId(in.getId());
			indicador.setObservacao("");
			indicador.setDescricao(in.getDescricao());
			indicador.setMetaMaiorMenorQue(in.getMetaMaiorMenorQue());

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

			// INÍCIO DOS CÁLCULOS PARA OS MESES
			if (ano != null) {
				// JANEIRO
				String expressao1 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/01/" + ano), ConverteStringDate.retornaData("31/01/" + ano));
				try {
					if (engine.eval(expressao1) != null) {
						indicador.setValor1(Double.parseDouble(engine.eval(expressao1).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// FEVEREIRO
				String expressao2 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/02/" + ano), ConverteStringDate.retornaData("29/02/" + ano));
				try {
					if (engine.eval(expressao2) != null) {
						indicador.setValor2(Double.parseDouble(engine.eval(expressao2).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// MARÇO
				String expressao3 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/03/" + ano), ConverteStringDate.retornaData("31/03/" + ano));
				try {
					if (engine.eval(expressao3) != null) {
						indicador.setValor3(Double.parseDouble(engine.eval(expressao3).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// ABRIL
				String expressao4 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/04/" + ano), ConverteStringDate.retornaData("30/04/" + ano));
				try {
					if (engine.eval(expressao4) != null) {
						indicador.setValor4(Double.parseDouble(engine.eval(expressao4).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// MAIO
				String expressao5 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/05/" + ano), ConverteStringDate.retornaData("31/05/" + ano));
				try {
					if (engine.eval(expressao5) != null) {
						indicador.setValor5(Double.parseDouble(engine.eval(expressao5).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// JUNHO
				String expressao6 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/06/" + ano), ConverteStringDate.retornaData("30/06/" + ano));
				try {
					if (engine.eval(expressao6) != null) {
						indicador.setValor6(Double.parseDouble(engine.eval(expressao6).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// JULHO
				String expressao7 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/07/" + ano), ConverteStringDate.retornaData("31/07/" + ano));
				try {
					if (engine.eval(expressao7) != null) {
						indicador.setValor7(Double.parseDouble(engine.eval(expressao7).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// AGOSTO
				String expressao8 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/08/" + ano), ConverteStringDate.retornaData("31/08/" + ano));
				try {
					if (engine.eval(expressao8) != null) {
						indicador.setValor8(Double.parseDouble(engine.eval(expressao8).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// SETEMBRO
				String expressao9 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/09/" + ano), ConverteStringDate.retornaData("30/09/" + ano));
				try {
					if (engine.eval(expressao9) != null) {
						indicador.setValor9(Double.parseDouble(engine.eval(expressao9).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// OUTUBRO
				String expressao10 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/10/" + ano), ConverteStringDate.retornaData("31/10/" + ano));
				try {
					if (engine.eval(expressao10) != null) {
						indicador.setValor10(Double.parseDouble(engine.eval(expressao10).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// NOVEMBRO
				String expressao11 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/11/" + ano), ConverteStringDate.retornaData("30/11/" + ano));
				try {
					if (engine.eval(expressao11) != null) {
						indicador.setValor11(Double.parseDouble(engine.eval(expressao11).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// DEZEMBRO
				String expressao12 = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/12/" + ano), ConverteStringDate.retornaData("31/12/" + ano));
				try {
					if (engine.eval(expressao12) != null) {
						indicador.setValor12(Double.parseDouble(engine.eval(expressao12).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				
				// NOVEMBRO Anterior
				String expressao11Anterior = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/11/" + (ano-1)), ConverteStringDate.retornaData("30/11/" + (ano-1)));
				try {
					if (engine.eval(expressao11Anterior) != null) {
						indicador.setValor11Anterior(Double.parseDouble(engine.eval(expressao11Anterior).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}
				// DEZEMBRO Anterior
				String expressao12Anterior = montarExpressaoGrupoLancamentos(in.getFormulaGrupoLancamento(),
						ConverteStringDate.retornaData("01/12/" + (ano-1)), ConverteStringDate.retornaData("31/12/" + (ano-1)));
				try {
					if (engine.eval(expressao12Anterior) != null) {
						indicador.setValor12Anterior(Double.parseDouble(engine.eval(expressao12Anterior).toString()));
					}
				} catch (Exception e) {
					System.out.println("erro na equação");

				}

			}
			// FIM DOS CÁLCULOS PARA OS MESES

			indicador.setValorCalculoGrupoLancamento(in.getValorCalculoGrupoLancamento());
			indicador.setValorFinal(in.getValorFinal());
			
			indicador.setMeta01(in.getMeta01());
			indicador.setMeta02(in.getMeta02());
			indicador.setMeta03(in.getMeta03());
			indicador.setMeta04(in.getMeta04());
			indicador.setMeta05(in.getMeta05());
			indicador.setMeta06(in.getMeta06());
			indicador.setMeta07(in.getMeta07());
			indicador.setMeta08(in.getMeta08());
			indicador.setMeta09(in.getMeta09());
			indicador.setMeta10(in.getMeta10());
			indicador.setMeta11(in.getMeta11());
			indicador.setMeta12(in.getMeta12());
			

			lr.add(indicador);
		}
		return lr;
	}

	private List<Indicador> calcularValorFinalIndicador(List<Indicador> listaIndicadores) {
		for (Indicador in : listaIndicadores) {
			Double valor = 0.;
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String foo = montarExpressaoIndicador(in.getFormulaIndicador(), listaIndicadores);
			System.out.println("FooInd: " + foo);
			try {
				if (engine.eval(foo) != null) {
					valor = Double.parseDouble(engine.eval(foo).toString());
				}

			} catch (Exception e) {
				System.out.println("erro na equação");

			}
			in.setValorFinal(valor + in.getValorFinal());
		}
		return listaIndicadores;
	}

	private Double buscarValorItensLancamento(Long idGrupoLancamento, Date dataInicial, Date dataFinal) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("DATA Formatada: " + format.format(dataInicial));
		List<ItensLancamento> listItensLancamento = new ArrayList<>();
		listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
				"grupoLancamento.id=" + idGrupoLancamento + " and dataLancamento between '" + format.format(dataInicial)
						+ "' and '" + format.format(dataFinal) + "'");
		Double valor = 0.;
		for (ItensLancamento it : listItensLancamento) {
			valor += it.getValor();
		}
		return valor;
	}

	private Double buscarValorIndicador(Long idIndicador, List<Indicador> listaIndicadores) {
		Double valor = 0.;
		for (Indicador i : listaIndicadores) {
			if (i.getId() == idIndicador) {
				System.out.println("No somar:::-----: " + i.getValorFinal());
				valor += i.getValorFinal();
			}
		}
		return valor;
	}

	private int buscarQuantidadeItensLancamento(Long idGrupoLancamento, Date dataInicial, Date dataFinal) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<ItensLancamento> listItensLancamento = new ArrayList<>();
		listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
				"grupoLancamento.id=" + idGrupoLancamento + " and dataLancamento between '" + format.format(dataInicial)
						+ "' and '" + format.format(dataFinal) + "'");

		return listItensLancamento.size();
	}

	private String montarExpressaoIndicador(String formula, List<Indicador> listaIndicadores) {

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
				equacao += String.valueOf(buscarValorIndicador(Long.parseLong(result[x + 2]), listaIndicadores));
				x = x + 3;
			} else if (posicao.equals("contar")) {
				equacao += String.valueOf(buscarValorIndicador(Long.parseLong(result[x + 2]), listaIndicadores));
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

	private Double buscaValorIndicador(Long id, List<ItensLancamento> listItensLancamento) {
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

		List<ItensLancamento> listItensLancamento = new ArrayList<>();
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
