package com.functional.programming;

import com.functional.programming.exception.NotImplementedException;
import com.functional.programming.model.Data;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

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
 * Create {@link Map} Map<Data, Set<Data>> where key is parent object and value is set of its children
 * Requirements: use Java 8 features
 */
public class ParentChildrenMapping {

    private Collection<Data> dataCollection;

    public static ParentChildrenMapping of(Collection<Data> dataCollection) {
        return new ParentChildrenMapping(dataCollection);
    }

    public ParentChildrenMapping(Collection<Data> dataCollection) {
        this.dataCollection = dataCollection;
    }

    public Map<Data, Set<Data>> collectParentAndItsChildren() {
        try {
            Set<Long> ids = new HashSet<>();
            Map<Data, Set<Data>> collect = dataCollection.stream()
                    .collect(groupingBy(a -> {
                        if (nonNull(a.getMark())) {
                            ids.add(a.getMark());
                            return Data.builder().build();
                        } else {
                            return a;
                        }
                    }, Collectors.filtering(p -> ids.contains(p.getMark()), toSet())));
            System.out.println(ids);

            System.out.println(collect);
            return  collect;
        } catch (Exception e) {
            throw new NotImplementedException("method not implemented yet");
        }
    }
}
