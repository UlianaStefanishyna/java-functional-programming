package com.intstream.streams.stream;

import com.intstream.streams.function.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsIntStream implements IntStream {

    private List<MiddleOps> middleOpsList = new ArrayList<>();
    private Integer index;
    private List<Integer> data;

    private AsIntStream(int[] values) {
        this.data = new ArrayList<>();
        for (int value : values) {
            this.data.add(value);
        }
    }

    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    @Override
    public Double average() {
        double sum = 0;
        int count = 0;
        for (Integer value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                sum += this.index;
                count++;
            }
        }
        return sum / count;
    }

    @Override
    public synchronized Integer max() {
        return Collections.max(this.collect(this.data));
    }

    @Override
    public synchronized Integer min() {
        return Collections.min(this.collect(this.data));
    }

    @Override
    public synchronized long count() {
        int count = 0;
        for (Integer value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) count++;
        }
        return count;
    }

    @Override
    public synchronized Integer sum() {
        int sum = 0;
        for (Integer value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                sum += this.index;
            }
        }
        return sum;
    }

    @Override
    public synchronized IntStream filter(IntPredicate predicate) {
        this.middleOpsList.add(MiddleOps.builder()
                .booleanSupplier(() -> predicate.test(index)).build());
        return this;
    }

    @Override
    public synchronized void forEach(IntConsumer action) {
        for (Integer value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                action.accept(this.index);
            }
        }
    }

    @Override
    public synchronized IntStream map(IntUnaryOperator mapper) {
        this.middleOpsList.add(MiddleOps.builder()
                .integerSupplier(() -> mapper.apply(index)).build());
        return this;
    }

    @Override
    public synchronized IntStream flatMap(IntToIntStreamFunction func) {
        this.middleOpsList.add(MiddleOps.builder()
                .supplier(() -> {
                    int[] ints = func.applyAsIntStream(this.index).toArray();
                    int[] newArray = new int[ints.length];

                    for (int i = 0; i < ints.length; i++) {
                        newArray[i] = ints[i];
                    }
                    return newArray;
                }).build());
        return this;
    }

    @Override
    public synchronized int reduce(int identity, IntBinaryOperator op) {
        for (Integer value : this.data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                identity = op.apply(identity, this.index);
            }
        }
        return identity;
    }

    @Override
    public synchronized int[] toArray() {
        List<Integer> result = this.collect(this.data);
        int[] array = new int[result.size()];

        for (int i = 0; i < result.size(); i++) {
            array[i] = result.get(i);
        }
        return array;
    }

    private boolean doMiddleOpsReturningEvaluatedValue(Integer value) {
        boolean skip = false;
        this.index = value;
        for (MiddleOps middleOps : this.middleOpsList) {
            if (middleOps.getBooleanSupplier() != null) {
                if (!middleOps.getBooleanSupplier().get()) {
                    skip = true;
                    break;
                }
            }
            if (middleOps.getIntegerSupplier() != null) {
                this.index = middleOps.getIntegerSupplier().get();
            }

            if (middleOps.getSupplier() != null) {
                int[] ints = middleOps.getSupplier().get();

            }
        }
        return !skip;
    }

    private List<Integer> collect(List<Integer> data) {
        List<Integer> resultList = new ArrayList<>();
        for (Integer value : data) {
            if (this.doMiddleOpsReturningEvaluatedValue(value)) {
                resultList.add(this.index);
            }
        }
        return resultList;
    }

    @Builder
    @Getter
    private static class MiddleOps {
        private Supplier<Integer> integerSupplier;
        private Supplier<Boolean> booleanSupplier;
        private Supplier<int[]> supplier;
    }
}