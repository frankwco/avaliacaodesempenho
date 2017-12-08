package util;

import org.primefaces.context.RequestContext;

public class FecharDialog {
	
	public static void fecharDialogTipoServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgTipoServidor').hide();");
	}
	
}
