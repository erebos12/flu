package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Utility class providing helper methods for Map operations.
 * This class contains methods for manipulating and querying Map objects.
 */
public final class MapUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private MapUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Retrieves all keys from a map as a List.
     *
     * @param map the source map
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a List containing all keys from the map
     */
    public static <K, V> List<K> fetchKeysFromMap(final Map<K, V> map) {
        return new ArrayList<>(ifNotMapNullOrEmpty(map).keySet());
    }

    /**
     * Converts all values from a map into a List.
     *
     * @param map the source map
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a List containing all values from the map
     */
    public static <K, V> List<V> mapValuesToList(final Map<K, V> map) {
        return new ArrayList<>(map.values());
    }

    /**
     * Flattens a map containing Lists as values into a single List.
     *
     * @param map the source map containing Lists as values
     * @param <K> the type of keys in the map
     * @param <V> the type of elements in the Lists
     * @return a flattened List containing all elements from the value Lists
     */
    public static <K, V> List<V> flatMapToList(final Map<K, List<V>> map) {
        return getNullableMap(map)
                .values()
                .parallelStream().
                flatMap(Collection::stream)
                .collect(toList());
    }

    /**
     * Flattens a nested map structure containing Lists into a single List.
     *
     * @param map the nested map structure
     * @param <K> the type of outer map keys
     * @param <V> the type of inner map keys
     * @param <T> the type of elements in the Lists
     * @return a flattened List containing all elements from the nested Lists
     */
    public static <K, V, T> List<T> flatMapInMapToList(final Map<K, Map<V, List<T>>> map) {
        return getNullableMap(map)
                .values()
                .stream()
                .flatMap(item -> item.values().stream().flatMap(Collection::stream))
                .collect(toList());
    }

    /**
     * Checks if a map is null or empty.
     *
     * @param map the map to check
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return true if the map is null or empty, false otherwise
     */
    public static <K, V> boolean isMapNullOrEmpty(final Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Intersects two maps by the key.
     * Returns a List of keys which are in common in both maps.
     *
     * @param map1 the first map
     * @param map2 the second map
     * @param <K> the type of keys in the maps
     * @param <V> the type of values in the first map
     * @param <W> the type of values in the second map
     * @return a List of keys which are in common in both maps
     * @deprecated Use intersectMapsByKey() instead!
     */
    @Deprecated()
    public static <K, V, W> List<K> intersectMaps(final Map<K, V> map1, final Map<K, W> map2) {
        return getNullableMap(map1)
                .keySet()
                .stream()
                .filter(getNullableMap(map2).keySet()::contains)
                .collect(toList());
    }

    /**
     * Intersects two maps by the key.
     * Returns an immutable List of the keys which are in common in both maps.
     *
     * @param map1 the first map
     * @param map2 the second map
     * @param <K> the type of keys in the maps
     * @param <V> the type of values in the first map
     * @param <W> the type of values in the second map
     * @return an immutable List of the keys which are in common in both maps
     */
    public static <K, V, W> ImmutableList<K> intersectMapsByKey(final ImmutableMap<K, V> map1, final ImmutableMap<K, W> map2) {
        return getNullableMap(map1)
                .keySet()
                .stream()
                .filter(getNullableMap(map2).keySet()::contains)
                .collect(ImmutableList.toImmutableList());
    }

    /**
     * Intersects two maps by the key.
     * Returns a map containing the entries which are in common in both maps.
     *
     * @param map1 the first map
     * @param map2 the second map
     * @param <K> the type of keys in the maps
     * @param <V> the type of values in the first map
     * @param <W> the type of values in the second map
     * @return a map containing the entries which are in common in both maps
     */
    public static <K, V, W> Map<K, V> intersectMapsReturnMap(final Map<K, V> map1, final Map<K, W> map2) {
        return getNullableMap(map1).entrySet()
                .stream()
                .filter(item -> map2.containsKey(item.getKey()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    /**
     * Intersects a map with a list by the key.
     * Returns a List of values from the map which have keys in the list.
     *
     * @param map the map
     * @param list the list of keys
     * @param <K> the type of keys in the map and list
     * @param <V> the type of values in the map
     * @return a List of values from the map which have keys in the list
     */
    public static <K, V> List<V> intersectMapAndList(final ImmutableMap<K, List<V>> map, final ImmutableList<K> list) {
        return getNullableMap(map).entrySet()
                .parallelStream()
                .filter(item -> list.contains(item.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    /**
     * Intersects a map with a list by the key.
     * Returns a map containing the entries from the original map which have keys in the list.
     *
     * @param map the map
     * @param list the list of keys
     * @param <K> the type of keys in the map and list
     * @param <V> the type of values in the map
     * @return a map containing the entries from the original map which have keys in the list
     */
    public static <K, V> Map<K, List<V>> intersectMapAndListToMap(final Map<K, List<V>> map, final List<K> list) {
        getNullableMap(map).keySet().retainAll(list);
        return map;
    }

    /**
     * Converts a list into a map with default values.
     *
     * @param list the list to convert
     * @param <T> the type of elements in the list
     * @return a map with default values
     */
    public static <T> Map<T, Double> convertListToEmptyMap(final List<T> list) {
        return ListUtils.getNullableList(list).stream()
                .collect(toMap(key -> key, value -> 0.0));
    }

    /**
     * Converts a set into a map with default values.
     *
     * @param set the set to convert
     * @param <T> the type of elements in the set
     * @return a map with default values
     */
    public static <T> Map<T, Double> convertSetToZeroValueMap(final Set<T> set) {
        return set.stream()
                .collect(toMap(key -> key, value -> 0.0));
    }

    /**
     * Returns a map if it is not null or empty, otherwise returns an empty map.
     *
     * @param map the map to check
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return the map if it is not null or empty, otherwise an empty map
     */
    public static <K, V> Map<K, V> ifNotMapNullOrEmpty(final Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    /**
     * Rounds the values in a map to a specified scale.
     *
     * @param map the map to round
     * @param scale the scale to round to
     * @param <T> the type of keys in the map
     */
    public static <T> void roundMapValues(final Map<T, Double> map, final int scale) {
        getNullableMap(map).replaceAll((k, v) -> MathUtils.roundValue(v, scale));
    }

    /**
     * Calculates the sum of all values in a map.
     *
     * @param map the map to calculate the sum for
     * @param <K> the type of keys in the map
     * @return the sum of all values in the map
     */
    public static <K> double calculateSumOfAllMapValues(final Map<K, Double> map) {
        return ifNotMapNullOrEmpty(map)
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * Calculates the sum of all values in a nested map structure.
     *
     * @param map the nested map structure
     * @param <K> the type of outer map keys
     * @return the sum of all values in the nested map structure
     */
    public static <K> double calculateSumOfAllNestedMapValues(final Map<K, Map<K, Double>> map) {
        return ifNotMapNullOrEmpty(map).values()
                .stream()
                .mapToDouble(MapUtils::calculateSumOfAllMapValues)
                .sum();
    }

    /**
     * Retrieves a list of values from a map by a key.
     *
     * @param map the map to retrieve from
     * @param key the key to retrieve by
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a list of values from the map by the key
     */
    public static <K, V> List<V> getListByMapKey(final Map<K, List<V>> map, final K key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(List::of);
    }

    /**
     * Returns a map if it is not null, otherwise returns an empty map.
     *
     * @param map the map to check
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return the map if it is not null, otherwise an empty map
     */
    public static <K, V> Map<K, V> getNullableMap(final Map<K, V> map) {
        return Optional.ofNullable(map).orElse(new HashMap<>());
    }

    /**
     * Upserts a value in a map.
     *
     * @param map the map to upsert in
     * @param key the key to upsert by
     * @param updateValue the value to upsert
     * @param <T> the type of keys in the map
     */
    public static <T> void upsertMapDoubleValues(final Map<T, Double> map,
                                                 final T key,
                                                 final double updateValue) {
        getNullableMap(map).compute(key, (k, v) -> v == null ? updateValue : v + updateValue);
    }

    /**
     * Sorts a map by its values.
     *
     * @param map the map to sort
     * @param funcToSortBy the function to sort by
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a sorted map
     */
    public static <K, V> Map<K, V> sortMap(final Map<K, V> map, final Comparator<? super V> funcToSortBy) {
        return getNullableMap(map)
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(funcToSortBy))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Filters a map by a predicate.
     *
     * @param map the map to filter
     * @param predicate the predicate to filter by
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a filtered map
     */
    public static <K, V> Map<K, V> filterByPredicate(final Map<K, V> map, final Predicate<V> predicate) {
        return getNullableMap(map)
                .entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Converts a list into a map.
     *
     * @param list the list to convert
     * @param funcForKey the function to generate keys
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a map
     */
    public static <K, V> Map<K, V> convertListToMap(final List<V> list, final Function<V, K> funcForKey) {
        return ListUtils.getNullableList(list)
                .stream()
                .collect(toMap(funcForKey, Function.identity()));
    }

    /**
     * Removes a key from a map.
     *
     * @param map the map to remove from
     * @param key the key to remove
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     */
    public static <K, V> void removeKeyFromMap(final Map<K, V> map, K key) {
        getNullableMap(map).remove(key);
    }

    /**
     * Removes a key from a list of maps.
     *
     * @param list the list of maps to remove from
     * @param key the key to remove
     * @param <K> the type of keys in the maps
     * @param <V> the type of values in the maps
     */
    public static <K, V> void removeKeyFromListOfMaps(final List<Map<K, V>> list, K key) {
        ListUtils.getNullableList(list).forEach(entry -> removeKeyFromMap(entry, key));
    }

    /**
     * Removes entries with null or empty keys from a map.
     *
     * @param map the map to remove from
     * @param <V> the type of values in the map
     */
    public static <V> void removeIfKeyIsNullOrEmpty(final Map<String, V> map) {
        getNullableMap(map).entrySet().removeIf(entry -> StringUtils.isStringNullOrEmpty(entry.getKey()));
    }

    /**
     * Calculates the sum of values in a map using a double getter function.
     *
     * @param map the map to calculate the sum for
     * @param doubleGetter the double getter function
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return the sum of values in the map
     */
    public static <K, V> double calculateDoubleSumByMember(final ImmutableMap<K, V> map, final Function<V, Double> doubleGetter) {
        return getNullableMap(map).values().stream().mapToDouble(doubleGetter::apply).sum();
    }

    /**
     * Creates a map from a list using key and value functions.
     *
     * @param list the list to create the map from
     * @param funcForKey the function to generate keys
     * @param funcForValue the function to generate values
     * @param <T> the type of elements in the list
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return a map
     */
    public static <T, V, K> Map<K, V> createMapFromListByMembers(final List<T> list,
                                                                 final Function<T, K> funcForKey,
                                                                 final Function<T, V> funcForValue) {
        return ListUtils.getNullableList(list).parallelStream()
                .collect(Collectors.toMap(funcForKey, funcForValue));
    }

    /**
     * Retrieves a map from a nested map structure by a key.
     *
     * @param map the nested map structure
     * @param key the key to retrieve by
     * @param <T> the type of outer map keys
     * @param <K> the type of inner map keys
     * @param <V> the type of values in the inner map
     * @return a map from the nested map structure
     */
    public static <T, K, V> Map<K, V> getMapValueByMapKey(final Map<T, Map<K, V>> map, final T key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(HashMap::new);
    }

    /**
     * Retrieves a set from a map by a key.
     *
     * @param map the map to retrieve from
     * @param key the key to retrieve by
     * @param <K> the type of keys in the map
     * @param <V> the type of elements in the set
     * @return a set from the map
     */
    public static <K, V> Set<V> getSetValueByMapKey(final Map<K, Set<V>> map, final K key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(HashSet::new);
    }
}
