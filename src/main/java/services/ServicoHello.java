package services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import base.modelo.Atividade;
import base.modelo.CategoriaIndicador;
import base.modelo.GrupoLancamento;
import base.modelo.Indicador;
import base.modelo.ItensLancamento;
import base.modelo.Lancamento;
import base.modelo.Ocorrencia;
import base.modelo.Processo;
import base.modelo.Retorno;
import base.modelo.Usuario;
import base.service.ItensLancamentoService;
import base.service.LancamentoService;
import base.service.OcorrenciaService;
import dados.ImcMock;
import dados.TimesMock;
import dao.GenericDAO;
import modelo.Imc;
import modelo.Time;
import util.ConverteStringDate;
import util.CriptografiaSenha;
import util.FuncoesMatematicas;

@Path("/service")
public class ServicoHello {

	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas

	@Inject
	private GenericDAO<Indicador> daoIndicador; // faz as buscas

	@Inject
	private GenericDAO<Atividade> daoAtividade; // faz as buscas

	@Inject
	private GenericDAO<Usuario> daoUsuario; // faz as buscas

	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicadores; // faz as buscas

	@Inject
	private GenericDAO<GrupoLancamento> daoGrupoLancamento; // faz as buscas

	@Inject
	private FuncoesMatematicas funcoesMatematicas;

	@Inject
	private OcorrenciaService ocorrenciaService; // inserir no banco

	@Inject
	private LancamentoService lancamentoService; // inserir no banco

	@Inject
	private ItensLancamentoService itensLancamentoService; // inserir no banco

	@POST
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/inserirOcorrencia")
	public Response inserirOcorrencia(List<Ocorrencia> ocorrencias) {
		System.out.println("Quantidade: " + ocorrencias.size());

		for (Ocorrencia o : ocorrencias) {
			o.setStatus(true);
			if (o.getAtividade() != null && o.getAtividade().getId() != null) {
				Atividade a = daoAtividade.buscarPorId(Atividade.class, o.getAtividade().getId());
				o.setAtividade(a);
			} else {
				o.setAtividade(null);
			}
			if (o.getIndicadorRelacionado() != null && o.getIndicadorRelacionado().getId() != null) {
				Indicador a = daoIndicador.buscarPorId(Indicador.class, o.getIndicadorRelacionado().getId());
				o.setIndicadorRelacionado(a);
			} else {
				o.setIndicadorRelacionado(null);
			}
			// o.setAtividade(null);
			// o.setIndicadorRelacionado(null);
			System.out.println(o.getDescricao());
			System.out.println(o.getAtividade());
			ocorrenciaService.inserirAlterar(o);
		}

		GenericEntity<Retorno> lista = new GenericEntity<Retorno>(new Retorno("Sucesso")) {

		};

		// return Response.status(200).entity(new Retorno("Sucesso")).build();
		String json = "{\"situacao\":\"feito\"}";// convert entity to json
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/inserirLancamento")
	public Response inserirLancamento(List<ItensLancamento> itensLancamento) {

		for (ItensLancamento o : itensLancamento) {
			o.setStatus(true);
			Lancamento lan = new Lancamento();
			lan.setStatus(true);
			lan.setDataLancamento(o.getDataLancamento());

			o.setStatus(true);
			if (o.getAtividade() != null && o.getAtividade().getId() != null) {
				Atividade a = daoAtividade.buscarPorId(Atividade.class, o.getAtividade().getId());
				lan.setAtividade(a);
			} else {
				o.setAtividade(null);
			}
			lancamentoService.inserirAlterar(lan);
			if (o.getGrupoLancamento() != null && o.getGrupoLancamento().getId() != null) {
				GrupoLancamento a = daoGrupoLancamento.buscarPorId(GrupoLancamento.class,
						o.getGrupoLancamento().getId());
				o.setGrupoLancamento(a);
			} else {
				o.setGrupoLancamento(null);
			}
			o.setLancamento(lan);
			// o.setAtividade(null);
			// o.setIndicadorRelacionado(null);
			System.out.println(o.getDescricao());
			System.out.println(o.getAtividade());
			itensLancamentoService.inserirAlterar(o);
		}

		GenericEntity<Retorno> lista = new GenericEntity<Retorno>(new Retorno("Sucesso")) {

		};

		// return Response.status(200).entity(new Retorno("Sucesso")).build();
		String json = "{\"situacao\":\"feito\"}";// convert entity to json
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/processos")
	public Response processos() {
		System.out.println("Dentro da Lista de Inventáriossss");
		List<Processo> processos = daoProcesso.listaComStatus(Processo.class);
		for (Processo p : processos) {
			p.setIndicadores(null);
		}

		GenericEntity<List<Processo>> lista = new GenericEntity<List<Processo>>(processos) {

		};
		return Response.status(200).entity(lista).build();

	}

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/variaveis")
	public Response variaveisCalculo() {
//		System.out.println("Dentro da Lista de Inventáriossss");
		List<GrupoLancamento> processos = daoGrupoLancamento.listaComStatus(GrupoLancamento.class);

		GenericEntity<List<GrupoLancamento>> lista = new GenericEntity<List<GrupoLancamento>>(processos) {

		};
		return Response.status(200).entity(lista).build();

	}

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/atividades")
	public Response atividade() {
//			System.out.println("Dentro da Lista de Inventáriossss");
		List<Atividade> lista = daoAtividade.listaComStatus(Atividade.class);
//		for(Atividade p:lista) {
//			p.setIndicadores(null);
//		}

		GenericEntity<List<Atividade>> retorno = new GenericEntity<List<Atividade>>(lista) {

		};
		return Response.status(200).entity(retorno).build();

	}

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/indicadores")
	public Response indicador() {
		System.out.println("Dentro da Lista de Inventáriossss");
		List<Indicador> lista = daoIndicador.listaComStatus(Indicador.class);

		for (Indicador p : lista) {
			p.setCategoriaIndicador(null);
			p.setProcesso(null);
		}

		GenericEntity<List<Indicador>> retorno = new GenericEntity<List<Indicador>>(lista) {

		};
		System.out.println("TAM: " + lista.size());

		return Response.status(200).entity(retorno).build();

	}

	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/autenticar")
	public Response autenticar(@QueryParam("email") String email, @QueryParam("senha") String senha) {
//			System.out.println("Dentro da Lista de Inventáriossss");
		List<Usuario> lista = daoUsuario.listarCadastro(Usuario.class, "email='" + email + "'");
		Usuario usuario = null;
		if (lista.size() > 0) {
			if (CriptografiaSenha.decript(senha, lista.get(0).getSenha())) {
				usuario = lista.get(0);
			}
		}

		if (usuario != null) {
			GenericEntity<Usuario> retorno = new GenericEntity<Usuario>(usuario) {
			};
			return Response.status(200).entity(retorno).build();
		} else {
			return Response.status(401).entity("falhou").build();
		}
	}

	// http://localhost:8080/avaliacaodesempenho/rest/service/indicadoresDataProcesso?categoria=Custo&dataInicial=01/08/2018&dataFinal=20/08/2018&processos=1;2;3;4;5;6;7;8
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/indicadoresDataProcesso")
	public Response indicadores(@QueryParam("categoria") String categoria, @QueryParam("mes") String mes,
			@QueryParam("ano") String ano, @QueryParam("processos") String processos,
			@QueryParam("indicador") String indicador) {
		String[] process = processos.split(";");
		Long[] listaProcessos = new Long[process.length];

		for (int x = 0; x < process.length; x++) {
			listaProcessos[x] = Long.parseLong(process[x]);
		}

		List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
				"descricao='" + categoria + "'");
		CategoriaIndicador categoriaIndicador = listIndic.get(0);
		List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessosMesAno(Integer.parseInt(mes),
				Integer.parseInt(ano), categoriaIndicador.getId(), listaProcessos);

		List<Indicador> liRetorno = new ArrayList<>();
		Long ind = Long.parseLong(indicador);
		for (Indicador i : li) {
			if (ind.equals(i.getId())) {
				liRetorno.add(i);
			}
		}

		GenericEntity<List<Indicador>> lista = new GenericEntity<List<Indicador>>(liRetorno) {
		};
		return Response.status(200).entity(lista).build();
	}

	// INÍCIO IMC

	// {"usuario":"1","peso":80,"altura":1.78,"imc":24,"situação":"acima do peso"}
	// {"nome":"João da
	// Silva","usuario":"1","peso":80,"altura":1.78,"imc":24,"situação":"acima do
	// peso"}
	// http://localhost:8080/hellows/rest/service/inserirImc
	@POST
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/inserirImc")
	public Response adicionarImc(Imc imc) {
		ImcMock.get().add(imc);
		System.out.println("Data: " + imc.getDataVerificacao());
		return Response.status(200).entity("{\"situacao\":\"inserido\"}").build();

	}

	// http://localhost:8080/hellows/rest/service/listaImc
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/listaImc")
	public Response listaImcGeral(@QueryParam("usuario") String usuario) {
		List<Imc> lt = ImcMock.getUsuario(usuario);
		GenericEntity<List<Imc>> lista = new GenericEntity<List<Imc>>(lt) {
		};
		return Response.status(200).entity(lista).build();
	}

	// http://localhost:8080/hellows/rest/service/limparImcUsuario
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/limparImcUsuario")
	public Response limparImcGeral(@QueryParam("usuario") String usuario) {
		ImcMock.removerImc(usuario);
		return Response.status(200).entity("{\"situacao\":\"lista de IMC do usuário " + usuario + " vazia\"}").build();
	}

	// http://localhost:8080/hellows/rest/service/limparImc
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/limparImc")
	public Response limparImc() {
		ImcMock.get().clear();
		return Response.status(200).entity("{\"situacao\":\"lista de IMC vazia\"}").build();
	}

	// FIM IMC

	// http://localhost:8080/hellows/rest/service/somarInteiros?valor=1&valor2=3
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/somarInteiros")
	public String helloWebService(@QueryParam("valor") Integer valor, @QueryParam("valor2") Integer valor2) {
		// return (valor+valor2);
		return "" + (valor + valor2);

	}

	// http://localhost:8080/hellows/rest/service/inserirTime
	@POST
	@Produces("application/json; charset=UTF-8")
	@Path("/inserirTime")
	public String recebeObjeto(Time time) {
		// System.out.println("Time inserido " + time.getNome());
		TimesMock.get().add(time);
		return "inserido";

	}

	// http://localhost:8080/hellows/rest/service/listaTimes
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/limparTimes")
	public String limparTimes() {

		TimesMock.get().clear();
		;

		return "Lista Vazia";
	}

	// http://localhost:8080/hellows/rest/service/listaTimes
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listaTimes")
	public Response listaTimesGeral() {

		List<Time> lt = TimesMock.get();

		GenericEntity<List<Time>> lista = new GenericEntity<List<Time>>(lt) {
		};
		return Response.status(200).entity(lista).build();
	}

	// http://localhost:8080/hellows/rest/service/listaTimesSP
	@GET
	@Produces("application/json; charset=UTF-8")
	// @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
	@Path("/listaTimesSP")
	public Response listaTimes() {

		List<Time> lt = new ArrayList<>();
		lt.add(new Time("Palmeiras - Maior Campeão Brasileiro", "São Paulo"));
		lt.add(new Time("São Paulo", "São Paulo"));
		lt.add(new Time("Corinthians", "São Paulo"));
		lt.add(new Time("Santos", "São Paulo"));
		lt.add(new Time("Ituano", "São Paulo"));
		lt.add(new Time("Linense", "São Paulo"));

		GenericEntity<List<Time>> lista = new GenericEntity<List<Time>>(lt) {
		};
		// return lt;
//		return Response.status(200).entity(lista).build();
		return Response.ok(lista, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listaTimesRio")
	public List<Time> listaTimes2() {

		List<Time> lt = new ArrayList<>();

		lt.add(new Time("Flamengo", "Rio de Janeiro"));
		lt.add(new Time("Vasco", "Rio de Janeiro"));

		// GenericEntity<List<Time>> lista = new
		// GenericEntity<List<Time>>(lt){};
		return lt;
		// return Response.status(200).entity(lista).build();
	}

	// http://localhost:8080/hellows/rest/service/hello2/10/10
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello2/{valor}/{valor2}")
	public String helloWebService2(@PathParam("valor") Integer valor, @PathParam("valor2") Integer valor2) {
		return "Olá Mundo WebService " + (valor + valor2);
	}

	// http://localhost:8080/hellows/rest/service/hello3/10/
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/hello3/{valor}")
	public String helloWebService3(@PathParam("valor") Integer valor) {
		return "Olá Mundo WebService " + valor;
	}

}
