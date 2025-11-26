package Database;

public final class Pair<K, V>{
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public K setKey(K key) {
        return key;
    }
    public V setValue(V value) {
        return value;
    }
}
