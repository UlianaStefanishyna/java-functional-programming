package com.intstream.streams;

import com.intstream.streams.stream.IntStream;
import com.intstream.streams.stream.AsIntStream;

public class StreamApp {

//    private static List<Integer> list = Arrays.asList(-1, 0, 2, 2, 1);

    public static int streamOperations(IntStream intStream) {
        //IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3); // input values
        return intStream
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1)) // 0, 1, 2, 3, 4, 5, 8, 9, 10
                .reduce(0, (a, b) -> a + b);
    }

    public static int[] streamToArray(IntStream intStream) {
        return intStream.toArray();
    }

    public static String streamForEach(IntStream intStream) {
        StringBuilder str = new StringBuilder();
        intStream.forEach(str::append);
        return str.toString();
    }

    public static void main(String[] args) {
        IntStream intStream = AsIntStream.of(-1, 1, 0);
        Integer sum = intStream
                .map(i -> i + 5)
                .sum();
        System.out.println(sum);
    }
}
