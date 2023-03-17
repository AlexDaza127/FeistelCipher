package main;

/**
 * Modelo Feistel con generado de subclaves hasta 8 rondas con rotacion a la derecha segun ronda
 * @author Michael
 *
 */
public class MainCipher {

	public static void main(String[] args) {
		String msg = "ab 49 72 7e 22 64 6f 63 68 22 77 69 75 22 62 63 74 65 6f 60 75 67 75 23";
		FeistelModel feistelModel = new FeistelModel();
		feistelModel.decript(msg);
	}
}
