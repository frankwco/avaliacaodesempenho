package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverteStringDate {
	
	/**Recebe uma data no formato dd/MM/yyyy e retorna um Date**/
	public static Date retornaData(String dataConverte) {
		try {
			String[] splitData = dataConverte.split("/");
			SimpleDateFormat in;
			if (splitData[0].length() > 2) {
				in = new SimpleDateFormat("DD/MM/yyyy");
			} else {
				in = new SimpleDateFormat("dd/MM/yyyy");
			}

			SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");

			String result = out.format(in.parse(dataConverte.toString()));
			System.out.println("DAta In: " + result);

			Date datse = (Date) out.parse(result);
			return datse;

		} catch (Exception e) {

			return new Date();
		}

	}
	
	/**Recebe um objeto Date e retorna uma data String formato yyyy-MM-dd**/
	public static String retornaData(Date dataConverte) {
		try {	
			SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");
			return out.format(dataConverte);
		} catch (Exception e) {
			return "";
		}

	}
}
