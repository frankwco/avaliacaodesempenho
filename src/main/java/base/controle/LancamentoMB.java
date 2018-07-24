package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import base.modelo.CategoriaIndicador;
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

@ViewScoped
@Named("lancamentoMB")
public class LancamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Lancamento lancamento;
	private List<Lancamento> lancamentoBusca;
	private List<Lancamento> listLancamento;
	private List<ItensLancamento> listItensLancamento;

	@Inject
	private GenericDAO<Lancamento> daoLancamento; // faz as buscas

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

		listLancamento = new ArrayList<>();
		listLancamento = daoLancamento.listaComStatus(Lancamento.class);
		lancamentoBusca = new ArrayList<>();
		listItensLancamento = new ArrayList<>();

	}

	public void calcularIndicadores() {
		for (ItensLancamento it : listItensLancamento) {
			if (!it.getIndicador().getParametros().trim().equals("+")) {
				// String expressao = "($1:@Valor com Pedágio;+$2:@Gastos com
				// Combustível;+3)/2";
				String expressao = it.getIndicador().getParametros();
				expressao = limpar(expressao);
				List<String> textos = new ArrayList<>();
				for (int x = 0; x < expressao.length(); x++) {
					char ca = expressao.charAt(x);
					if (ca == '$') {
						String textoSubstituir = "";
						for (int y = x; y < expressao.length(); y++) {
							if (expressao.charAt(y) == ':') {
								break;
							} else {
								textoSubstituir += expressao.charAt(y);
							}
						}
						textoSubstituir += ":";
						textos.add(textoSubstituir);
					}
				}
				for (String s : textos) {					
					String idd = s.replace("$", "").replace(":", "");					
					expressao = expressao.replace(s, String.valueOf(buscaValorIndicador(new Long(idd))));
				}
				try {
//					String va = (String) new ScriptEngineManager().getEngineByName("JavaScript").eval(expressao).t;
					it.setValor(Double.parseDouble(new ScriptEngineManager().getEngineByName("JavaScript").eval(expressao).toString()));
					System.out.println("Valor IT: "+it.getValor());
//					System.out.println(new ScriptEngineManager().getEngineByName("JavaScript").eval(expressao));
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

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

	private String limpar(String expressao) {
		List<String> textos = new ArrayList<>();
		for (int x = 0; x < expressao.length(); x++) {
			char ca = expressao.charAt(x);
			if (ca == '@') {
				String textoSubstituir = "";
				for (int y = x; y < expressao.length(); y++) {
					if (expressao.charAt(y) != ';') {
						textoSubstituir += expressao.charAt(y);
					} else {
						textoSubstituir += ";";
						break;
					}
				}
				textos.add(textoSubstituir);
			}
		}
		for (String s : textos) {
			expressao = expressao.replace(s, "");
		}
		return expressao;
	}

	public void finalizarLancamentos() {
		lancamento.setStatus(true);
		lancamentoService.inserirAlterar(lancamento);
		for (ItensLancamento it : listItensLancamento) {
			it.setStatus(true);
			it.setLancamento(lancamento);
			itensLancamentoService.inserirAlterar(it);

		}
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
	}

	public void preencherLista(Lancamento t) {
		this.lancamento = t;
		buscarIndicadores();

	}

	public void buscarIndicadores() {

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

	public void inativar(Lancamento t) {
		t.setStatus(false);
		lancamentoService.inserirAlterar(t);
		// lancamentoService.update(" Lancamento set status = false where id = "
		// +
		// t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			if (lancamento.getId() == null) {
				lancamento.setStatus(true);
				lancamentoService.inserirAlterar(lancamento);

			} else {
				lancamento.setStatus(true);
				lancamentoService.inserirAlterar(lancamento);
			}

			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogTipoServidor();
			carregarLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public void criarNovoObjeto() {
		lancamento = new Lancamento();
	}

	public void carregarLista() {
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

	public List<ItensLancamento> getListItensLancamento() {
		return listItensLancamento;
	}

	public void setListItensLancamento(List<ItensLancamento> listItensLancamento) {
		this.listItensLancamento = listItensLancamento;
	}

}
