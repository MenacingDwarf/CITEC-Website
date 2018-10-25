package com.dreamwork.art.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GraphQLRequest {
    @Setter
    private String query;

    private Map<String, Object> variables;

    public GraphQLRequest() {
        this.variables = new HashMap<>();
    }

    public void addVariable(String name, Object variable) {
        this.variables.put(name, variable);
    }
}
