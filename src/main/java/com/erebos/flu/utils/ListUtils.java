package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

/**
 * Utility class for various list operations.
 */
public final class ListUtils {

    private ListUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if an element is present in the specified list.
     *
     * @param list   the list to search within
     * @param search the element to search for
     * @param <T>    the type of elements in the list
     * @return {@code true} if the element is present, {@code false} otherwise
     */
    public static <T> boolean checkIfElementIsPresentInList(final List<T> list, final T search) {
        if (Optional.ofNullable(search).isEmpty()) {
            return false;
        }
        return Optional.ofNullable(list)
                .map(l -> l.stream().anyMatch(search::equals))
                .orElse(false);
    }

    /**
     * Checks if a list is null or empty.
     *
     * @param list the list to check
     * @param <T>  the type of elements in the list
     * @return {@code true} if the list is null or empty, {@code false} otherwise
     */
    public static <T> boolean isListNullOrEmpty(final List<T> list) {
        return isNull(list) || list.isEmpty();
    }

    /**
     * Checks if a list is not null and not empty.
     *
     * @param list the list to check
     * @param <T>  the type of elements in the list
     * @return {@code true} if the list is not null and not empty, {@code false} otherwise
     */
    public static <T> boolean isListNotNullAndNotEmpty(final List<T> list) {
        return !isNull(list) && !list.isEmpty();
    }

    /**
     * Finds the intersection of two lists.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @param <T>   the type of elements in the lists
     * @return a list containing the intersection of the two lists
     */
    public static <T> List<T> intersectLists(final List<T> list1, final List<T> list2) {
        final Set<T> intersection = new HashSet<>(list1);
        final Set<T> set2 = new HashSet<>(list2);
        intersection.retainAll(set2);
        return new ArrayList<>(intersection);
    }

    /**
     * Finds the intersection of two lists based on a matching function.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @param func  the function to apply for matching elements
     * @param <T>   the type of elements in the first list
     * @param <U>   the type of elements in the second list
     * @return a list containing elements in the first list that have matches in the second list based on the function
     */
    public static <T, U> List<T> intersectListsByMatchingFunction(final List<T> list1,
                                                                  final List<U> list2,
                                                                  final Function<T, U> func) {
        return getNullableList(list1)
                .parallelStream()
                .filter(i -> checkIfElementIsPresentInList(list2, func.apply(i)))
                .toList();
    }

    /**
     * Sums a double property over all items in a collection.
     *
     * @param list            the collection of items
     * @param doubleGetMethod the method to get the double property
     * @param <T>             the type of items in the collection
     * @return the sum of the double property over all items
     */
    public static <T> double sumOfDoubleMemberOverAllItems(final Collection<T> list,
                                                           final ToDoubleFunction<? super T> doubleGetMethod) {
        return list.stream().mapToDouble(doubleGetMethod).sum();
    }

    /**
     * Filters a list by a predicate.
     *
     * @param list      the list to filter
     * @param predicate the predicate to apply
     * @param <T>       the type of elements in the list
     * @return an ImmutableList containing elements that match the predicate
     */
    public static <T> ImmutableList<T> filterListByPredicate(final List<T> list, final Predicate<T> predicate) {
        return ImmutableList.copyOf(getNullableList(list)
                .parallelStream()
                .filter(predicate)
                .toList());
    }

    /**
     * Finds the first element in a list that matches a predicate.
     *
     * @param list      the list to search
     * @param predicate the predicate to match
     * @param <T>       the type of elements in the list
     * @return an Optional containing the first matching element, or an empty Optional if none match
     */
    public static <T> Optional<T> findFirstByPredicate(final List<T> list, final Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Returns the given list or an empty list if the input is null.
     *
     * @param list the list to check
     * @param <T>  the type of elements in the list
     * @return the input list or an empty list if null
     */
    public static <T> List<T> getNullableList(final List<T> list) {
        return Optional.ofNullable(list).orElseGet(List::of);
    }

    /**
     * Sorts a list using a specified comparator.
     *
     * @param list         the list to sort
     * @param funcToSortBy the comparator to use for sorting
     * @param <T>          the type of elements in the list
     * @return a sorted list
     */
    public static <T> List<T> sortList(final List<T> list, final Comparator<? super T> funcToSortBy) {
        return getNullableList(list).stream()
                .sorted(funcToSortBy)
                .toList();
    }

    /**
     * Filters elements where a String member is not null or empty.
     *
     * @param list         the list to filter
     * @param stringGetter function to get the String member from each element
     * @param <T>          the type of elements in the list
     * @return an ImmutableList of elements where the String member is not null or empty
     */
    public static <T> ImmutableList<T> filterItemsWhereStringMemberIsNotNullOrEmpty(final ImmutableList<T> list, final Function<T, String> stringGetter) {
        Predicate<T> stringMemberIsNotNullOrEmpty = i -> !StringUtils.isStringNullOrEmpty(stringGetter.apply(i));
        return filterListByPredicate(list, stringMemberIsNotNullOrEmpty);
    }

    /**
     * Filters elements where a String member is null or empty.
     *
     * @param list         the list to filter
     * @param stringGetter function to get the String member from each element
     * @param <T>          the type of elements in the list
     * @return an ImmutableList of elements where the String member is null or empty
     */
    public static <T> ImmutableList<T> filterItemsWhereStringMemberIsNullOrEmpty(final ImmutableList<T> list, final Function<T, String> stringGetter) {
        Predicate<T> stringMemberIsNullOrEmpty = i -> StringUtils.isStringNullOrEmpty(stringGetter.apply(i));
        return filterListByPredicate(list, stringMemberIsNullOrEmpty);
    }

    /**
     * Filters elements where a specified member is null.
     *
     * @param list   the list to filter
     * @param getter function to get the member to check for null
     * @param <T>    the type of elements in the list
     * @param <U>    the type of the member being checked
     * @return an ImmutableList of elements where the member is null
     */
    public static <T, U> ImmutableList<T> filterItemsWhereMemberIsNull(final ImmutableList<T> list, final Function<T, U> getter) {
        Predicate<T> memberIsNull = i -> getter.apply(i) == null;
        return filterListByPredicate(list, memberIsNull);
    }

    /**
     * Filters elements where a specified member is not null.
     *
     * @param list   the list to filter
     * @param getter function to get the member to check for null
     * @param <T>    the type of elements in the list
     * @param <U>    the type of the member being checked
     * @return an ImmutableList of elements where the member is not null
     */
    public static <T, U> ImmutableList<T> filterItemsWhereMemberIsNotNull(final ImmutableList<T> list, final Function<T, U> getter) {
        Predicate<T> memberIsNotNull = i -> getter.apply(i) != null;
        return filterListByPredicate(list, memberIsNotNull);
    }

    /**
     * Filters a list by a predicate and maps the filtered elements to a specific attribute.
     *
     * @param list      the list to filter and map
     * @param predicate the predicate to filter elements
     * @param func      function to map each filtered element to the desired attribute
     * @param <T>       the type of elements in the input list
     * @param <U>       the type of the mapped attribute
     * @return a list of mapped attributes from filtered elements
     */
    public static <T, U> List<U> getAttributesByListFilter(final ImmutableList<T> list,
                                                           final Predicate<T> predicate,
                                                           final Function<T, U> func) {
        return filterListByPredicate(list, predicate)
                .stream()
                .map(func)
                .toList();
    }

    /**
     * Extracts all members from a list using a specified getter function.
     *
     * @param list   the list of elements
     * @param getter function to extract the member from each element
     * @param <T>    the type of elements in the list
     * @param <U>    the type of extracted members
     * @return a list of extracted members
     */
    public static <T, U> List<U> extractMembersAsList(final ImmutableList<T> list, final Function<T, U> getter) {
        return getNullableList(list).stream().map(getter).toList();
    }

    /**
     * Concatenates multiple lists into a single list.
     *
     * @param lists the lists to concatenate
     * @param <T>   the type of elements in the lists
     * @return a list containing all elements from the input lists
     */
    @SafeVarargs
    public static <T> List<T> concatenateLists(final List<T>... lists) {
        final List<T> result = new ArrayList<>();
        stream(lists).forEach(result::addAll);
        return result;
    }

    /**
     * Finds duplicate entries in a collection.
     *
     * @param collection the collection to check for duplicates
     * @param <T>        the type of elements in the collection
     * @return a set containing duplicate elements
     */
    public static <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> uniques = new HashSet<>();
        return collection.stream()
                .filter(e -> !uniques.add(e))
                .collect(Collectors.toSet());
    }

    /**
     * Gets all distinct values from a list based on a specified property.
     *
     * @param list   the list of elements
     * @param getter function to extract the property from each element
     * @param <T>    the type of elements in the list
     * @param <U>    the type of the distinct property
     * @return a set containing distinct values of the specified property
     */
    public static <T, U> Set<U> createDistinctSetFromListProperty(final List<T> list, final Function<T, U> getter) {
        return getNullableList(list).parallelStream().map(getter).collect(Collectors.toSet());
    }

}
