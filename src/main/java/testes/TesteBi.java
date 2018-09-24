package testes;

public class TesteBi {
	public static void main(String[] args) {

		int ano = 200;

		if (ano % 400 == 0) {
			System.out.println(ano + " é bissexto.");
		} else if ((ano % 4 == 0) && (ano % 100 != 0)) {
			System.out.println(ano + " é bissexto.");
		} else {
			System.out.println(ano + " não é bissexto");
		}
	}
}
