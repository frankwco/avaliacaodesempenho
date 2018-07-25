package testes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Teste {

	public static String limpar(String expressao) {
		List<String> textos = new ArrayList<>();
		for (int x = 0; x < expressao.length(); x++) {
			char ca = expressao.charAt(x);
			if (ca == '@') {
				String textoSubstituir = "";
				for (int y = x; y < expressao.length(); y++) {
					if (expressao.charAt(y) != ';') {
						textoSubstituir += expressao.charAt(y);
					} else {
						textoSubstituir += ";";
						break;
					}
				}
				textos.add(textoSubstituir);
			}
		}
		for (String s : textos) {
			expressao = expressao.replace(s, "");
		}
		return expressao;
	}

	public static void main(String[] args) {
		String expressao = "($1:@Valor com Pedágio;+$2:@Gastos com Combustível;+3)/2";
		expressao = limpar(expressao);
		List<String> textos = new ArrayList<>();

		for (int x = 0; x < expressao.length(); x++) {
			char ca = expressao.charAt(x);
			if (ca == '$') {
				String textoSubstituir = "";
				for (int y = x; y < expressao.length(); y++) {
					if (expressao.charAt(y) == ':') {
						break;
					} else {
						textoSubstituir += expressao.charAt(y);
					}
				}
				textoSubstituir += ":";
				textos.add(textoSubstituir);
			}
		}
		for (String s : textos) {
//			System.out.println("D>> " + s);
			String idd = s.replace("$", "").replace(":", "");
			System.out.println(idd);
			expressao = expressao.replace(s, idd);
//			System.out.println("Here: " + expressao);
		}
		try {
			System.out.println("Here: "+new ScriptEngineManager().getEngineByName("JavaScript").eval(expressao));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
