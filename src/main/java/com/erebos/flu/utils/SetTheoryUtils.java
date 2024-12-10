package com.erebos.flu.utils;

import java.util.*;
import java.util.stream.Collectors;


public class SetTheoryUtils {

    // Sub-function to compute the intersection (inner join) of two lists
    public static <T> List<T> intersection(List<T> leftList, List<T> rightList) {
        return leftList.stream()
                .filter(rightList::contains)
                .collect(Collectors.toList());
    }

    // Function to compute the exclusive left outer join
    public static <T> List<T> leftOuterJoinExclusive(List<T> leftList, List<T> rightList) {
        // Step 1: Get the intersection
        List<T> intersection = intersection(leftList, rightList);

        // Step 2: Subtract the intersection from the left list
        return leftList.stream()
                .filter(element -> !intersection.contains(element))
                .collect(Collectors.toList());
    }

    // Function to compute the full left outer join
    public static <T> List<T> leftOuterJoin(List<T> leftList, List<T> rightList) {
        // Step 1: Get the intersection
        List<T> intersection = intersection(leftList, rightList);

        // Step 2: Get the exclusive elements from the left list
        List<T> exclusiveLeft = leftOuterJoinExclusive(leftList, rightList);

        // Step 3: Combine the exclusive left and intersection
        List<T> result = new ArrayList<>(exclusiveLeft);
        result.addAll(intersection);

        return result;
    }

    // Function to compute the union of two lists
    public static <T> List<T> union(List<T> leftList, List<T> rightList) {
        // Combine both lists and remove duplicates
        return new ArrayList<>(new HashSet<>(leftList) {{
            addAll(rightList);
        }});
    }
}
