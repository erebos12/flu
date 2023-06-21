package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class MapUtils {

    private MapUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <K, V> List<K> fetchKeysFromMap(final Map<K, V> map) {
        return new ArrayList<>(ifNotMapNullOrEmpty(map).keySet());
    }

    public static <K, V> List<V> mapValuesToList(final Map<K, V> map) {
        return new ArrayList<>(map.values());
    }

    public static <K, V> List<V> flatMapToList(final Map<K, List<V>> map) {
        return getNullableMap(map)
                .values()
                .parallelStream().
                flatMap(Collection::stream)
                .collect(toList());
    }

    public static <K, V, T> List<T> flatMapInMapToList(final Map<K, Map<V, List<T>>> map) {
        return getNullableMap(map)
                .values()
                .stream()
                .flatMap(item -> item.values().stream().flatMap(Collection::stream))
                .collect(toList());
    }

    public static <K, V> boolean isMapNullOrEmpty(final Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    @Deprecated()
    /*
     * Do not use this function. Use intersectMapsByKey() instead!
     */
    public static <K, V, W> List<K> intersectMaps(final Map<K, V> map1, final Map<K, W> map2) {
        return getNullableMap(map1)
                .keySet()
                .stream()
                .filter(getNullableMap(map2).keySet()::contains)
                .collect(toList());
    }

    /**
     * Intersects two maps by the key.
     * Returns an immutable list of the keys which are in common in both maps.
     */
    public static <K, V, W> ImmutableList<K> intersectMapsByKey(final ImmutableMap<K, V> map1, final ImmutableMap<K, W> map2) {
        return getNullableMap(map1)
                .keySet()
                .stream()
                .filter(getNullableMap(map2).keySet()::contains)
                .collect(ImmutableList.toImmutableList());
    }

    public static <K, V, W> Map<K, V> intersectMapsReturnMap(final Map<K, V> map1, final Map<K, W> map2) {
        return getNullableMap(map1).entrySet()
                .stream()
                .filter(item -> map2.containsKey(item.getKey()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public static <K, V> List<V> intersectMapAndList(final ImmutableMap<K, List<V>> map,
                                                     final ImmutableList<K> list) {
        return getNullableMap(map).entrySet()
                .parallelStream()
                .filter(item -> list.contains(item.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public static <K, V> Map<K, List<V>> intersectMapAndListToMap(final Map<K, List<V>> map, final List<K> list) {
        getNullableMap(map).keySet().retainAll(list);
        return map;
    }

    public static <T> Map<T, Double> convertListToEmptyMap(final List<T> list) {
        return ListUtils.getNullableList(list).stream()
                .collect(toMap(key -> key, value -> 0.0));
    }

    public static <T> Map<T, Double> convertSetToZeroValueMap(final Set<T> set) {
        return set.stream()
                .collect(toMap(key -> key, value -> 0.0));
    }

    public static <K, V> Map<K, V> ifNotMapNullOrEmpty(final Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    public static <T> void roundMapValues(final Map<T, Double> map, final int scale) {
        getNullableMap(map).replaceAll((k, v) -> MathUtils.roundValue(v, scale));
    }

    public static <K> double calculateSumOfAllMapValues(final Map<K, Double> map) {
        return ifNotMapNullOrEmpty(map)
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static <K> double calculateSumOfAllNestedMapValues(final Map<K, Map<K, Double>> map) {
        return ifNotMapNullOrEmpty(map).values()
                .stream()
                .mapToDouble(MapUtils::calculateSumOfAllMapValues)
                .sum();
    }

    public static <K, V> List<V> getListByMapKey(final Map<K, List<V>> map, final K key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(List::of);
    }

    public static <K, V> Map<K, V> getNullableMap(final Map<K, V> map) {
        return Optional.ofNullable(map).orElse(new HashMap<>());
    }

    public static <T> void upsertMapDoubleValues(final Map<T, Double> map,
                                                 final T key,
                                                 final double updateValue) {
        getNullableMap(map).compute(key, (k, v) -> v == null ? updateValue : v + updateValue);
    }

    public static <K, V> Map<K, V> sortMap(final Map<K, V> map, final Comparator<? super V> funcToSortBy) {
        return getNullableMap(map).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(funcToSortBy))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <K, V> Map<K, V> filterByPredicate(final Map<K, V> map, final Predicate<V> predicate) {
        return getNullableMap(map).entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> convertListToMap(final List<V> list, final Function<V, K> funcForKey) {
        return ListUtils.getNullableList(list).stream()
                .collect(toMap(funcForKey, Function.identity()));
    }

    public static <K, V> void removeKeyFromMap(final Map<K, V> map, K key) {
        getNullableMap(map).remove(key);
    }

    public static <K, V> void removeKeyFromListOfMaps(final List<Map<K, V>> list, K key) {
        ListUtils.getNullableList(list).forEach(entry -> removeKeyFromMap(entry, key));
    }

    public static <V> void removeIfKeyIsNullOrEmpty(final Map<String, V> map) {
        getNullableMap(map).entrySet().removeIf(entry -> StringUtils.isStringNullOrEmpty(entry.getKey()));
    }

    public static <K, V> double calculateDoubleSumByMember(final ImmutableMap<K, V> map, final Function<V, Double> doubleGetter) {
        return getNullableMap(map).values().stream().mapToDouble(doubleGetter::apply).sum();
    }

    public static <T, V, K> Map<K, V> createMapFromListByMembers(final List<T> list,
                                                                 final Function<T, K> funcForKey,
                                                                 final Function<T, V> funcForValue) {
        return ListUtils.getNullableList(list).parallelStream()
                .collect(Collectors.toMap(funcForKey, funcForValue));
    }

    public static <T, V, K> Map<K, V> getMapValueByMapKey(final Map<T, Map<K, V>> map, final T key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(HashMap::new);
    }

    public static <K, V> Set<V> getSetValueByMapKey(final Map<K, Set<V>> map, final K key) {
        return Optional.ofNullable(getNullableMap(map).get(key)).orElseGet(HashSet::new);
    }
}

