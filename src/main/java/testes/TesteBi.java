package testes;

import java.text.DecimalFormat;

public class TesteBi {
	public static void main(String[] s) {
		double meta = 10;
		double alcancado = 7;
		double resultado = alcancado/meta*100;
		System.out.println(resultado);
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		System.out.println(df.format(1122234565.916385));
		// Imprime 0,91238
	}
}
