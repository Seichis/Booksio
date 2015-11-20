package com.kmpdip.booksio.classification;

import android.util.Log;

import weka.classifiers.Classifier;
import weka.core.Instance;

public class J48Wrapper extends WekaWrapper {

    Classifier classifier = null;

    @Override
    public String predict(Instance i) {
        String resultClass = null;
        try {

            Object[] s = new Object[i.numAttributes()];

            for (int j = 0; j < s.length; j++) {
                if (!i.isMissing(j)) {

                    s[j] = new Double(i.value(j));
                }
            }


            double result = DecisionTreeModel.classify(s);
            resultClass = String.valueOf((int)result);

        } catch (Exception e) {
            Log.i("Predict", e.toString() + "error message:" + e.getMessage());
        }

        return resultClass;
    }


}
