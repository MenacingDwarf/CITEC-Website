package com.dreamwork.backapp;

import com.dreamwork.art.service.SingleValueConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Example {
    static class Metric {
        private final String type;

        private final float value;

        Metric(String type, float value) {
            this.type = type;
            this.value = value;
        }
    }

    static class MetricGroup {
        private final Timestamp timestamp;

        private final List<Metric> metrics;

        public MetricGroup(Timestamp timestamp) {
            this.timestamp = timestamp;
            this.metrics = new ArrayList<>();
        }

        public void addMetric(Metric metric) {
            this.metrics.add(metric);
        }
    }


    static String data = "{\"data\":{\"rateLimit\":{\"cost\":4,\"remaining\":4992,\"resetAt\":\"2018-11-20T20:09:42Z\"},\"nodes\":[{\"milestones\":{\"nodes\":[{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2015-11-21T21:14:47Z\",\"closed\":true}]},\"state\":\"OPEN\",\"dueOn\":null,\"title\":\"1.0\"}]}},{\"milestones\":{\"nodes\":[]}},{\"milestones\":{\"nodes\":[{\"issues\":{\"nodes\":[{\"closedAt\":\"2016-05-28T17:34:56Z\",\"closed\":true},{\"closedAt\":\"2016-05-28T17:34:50Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.49\"},{\"issues\":{\"nodes\":[{\"closedAt\":\"2017-08-05T11:48:00Z\",\"closed\":true},{\"closedAt\":\"2017-08-09T12:49:54Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.51\"},{\"issues\":{\"nodes\":[]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.52\"},{\"issues\":{\"nodes\":[{\"closedAt\":\"2018-03-08T16:03:53Z\",\"closed\":true},{\"closedAt\":\"2018-04-07T23:39:12Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.60\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-10-01T16:52:29Z\",\"closed\":true}]},\"state\":\"OPEN\",\"dueOn\":null,\"title\":\"v1.70\"},{\"issues\":{\"nodes\":[{\"closedAt\":\"2018-05-08T12:00:57Z\",\"closed\":true},{\"closedAt\":\"2018-05-14T17:11:54Z\",\"closed\":true},{\"closedAt\":\"2018-05-08T09:47:28Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.61\"},{\"issues\":{\"nodes\":[{\"closedAt\":\"2018-06-27T07:57:23Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.62\"},{\"issues\":{\"nodes\":[]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"v1.63\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false}]},\"state\":\"OPEN\",\"dueOn\":null,\"title\":\"v1.65\"}]}},{\"milestones\":{\"nodes\":[{\"issues\":{\"nodes\":[{\"closedAt\":\"2018-06-20T21:59:27Z\",\"closed\":true},{\"closedAt\":\"2018-05-23T20:35:49Z\",\"closed\":true},{\"closedAt\":\"2018-06-07T19:42:23Z\",\"closed\":true},{\"closedAt\":\"2018-06-07T19:43:29Z\",\"closed\":true},{\"closedAt\":\"2018-05-31T15:39:17Z\",\"closed\":true},{\"closedAt\":\"2018-04-24T02:20:47Z\",\"closed\":true},{\"closedAt\":\"2018-05-29T22:32:43Z\",\"closed\":true},{\"closedAt\":\"2018-07-18T21:20:15Z\",\"closed\":true},{\"closedAt\":\"2018-04-19T20:33:56Z\",\"closed\":true},{\"closedAt\":\"2018-05-02T18:26:49Z\",\"closed\":true},{\"closedAt\":\"2018-04-25T18:20:37Z\",\"closed\":true},{\"closedAt\":\"2018-04-26T15:50:12Z\",\"closed\":true},{\"closedAt\":\"2018-04-25T21:15:59Z\",\"closed\":true},{\"closedAt\":\"2018-04-19T20:44:28Z\",\"closed\":true},{\"closedAt\":\"2018-04-27T16:57:44Z\",\"closed\":true},{\"closedAt\":\"2018-05-18T03:03:44Z\",\"closed\":true},{\"closedAt\":\"2018-04-19T16:53:53Z\",\"closed\":true},{\"closedAt\":\"2018-04-13T22:40:06Z\",\"closed\":true},{\"closedAt\":\"2018-04-13T22:02:24Z\",\"closed\":true},{\"closedAt\":\"2018-06-13T17:30:16Z\",\"closed\":true},{\"closedAt\":\"2018-04-13T21:52:12Z\",\"closed\":true},{\"closedAt\":\"2018-05-21T18:04:13Z\",\"closed\":true},{\"closedAt\":\"2018-04-30T23:18:59Z\",\"closed\":true},{\"closedAt\":\"2018-06-08T22:49:57Z\",\"closed\":true},{\"closedAt\":\"2018-05-05T23:11:19Z\",\"closed\":true},{\"closedAt\":\"2018-05-02T18:29:09Z\",\"closed\":true},{\"closedAt\":\"2018-05-21T18:02:57Z\",\"closed\":true},{\"closedAt\":\"2018-06-05T23:22:11Z\",\"closed\":true},{\"closedAt\":\"2018-04-26T17:32:32Z\",\"closed\":true},{\"closedAt\":\"2018-06-06T18:50:21Z\",\"closed\":true},{\"closedAt\":\"2018-05-05T23:11:19Z\",\"closed\":true},{\"closedAt\":\"2018-04-30T23:18:14Z\",\"closed\":true},{\"closedAt\":\"2018-06-01T17:38:13Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify@0.4\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-08-07T17:52:00Z\",\"closed\":true},{\"closedAt\":\"2018-08-07T19:24:51Z\",\"closed\":true},{\"closedAt\":\"2018-06-06T18:53:51Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify-react@0.2.0\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-06-04T22:18:19Z\",\"closed\":true},{\"closedAt\":\"2018-04-25T21:13:58Z\",\"closed\":true},{\"closedAt\":\"2018-05-02T21:49:38Z\",\"closed\":true},{\"closedAt\":\"2018-06-05T23:26:53Z\",\"closed\":true},{\"closedAt\":\"2018-06-05T23:12:57Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify-react-native@0.3.0\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-04-19T16:51:34Z\",\"closed\":true},{\"closedAt\":\"2018-07-19T17:11:38Z\",\"closed\":true},{\"closedAt\":\"2018-11-15T23:48:00Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"amazon-cognito-identity-js@2.1\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-06-01T18:24:46Z\",\"closed\":true},{\"closedAt\":\"2018-06-01T18:24:46Z\",\"closed\":true},{\"closedAt\":\"2018-05-07T17:16:41Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify-angular@0.1\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":null,\"closed\":false},{\"closedAt\":null,\"closed\":false}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify@0.6\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-04-25T17:58:19Z\",\"closed\":true},{\"closedAt\":\"2018-08-07T19:55:08Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify@0.7\"},{\"issues\":{\"nodes\":[{\"closedAt\":null,\"closed\":false},{\"closedAt\":\"2018-04-26T20:38:59Z\",\"closed\":true}]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify@0.8\"},{\"issues\":{\"nodes\":[]},\"state\":\"CLOSED\",\"dueOn\":null,\"title\":\"aws-amplify@0.9\"}]}}]}}";

    private static LinkedHashMap<String, Object> toMap(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, new TypeReference<LinkedHashMap<String, Object>>(){});
    }

    public static void main(String[] args) throws IOException {
        //LinkedHashMap<String, Object> map = toMap(data);

        //List list = (List)((Map)map.get("data")).get("nodes");

        //System.out.println(list.get(0).getClass());

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(SingleValueConverter.class));
        System.out.println(provider.findCandidateComponents("com.dreamwork.art.service.converters"));
    }
}
