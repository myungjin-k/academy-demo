package my.myungjin.academyDemo.commons;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Id<R, V> {

    private final Class<R> reference;

    private final V value;

    public static <R, V> Id<R, V> of(Class<R> reference, V value){
        return new Id<>(reference, value);
    }

    public V value(){
        return value;
    }

}
