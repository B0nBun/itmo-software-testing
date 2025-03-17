package my.beloved.subject.mapreduce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class MapReduce<K, V, R> {
    private Collection<String> inputFiles;
    private MapFunc<K, V> map;
    private ReduceFunc<K, V, R> reduce;

    private ExecutorService mapExecutorService;
    private ExecutorService reduceExecutorService;
    
    private ConcurrentHashMap<K, Collection<V>> mapResult = new ConcurrentHashMap<>();
    private ConcurrentHashMap<K, R> reduceResult = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Exception> filenameToMapException = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Pair<K, Collection<V>>, Exception> reduceException = new ConcurrentHashMap<>();
    private boolean mappingFinished = false;

    public MapReduce(
        Collection<String> inputFiles,
        Supplier<ExecutorService> mapExecutorService,
        Supplier<ExecutorService> reduceExecutorService,
        MapFunc<K, V> map,
        ReduceFunc<K, V, R> reduce
    ) {
        this.inputFiles = inputFiles;
        this.mapExecutorService = mapExecutorService.get();
        this.reduceExecutorService = reduceExecutorService.get();
        this.map = map;
        this.reduce = reduce;
    }

    @FunctionalInterface
    public static interface MapFunc<K, V> {
        public Stream<Pair<K, V>> map(String filename) throws Exception;
    }

    @FunctionalInterface
    public static interface ReduceFunc<K, V, R> {
        public R reduce(K key, Collection<V> values) throws Exception;
    }

    public static final class Pair<K, V> {
        public K key;
        public V val;

        public Pair(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public Map<K, R> run() throws InterruptedException {
        var mapCountDown = new CountDownLatch(inputFiles.size());
        for (String filename : inputFiles) {
            this.mapExecutorService.execute(() -> {
                try (Stream<Pair<K, V>> mapped = this.map.map(filename)) {
                    mapped.forEach(pair -> {
                        Collection<V> coll = this.mapResult.get(pair.key);
                        if (coll == null) {
                            coll = Collections.synchronizedCollection(new ArrayList<>());
                            this.mapResult.put(pair.key, coll);
                        }
                        coll.add(pair.val);
                    });
                } catch (Exception e) {
                    this.filenameToMapException.put(filename, e);
                } finally {
                    mapCountDown.countDown();
                }
            });
        }
        this.mapExecutorService.shutdown();
        mapCountDown.await();
        this.mappingFinished = true;

        var reduceCountDown = new CountDownLatch(this.mapResult.size());
        for (var entry : this.mapResult.entrySet()) {
            this.reduceExecutorService.execute(() -> {
                try {
                    R result = this.reduce.reduce(entry.getKey(), entry.getValue());
                    this.reduceResult.put(entry.getKey(), result);
                } catch (Exception e) {
                    this.reduceException.put(
                        new Pair<>(entry.getKey(), entry.getValue()),
                        e
                    );
                } finally {
                    reduceCountDown.countDown();
                }
            });
        }
        this.reduceExecutorService.shutdown();
        reduceCountDown.await();

        return this.reduceResult;
    }

    public Map<String, Exception> getMapExceptions() {
        return Collections.unmodifiableMap(this.filenameToMapException);
    }

    public Map<Pair<K, Collection<V>>, Exception> getReduceExceptions() {
        return Collections.unmodifiableMap(this.reduceException);
    }

    public boolean isMappingFinished() {
        return this.mappingFinished;
    }
}
