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
	@Transient
	private Double valor1;
	@Transient
	private Double valor2;
	@Transient
	private Double valor3;
	@Transient
	private Double valor4;
	@Transient
	private Double valor5;
	@Transient
	private Double valor6;
	@Transient
	private Double valor7;
	@Transient
	private Double valor8;
	@Transient
	private Double valor9;
	@Transient
	private Double valor10;
	@Transient
	private Double valor11;
	@Transient
	private Double valor12=0.;
	@Transient
	private Double valor12Anterior;
	@Transient
	private Double valor11Anterior;
	

	private String formulaGrupoLancamento = "";

	private String formulaIndicador = "";
	
	private Double meta01;
	private Double meta02;
	private Double meta03;
	private Double meta04;
	private Double meta05;
	private Double meta06;
	private Double meta07;
	private Double meta08;
	private Double meta09;
	private Double meta10;
	private Double meta11;
	private Double meta12;
	private String metaMaiorMenorQue="<=Meta";
	
	@Column(length=10000)
	private String acoesMelhoria;
	
	private boolean especifico;
	private boolean temporizavel;
	private boolean mensuravel;
	private boolean atingivel;
	private boolean relevante;
	
	private String observacoesAnalises;
	
	
		
	public String getObservacoesAnalises() {
		return observacoesAnalises;
	}

	public void setObservacoesAnalises(String observacoesAnalises) {
		this.observacoesAnalises = observacoesAnalises;
	}

	public String getMetaMaiorMenorQue() {
		if(metaMaiorMenorQue==null) {
			metaMaiorMenorQue = "<=Meta";
		}
		return metaMaiorMenorQue;
	}

	public void setMetaMaiorMenorQue(String metaMaiorMenorQue) {
		this.metaMaiorMenorQue = metaMaiorMenorQue;
	}

	public boolean isEspecifico() {
		return especifico;
	}

	public void setEspecifico(boolean especifico) {
		this.especifico = especifico;
	}

	public boolean isTemporizavel() {
		return temporizavel;
	}

	public void setTemporizavel(boolean temporizavel) {
		this.temporizavel = temporizavel;
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

	public String getAcoesMelhoria() {
		return acoesMelhoria;
	}

	public void setAcoesMelhoria(String acoesMelhoria) {
		this.acoesMelhoria = acoesMelhoria;
	}

	public Double getMeta01() {
		return meta01;
	}

	public void setMeta01(Double meta01) {
		this.meta01 = meta01;
	}

	public Double getMeta02() {
		return meta02;
	}

	public void setMeta02(Double meta02) {
		this.meta02 = meta02;
	}

	public Double getMeta03() {
		return meta03;
	}

	public void setMeta03(Double meta03) {
		this.meta03 = meta03;
	}

	public Double getMeta04() {
		return meta04;
	}

	public void setMeta04(Double meta04) {
		this.meta04 = meta04;
	}

	public Double getMeta05() {
		return meta05;
	}

	public void setMeta05(Double meta05) {
		this.meta05 = meta05;
	}

	public Double getMeta06() {
		return meta06;
	}

	public void setMeta06(Double meta06) {
		this.meta06 = meta06;
	}

	public Double getMeta07() {
		return meta07;
	}

	public void setMeta07(Double meta07) {
		this.meta07 = meta07;
	}

	public Double getMeta08() {
		return meta08;
	}

	public void setMeta08(Double meta08) {
		this.meta08 = meta08;
	}

	public Double getMeta09() {
		return meta09;
	}

	public void setMeta09(Double meta09) {
		this.meta09 = meta09;
	}

	public Double getMeta10() {
		return meta10;
	}

	public void setMeta10(Double meta10) {
		this.meta10 = meta10;
	}

	public Double getMeta11() {
		return meta11;
	}

	public void setMeta11(Double meta11) {
		this.meta11 = meta11;
	}

	public Double getMeta12() {
		return meta12;
	}

	public void setMeta12(Double meta12) {
		this.meta12 = meta12;
	}	

	public Double getValor1() {
		return valor1;
	}

	public void setValor1(Double valor1) {
		this.valor1 = valor1;
	}

	public Double getValor2() {
		return valor2;
	}

	public void setValor2(Double valor2) {
		this.valor2 = valor2;
	}

	public Double getValor3() {
		return valor3;
	}

	public void setValor3(Double valor3) {
		this.valor3 = valor3;
	}

	public Double getValor4() {
		return valor4;
	}

	public void setValor4(Double valor4) {
		this.valor4 = valor4;
	}

	public Double getValor5() {
		return valor5;
	}

	public void setValor5(Double valor5) {
		this.valor5 = valor5;
	}

	public Double getValor6() {
		return valor6;
	}

	public void setValor6(Double valor6) {
		this.valor6 = valor6;
	}

	public Double getValor7() {
		return valor7;
	}

	public void setValor7(Double valor7) {
		this.valor7 = valor7;
	}

	public Double getValor8() {
		return valor8;
	}

	public void setValor8(Double valor8) {
		this.valor8 = valor8;
	}

	public Double getValor9() {
		return valor9;
	}

	public void setValor9(Double valor9) {
		this.valor9 = valor9;
	}

	public Double getValor10() {
		return valor10;
	}

	public void setValor10(Double valor10) {
		this.valor10 = valor10;
	}

	public Double getValor11() {
		return valor11;
	}

	public void setValor11(Double valor11) {
		this.valor11 = valor11;
	}

	public Double getValor12() {
		return valor12;
	}

	public void setValor12(Double valor12) {
		this.valor12 = valor12;
	}
		
	public Double getValor12Anterior() {
		return valor12Anterior;
	}

	public void setValor12Anterior(Double valor12Anterior) {
		this.valor12Anterior = valor12Anterior;
	}

	public Double getValor11Anterior() {
		return valor11Anterior;
	}

	public void setValor11Anterior(Double valor11Anterior) {
		this.valor11Anterior = valor11Anterior;
	}

	public String getAbreviacao() {
		//System.out.println("Abre: "+abreviacao);
		//System.out.println("Descricao: "+descricao);
		//if (abreviacao == null) {
			abreviacao="";
			if (descricao != null) {
				String[] palavra = descricao.split(" ");
				for (String p : palavra) {
					///System.out.println("Tamanho da palavra: "+p);
					if (p.length() > 2 && !p.equalsIgnoreCase("por") && !p.equalsIgnoreCase("com") ) {
						abreviacao += p.charAt(0);
					}
				}
			} else {
				abreviacao = ".";
			}
		//}
		return abreviacao.toUpperCase();
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
