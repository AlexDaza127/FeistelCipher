package main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeistelModel {

	public void initializer(String msg) {
		
		List<String> msgBin = new ArrayList<>();
		msgBin.addAll(asciiToBinMsg(msg));

		String[] msgSplit = msg.split(",");
		for (int i = 0; i < msgSplit.length; i++) {
			System.out.println(msgSplit[i] + " = " + msgBin.get(i));
		}
		
		System.out.println("\nascii\n");
		for (int index : ascciDecToBin().keySet()) {
			System.out.println(index + " = " + ascciDecToBin().get(index));
		}
		
	}

	public List<String> asciiToBinMsg(String msg) {
		List<String> msgBin = new ArrayList<>();
		String[] msgSplit = msg.split(",");
		for (String m : msgSplit) {
			msgBin.add(String.format("%08d",Integer.parseInt(new BigInteger(m, 16).toString(2))));
		}
		return msgBin;
	}
	
	public Map<Integer, String> ascciDecToBin(){
		Map<Integer, String> ascciConverter = new HashMap<>();
		
		for (int i = 32; i < 256; i++) {
			ascciConverter.put(i,String.format("%08d",Integer.parseInt(new BigInteger(String.valueOf(i), 10).toString(2))));
		}
		
		return ascciConverter;
	}

}
