package com.dreamwork.art.service.converters;

import com.dreamwork.art.model.Metric;
import com.dreamwork.art.service.Converter;

import java.util.*;

public class TwoMetrics implements Converter {
    private static int countMatches(String str, String sub) {
        int count = 0;
        if (!str.isEmpty() && !sub.isEmpty()) {
            for (int i = 0; (i = str.indexOf(sub, i)) != -1; i += sub.length()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<Metric> convert(LinkedHashMap projectData) {
        String data = projectData.toString();

        int count = 0;
        int count1 = 0;
        int countmile = 0;
        int countclosedmile = 0;
        count = countMatches(data, "closed=true");
        count1 = countMatches(data, "closed=false") + count;
        countmile = countMatches(data, "dueOn");
        countclosedmile = countMatches(data, "state=CLOSED");
        float ans = 0;
        if (count1 == 0) ans = 1;
        else ans = (float) count / count1;
        float toreturnprogress = 0;
        if (countmile > 0) toreturnprogress = ans * (countclosedmile + 1) / countmile;
        else toreturnprogress = 0;
        float toreturncount = count1;


        Metric test1 = new Metric("progress", toreturnprogress);
        Metric test2 = new Metric("countclosed", toreturncount);
        List<Metric> toreturn = new ArrayList<>();
        Collections.addAll(toreturn, test1, test2);
        return toreturn;

    }

    @Override
    public Set<String> types() {
        return new HashSet<>(Arrays.asList("progress", "countclosed"));
    }
}