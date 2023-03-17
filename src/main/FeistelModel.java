package main;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FeistelModel {

	public void decript(String msg) {
		try {

			List<String> msgBin = new ArrayList<>();
			List<String> kDesc = new ArrayList<>();
			StringBuilder ascciText = new StringBuilder();
			msgBin.addAll(asciiToBinMsg(msg));

			File file = new File("./MensajeDescencriptado.txt");
			file.delete();

			System.out.println("Se crea el archivo: MensajeDescencriptado.txt");
			PrintWriter escritor = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true));
			long inicio = System.currentTimeMillis();
			for (int keyDec : ascciDecToBin().keySet()) {

				kDesc.addAll(subclavesDes(ascciDecToBin().get(keyDec)));

				for (String charMsgBin : msgBin) {
					String feistelDesBin = charMsgBin;

					for (String k : kDesc) {
						String xorF = xorFunction(feistelDesBin, k);
						feistelDesBin = xorFeistel(feistelDesBin, xorF);
					}

					ascciText.append(
							convBinToAscii(feistelDesBin).stream().map(Object::toString).collect(Collectors.joining()));
				}

				escritor.println("Clave = " + convBinToAscii(ascciDecToBin().get(keyDec)).stream().map(Object::toString)
						.collect(Collectors.joining()) + " | Mensaje = " + ascciText.toString());
				ascciText.setLength(0);
			}

			escritor.close();
			long fin = System.currentTimeMillis();
			double tiempoTotal = (double) ((fin - inicio));
			System.out.println("Tiempo total de ejecución: " + tiempoTotal + " ms");
			System.out.println("Fin del proceso");

		} catch (IOException e) {
			System.out.println("Ha ocurrido un error al escribir el archivo.");
			e.printStackTrace();
		}

	}

	/**
	 * toma el mensaje de hexadecimal y lo convierte en binario, todo en un array
	 * 
	 * @param msg
	 * @return
	 */
	public List<String> asciiToBinMsg(String msg) {
		List<String> msgBin = new ArrayList<>();
		String[] msgSplit = msg.split(" ");
		for (String m : msgSplit) {
			msgBin.add(String.format("%08d", Integer.parseInt(new BigInteger(m, 16).toString(2))));
		}
		return msgBin;
	}

	/**
	 * toma todos los decimales del ascci y los convierte en binario, todo en un
	 * hashmap
	 * 
	 * @return
	 */
	public Map<Integer, String> ascciDecToBin() {
		Map<Integer, String> ascciConverter = new HashMap<>();

		for (int i = 0; i < 256; i++) {
			ascciConverter.put(i,
					String.format("%08d", Integer.parseInt(new BigInteger(String.valueOf(i), 10).toString(2))));
		}

		return ascciConverter;
	}

	/**
	 * Rota la subclave en binario a la derecha segun ronda
	 * 
	 * @param bits  subclave
	 * @param round ronda
	 * @return
	 */
	public String rotateR(String bits, int round) {

		byte b = (byte) Integer.parseInt(bits, 2);
		byte resultado = b;

		if (round > 2) {
			for (int i = 0; i < 2; i++) {
				byte parte1 = (byte) ((resultado >> 4) & 0x0F);
				byte parte2 = (byte) (resultado & 0x0F);
				parte1 = (byte) ((parte1 >> 1) | ((parte1 << 3) & 0x08));
				parte2 = (byte) ((parte2 >> 1) | ((parte2 << 3) & 0x08));
				resultado = (byte) ((parte1 << 4) | parte2);
			}
		} else {
			byte parte1 = (byte) ((b >> 4) & 0x0F);
			byte parte2 = (byte) (b & 0x0F);
			parte1 = (byte) ((parte1 >> 1) | ((parte1 << 3) & 0x08));
			parte2 = (byte) ((parte2 >> 1) | ((parte2 << 3) & 0x08));
			resultado = (byte) ((parte1 << 4) | parte2);
		}

		String bin = String.format("%08d",
				Integer.parseInt(new BigInteger(String.valueOf(resultado & 0xFF), 10).toString(2)));
		return bin;
	}

	/**
	 * funcion entre el mensaje de feistel y las subclaves
	 * 
	 * @param msgBits mensaje en binario
	 * @param keyBits subclave en binario
	 * @return
	 */
	public String xorFunction(String msgBits, String keyBits) {
		String resultXor = "";
		for (int i = 4; i < 8; i++) {
			char msgLsb4 = msgBits.charAt(i);
			char lsb4 = keyBits.charAt(i);
			resultXor += (msgLsb4 == lsb4) ? "0" : "1";

		}
		return resultXor;
	}

	/**
	 * Operacion XOR en el modelo Feistel
	 * 
	 * @param msgBitsHsb
	 * @param xorFunc
	 * @return
	 */
	public String xorFeistel(String msgBitsHsb, String xorFunc) {
		String resultXor = "";

		for (int i = 0; i < 4; i++) {
			char msg = msgBitsHsb.subSequence(0, 4).charAt(i);
			char xorF = xorFunc.charAt(i);
			resultXor += (msg == xorF) ? "0" : "1";
		}

		String concatResult = xorFunc + resultXor;
		return concatResult;
	}

	/**
	 * Se llena una lista de subclaves comenzando con la ultima subclave al primero,
	 * esto para desencriptar el msg
	 * 
	 * @param claveBits
	 * @return
	 */
	public List<String> subclavesDes(String claveBits) {
		List<String> k = new ArrayList<>();

		String rot = rotateR(claveBits, 8);
		for (int i = 8; i > 0; i--) {
			k.add(rot);
			rot = rotateR(rot, i);
		}

		return k;
	}

	/**
	 * Metodo para convertir binario a ascii
	 * @param bin
	 * @return
	 */
	public List<Character> convBinToAscii(String bin) {
		List<Character> hex = new ArrayList<>();
		int decimal = Integer.parseInt(bin, 2); // convertir a decimal
		char caracter = (char) decimal;
		hex.add(caracter);
		return hex;
	}

	/**
	 * Metodo para convertir binario a hexadecimal
	 * @param bin
	 * @return
	 */
	public List<String> convBinToHex(String bin) {
		List<String> hex = new ArrayList<>();
		int decimal = Integer.parseInt(bin, 2); // convertir a decimal
		String hexadecimal = Integer.toHexString(decimal);
		hex.add(hexadecimal);
		return hex;
	}

}
