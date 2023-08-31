package com.example.trendifywriter.infrastructure;

import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public class Algorithm {

    public static void main(String[] args) {
        String test = "1231313121113121311112122121211";
        long result = StringToLongDeveloped(test ,  c -> (long) (c-'0'));
        System.out.println("result = " + result);
    }

    private static long StringToLongDeveloped(String test , ToLongFunction<Character> characterToLongFunction) {

        return IntStream.range(0, test.length())
                .mapToLong(i -> characterToLongFunction.applyAsLong(test.charAt(i)))
                .reduce(0L, (a, b) -> a * 10 + b);

    }

    private static long StringToLong(String test , Function<Character , Long> mapper) {

        return IntStream.range(0, test.length())
                .mapToObj(i -> mapper.apply(test.charAt(i)))
                .reduce(0L, (a, b) -> a * 10 + b);
    }


}
