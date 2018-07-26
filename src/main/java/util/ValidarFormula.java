package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ValidarFormula {

	public static void main(String[] args) {
		System.out.println(validar(""));
	}

	public static boolean validar(String expressao) {
		if (expressao.trim().equals("")) {
			return true;
		}
		if (Pattern.matches("[a-zA-Z]*", expressao)) {
			if (!expressao.contains("somar(") || !expressao.contains("contar(")) {
				return false;
			} else {
				expressao = expressao.replaceAll("somar", "").replaceAll("contar", "");
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine engine = mgr.getEngineByName("JavaScript");
				String foo = expressao;
				try {
					engine.eval(foo);
					return true;
				} catch (ScriptException e) {
					System.out.println("erro na equação");
					return false;
				}
			}
		} else {
			expressao = expressao.replaceAll("somar", "").replaceAll("contar", "");
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String foo = expressao;
			try {
				engine.eval(foo);
				return true;
			} catch (ScriptException e) {
				System.out.println("erro na equação");
				return false;
			}
		}

	}

}
