package util;

import org.primefaces.context.RequestContext;

public class FecharDialog {
	
	public static void fecharDialogTipoServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgTipoServidor').hide();");
	}
	
	public static void fecharDialogInserirLancamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgInserirLancamento').hide();");
	}
	
	public static void fecharDialogVeiculo() {
		RequestContext.getCurrentInstance().execute("PF('dlgVeiculo').hide();");
	}
	
	public static void fecharDialogSalario() {
		RequestContext.getCurrentInstance().execute("PF('dlgSalario').hide();");
	}
	
	public static void fecharDialogPessoa() {
		RequestContext.getCurrentInstance().execute("PF('dlgPessoa').hide();");
	}
	
	public static void fecharDialogUsuario() {
		RequestContext.getCurrentInstance().execute("PF('dlgUsuario').hide();");
	}
	
	public static void fecharDialogProcesso() {
		RequestContext.getCurrentInstance().execute("PF('dlgProcesso').hide();");
	}
	
	public static void fecharDialogAtividade() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtividade').hide();");
	}
	
	public static void fecharDialogOcorrencia() {
		RequestContext.getCurrentInstance().execute("PF('dlgOcorrencia').hide();");
	}
	
	public static void fecharDialogCategoriaIndicador() {
		RequestContext.getCurrentInstance().execute("PF('dlgCategoriaIndicador').hide();");
	}

	public static void fecharDialogGrupoLancamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgGrupoLancamento').hide();");
	}
	
	public static void fecharDialogIndicador() {
		RequestContext.getCurrentInstance().execute("PF('dlgIndicador').hide();");
	}
	
	public static void fecharDialogDescricaoDespesa() {
		RequestContext.getCurrentInstance().execute("PF('dlgDescricaoDespesa').hide();");
	}
	
	public static void fecharDialogDespesa() {
		RequestContext.getCurrentInstance().execute("PF('dlgDespesa').hide();");
	}
	
}
