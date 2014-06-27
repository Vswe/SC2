package vswe.stevesvehicles.util;


public class Tuple<T, S> {
    private T firstObject;
    private S secondObject;

    public Tuple(T firstObject, S secondObject) {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    public T getFirstObject() {
        return firstObject;
    }

    public S getSecondObject() {
        return secondObject;
    }
}
