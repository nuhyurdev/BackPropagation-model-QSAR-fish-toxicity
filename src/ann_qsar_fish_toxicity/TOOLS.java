/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ann_qsar_fish_toxicity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author noaahhh
 */
public class TOOLS {

    public static double MSE(double[] expected, double[] actual) {

        double lineError = 0;
        lineError += Math.pow((expected[0] - actual[0]), 2);

        return lineError;
    }

    public static double MIN_MAX(double max, double min, double x) {
        return (x - min) / (max - min);
    }

    public static void MaxMinReplace(File file) throws FileNotFoundException {
        try (Scanner read = new Scanner(file)) {
            while (read.hasNextDouble()) {
                for (int i = 0; i < CONSTS.INPUTS_NUMBER; i++) {
                    double data = read.nextDouble();
                    if (data > CONSTS.maximums[i]) {
                        CONSTS.maximums[i] = data;
                    } else if (data < CONSTS.minimums[i]) {
                        CONSTS.minimums[i] = data;
                    }
                }

                double data = read.nextDouble();
                if (data > CONSTS.maximums[6]) {
                    CONSTS.maximums[6] = data;
                } else if (data < CONSTS.minimums[6]) {
                    CONSTS.minimums[6] = data;
                }

            }
        }
    }

    static void initializeDataSet(File file, DataSet ds) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextDouble()) {
            double[] inputs = new double[CONSTS.INPUTS_NUMBER];
            double output[] = new double[1];

            for (int i = 0; i < CONSTS.INPUTS_NUMBER; i++) {
                double data = scanner.nextDouble();
                inputs[i] = MIN_MAX(CONSTS.maximums[i], CONSTS.minimums[i], data);
            }
            double data = scanner.nextDouble();
            output[0] = MIN_MAX(CONSTS.maximums[6], CONSTS.minimums[6], data);

            ds.add(new DataSetRow(inputs, output));

        }
        scanner.close();
    }

    public static void parseDataSet() throws FileNotFoundException, IOException {

        BufferedReader br;
        PrintWriter pw;
        PrintWriter pw2;
        FileReader file;
        try {

            br = new BufferedReader(new FileReader("dataFiles/qsar_fish_toxicity.data"));
            pw = new PrintWriter(new FileWriter(CONSTS.TRAIN_DATA_FILE));
            pw2 = new PrintWriter(new FileWriter(CONSTS.TEST_DATA_FILE));
            String line;
            int datasetLineCount = CONSTS.DATASET_LINE_COUNT;
            int trainLineCount = (int) (datasetLineCount * 0.7);
            int testLineCount = (int) (datasetLineCount - trainLineCount);
            
            
                for (int i = 0; i < trainLineCount; i++) {
                    line=br.readLine();
                    if(i==trainLineCount-1){
                    pw.print(line);
                    }
                    else
                    pw.println(line);
                    

                }
                for (int i = 0; i < testLineCount; i++) {
                    line=br.readLine();
                   if(i==testLineCount-1){
                       pw2.print(line);
                   }
                   else
                    pw2.println(line);
                }
            
            br.close();
            pw.close();
            pw2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
