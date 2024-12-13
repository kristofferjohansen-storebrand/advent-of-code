package no.krazyglitch.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtil {

    public static <E> List<List<E>> createPermutations(final List<E> list) {
        if (list.isEmpty()) {
            final List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        final List<List<E>> permutations = new ArrayList<>();
        final E firstElement = list.removeFirst();
        final List<List<E>> recursivePermutations = createPermutations(list);

        for (final List<E> innerList : recursivePermutations) {
            for (int i = 0; i <= innerList.size(); i++) {
                final List<E> tempElements = new ArrayList<>(innerList);
                tempElements.add(i, firstElement);
                permutations.add(tempElements);
            }
        }

        return permutations;
    }

    public static int getWrappedIndex(final int index, final List<?> list) {
        if (index < 0) {
            return list.size() + index;
        }

        return index % list.size();
    }

    public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
