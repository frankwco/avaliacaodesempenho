package base.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "ocorrencia")
public class Ocorrencia implements Serializable {

	public Ocorrencia() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length=5000)
	private String descricao;
	
	private String titulo;
	
	//vai de 1 a 5
	private int gravidade;
	
	@ManyToOne
	private Indicador indicadorRelacionado;
	
	@ManyToOne
	private Pessoa responsavelRegistro;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataOcorrencia = new Date();
	
	private boolean status;
	
	@ManyToOne
	private Atividade atividade;


	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Pessoa getResponsavelRegistro() {
		return responsavelRegistro;
	}

	public void setResponsavelRegistro(Pessoa responsavelRegistro) {
		this.responsavelRegistro = responsavelRegistro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getGravidade() {
		return gravidade;
	}

	public void setGravidade(int gravidade) {
		this.gravidade = gravidade;
	}

	public Indicador getIndicadorRelacionado() {
		return indicadorRelacionado;
	}

	public void setIndicadorRelacionado(Indicador indicadorRelacionado) {
		this.indicadorRelacionado = indicadorRelacionado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ocorrencia other = (Ocorrencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
