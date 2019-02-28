package com.intstream.streams;

import org.junit.Before;
import org.junit.Test;
import com.intstream.streams.stream.AsIntStream;
import com.intstream.streams.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StreamAppTest {

    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);

    }

    @Test
    public void test() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
        System.out.println(intStream);
    }

    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamSum() {
        System.out.println("streamSum");
        Integer expected = -1;
        Integer actual = intStream.filter(i -> i < 0).sum();
        assertEquals(expected, actual);
    }

    @Test
    public void testStreamReduce() {
        System.out.println("streamReduse");
        int expected = 6;
        int actual = intStream.filter(i -> i > 0)
                .reduce(0, (a, b) -> a + b);
        assertEquals(expected, actual);
    }

    @Test
    public void testStreamMin() {
        System.out.println("streamMin");
        Integer expected = 1;
        Integer actual = intStream.filter(i -> i > 0)
                .min();
        assertEquals(expected, actual);
    }

    @Test
    public void testStreamMax() {
        System.out.println("streamMax");
        Integer expected = 3;
        Integer actual = intStream.filter(i -> i > 0)
                .max();
        assertEquals(expected, actual);
    }

    @Test
    public void testStreamCount() {
        System.out.println("streamCount");
        long expected = 3;
        long actual = intStream.filter(i -> i > 0)
                .count();
        assertEquals(expected, actual);
    }

    @Test
    public void testStreamAvg() {
        System.out.println("streamAvg");
        double expected = 2;
        double actual = intStream.filter(i -> i > 0)
                .average();
        assertEquals(expected, actual, 1);
    }
}