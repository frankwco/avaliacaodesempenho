package base.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "indicador")
public class Indicador implements Serializable {

	public Indicador() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String descricao;

	private String abreviacao;

	private Boolean status;

	private String observacao;

	private boolean utilizarAnalise = true;

	@ManyToOne
	@JoinColumn(name = "idCategoriaIndicador")
	private CategoriaIndicador categoriaIndicador;

	@ManyToOne
	@JoinColumn(name = "idProcesso")
	private Processo processo;

	private String justificativa;

	private String parametros;

	@Transient
	private Double valorCalculoGrupoLancamento;
	@Transient
	private Double valorFinal;

	private String formulaGrupoLancamento = "";

	private String formulaIndicador = "";

	public String getAbreviacao() {
		//System.out.println("Abre: "+abreviacao);
		//System.out.println("Descricao: "+descricao);
		if (abreviacao == null) {
			abreviacao="";
			if (descricao != null) {
				String[] palavra = descricao.split(" ");
				for (String p : palavra) {
					///System.out.println("Tamanho da palavra: "+p);
					if (p.length() > 0) {
						abreviacao += p.charAt(0);
					}
				}
			} else {
				abreviacao = ".";
			}
		}
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	public Double getValorCalculoGrupoLancamento() {
		return valorCalculoGrupoLancamento;
	}

	public void setValorCalculoGrupoLancamento(Double valorCalculoGrupoLancamento) {
		this.valorCalculoGrupoLancamento = valorCalculoGrupoLancamento;
	}

	public Double getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(Double valorFinal) {
		this.valorFinal = valorFinal;
	}

	public String getFormulaIndicador() {
		if (formulaIndicador == null) {
			formulaIndicador = "";
		}
		return formulaIndicador;
	}

	public void setFormulaIndicador(String formulaIndicador) {
		this.formulaIndicador = formulaIndicador;
	}

	public String getFormulaGrupoLancamento() {
		if (formulaGrupoLancamento == null) {
			formulaGrupoLancamento = "";
		}
		return formulaGrupoLancamento;
	}

	public void setFormulaGrupoLancamento(String formulaGrupoLancamento) {
		this.formulaGrupoLancamento = formulaGrupoLancamento;
	}

	public boolean isUtilizarAnalise() {
		return utilizarAnalise;
	}

	public void setUtilizarAnalise(boolean utilizarAnalise) {
		this.utilizarAnalise = utilizarAnalise;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public CategoriaIndicador getCategoriaIndicador() {
		return categoriaIndicador;
	}

	public void setCategoriaIndicador(CategoriaIndicador categoriaIndicador) {
		this.categoriaIndicador = categoriaIndicador;
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

	public String getObservacao() {
		return observacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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
		Indicador other = (Indicador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
