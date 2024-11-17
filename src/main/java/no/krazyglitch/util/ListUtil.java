package no.krazyglitch.util;

import java.util.ArrayList;
import java.util.List;

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
}
