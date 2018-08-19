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
import javax.ws.rs.core.Response;


import base.modelo.CategoriaIndicador;
import base.modelo.Indicador;
import base.modelo.Processo;
import dados.ImcMock;
import dados.TimesMock;
import dao.GenericDAO;
import modelo.Imc;
import modelo.Time;
import util.ConverteStringDate;
import util.FuncoesMatematicas;

@Path("/service")
public class ServicoHello {
	
	@Inject
	private GenericDAO<Processo> daoProcesso; // faz as buscas
	
	@Inject
	private GenericDAO<CategoriaIndicador> daoCategoriaIndicadores; // faz as buscas
	
	
	@Inject
	private FuncoesMatematicas funcoesMatematicas;
	
	
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/processos")
	public Response processos() {
			System.out.println("Dentro da Lista de Inventáriossss");
		List<Processo> processos = daoProcesso.listaComStatus(Processo.class);
		for(Processo p:processos) {
			p.setIndicadores(null);
		}
		
		GenericEntity<List<Processo>> lista = new GenericEntity<List<Processo>>(processos) {
			
		};
		return Response.status(200).entity(lista).build();

	}
	
	//http://localhost:8080/avaliacaodesempenho/rest/service/indicadoresDataProcesso?categoria=Custo&dataInicial=01/08/2018&dataFinal=20/08/2018&processos=1;2;3;4;5;6;7;8
		@GET
		@Produces("application/json;charset=UTF-8")
		@Consumes("application/json;charset=UTF-8")
		@Path("/indicadoresDataProcesso")
		public Response indicadores(@QueryParam("categoria") String categoria, @QueryParam("dataInicial") String dataInicial, @QueryParam("dataFinal") String dataFinal, @QueryParam("processos") String processos ) {
			String[] process = processos.split(";");
			Long[] listaProcessos = new Long[process.length];
			
			for(int x=0;x<process.length;x++) {
				listaProcessos[x]=Long.parseLong(process[x]);
			}
			
			List<CategoriaIndicador> listIndic = daoCategoriaIndicadores.listar(CategoriaIndicador.class,
					"descricao='" + categoria + "'");
			CategoriaIndicador categoriaIndicador = listIndic.get(0);
			List<Indicador> li = funcoesMatematicas.calcularIndicadoresPorCategoriaProcessos(ConverteStringDate.retornaData(dataInicial), ConverteStringDate.retornaData(dataFinal),
					categoriaIndicador.getId(), listaProcessos);		

			GenericEntity<List<Indicador>> lista = new GenericEntity<List<Indicador>>(li) {};
			return Response.status(200).entity(lista).build();
		}
	
	
	//INÍCIO IMC
	
	//{"usuario":"1","peso":80,"altura":1.78,"imc":24,"situação":"acima do peso"}
	//{"nome":"João da Silva","usuario":"1","peso":80,"altura":1.78,"imc":24,"situação":"acima do peso"}
	// http://localhost:8080/hellows/rest/service/inserirImc
	@POST
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/inserirImc")
	public Response adicionarImc(Imc imc) {
		ImcMock.get().add(imc);
		System.out.println("Data: "+imc.getDataVerificacao());
		return Response.status(200).entity("{\"situacao\":\"inserido\"}").build();

	}
	
	// http://localhost:8080/hellows/rest/service/listaImc
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/listaImc")
	public Response listaImcGeral(@QueryParam("usuario") String usuario) {
		List<Imc> lt = ImcMock.getUsuario(usuario);
		GenericEntity<List<Imc>> lista = new GenericEntity<List<Imc>>(lt) {};
		return Response.status(200).entity(lista).build();
	}
	
	// http://localhost:8080/hellows/rest/service/limparImcUsuario
	@GET
	@Produces("application/json;charset=UTF-8")
	@Consumes("application/json;charset=UTF-8")
	@Path("/limparImcUsuario")
	public Response limparImcGeral(@QueryParam("usuario") String usuario) {
		ImcMock.removerImc(usuario);	
		return Response.status(200).entity("{\"situacao\":\"lista de IMC do usuário "+usuario+" vazia\"}").build();
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
	
	
	//FIM IMC
	
	
	

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
		//System.out.println("Time inserido " + time.getNome());
		TimesMock.get().add(time);
		return "inserido";

	}
	
	// http://localhost:8080/hellows/rest/service/listaTimes
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/limparTimes")
	public String limparTimes() {

		TimesMock.get().clear();;

	
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
		return Response.status(200).entity(lista).build();
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
