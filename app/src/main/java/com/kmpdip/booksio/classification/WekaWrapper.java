package com.kmpdip.booksio.classification;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instance;
import weka.core.Instances;

public abstract class WekaWrapper {

    protected final static String TAG = "ClassifierWrapper";
    protected Instances instancesForTraining = null;

    public abstract String predict(Instance instance);

}
