package testes;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class TesteCorrelation {
	
	public static void main(String[] args) {
			double[] idade = {20.,21.,35.0, 44.};
			double[] peso = {60.,65.,70., 75.};
			double[] salario = {500.,600.,1000., 1500.};
			
			
			double[][] matriz = {idade,peso,salario};
			
		
			PearsonsCorrelation p = new PearsonsCorrelation();
			Double corre = p.correlation(idade, peso);
			
			
			System.out.println("Correlação: "+corre);
			
	}

}
