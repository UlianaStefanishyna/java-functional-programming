package com.functional.programming;

import com.functional.programming.exception.NotImplementedException;
import com.functional.programming.model.Data;
import com.functional.programming.model.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * {@link Data id} is unique identifier (primary key) cannot be null
 * {@link Data data} is some data (not used);
 * {@link Data mark} is foreign key to the primary key {@link Data id} can be null
 * <p>
 * There are three possible cases:
 * - object is parent (when field {@link Data mark} is NULL AND at least one child has foreign key to this object;
 * - object is child (when field {@link Data mark} is NOT NULL;
 * - object is usual (when no one has foreign key to it).
 * <p>
 * Task:
 * Create {@link Map} Map<Data, Set<Data>> where key is parent object and value is set of its children and group
 * Requirements: use Java 8 features
 */
public class ParentChildrenMapping {

    private Collection<Data> dataCollection;
    private Map<String, Set<String>> inputData;

    public static ParentChildrenMapping of(Collection<Data> dataCollection, Map<String, Set<String>> inputData) {
        return new ParentChildrenMapping(dataCollection, inputData);
    }

    public ParentChildrenMapping(Collection<Data> dataCollection, Map<String, Set<String>> inputData) {
        this.dataCollection = dataCollection;
        this.inputData = inputData;
    }

    public List<Pair<String, Optional<Long>>> collectParentAndItsChildren() {
        try {

            Set<Long> ids = new HashSet<>();
            Map<Long, Data> dataMap = dataCollection.stream().collect(toMap(Data::getId, identity()));

            Map<Data, Set<String>> collect = this.dataCollection.stream()
                    .peek(data -> Optional.ofNullable(data.getMark()).ifPresent(ids::add))
                    .filter(p -> ids.contains(p.getMark()))
                    .collect(groupingBy(
                            a -> Optional.ofNullable(a.getMark()).map(dataMap::get).orElse(a),
                            mapping(Data::getData, toSet())));

            System.out.println(ids);

            List<Pair<String, Optional<Long>>> collect1 = inputData.entrySet().stream()
                    .flatMap(e -> e.getValue().stream().map(v -> new Pair<>(e.getKey(), v)))
                    .map(pair -> new Pair<>(pair.getValue(), collect.entrySet().stream()
                            .filter(parent -> parent.getKey().getData().equals(pair.getKey()))
                            .filter(parent -> !parent.getValue().contains(pair.getValue()))
                            .map(parent -> parent.getKey().getId()).findAny()))
                    .collect(toList());


            System.out.println(collect);
            System.out.println("---");
            System.out.println(collect1);
            return collect1;
        } catch (Exception e) {
            throw new NotImplementedException("method not implemented yet");
        }
    }
}
