/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ann_qsar_fish_toxicity;

/**
 *
 * @author noaahhh
 */
public class CONSTS {

    static final String TRAIN_DATA_FILE = "dataFiles/qsar-train.data";
    static final String TEST_DATA_FILE = "dataFiles/qsar-test.data";
    static final String RESULTS_FILE = "dataFiles/result.nnet";
    static final int DATASET_LINE_COUNT = 908;
    
    static final int INPUTS_NUMBER = 6;
    static final int OUTPUTS_NUMBER = 1;
    static Double[] maximums = new Double[7];
    static Double[] minimums = new Double[7];

}
