package my.beloved.subject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

import my.beloved.subject.mapreduce.MapReduce;

public class App {
    public static void main(String[] args) throws InterruptedException {
        var files = new ArrayList<>(Arrays.asList(args));
        var mapReduce = new MapReduce<String, Integer, Long>(
            files,
            () -> Executors.newSingleThreadExecutor(), 
            () -> Executors.newSingleThreadExecutor(), 
            filename -> {
                return Files.
                    lines(Paths.get(filename)).
                    flatMap(line -> Arrays.stream(line.split("\\s+"))).
                    map(word -> new MapReduce.Pair<>(word, 1));
            },
            (_key, values) -> {
                return Long.valueOf(values.size());
            }
        );

        assert mapReduce.getMapExceptions().size() == 0;
        assert mapReduce.getReduceExceptions().size() == 0;

        var result = mapReduce.run();
        for (var entry : result.entrySet()) {
            System.out.println(entry.getKey() + " "  + entry.getValue());
        }
    }
}
