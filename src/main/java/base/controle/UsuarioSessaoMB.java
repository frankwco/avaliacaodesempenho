package base.controle;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import base.modelo.Usuario;
import dao.GenericDAO;

@ViewScoped
@Named("usuarioSessaoMB")
public class UsuarioSessaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	String nomeUsuario = "";

	@Inject
	private GenericDAO<Usuario> daoUsuario; // faz as buscas

	@PostConstruct
	public void init() {

		System.out.println("no usuario");


		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			System.out.println("Aquii");
			Object obj = authentication.getPrincipal();
			if (obj instanceof UserDetails) {
				System.out.println("Aquii 2");
				nomeUsuario = ((UserDetails) obj).getUsername();
				System.out.println("Nome: "+nomeUsuario);
			} else {
				System.out.println("Aquii 3");
				nomeUsuario = obj.toString();
			}
		}
		
		List<Usuario> usu = daoUsuario.listar(Usuario.class, "email='" + nomeUsuario + "'");
		if (usu.size() > 0) {
			usuario = usu.get(0);
		}

	}

	public Usuario getUsuario() {	
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
