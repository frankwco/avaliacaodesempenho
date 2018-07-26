package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ExibirMensagem {

	public static void exibirMensagem(String mensagem){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(mensagem));
	}
	public static void exibirMessageFatal(){
	   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "CPF inválido"));
	
	}
	
	public static void exibirMessageWarn(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Questionário indisponível"));
		}
}
