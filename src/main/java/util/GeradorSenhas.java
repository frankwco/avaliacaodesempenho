package util;

import java.util.Random;

public class GeradorSenhas {
	public static String gerarSenha() {
		Random random = new Random();
		String resultado = "";
		String valores[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		for (int i = 0; i <= 21; i++) {
			int a = random.nextInt(valores.length);
			resultado += valores[a];
		}
		return resultado;
	}
}
