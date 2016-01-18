package net.redborder.samza.util;

import org.apache.samza.storage.kv.Entry;
import org.apache.samza.storage.kv.KeyValueIterator;
import org.apache.samza.storage.kv.KeyValueStore;

import java.util.*;

public class MockKeyValueStore implements KeyValueStore<String, List<String>> {
    Map<String, List<String>> store = new HashMap<>();

    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public List<String> get(String s) {
        return store.get(s);
    }

    @Override
    public Map<String, List<String>> getAll(List<String> list) {
        return null;
    }

    @Override
    public void put(String s, List<String> strings) {
        store.put(s, strings);
    }

    @Override
    public void putAll(List<Entry<String, List<String>>> list) {

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
    public KeyValueIterator<String, List<String>> range(String s, String k1) {
        return null;
    }

    @Override
    public KeyValueIterator<String, List<String>> all() {
        final Iterator<java.util.Map.Entry<String, List<String>>> iterator = store.entrySet().iterator();

        KeyValueIterator<String, List<String>> keyValueIterator = new KeyValueIterator<String, List<String>>() {

            @Override
            public void close() {

            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<String, List<String>> next() {
                java.util.Map.Entry<String, List<String>> entry = iterator.next();
                Entry<String, List<String>> e = new Entry<>(entry.getKey(), entry.getValue());

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