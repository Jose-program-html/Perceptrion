package OpencvNeuroph;

import java.util.Arrays;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class Entrenamiento {


	public void convert(String name, double[][] bits){
		
		// create training set (logical XOR function)
		double[] entrenar = null;
		DataSet trainingSet = new DataSet(10000, 1);
		for(int i=0;i<1;i++){
			entrenar=bits[i];
			trainingSet.addRow(new DataSetRow(entrenar, new double[]{1}));
		}
		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron  = new MultiLayerPerceptron(TransferFunctionType.TANH, 10000, 5, 1);
		// learn the training set
		myMlPerceptron.learn(trainingSet);

		// test perceptron
		System.out.println("Testing trained neural network");

		// save trained neural network
		myMlPerceptron.save("myMlPerceptron.nnet");
		
		for (DataSetRow dataRow : trainingSet.getRows()) {
            myMlPerceptron.setInput(dataRow.getInput());
            myMlPerceptron.calculate();
            double[] networkOutput = myMlPerceptron.getOutput();
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
	}

}
