package com.intstream.streams.stream;

import com.intstream.streams.function.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class AsIntStream implements IntStream {

    private List<MiddleOps> middleOpsList = new ArrayList<>();
    private List<AtomicInteger> data;

    private AsIntStream(int[] values) {
        this.data = new ArrayList<>();
        for (int value : values) {
            this.data.add(new AtomicInteger(value));
        }
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        double sum = 0;
        int count = 0;
        for (AtomicInteger value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                sum += value.get();
                count++;
            }
        }
        return sum / count;
    }

    @Override
    public Integer max() {
        return Collections.max(this.collect(this.data));
    }

    @Override
    public Integer min() {
        return Collections.min(this.collect(this.data));
    }

    @Override
    public long count() {
        int count = 0;
        for (AtomicInteger value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) count++;
        }
        return count;
    }

    @Override
    public Integer sum() {
        int sum = 0;
        for (AtomicInteger value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                sum += value.get();
            }
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        this.middleOpsList.add(MiddleOps.builder()
                .booleanFunction(predicate::test).build());
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        for (AtomicInteger value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                action.accept(value.intValue());
            }
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        this.middleOpsList.add(MiddleOps.builder()
                .integerFunction(mapper::apply).build());
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        this.middleOpsList.add(MiddleOps.builder()
                .function(index -> {
                    int[] ints = func.applyAsIntStream(index).toArray();
                    int[] newArray = new int[ints.length];

                    for (int i = 0; i < ints.length; i++) {
                        newArray[i] = ints[i];
                    }
                    return newArray;
                }).build());
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        for (AtomicInteger value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                identity = op.apply(identity, value.get());
            }
        }
        return identity;
    }

    @Override
    public int[] toArray() {
        List<Integer> result = this.collect(this.data);
        int[] array = new int[result.size()];

        for (int i = 0; i < result.size(); i++) {
            array[i] = result.get(i);
        }
        return array;
    }

    private boolean doMiddleOpsReturningEvaluatedValue(AtomicInteger value) {
        boolean skip = false;
        for (MiddleOps middleOps : this.middleOpsList) {
            if (middleOps.getBooleanFunction() != null) {
                if (!middleOps.getBooleanFunction().apply(value.get())) {
                    skip = true;
                    break;
                }
            }
            if (middleOps.getIntegerFunction() != null) {
                value.set(middleOps.getIntegerFunction().apply(value.get()));
            }
            if (middleOps.getFunction() != null) {
                int[] ints = middleOps.getFunction().apply(value.get());
            }
        }
        return !skip;
    }

    private List<Integer> collect(List<AtomicInteger> data) {
        List<Integer> resultList = new ArrayList<>();
        for (AtomicInteger value : data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                resultList.add(value.get());
            }
        }
        return resultList;
    }

    @Builder
    @Getter
    private static class MiddleOps {
        private Function<Integer, Integer> integerFunction;
        private Function<Integer, Boolean> booleanFunction;
        private Function<Integer, int[]> function;
    }
}