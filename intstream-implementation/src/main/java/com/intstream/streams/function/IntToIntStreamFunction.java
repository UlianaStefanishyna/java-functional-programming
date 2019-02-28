package com.intstream.streams.function;

import com.intstream.streams.stream.IntStream;

public interface IntToIntStreamFunction {
     IntStream applyAsIntStream(int value);
}
