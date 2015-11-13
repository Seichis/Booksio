package com.kmpdip.booksio.classification;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;


public class FeatureGenerator {

    final static String TAG = "FeatureGenerator";

    public FeatureGenerator() {
        ;
    }

    public static HashMap<String, Float> processUser(DataInstance di) {

        float age = 0f;
        float number_loans = 0f;
        float science_research = 0f;
        float religion = 0f;
        float social_science = 0f;
        float geography =0f;
        float math_science = 0f;
        float practical = 0f;
        float arts_sports = 0f;
        float literature = 0f;
        float history = 0f;
        float gender = -1f;


        // Output variables
        HashMap<String, Float> featureMap = new HashMap<String, Float>();
        featureMap.put(Constants.AGE, age);
        featureMap.put(Constants.NUMBER_LOANS, number_loans);
        featureMap.put(Constants.SCIENCE_RESEARCH, science_research);
        featureMap.put(Constants.RELIGION, religion);
        featureMap.put(Constants.SOCIAL_SCIENCES, social_science);
        featureMap.put(Constants.GEOGRAPHY, geography);
        featureMap.put(Constants.MATH_SCIENCES, math_science);
        featureMap.put(Constants.PRACTICAL, practical);
        featureMap.put(Constants.ARTS_SPORTS, arts_sports);
        featureMap.put(Constants.LITERATURE, literature);
        featureMap.put(Constants.HISTORY, history);
        featureMap.put(Constants.GENDER, gender);

        return featureMap;

    }


    public static Instances createEmptyInstances(String[] headers, boolean isLabelRequired) {

        FastVector attrs = new FastVector();

        for (String header : headers) {
            attrs.addElement(new Attribute(header));
        }

        if (isLabelRequired) {
            FastVector fv = new FastVector();
            for (String classLabel : Constants.CLASS_LABELS) {
                fv.addElement(classLabel);
            }
            attrs.addElement(new Attribute(Constants.HEADER_CLASS_LABEL, fv));

        }

        String formattedDate = "";
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDate = sdf.format(cal.getTime());
        }

        Instances data = new Instances(formattedDate, attrs, 10000);

        return data;
    }
}
