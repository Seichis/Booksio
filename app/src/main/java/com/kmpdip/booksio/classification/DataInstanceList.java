package com.kmpdip.booksio.classification;

import java.util.ArrayList;


public class DataInstanceList extends ArrayList<DataInstance> {

    final static String TAG = "DataInstanceList";

    private long timeId; // Unixtime for window time id

    public DataInstanceList() {
        ;
    }

    public DataInstanceList(long timeId) {
        this.timeId = timeId;
    }

    public long getTimeId() {
        return timeId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(timeId + "(" + this.size() + ") ");
        sb.append("{");
        for (int i = 0; i < this.size(); i++) {
            DataInstance di = this.get(i);
            sb.append(di.getUnixtime());
            if (i != this.size() - 1)
                sb.append(",");
        }
        sb.append("}");

        return sb.toString();

    }

}

