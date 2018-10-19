package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.CriptografiaSenha;
import util.EnviarEmail;
import util.GeradorSenhas;

import base.modelo.Usuario;
import base.service.UsuarioService;
import dao.GenericDAO;

@ViewScoped
@Named("recuperSenhaLoginMB")
public class RecuperSenhaLoginMB implements Serializable{

	private static final long serialVersionUID = 1L;

	private String email;
	
	private List<Usuario> lista;
	private String mensagem;
	
	@Inject
	private GenericDAO<Usuario> daoPessoa;
	
	@Inject
	private UsuarioService pessoaService;
	
	@PostConstruct
	public void inicializa() {
		lista = new ArrayList<>();
		mensagem = "";
		email = "";
	}
	public Boolean buscarEmail() {
		lista = daoPessoa.listaComStatus(Usuario.class);
		return lista.stream().anyMatch(p -> p.getEmail().equals(email));
	}

	public void recuperarSenhaLogin() {
		String senha;
		String novaSenha;
		if (buscarEmail()) {
			senha = GeradorSenhas.gerarSenha();
			novaSenha = senha.charAt(0)+""+senha.charAt(1)+""+senha.charAt(3)+""+senha.charAt(5)+""+senha.charAt(6);
			pessoaService.updateSenha(CriptografiaSenha.criptografar(novaSenha), email);
			if (EnviarEmail.enviarEmail(email, "Cronos, Recuperação de senha ",
					"Olá, " + "\n" + "Sua nova senha: " + novaSenha)) {
				mensagem = "E-mail enviado";
			} else {
				mensagem = "Erros ao enviar e-mail";
			}
		} else {
			mensagem = "E-mail nÃ£o encontrado";
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
