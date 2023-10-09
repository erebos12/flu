package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

public final class ListUtils {

    private ListUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> boolean checkIfElementIsPresentInList(final List<T> list, final T search) {
        if (Optional.ofNullable(list).isEmpty() || Optional.ofNullable(search).isEmpty()) {
            return false;
        }
        return list.stream().anyMatch(item -> Optional.ofNullable(item).isPresent() && item.equals(search));
    }

    public static <T> boolean isListNullOrEmpty(final List<T> list) {
        return isNull(list) || list.isEmpty();
    }

    public static <T> boolean isListNotNullAndNotEmpty(final List<T> list) {
        return !isNull(list) && !list.isEmpty();
    }

    public static <T> List<T> intersectLists(final List<T> list1, final List<T> list2) {
        final Set<T> intersection = new HashSet<>(list1);
        final Set<T> set2 = new HashSet<>(list2);
        intersection.retainAll(set2);
        return new ArrayList<>(intersection);
    }

    public static <T, U> List<T> intersectListsByMatchingFunction(final List<T> list1,
                                                                  final List<U> list2,
                                                                  final Function<T, U> func) {
        return getNullableList(list1).parallelStream()
                .filter(i -> checkIfElementIsPresentInList(list2, func.apply(i)))
                .toList();
    }

    public static <T> double sumOfDoubleMemberOverAllItems(final Collection<T> list,
                                                           final ToDoubleFunction<? super T> doubleGetMethod) {
        return list.stream().mapToDouble(doubleGetMethod).sum();
    }

    public static <T> ImmutableList<T> filterListByPredicate(final List<T> list, final Predicate<T> predicate) {
        return ImmutableList.copyOf(getNullableList(list).parallelStream()
                .filter(predicate)
                .toList());
    }

    public static <T> Optional<T> findFirstByPredicate(final List<T> list, final Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst();
    }

    public static <T> List<T> getNullableList(final List<T> list) {
        return Optional.ofNullable(list).orElseGet(List::of);
    }

    public static <T> List<T> sortList(final List<T> list, final Comparator<? super T> funcToSortBy) {
        return getNullableList(list).stream()
                .sorted(funcToSortBy)
                .toList();
    }

    public static <T> ImmutableList<T> filterItemsWhereStringMemberIsNotNullOrEmpty(final ImmutableList<T> list, final Function<T, String> stringGetter) {
        Predicate<T> stringMemberIsNotNullOrEmpty = i -> !StringUtils.isStringNullOrEmpty(stringGetter.apply(i));
        return filterListByPredicate(list, stringMemberIsNotNullOrEmpty);
    }

    public static <T> ImmutableList<T> filterItemsWhereStringMemberIsNullOrEmpty(final ImmutableList<T> list, final Function<T, String> stringGetter) {
        Predicate<T> stringMemberIsNullOrEmpty = i -> StringUtils.isStringNullOrEmpty(stringGetter.apply(i));
        return filterListByPredicate(list, stringMemberIsNullOrEmpty);
    }

    public static <T, U> ImmutableList<T> filterItemsWhereMemberIsNull(final ImmutableList<T> list, final Function<T, U> getter) {
        Predicate<T> memberIsNull = i -> getter.apply(i) == null;
        return filterListByPredicate(list, memberIsNull);
    }

    public static <T, U> ImmutableList<T> filterItemsWhereMemberIsNotNull(final ImmutableList<T> list, final Function<T, U> getter) {
        Predicate<T> memberIsNotNull = i -> getter.apply(i) != null;
        return filterListByPredicate(list, memberIsNotNull);
    }


    /**
     * Filters a list by a given Predicate and applies a given Function i.e. Class::function
     * to the filtered result, so that only attributes defined by the Function will be returned.
     * Use this function when you want to filter a list and only specific members from the result are relevant.
     */
    public static <T, U> List<U> mapAttributesWithPredicate(final ImmutableList<T> list,
                                                            final Predicate<T> predicate,
                                                            final Function<T, U> func) {
        return filterListByPredicate(list, predicate)
                .stream()
                .map(func)
                .toList();
    }

    /**
     * Gets all members by getter (Function<T, U> getter) from list of objects
     * and puts them into a new list.
     */
    public static <T, U> List<U> extractMembersAsList(final ImmutableList<T> list, final Function<T, U> getter) {
        return getNullableList(list).stream().map(getter).toList();
    }

    /**
     * Concatenate the same type of lists to one.
     */
    @SafeVarargs
    public static <T> List<T> concatenateLists(final List<T>... lists) {
        final List<T> result = new ArrayList<>();
        stream(lists).forEach(result::addAll);
        return result;
    }

    /**
     * Finds duplicate entries in a list.
     */
    public static <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> uniques = new HashSet<>();
        return collection.stream()
                .filter(e -> !uniques.add(e))
                .collect(Collectors.toSet());
    }

    /**
     * Get all distinct values from a list property
     */
    public static <T, U> Set<U> createDistinctSetFromListProperty(final List<T> list, final Function<T, U> getter) {
        return getNullableList(list).parallelStream().map(getter).collect(Collectors.toSet());
    }
}
