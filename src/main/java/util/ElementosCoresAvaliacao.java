package util;

public class ElementosCoresAvaliacao {
	private String statusVermelho = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:red\"></i>";
	private String statusVerde = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:green\"></i>";
	private String statusVerdeFraco = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:#7CFC00\"></i>";
	private String statusAmarelo = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:yellow\"></i>";
	private String statusLaranja = "<i class=\"fa fa-circle\" style=\"font-size:36px;color:orange\"></i>";

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

	public String retornaEstadoPorcentagem(Double meta, Double realizado) {
		Double a= new Double(Double.NaN);
		if(realizado.equals(a)) {
			realizado = 0.;
		}
		//ISSO PARA VALORES QUE NÃO PODEM ULTRAPASSAR A META
		Double resultado = meta / realizado * 100;
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
