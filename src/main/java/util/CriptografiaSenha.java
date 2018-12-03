package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptografiaSenha {
	public static String criptografar(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}

	public static boolean decript(String senha, String hash) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if (passwordEncoder.matches(senha, hash)) {
			System.out.println("true");
			return true;
		} else {
			System.out.println("false");
			return false;
		}
	}
}
