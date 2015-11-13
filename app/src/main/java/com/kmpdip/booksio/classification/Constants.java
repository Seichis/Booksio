package com.kmpdip.booksio.classification;

public class Constants {
    // Parameters
    public final static String[] CLASS_LABELS = {"cluster0", "cluster1", "cluster2", "cluster3","cluster4","cluster5","cluster6"}; // Should be predefined before collecting data
    public final static String WORKING_DIR_NAME = "booksio"; // Folder name in SD card


    // Optional fields
    public final static String HEADER_CLASS_LABEL = "label";

    // Features (Timbre)
    public final static String AGE = "age";
    public final static String NUMBER_LOANS = "number_loans";
    public final static String SCIENCE_RESEARCH = "science_research";
    public final static String RELIGION = "religion";
    public final static String SOCIAL_SCIENCES = "social_science";
    public final static String GEOGRAPHY = "geography";
    public final static String MATH_SCIENCES = "math_science";
    public final static String PRACTICAL = "practical";
    public final static String ARTS_SPORTS = "arts_sports";
    public final static String LITERATURE = "literature";
    public final static String HISTORY = "history";
    public final static String GENDER = "gender";


    // List of Features
    public final static String[] LIST_FEATURES = {
            AGE,
            NUMBER_LOANS,
            SCIENCE_RESEARCH,
            RELIGION,
            SOCIAL_SCIENCES,
            GEOGRAPHY,
            MATH_SCIENCES,
            PRACTICAL,
            ARTS_SPORTS,
            LITERATURE,
            HISTORY,
            GENDER,

    };
}
