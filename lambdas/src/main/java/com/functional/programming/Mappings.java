package com.functional.programming;

import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * {@link Data id} is unique identifier (primary key) cannot be null
 * {@link Data data} is some data (not used);
 * {@link Data mark} is foreign key to the primary key {@link Data id} can be null
 *
 * There are three possible cases:
 *  - object is parent (when field {@link Data mark} is NULL AND at least one child has foreign key to this object;
 *  - object is child (when field {@link Data mark} is NOT NULL;
 *  - object is usual (when no one has foreign key to it).
 *
 *  Task:
 *  Create {@link Map} Map<Data, Set<Data>> where key is parent object and value is set of its children
 *  Requirements: use Java 8 features
 *
 */
public class Mappings {

    @Builder
    @lombok.Data
    static class Data {
        Long id;
        String data;
        Long mark;
    }

    public static void main(String[] args) {
        List<Data> dataList = initData();
        Set<Long> ids = new HashSet<>();
        Map<Data, Set<Data>> collect = dataList.stream()
                .collect(groupingBy(a -> {
                    if (nonNull(a.mark)) {
                        ids.add(a.mark);
                        return Data.builder().build();
                    } else {
                        return a;
                    }
                }, Collectors.filtering(p -> ids.contains(p.mark), toSet())));
        System.out.println(ids);

        System.out.println(collect);

    }

    private static List<Data> initData() {
        return Arrays.asList(
                Data.builder().id(1L).data("data1").mark(2L).build(), // child
                Data.builder().id(2L).data("data2").mark(null).build(), // parent
                Data.builder().id(3L).data("data3").mark(2L).build(), // child
                Data.builder().id(4L).data("data4").mark(2L).build(), // child
                Data.builder().id(5L).data("data5").mark(4L).build(), // child
                Data.builder().id(6L).data("data6").mark(null).build()); // usual object
    }
}
