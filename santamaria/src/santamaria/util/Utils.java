package santamaria.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class Utils {

	/*@PersistenceUnit
	protected EntityManagerFactory emf;
	protected EntityManager em;*/
	
	public Utils() {
		
	}
	
	public static String getMD5String(String texto) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(texto.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			return number.toString(16).toUpperCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getRandomUUID() {
		String strUUID;
		try {
			UUID genUUID = UUID.randomUUID();
			strUUID = genUUID.toString();
		} catch (Exception e) {
			return null;
		}
		return strUUID;
	}
	
	public static String getPassword() {
		return getPassword(8);
	}
	
	public static String getPassword(int length) {
		return getPassword(AppConstant.NUMEROS + AppConstant.MAYUSCULAS
				+ AppConstant.MINUSCULAS, length);
	}
	
	public static String getPassword(String key, int length) {
		String pswd = "";
		for (int i = 0; i < length; i++) {
			pswd += (key.charAt((int) (Math.random() * key.length())));
		}
		return pswd;
	}
	
	public static Date getHoy() {
		Date hoy = Calendar.getInstance(
				TimeZone.getTimeZone("America/El_Salvador"),
				new Locale("es", "SV")).getTime();
		return hoy;
	}
}
