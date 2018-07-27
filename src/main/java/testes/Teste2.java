package testes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Teste2 {

	public static void main(String[] args) {
		//String expressao = "((soma(31)+soma(2))/2+contar(33999)+soma(2))*2-soma(44)";
		String expressao = "(soma(1222)+6soma(6))/4";
		List<String> resultado = new ArrayList<String>();
		// Matcher m = Pattern.compile(":([^:\\) ]+)").matcher(
		// "(:TEXTOQUALQUER NADA DO FOI :TEXTOQQDENOVO SERÁ DE NOVO :TEXTOQQMAIS DO
		// JEITO QUE UM DIA :TEXTO3343)");
		Matcher m = Pattern.compile("soma\\([0-9]*\\d\\)").matcher(expressao);
		while (m.find())
			resultado.add(m.group(0));

		System.out.println(resultado);

		//String str = "a d, m, i.n";
		//String delimiters = "\\s+|,\\s*|\\.\\s*";
		String delimiters = ":.*\\)";

		// analyzing the string
		String[] tokensVal = expressao.split(resultado.get(0));

		// prints the number of tokens
		System.out.println("Count of tokens = " + tokensVal.length);

		for (String token : tokensVal) {
			System.out.println(token);
		}
		
//		String myString= "a+b-c*d/e";
		//Cada elemento ficará em uma posição, dá para FAZER VALIDAÇÕES
		String[] result = expressao.split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])");
		System.out.println(Arrays.toString(result));
		String equacao = "";
		for(int x=0; x<result.length;x++) {
			String posicao = result[x];
			if(posicao.equals("soma")) {
				//AQUI TEM QUE BUSCAR PELO VALOR....
				equacao+=result[x+2];
				x=x+3;
			}else {
				equacao+=posicao;
			}
		}
		System.out.println("Equação: "+equacao);

//		Just to see, what the two parts in the above regex print separately 
		System.out.println(Arrays.toString(expressao.split("(?<=[-+*/])")));
		System.out.println(Arrays.toString(expressao.split("(?=[-+*/])")));
		
		
		//String myString= "a+b-c*d/e";
		//String[] result = myString.split("(?<=[-+*/])|(?=[-+*/])");
//		System.out.println(Arrays.toString(result));

//		Just to see, what the two parts in the above regex print separately 
//		System.out.println(Arrays.toString(myString.split("(?<=[-+*/])")));
//		System.out.println(Arrays.toString(myString.split("(?=[-+*/])")));
		
	}

}
