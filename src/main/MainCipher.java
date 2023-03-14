package main;

public class MainCipher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String msg = "ab,49,72,7e,22,64,6f,63,68,22,77,69,75,22,62,63,74,65,6f,60,75,67,75,23";
		FeistelModel feistelModel = new FeistelModel();
		feistelModel.initializer(msg);
	}

}
