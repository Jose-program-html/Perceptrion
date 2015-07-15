package Registro;

import Bd.conexion;

public class SigninTrainingBinary {
	static String[] binario;
	static double[][] Entrenarbinarios;
	static double[] Entrenarsalidas;
	public static int ids;

	public SigninTrainingBinary() {
		conexion con = new conexion();
		con.conteo("entrada", "COUNT(*)");
		int id = Integer.parseInt(con.registro_busqueda);
		Entrenarbinarios = new double[id][10000];
		ids = id;
		for (int j = 0; j < ids; j++) {
			con.busquedaClausula("entrada", "id", "bn", String.valueOf(j + 1));
			binario = con.registro_busqueda.split(" ");
			double[] entrenar = new double[10000];
			for (int i = 0; i < 10000; i++) {
				entrenar[i] = Double.parseDouble(binario[i]);
			}
			Entrenarbinarios[j]=entrenar;
		}
	}

	public static int numEpocas = 10; // número de ciclos de entrenamiento
	public static int numEntradas = 10001; // número de entradas - esto incluye
											// la entrada bias (umbral)
	public static int numUOcultas = 10; // número de unidades ocultas
	public static int numPatrones = ids; // número de patrones a entranar
	public static double TA_EO = 0.7; // tasa de aprendizaje
	public static double TA_SO = 0.07; // tasa de aprendizaje

	// procesa variables
	public static int numPat;
	public static double errorEstePatron;
	public static double predSalida;
	public static double RMSerror;

	// entrenamiento de datos
	// public static double[][] entrenaEntradas = new double[1][10001];
	public static double[][] entrenaEntradas = new double[numPatrones][numEntradas];
	public static double[] entrenaSalidas = new double[numPatrones];

	// las salidas de las neuronas ocultas
	public static double[] valOculto = new double[numUOcultas];

	// los pesos
	public static double[][] pesosEO = new double[numEntradas][numUOcultas];
	public static double[] pesosOS = new double[numUOcultas];

	public void principal() {
		numPatrones = ids;
		entrenaEntradas = new double[numPatrones][numEntradas];
		entrenaSalidas = new double[numPatrones];
		// inicializa los pesos
		inicioPesos();
		// carga los datos
		inicioDatos();
		// entrana la red
		for (int j = 0; j <= numEpocas; j++) {
			for (int i = 0; i < numPatrones; i++) {
				// selecciona un patrón como aleatorio
				numPat = (int) ((Math.random() * numPatrones) - 0.001);
				// calcula la salida de la red en turno (presente) y el error
				// para éste patrón
				calcRed();
				// cambia los pesos de la red
				CambioPesosOS();
				CambioPesosEO();
			}
			// despliega (muestra) el error de la red total después de cada
			// época
			calcTotalError();
			System.out.println("epocas = " + j + "  RMS Error = " + RMSerror);
		}
		// el entranamiento ha terminado y muestra los resultados
		muestraResultados();
	}

	public static void calcRed() {
		// calcula las salidas de las neuronas ocultas. Las neuronas ocultas
		// están en tanh(tangente)

		for (int i = 0; i < numUOcultas; i++) {
			valOculto[i] = 0.0;

			for (int j = 0; j < numEntradas; j++)
				valOculto[i] = valOculto[i]
						+ (entrenaEntradas[numPat][j] * pesosEO[j][i]);

			valOculto[i] = tanh(valOculto[i]);
		}

		// calcula la salidade la RED
		// la salida de la neurona es lineal
		predSalida = 0.0;

		for (int i = 0; i < numUOcultas; i++)
			predSalida = predSalida + valOculto[i] * pesosOS[i];

		// calcula el error
		errorEstePatron = predSalida - entrenaSalidas[numPat];
	}

	// ************************************
	public static void CambioPesosOS()
	// ajusta los pesos de la salida-oculta
	{
		for (int k = 0; k < numUOcultas; k++) {
			double cambioPeso = TA_SO * errorEstePatron * valOculto[k];
			pesosOS[k] = pesosOS[k] - cambioPeso;

			// regularización sobre los pesos de salida
			if (pesosOS[k] < -5)
				pesosOS[k] = -5;
			else if (pesosOS[k] > 5)
				pesosOS[k] = 5;
		}
	}

	public static void CambioPesosEO()
	// ajusta los pesos entradas-ocultas
	{
		for (int i = 0; i < numUOcultas; i++) {
			for (int k = 0; k < numEntradas; k++) {
				double x = 1 - (valOculto[i] * valOculto[i]);
				x = x * pesosOS[i] * errorEstePatron * TA_EO;
				x = x * entrenaEntradas[numPat][k];
				double cambioPeso = x;
				pesosEO[k][i] = pesosEO[k][i] - cambioPeso;
			}
		}
	}

	public static void inicioPesos() {

		for (int j = 0; j < numUOcultas; j++) {
			pesosOS[j] = (Math.random() - 0.5) / 2;
			for (int i = 0; i < numEntradas; i++)
				pesosEO[i][j] = (Math.random() - 0.5) / 5;
		}

	}

	public static void inicioDatos() {
		System.out.println("initializando datos");

		for (int j = 0; j < numPatrones; j++) {
			for (int i = 0; i < 10001; i++) {
				if (i == 10000) {
					entrenaEntradas[j][i] = 1;
				} else {
					entrenaEntradas[j][i] = Entrenarbinarios[j][i];
				}
			}
		}
		conexion con = new conexion();
		for(int i = 0; i< numPatrones;i++){
			con.busquedaClausula("entrada", "id", "idusuario", String.valueOf(i + 1));
			String aux=con.registro_busqueda.substring(0, con.registro_busqueda.length()-1);
			System.out.println(aux);
			int salida = Integer.parseInt(aux);
			entrenaSalidas[i] = salida;
		}
	}

	public static double tanh(double x) {
		if (x > 20)
			return 1;
		else if (x < -20)
			return -1;
		else {
			double a = Math.exp(x);
			double b = Math.exp(-x);
			return (a - b) / (a + b);
		}
	}

	public static void muestraResultados() {
		conexion con = new conexion();
		for (int i = 0; i < numPatrones; i++) {
			numPat = i;
			calcRed();
			System.out
					.println("patrón = " + (numPat + 1) + " actual = "
							+ entrenaSalidas[numPat] + " modelo neural = "
							+ predSalida);
			con.actualizar("entrada", "entrenamientobinario", predSalida+"", "id="+(i+1));
		}
	}

	public static void calcTotalError() {
		RMSerror = 0.0;
		for (int i = 0; i < numPatrones; i++) {
			numPat = i;
			calcRed();
			RMSerror = RMSerror + (errorEstePatron * errorEstePatron);
		}
		RMSerror = RMSerror / numPatrones;
		RMSerror = java.lang.Math.sqrt(RMSerror);
	}

}
