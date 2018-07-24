package base.modelo;

public class Smart {

	private boolean especifico = true;
	private boolean mensuravel = true;
	private boolean atingivel = true;
	private boolean relevante = true;
	private boolean temporizavel = true;

	public boolean isEspecifico() {
		return especifico;
	}

	public void setEspecifico(boolean especifico) {
		this.especifico = especifico;
	}

	public boolean isMensuravel() {
		return mensuravel;
	}

	public void setMensuravel(boolean mensuravel) {
		this.mensuravel = mensuravel;
	}

	public boolean isAtingivel() {
		return atingivel;
	}

	public void setAtingivel(boolean atingivel) {
		this.atingivel = atingivel;
	}

	public boolean isRelevante() {
		return relevante;
	}

	public void setRelevante(boolean relevante) {
		this.relevante = relevante;
	}

	public boolean isTemporizavel() {
		return temporizavel;
	}

	public void setTemporizavel(boolean temporizavel) {
		this.temporizavel = temporizavel;
	}
}
