package OpencvNeuroph;

import java.awt.EventQueue;
import java.util.Arrays;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

import Bd.conexion;
import Registro.registro;

public class Entrenamiento {
	static String []binario;
	double aux=1.0;
	public int ids;
	
	conexion con;
	public Entrenamiento(int id) {
		con= new conexion();
		ids=id;
		con.busquedaClausula("entrada", "idUsuario", "bn", String.valueOf(id));
		binario=con.registro_busqueda.split(" ");
		convert(binario);
	}

	public void convert(String [] binario) {
		System.out.print("ENTRENAMIENTO PRUEBA 1 "+ binario);
		// create training set (logical XOR function)
		double[] entrenar = new double[10000];
		for(int i=0;i<10000;i++){
			entrenar[i]=Double.parseDouble(binario[i]);
		}
		DataSet trainingSet = new DataSet(10000, 1);
		for(int i=0;i<1;i++){
		trainingSet.addRow(new DataSetRow(entrenar, new double[]{1}));
		}
		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new
		MultiLayerPerceptron(TransferFunctionType.TANH, 10000, 5, 1);
		// learn the training set
		myMlPerceptron.learn(trainingSet);
		
		for (DataSetRow dataRow : trainingSet.getRows()) {
		myMlPerceptron.setInput(dataRow.getInput());
		myMlPerceptron.calculate();
		double[] networkOutput = myMlPerceptron.getOutput();
		con.actualizar("entrada", "entrenamientobinario", networkOutput[0]+"", "idUsuario="+ids);
		}
	}
	public static void main(String[] args) {
		

	}

}
