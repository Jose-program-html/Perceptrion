package Registro;

public class variables {
	private static String nombre = "";
	private static double w1 = 0;
	private static double w2 = 0;
	private static double Ɵ = 0;
	private static String Bw="";
	private static String Gray="";
	private static int _id=0;
	
	public static int get_id() {
		return _id;
	}

	public static void set_id(int _id) {
		variables._id = _id;
	}

	public static String getBw() {
		return Bw;
	}

	public static void setBw(String bw) {
		Bw = bw;
	}

	public static String getGray() {
		return Gray;
	}

	public static void setGray(String gray) {
		Gray = gray;
	}

	

	/**
	 * @return the nombre
	 */
	public static String getNombre() {
		return nombre;
	}

	/**
	 * @param aNombre
	 *            the nombre to set
	 */
	public static void setNombre(String aNombre) {
		nombre = aNombre;
	}

	/**
	 * @return the w1
	 */
	public static double getW1() {
		return w1;
	}

	/**
	 * @param aW1
	 *            the w1 to set
	 */
	public static void setW1(double aW1) {
		w1 = aW1;
	}

	/**
	 * @return the w2
	 */
	public static double getW2() {
		return w2;
	}

	/**
	 * @param aW2
	 *            the w2 to set
	 */
	public static void setW2(double aW2) {
		w2 = aW2;
	}

	/**
	 * @return the Ɵ
	 */
	public static double getƟ() {
		return Ɵ;
	}

	/**
	 * @param aƟ
	 *            the Ɵ to set
	 */
	public static void setƟ(double aƟ) {
		Ɵ = aƟ;
	}
}
