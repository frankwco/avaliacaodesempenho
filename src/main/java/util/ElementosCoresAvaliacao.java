package util;

public class ElementosCoresAvaliacao {
	private String statusVermelho = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#ff0000\"></i>";
	private String statusVerde = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#00a000\"></i>";
	private String statusVerdeFraco = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#7CFC00\"></i>";
	private String statusAmarelo = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#FFF400\"></i>";
	private String statusLaranja = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#ff8100\"></i>";
	
	private String corVermelho = "#ff0000";
	private String corVerde = "#00a000";
	private String corVerdeFraco = "#7CFC00";
	private String corAmarelo = "#FFF400";
	private String corLaranja = "#ff8100";
	
	

	private String statusCirculoPretoPequeno = "<i class=\"fa fa-circle\"></i>";

	public String get(Integer gravidade) {
		if (gravidade != null) {
			switch (gravidade) {
			case 1:
				return statusVerde;
			case 2:
				return statusVerdeFraco;
			case 3:
				return statusAmarelo;
			case 4:
				return statusLaranja;
			case 5:
				return statusVermelho;
			default:
				return "";
			}
		} else {
			return "";
		}

	}
	
	public String getCor(Integer gravidade) {
		if (gravidade != null) {
			switch (gravidade) {
			case 1:
				return corVerde;
			case 2:
				return corVerdeFraco;
			case 3:
				return corAmarelo;
			case 4:
				return corLaranja;
			case 5:
				return corVermelho;
			default:
				return "";
			}
		} else {
			return "";
		}

	}

	public String retornaEstadoPorcentagem(Double meta, Double realizado, String maiorMenorQue) {
		Double a = new Double(Double.NaN);
		if (realizado.equals(a)) {
			realizado = 0.;
		}
		Double resultado = 0.;
		// ISSO PARA VALORES QUE NÃO PODEM ULTRAPASSAR A META
		//System.out.println("Maior Menor que: "+maiorMenorQue);
		if (maiorMenorQue.equals("<=Meta")) {
			resultado = (meta / realizado) * 100;
			//System.out.println("REsultado: "+resultado);
			if (resultado < 60) {
				return get(5);
			} else if (resultado < 80) {
				return get(4);
			} else if (resultado < 90) {
				return get(3);
			} else if (resultado < 100) {
				return get(2);
			} else {
				return get(1);
			}
		} else {
			resultado = (realizado / meta)*100;
			if (resultado < 60) {
				return get(5);
			} else if (resultado < 80) {
				return get(4);
			} else if (resultado < 90) {
				return get(3);
			} else if (resultado < 100) {
				return get(2);
			} else {
				return get(1);
			}
		}

	}
	
	public String retornaEstadoPorcentagemCor(Double meta, Double realizado, String maiorMenorQue) {
		Double a = new Double(Double.NaN);
		if (realizado.equals(a)) {
			realizado = 0.;
		}
		Double resultado = 0.;
		// ISSO PARA VALORES QUE NÃO PODEM ULTRAPASSAR A META
		//System.out.println("Maior Menor que: "+maiorMenorQue);
		if (maiorMenorQue.equals("<=Meta")) {
			resultado = (meta / realizado) * 100;
			//System.out.println("REsultado: "+resultado);
			if (resultado < 60) {
				return getCor(5);
			} else if (resultado < 80) {
				return getCor(4);
			} else if (resultado < 90) {
				return getCor(3);
			} else if (resultado < 100) {
				return getCor(2);
			} else {
				return getCor(1);
			}
		} else {
			resultado = (realizado / meta)*100;
			if (resultado < 60) {
				return getCor(5);
			} else if (resultado < 80) {
				return getCor(4);
			} else if (resultado < 90) {
				return getCor(3);
			} else if (resultado < 100) {
				return getCor(2);
			} else {
				return getCor(1);
			}
		}

	}
	
	public Double retornaPorcentagem(Double meta, Double realizado, String maiorMenorQue) {
		Double a = new Double(Double.NaN);
		if (realizado.equals(a)) {
			realizado = 0.;
		}
		
		Double resultado = 0.;
		// ISSO PARA VALORES QUE NÃO PODEM ULTRAPASSAR A META
		//System.out.println("Maior Menor que: "+maiorMenorQue);
		if (maiorMenorQue.equals("<=Meta")) {
			resultado = (meta / realizado) * 100;
			//System.out.println("REsultado: "+resultado);			
			return resultado;
		} else {
			resultado = (realizado / meta)*100;
			return resultado;
		}

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

	public String getStatusVerdeFraco() {
		return statusVerdeFraco;
	}

	public void setStatusVerdeFraco(String statusVerdeFraco) {
		this.statusVerdeFraco = statusVerdeFraco;
	}

	public String getStatusAmarelo() {
		return statusAmarelo;
	}

	public void setStatusAmarelo(String statusAmarelo) {
		this.statusAmarelo = statusAmarelo;
	}

	public String getStatusLaranja() {
		return statusLaranja;
	}

	public void setStatusLaranja(String statusLaranja) {
		this.statusLaranja = statusLaranja;
	}

	public String getStatusCirculoPretoPequeno() {
		return statusCirculoPretoPequeno;
	}

	public void setStatusCirculoPretoPequeno(String statusCirculoPretoPequeno) {
		this.statusCirculoPretoPequeno = statusCirculoPretoPequeno;
	}

}
