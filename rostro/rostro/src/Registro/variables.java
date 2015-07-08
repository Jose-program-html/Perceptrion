package Registro;

public class variables {
	private static String [] Bw= {"","","","",""};
	private static String [] Gray= {"","","","",""};
	private static int _id=0;
	private static int idusuario=0;

	public static String [] getBw() {
		return Bw;
	}

	public static void setBw(String [] bw) {
		Bw = bw;
	}

	public static String [] getGray() {
		return Gray;
	}

	public static void setGray(String [] gray) {
		Gray = gray;
	}

	public static int get_id() {
		return _id;
	}

	public static void set_id(int _id) {
		variables._id = _id;
	}

	public static int getIdusuario() {
		return idusuario;
	}

	public static void setIdusuario(int idusuario) {
		variables.idusuario = idusuario;
	}
}
