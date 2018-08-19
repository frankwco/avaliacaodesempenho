package modelo;


public class Imc {
	private String nome;
	private String usuario="";
	private Double peso;
	private Double altura;
	private Double imc;
	private String situacao;
	 	 	
	private String dataVerificacao;
	

	public Imc() {
	}
	

	public String getDataVerificacao() {
		return dataVerificacao;
	}


	public void setDataVerificacao(String dataVerificacao) {
		this.dataVerificacao = dataVerificacao;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public Double getPeso() {
		return peso;
	}


	public void setPeso(Double peso) {
		this.peso = peso;
	}


	public Double getAltura() {
		return altura;
	}


	public void setAltura(Double altura) {
		this.altura = altura;
	}

	


	public Double getImc() {
		return imc;
	}


	public void setImc(Double imc) {
		this.imc = imc;
	}


	public String getSituacao() {
		return situacao;
	}


	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	

}
