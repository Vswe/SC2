package vswe.stevesvehicles.registries;


import java.util.Collection;

public interface IRegistry<E> {
    String getFullCode(E obj);
    Collection<E> getElements();
    String getCode();
}
