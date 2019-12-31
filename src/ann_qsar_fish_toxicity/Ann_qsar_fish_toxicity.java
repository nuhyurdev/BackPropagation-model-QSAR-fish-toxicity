/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ann_qsar_fish_toxicity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author noaahhh
 */
public class Ann_qsar_fish_toxicity {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        int middlewareNeuronCount, maxEpochCount;
        double momentum, learningRate, maxError;
        ANN ann = null;
        int input = 0;
        File train = new File(CONSTS.TRAIN_DATA_FILE);
        File test = new File(CONSTS.TEST_DATA_FILE);

        do {

            System.out.println("1. Training ann");
            System.out.println("2. Testing ann");
            System.out.println("3. Testing with single data");
            //System.out.println("4. Total error and All errors");
            System.out.println("4. Exit");
            System.out.print("Input...: ");
            input = scanner.nextInt();

            switch (input) {

                case 1: //training  

                    if (train.exists() && test.exists()) {
                        train.delete();
                        test.delete();
                        TOOLS.parseDataSet();
                    } else {
                        TOOLS.parseDataSet();
                    }

                    System.out.print("Middle layer number: ");
                    middlewareNeuronCount = scanner.nextInt();
                    System.out.print("Momentum: ");
                    momentum = scanner.nextDouble();
                    System.out.print("Learning rate: ");
                    learningRate = scanner.nextDouble();
                    System.out.print("Max epoch: ");
                    maxEpochCount = scanner.nextInt();
                    ann = new ANN(middlewareNeuronCount, momentum, learningRate, maxEpochCount);
                    //ann = new ANN(25, 0.899d, 0.8d, 1000); 
                    ann.training();

                    break;

                case 2:
                    double testError = ann.testing();
                    System.out.println("Test Error Rate" + "\t\t" + testError);

                    System.in.read();

                    break;

                case 3:// Single test 

                    double[] inputs = new double[CONSTS.INPUTS_NUMBER];
                    System.out.print(" CIC0(0.965-5.158)(Double: ");
                    inputs[0] = scanner.nextDouble();
                    System.out.print("SM1_DZ (0.64-1.825)(Double): ");
                    inputs[1] = scanner.nextDouble();
                    System.out.print("GATS1 (1.11-2.92)(Double): ");
                    inputs[2] = scanner.nextDouble();
                    System.out.print("NdsCH (0-4)(Integer): ");
                    inputs[3] = scanner.nextInt();
                    System.out.print("NdssC (0,6)(Integer: ");
                    inputs[4] = scanner.nextInt();
                    System.out.print("MLOPG (-2.884-4.862)(Double): ");
                    inputs[5] = scanner.nextDouble();

                    // inputs = { 2.094, 0.827, 0.86, 0d, 0d, 1.886};
                    //ann.singleTest(new double[]{3.651, 1.151, 0.911, 0, 2, 2.278});
                    ann.singleTest(inputs);
                    System.in.read();

                    break;
              /*
                case 8:

                    double[] errors = ann.getErrors();
                    int epoch = 1;
                    
                    File file = new File("/home/noaahhh/error.data");
                    FileOutputStream fos = new FileOutputStream(file);
                    DataOutputStream dos = new DataOutputStream(fos);
                    
                    for (double error : errors) {
                        System.out.println(epoch + "\t\t" + error);
                        //dos.writeDouble((double) error);
                        epoch++;
                    }

                    //dos.close();

                    break;
                */ 
            }

        } while (input != 4);

        if (train.exists() && test.exists()) {
            train.delete();
            test.delete();

        }
    }

}
