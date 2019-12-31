/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ann_qsar_fish_toxicity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author noaahhh
 */
public class ANN {

    private int maxEpoch;
    private File trainDataFile;
    private File testDataFile;
    private DataSet trainDataset;
    private DataSet testDataset;
    private int middlewareNeuronCount;

    private double[] errors;
    private MomentumBackpropagation mbp;
    private BackPropagation bp;
    private boolean trained = false;

    public ANN(int middlewareLayer, double momentum, double lr, int epoch) throws FileNotFoundException {

        this.middlewareNeuronCount = middlewareLayer;
        this.maxEpoch = epoch;
        this.errors = new double[maxEpoch];

        for (int i = 0; i < 7; i++) {
            CONSTS.maximums[i] = Double.MIN_VALUE;
            CONSTS.minimums[i] = Double.MAX_VALUE;
        }

        TOOLS.MaxMinReplace(getTrainDataFile());
        TOOLS.MaxMinReplace(getTestDataFile());

        trainDataset = new DataSet(6, 1);
        TOOLS.initializeDataSet(getTrainDataFile(), trainDataset);

        testDataset = new DataSet(6, 1);
        TOOLS.initializeDataSet(getTestDataFile(), testDataset);

        /*
        Scanner scan = new Scanner(trainDataset.toString());
        while (scan.hasNext()) {
            String l = scan.nextLine();
            System.out.println(l);
        }*/

        mbp = new MomentumBackpropagation();
        bp = new BackPropagation();
        mbp.setMomentum(momentum);
        mbp.setLearningRate(lr);
        bp.setLearningRate(lr);
        mbp.setMaxIterations(maxEpoch);
        bp.setMaxIterations(maxEpoch);

    }

    public void training() {
        trained = false;
      // NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 6, middlewareNeuronCount,10,20,10, 1);
        MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 6, middlewareNeuronCount,15,20 , 1);// 3 katmanlı ilki constructer ile geliyor.
        neuralNetwork.setLearningRule(mbp);
        neuralNetwork.learn(trainDataset);
        neuralNetwork.save(CONSTS.RESULTS_FILE);
/*
        for (int i = 0; i < maxEpoch; i++) {
            neuralNetwork.getLearningRule().doOneLearningIteration(trainDataset);
           
            if (i == 0) {
                errors[i] = 1;
            } else {
                errors[i] = neuralNetwork.getLearningRule().getPreviousEpochError();
            }

        }
*/
        System.out.println("Last Error   : \t" + neuralNetwork.getLearningRule().getPreviousEpochError());
        System.out.println("Training Complated.");
        trained = true;
    }

    public double testing() {

        if (trained == false) {
            System.err.println("Train the ann as first");
            return Double.NaN;
        }

        NeuralNetwork neuralNetwork = new NeuralNetwork().createFromFile(CONSTS.RESULTS_FILE);
        double totalError = 0;

        neuralNetwork.setLearningRule(mbp);
        neuralNetwork.calculate();

        for (DataSetRow r : testDataset) {
            neuralNetwork.setInput(r.getInput());
            neuralNetwork.calculate();
            totalError += TOOLS.MSE(r.getDesiredOutput(), neuralNetwork.getOutput());
            //System.out.println("getoutput"+Arrays.toString(neuralNetwork.getOutput()));
            //System.out.println(totalError/testDataset.size());
        }
        //return neuralNetwork.getOutput();
        return totalError / testDataset.size();

    }

    void singleTest(double[] inputs) {
        for (int i = 0; i < CONSTS.INPUTS_NUMBER; i++) {
            inputs[i] = TOOLS.MIN_MAX(CONSTS.maximums[i], CONSTS.minimums[i], inputs[i]);
        }
        NeuralNetwork nn = NeuralNetwork.createFromFile(CONSTS.RESULTS_FILE);
        nn.setInput(inputs);
        nn.calculate();
        System.out.println(Arrays.toString(nn.getOutput()*maximums[6]));
    }

//getter Function block    
    private File getTrainDataFile() {
        if (trainDataFile == null) {
            trainDataFile = new File(CONSTS.TRAIN_DATA_FILE);
        }
        return trainDataFile;
    }

    private File getTestDataFile() {
        if (testDataFile == null) {
            testDataFile = new File(CONSTS.TEST_DATA_FILE);
        }
        return testDataFile;
    }

    double getTotalError() {
        return bp.getTotalNetworkError();
    }

    public double[] getErrors() {
        return errors;
    }

}
