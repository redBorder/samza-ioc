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
    public void deleteAll(List<String> list) {

    }

    @Override
    public KeyValueIterator<String, Set<String>> range(String s, String k1) {
        return null;
    }

    @Override
    public KeyValueIterator<String, Set<String>> all() {
        return null;
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