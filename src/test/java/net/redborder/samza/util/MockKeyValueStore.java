package net.redborder.samza.util;

import org.apache.samza.storage.kv.Entry;
import org.apache.samza.storage.kv.KeyValueIterator;
import org.apache.samza.storage.kv.KeyValueStore;

import java.util.*;

public class MockKeyValueStore implements KeyValueStore<String, Set<String>> {
    Map<String, Set<String>> store = new HashMap<>();

    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public Set<String> get(String s) {
        return store.get(s);
    }

    @Override
    public Map<String, Set<String>> getAll(List<String> list) {
        return null;
    }

    @Override
    public void put(String s, Set<String> strings) {
        store.put(s, strings);
    }

    @Override
    public void putAll(List<Entry<String, Set<String>>> list) {

    }

    @Override
    public void delete(String s) {
        store.remove(s);
    }

    @Override
    public String toString() {
        return store.toString();
    }

    @Override
    public void deleteAll(List<String> list) {

    }

    @Override
    public KeyValueIterator<String, Set<String>> range(String s, String k1) {
        return null;
    }

    @Override
    public KeyValueIterator<String, Set<String>> all() {
        final Iterator<java.util.Map.Entry<String, Set<String>>> iterator = store.entrySet().iterator();

        KeyValueIterator<String, Set<String>> keyValueIterator = new KeyValueIterator<String, Set<String>>() {

            @Override
            public void close() {

            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<String, Set<String>> next() {
                java.util.Map.Entry<String, Set<String>> entry = iterator.next();
                Entry<String, Set<String>> e = new Entry<>(entry.getKey(), entry.getValue());

                return e;
            }

            @Override
            public void remove() {

            }
        };

        return keyValueIterator;
    }

    @Override
    public void close() {
        store.clear();
        store = null;
    }

    @Override
    public void flush() {
        store.clear();
    }
}