package dados;

import java.util.ArrayList;
import java.util.List;

import modelo.Imc;

public class ImcMock {

	public static List<Imc> imc;

	public static List<Imc> get() {
		if (imc == null) {
			imc = new ArrayList<>();
		}
		return imc;
	}

	public static List<Imc> getUsuario(String usuario) {
		List<Imc> imcRetorno = new ArrayList<>();
		if (usuario != null && usuario.trim().length() > 0) {
			for (Imc im : get()) {
				if (im.getUsuario().equalsIgnoreCase(usuario)) {
					imcRetorno.add(im);
				}
			}
		} else {
			imcRetorno = get();
		}
		return imcRetorno;
	}
	
	public static Boolean removerImc(String usuario) {
		List<Imc> imcRetorno = new ArrayList<>();
		if (usuario != null && usuario.trim().length() > 0) {
			for (Imc im : get()) {
				if (im.getUsuario().equalsIgnoreCase(usuario)) {
					imcRetorno.add(im);
				}
			}
		} 
		return (get().removeAll(imcRetorno));
	}

}
