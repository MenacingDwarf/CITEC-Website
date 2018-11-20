package com.dreamwork.art.tools;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pair<F, S> {
    private F first;
    private S second;
}
