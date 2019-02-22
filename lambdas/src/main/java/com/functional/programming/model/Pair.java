package com.functional.programming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class Pair<K,V> {
    private K key;
    private V value;
}
