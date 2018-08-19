package dados;

import java.util.ArrayList;
import java.util.List;


import modelo.Time;

public class TimesMock {
	
	public static List<Time> lt;
	public  static List<Time> get(){
		if (lt == null) {
			lt = new ArrayList<>();
		}
		return lt;
	}

}
