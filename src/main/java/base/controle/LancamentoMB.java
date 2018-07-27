package base.controle;

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
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
import util.ConverteStringDate;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import dao.GenericDAO;
import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@ViewScoped
@Named("lancamentoMB")
public class LancamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Lancamento lancamento;
	private List<Lancamento> lancamentoBusca;
	private List<Lancamento> listLancamento;
	private List<ItensLancamento> listItensLancamento;
	private GrupoLancamento grupoLancamentoSelecionado;
	private ItensLancamento itensLancamento;

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

	}

	public void adicionarItemLancamento() {
		listItensLancamento.add(itensLancamento);
		itensLancamento = new ItensLancamento();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogInserirLancamento();
	}

	public void verificaValor(Double valor) {
		System.out.println(valor);
	}

	@Deprecated
	public void calcularIndicadores() {
		for (ItensLancamento it : listItensLancamento) {
			if (!it.getIndicador().getParametros().trim().equals("+")) {
				// String expressao = "($1:@Valor com Ped�gio;+$2:@Gastos com
				// Combust�vel;+3)/2";
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
					it.setValor(Double.parseDouble(
							new ScriptEngineManager().getEngineByName("JavaScript").eval(expressao).toString()));
					System.out.println("Valor IT: " + it.getValor());
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

	public void uploadDados(FileUploadEvent evento) {
		System.out.println("DEntro do Upload de Dados");
		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {

				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				Path origem = Paths.get(arquivoTemp.toString());

				Path destino = Paths.get(System.getProperty("java.io.tmpdir") + "//" + evento.getFile().getFileName());
				Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

				/*
				 * File tempdir = new File(System.getProperty("java.io.tmpdir"));
				 * 
				 * File tempFile = File.createTempFile("arquivo_dados_indic", ".xls", tempdir);
				 * 
				 * tempFile.deleteOnExit(); try {
				 * 
				 * } finally { tempFile.delete(); }
				 */
				System.out.println(System.getProperty("java.io.tmpdir") + "//" + arquivoUpload.getFileName());
				ExibirMensagem.exibirMensagem(Mensagem.UPLOAD);

				importaDados(String.valueOf(arquivoUpload.getFileName()));

			}

		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void importaDados(String nomeArquivo) throws IOException, BiffException {

		Workbook workbook = Workbook.getWorkbook(new File(System.getProperty("java.io.tmpdir") + "/" + nomeArquivo));

		Sheet sheet = workbook.getSheet(0);

		boolean controle;
		int linhas = sheet.getRows();

		for (int i = 1; i < linhas; i++) {

			Cell celulaGrupoLancamento = sheet.getCell(0, i);
			Cell celulaData = sheet.getCell(1, i);
			Cell celulaValor = sheet.getCell(2, i);
			Cell celulaObservacao = sheet.getCell(3, i);

			String grupoLancamento = celulaGrupoLancamento.getContents();
			Date data = ConverteStringDate.retornaData(celulaData.getContents());
			String valor = celulaValor.getContents();
			String observacao = celulaObservacao.getContents();
			
			itensLancamento = new ItensLancamento();
			itensLancamento.setDataLancamento(data);
			itensLancamento.setGrupoLancamento(retornaGrupoLancamentoDescricao(grupoLancamento));
			itensLancamento.setObservacao(observacao);
			try {
				itensLancamento.setValor(Double.parseDouble(valor.replace(",", ".")));
			}catch(Exception e) {
				itensLancamento.setValor(0.0);
				itensLancamento.setObservacao(observacao+" - Ocorreu erro na importação do valor");
			}
			
			listItensLancamento.add(itensLancamento);
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			System.out.print("GL: " + grupoLancamento);
			System.out.print(" Data: " + dateFormat.format(data));
			System.out.print(" Valor: " + valor);
			System.out.println(" Observação: " + observacao);

		}

		workbook.close();

	}
	
	private GrupoLancamento retornaGrupoLancamentoDescricao(String descricao) {
		GrupoLancamento grupo = null;
		List<GrupoLancamento> listaGrupol = daoGrupoLancamentos.listar(GrupoLancamento.class, "descricao='"+descricao.trim()+"'");
		if(listaGrupol.size()>0) {
			grupo = listaGrupol.get(0);
		}
		
		return grupo;
	}

	

	public void finalizarLancamentos() {
		System.out.println("Finalizando os Lançamentos");
		lancamento.setStatus(true);
		lancamentoService.inserirAlterar(lancamento);
		for (ItensLancamento it : listItensLancamento) {
			it.setStatus(true);
			it.setLancamento(lancamento);
			itensLancamentoService.inserirAlterar(it);

		}
		criarNovoObjeto();
		listItensLancamento = new ArrayList<>();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
	}

	public void preencherLista(Lancamento t) {
		this.lancamento = t;
		buscarIndicadores();

	}

	@Deprecated
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
				System.out.println("Buscando os Itens do Lan�amento");
				listItensLancamento = daoItensLancamento.listar(ItensLancamento.class,
						"lancamento.id=" + lancamento.getId());
			}
		}
	}

	public void inativar(Lancamento t) {

		t.setStatus(false);
		lancamentoService.inserirAlterar(t);
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void inativarItens(ItensLancamento t) {

		if (t.getId() != null) {
			t.setStatus(false);
			itensLancamentoService.inserirAlterar(t);
			carregarListaItens();
		} else {
			System.out.println("Aqui no remover");

			listItensLancamento.remove(t);
		}

		criarNovoObjetoItens();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

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

	public void criarNovoObjetoItens() {
		itensLancamento = new ItensLancamento();
	}

	public void carregarLista() {
		listLancamento = daoLancamento.listaComStatus(Lancamento.class);
	}

	public void carregarListaItens() {
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
