package testes;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TesteCalculos {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//			String te = "(1+2+(2/2))";
		
		String te="frank jose";
		String teste[] = te.split(" ");
		System.out.println(teste.length);
		
		List<String> lista=new ArrayList<>();
		lista.add("Frank");
		lista.add("jsoe");
		for(String l:lista) {
			System.out.println(l);
		}
				
	
//    ScriptEngineManager mgr = new ScriptEngineManager();
//    ScriptEngine engine = mgr.getEngineByName("JavaScript");
//    String foo = te;
//    try {
//		System.out.println(engine.eval(foo));
//	} catch (ScriptException e) {
//		System.out.println("erro na equação");
//	}
	}

}
