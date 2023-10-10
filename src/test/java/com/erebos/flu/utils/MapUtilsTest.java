package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.erebos.flu.utils.pojo.AccountTransaction;
import com.erebos.flu.utils.pojo.CostCenter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static com.erebos.flu.utils.MapUtils.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapUtilsTest {

    @Test
    void testRoundValues() {
        final var map = new HashMap<String, Double>();
        map.put("k1", 2.8912);
        map.put("k2", 1.0133);
        roundMapValues(map, 2);
        assertThat(map.get("k1"), is(2.89));
        assertThat(map.get("k2"), is(1.01));

        roundMapValues(map, 1);
        assertThat(map.get("k1"), is(2.9));
        assertThat(map.get("k2"), is(1.0));
    }

    @Test
    void testIsMapNullOrEmpty() {
        var map = Map.of("k1", 1.0);
        assertThat(isMapNullOrEmpty(map), is(false));
        map = new HashMap<>();
        assertThat(isMapNullOrEmpty(map), is(true));
        assertThat(isMapNullOrEmpty(null), is(true));
    }

    @Test
    void testIntersectMaps() {
        final var map1 = Map.of("KS1", 9.0, "KS2", 10.0);
        final var map2 = Map.of("KS2", List.of("hello", "you"), "KS77", List.of("How", "are", "you"));
        final var map3 = Map.of("KS1", "the value is irrelevant", "KS2", "another irrelevant value");
        final var emptyMap = new HashMap<String, Double>();

        var intersection = intersectMaps(map1, map2);
        assertThat(intersection.size(), is(1));
        assertThat(intersection.get(0), is("KS2"));

        intersection = intersectMaps(map1, map3);
        assertThat(intersection.size(), is(2));
        assertThat(intersection, containsInAnyOrder("KS1", "KS2"));

        intersection = intersectMaps(map1, emptyMap);
        assertThat(intersection.size(), is(0));
    }

    @Test
    void testIntersectMapsByKey() {
        final var map1 = ImmutableMap.copyOf(Map.of("KS1", 9.0, "KS2", 10.0));
        final var map2 = ImmutableMap.copyOf(Map.of("KS2", List.of("hello", "you"), "KS77", List.of("How", "are", "you")));
        final var map3 = ImmutableMap.copyOf(Map.of("KS1", "the value is irrelevant", "KS2", "another irrelevant value"));

        var intersection = intersectMapsByKey(map1, map2);
        assertThat(intersection.size(), is(1));
        assertThat(intersection.get(0), is("KS2"));

        intersection = intersectMapsByKey(map1, map3);
        assertThat(intersection.size(), is(2));
        assertThat(intersection, containsInAnyOrder("KS1", "KS2"));

        var empty_intersection = intersectMapsByKey(map1, ImmutableMap.copyOf(new HashMap<String, String>()));
        assertThat(empty_intersection.size(), is(0));

        intersectMapsByKey(null, null);
    }

    @Test
    void testIntersectMapAndList() {
        final var map = Map.of("KS2", List.of("1", "2"),
                "KS1", List.of("3", "4", "5"),
                "KS77", List.of("irrelevant"));

        assertThat(map.size(), is(3));
        final var list = List.of("KS1", "KS2", "KS1", "KS33");
        final List<String> intersection = intersectMapAndList(ImmutableMap.copyOf(map), ImmutableList.copyOf(list));
        assertThat(map.size(), is(3));
        assertThat(intersection.size(), is(5));
        assertThat(intersection, containsInAnyOrder("1", "2", "3", "4", "5"));
    }

    @Test
    void testIntersectMapAndListToMap() {
        final Map<String, List<String>> map = new HashMap<>();
        map.put("KS2", List.of("1", "2"));
        map.put("KS1", List.of("3", "4", "5"));
        map.put("KS77", List.of("irrelevant"));
        final var list = List.of("KS1", "KS2", "KS1", "KS33");
        final Map<String, List<String>> intersection = intersectMapAndListToMap(map, list);
        assertThat(intersection.size(), is(2));

        assertThat(intersection, hasEntry("KS2", List.of("1", "2")));
        assertThat(intersection, hasEntry("KS1", List.of("3", "4", "5")));
    }

    @Test
    void testMapValuesToList() {
        final var map = Map.of("k1", 2.0, "k2", 3.0);
        final List<Double> mapValuesAList = mapValuesToList(map);

        assertThat(mapValuesAList.size(), is(2));
        assertThat(mapValuesAList, containsInAnyOrder(2.0, 3.0));
    }

    @Test
    void flatMapToListTest() {
        final var costCenters = List.of(
                new CostCenter("CC1", 3, ""),
                new CostCenter("CC1", 12, ""),
                new CostCenter("CC3", 14, "")
        );
        final Map<String, List<CostCenter>> groupedMap = costCenters.stream().collect(groupingBy(CostCenter::shortName));
        assertThat(groupedMap, is(notNullValue()));

        final List<CostCenter> flatList = MapUtils.flatMapToList(groupedMap);
        assertThat(flatList, is(notNullValue()));

        assertThat(costCenters, is(flatList));
    }

    @Test
    void flatMapInMapToListTest() {
        final var costCenters = List.of(
                new CostCenter("CC1", 3, ""),
                new CostCenter("CC1", 3, ""),
                new CostCenter("CC3", 4, "")
        );
        final Function<CostCenter, String> compositeKey1 = CostCenter::shortName;
        final Function<CostCenter, Double> compositeKey2 = CostCenter::reimbursementNeeds;
        final Map<String, Map<Double, List<CostCenter>>> groupedMap =
                costCenters
                        .stream()
                        .collect(groupingBy(compositeKey1, groupingBy(compositeKey2)));
        assertThat(groupedMap, is(notNullValue()));

        final List<CostCenter> flatList = MapUtils.flatMapInMapToList(groupedMap);
        assertThat(flatList, is(notNullValue()));
        assertThat(costCenters, is(flatList));
    }

    @Test
    void fetchKeysFromMapTest() {
        Map<String, Integer> map = new HashMap<>();
        List<String> keys = fetchKeysFromMap(map);
        assertThat(keys.size(), is(0));

        keys = fetchKeysFromMap(null);
        assertThat(keys.size(), is(0));

        map = Map.of("k1", 1, "k2", 2);
        keys = fetchKeysFromMap(map);
        assertThat(keys.size(), is(2));
        assertThat(keys, containsInAnyOrder("k1", "k2"));
    }

    @Test
    void calculateSumOfAllMapValuesTest() {
        double sum = calculateSumOfAllMapValues(null);
        assertThat(sum, is(0.0));

        final Map<String, Double> map = Map.of("K1", 10.0, "k2", 1.0, "k 1", 20.0);
        sum = calculateSumOfAllMapValues(map);
        assertThat(sum, is(31.0));
    }

    @Test
    void intersectMapsReturnMapTest() {
        final var cc1 = new CostCenter("KS 1", 1, "");
        final var cc2 = new CostCenter("KS 2", 2, "");
        final var cc3 = new CostCenter("KS3", 3, "");

        var map1 = Map.of("KS 1", cc1, "KS 2", cc2, "KS 3", cc3);
        var map2 = Map.of("KS 1", 0.0, "KS 2", 0.0, "KS 3", 0.0);
        var map = intersectMapsReturnMap(map1, map2);
        assertThat(map.size(), is(3));
        assertThat(map.get("KS 1"), is(instanceOf(CostCenter.class)));
        assertThat(map.get("KS 1").shortName(), is("KS 1"));

        map1 = Map.of("KS 2", cc2);
        map2 = Map.of("KS 1", 0.0, "KS 2", 0.0, "KS 3", 0.0);
        map = intersectMapsReturnMap(map1, map2);
        assertThat(map.size(), is(1));
        assertThat(map.get("KS 2"), is(instanceOf(CostCenter.class)));
        assertThat(map.get("KS 2").shortName(), is("KS 2"));

        map1 = Map.of("KS 1", cc1, "KS 2", cc2);
        map2 = Map.of("KS 3", 0.0, "KS 4", 0.0, "KS 5", 0.0);
        map = intersectMapsReturnMap(map1, map2);
        assertThat(map.size(), is(0));
    }

    @Test
    void testConvertListToEmptyMap() {
        var mapWithZeroValues = convertListToEmptyMap(List.of("KS1", "KS2"));
        assertThat(mapWithZeroValues.size(), is(2));
        assertThat(mapWithZeroValues.get("KS1"), is(0.0));
        assertThat(mapWithZeroValues.get("KS2"), is(0.0));

        mapWithZeroValues = convertListToEmptyMap(List.of());
        assertThat(mapWithZeroValues.size(), is(0));
    }

    @Test
    void TestGetListByMapKey() {
        final var costCenters = List.of(
                new CostCenter("KS 1", 100, ""),
                new CostCenter("KS 1", 150, ""),
                new CostCenter("KS 2", 200, "")
        );
        final var map = costCenters
                .stream()
                .collect(groupingBy(CostCenter::shortName));
        var list = MapUtils.getListByMapKey(map, "KS 1");
        assertThat(list.size(), is(2));
        assertThat(list.get(0).reimbursementNeeds(), is(100.00));
        assertThat(list.get(1).reimbursementNeeds(), is(150.00));

        list = MapUtils.getListByMapKey(map, "9999");
        assertThat(list.size(), is(0));

        list = MapUtils.getListByMapKey(map, "");
        assertThat(list.size(), is(0));

        list = MapUtils.getListByMapKey(map, null);
        assertThat(list.size(), is(0));

        list = MapUtils.getListByMapKey(null, null);
        assertThat(list.size(), is(0));
    }

    @Test
    void testGetNullableMap() {
        var emptyMap = getNullableMap(null);
        // empty returned map is modifiable
        assertThat(emptyMap, is(Map.of()));
        emptyMap.put("String1", List.of());
        assertThat(emptyMap.size(), is(1));
        final Map<String, List<String>> map = Map.of("1", List.of("1", "3"), "2", List.of("2"));
        assertThat(getNullableMap(map), is(map));
    }

    @Test
    void testUpdateMapDoubleValues() {
        final var map = new HashMap<String, Double>();
        map.put("1", 1000.0);
        map.put("2", 2000.0);
        map.put("3", null);
        map.put("4", 0.0);
        final String key1 = "1";
        upsertMapDoubleValues(map, key1, 100.0);
        assertThat(map.get(key1), is(1100.0));
        final String key2 = "2";
        upsertMapDoubleValues(map, key2, 200.0);
        assertThat(map.get(key2), is(2200.0));
        final String key3 = "3";
        upsertMapDoubleValues(map, key3, 33.0);
        assertThat(map.get(key3), is(33.0));
        final String key4 = "4";
        upsertMapDoubleValues(map, key4, 77.0);
        assertThat(map.get(key4), is(77.0));
    }

    @Test
    void testSortMap() {
        final var at1 = new AccountTransaction("4", "at 4");
        final var at2 = new AccountTransaction("2", "at 2");
        final var at3 = new AccountTransaction("3", "at 3");
        final var at4 = new AccountTransaction("1", "at 1");
        final var map = new HashMap<String, AccountTransaction>();
        map.put(at1.id(), at1);
        map.put(at2.id(), at2);
        map.put(at3.id(), at3);
        map.put(at4.id(), at4);

        final var sortedMap = sortMap(map, comparing(AccountTransaction::id));

        assertThat((sortedMap.keySet().toArray())[0], is("1"));
        assertThat(sortedMap.get(sortedMap.keySet().toArray()[0]).name(), is("at 1"));
        assertThat((sortedMap.keySet().toArray())[1], is("2"));
        assertThat(sortedMap.get(sortedMap.keySet().toArray()[1]).name(), is("at 2"));
        assertThat((sortedMap.keySet().toArray())[2], is("3"));
        assertThat(sortedMap.get(sortedMap.keySet().toArray()[2]).name(), is("at 3"));
        assertThat((sortedMap.keySet().toArray())[3], is("4"));
        assertThat(sortedMap.get(sortedMap.keySet().toArray()[3]).name(), is("at 4"));
    }

    @Test
    void testFilterByValue() {
        final var at1 = new AccountTransaction("4", "at 4");
        final var at2 = new AccountTransaction("2", "at 2");
        final var at3 = new AccountTransaction("3", "at 3");
        final var at4 = new AccountTransaction("1", "at 1");
        final var map = new HashMap<String, AccountTransaction>();
        map.put(at1.id(), at1);
        map.put(at2.id(), at2);
        map.put(at3.id(), at3);
        map.put(at4.id(), at4);
        final var filteredMap =
                filterByPredicate(map, at -> at.name().equals("at 3") || at.name().equals("at 1"));

        assertThat(filteredMap.size(), is(2));
        assertThat(filteredMap.get("1"), is(at4));
        assertThat(filteredMap.get("2"), is(nullValue()));
        assertThat(filteredMap.get("3"), is(at3));
        assertThat(filteredMap.get("4"), is(nullValue()));
    }

    @Test
    void testConvertListToMap() {
        final var cc1 = new CostCenter("KS 1", 100, "");
        final var cc2 = new CostCenter("KS 3", 150, "");
        final var cc3 = new CostCenter("KS 2", 200, "");
        final var map = convertListToMap(List.of(cc1, cc2, cc3), CostCenter::shortName);
        assertThat(map.size(), is(3));

        assertThat(map.get("KS 1"), instanceOf(CostCenter.class));
        assertThat(map.get("KS 1").shortName(), is("KS 1"));
        assertThat(map.get("KS 1").reimbursementNeeds(), is(100.00));

        assertThat(map.get("KS 3"), instanceOf(CostCenter.class));
        assertThat(map.get("KS 3").shortName(), is("KS 3"));
        assertThat(map.get("KS 3").reimbursementNeeds(), is(150.00));

        assertThat(map.get("KS 2"), instanceOf(CostCenter.class));
        assertThat(map.get("KS 2").shortName(), is("KS 2"));
        assertThat(map.get("KS 2").reimbursementNeeds(), is(200.00));
    }

    @Test
    void testConvertListToMap_with_duplicate_key() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            var cc1 = new CostCenter("KS 1", 100, "");
            var cc2 = new CostCenter("KS 1", 100, "");
            convertListToMap(List.of(cc1, cc2), CostCenter::shortName);
        });
    }

    @Test
    void testCreateMapFromListByMembers() {
        var list = new ArrayList<CostCenter>();
        list.add(new CostCenter("CC01", 1200.00, "longName01"));
        list.add(new CostCenter("CC02", 42.00, "longName02"));
        list.add(new CostCenter("CC03", 999.00, "longName03"));

        Map<String, Double> map = createMapFromListByMembers(list, CostCenter::shortName, CostCenter::reimbursementNeeds);
        assertEquals(map.size(), 3);
        assertThat(map.get("CC01"), is(1200.00));
        assertThat(map.get("CC02"), is(42.00));
        assertThat(map.get("CC03"), is(999.00));

        Map<String, String> map2 = createMapFromListByMembers(list, CostCenter::shortName, CostCenter::longName);
        assertEquals(map2.size(), 3);
        assertThat(map2.get("CC01"), is("longName01"));
        assertThat(map2.get("CC02"), is("longName02"));
        assertThat(map2.get("CC03"), is("longName03"));

        // add duplicate
        list.add(new CostCenter("CC01", 1200.00, "longName01"));
        IllegalStateException thrown = assertThrows(IllegalStateException.class, ()
                -> createMapFromListByMembers(list, CostCenter::shortName, CostCenter::longName));
        assertThat(thrown.getMessage(), containsString("Duplicate key CC01 (attempted merging values longName01 and longName01)"));
    }

    @Test
    void testCalculateSumOfAllNestedMapValues() {
        double sum = calculateSumOfAllNestedMapValues(Map.of("key01", Map.of("1", 90.0, "2", 90.0)));
        assertEquals(180.0, sum);
        sum = calculateSumOfAllNestedMapValues(null);
        assertEquals(0, sum);
    }

    @Test
    void testRemoveKeyFromMap() {
        // given
        final var map = new HashMap<String, String>();
        map.put("k1", "");
        map.put("k2", "");
        // when
        removeKeyFromMap(map, "k1");
        // then
        assertThat(map, aMapWithSize(1));
        assertThat(map, hasKey("k2"));
    }

    @Test
    void testRemoveKeyFromEmptyMap() {
        // given
        final var map = new HashMap<String, String>();
        // when
        removeKeyFromMap(map, "k1");
        // then
        assertThat(map, aMapWithSize(0));
    }

    @Test
    void testRemoveKeyFromListOfMaps() {
        // given
        final var map = new HashMap<String, String>();
        map.put("k1", "");
        map.put("k2", "");
        final var map1 = new HashMap<String, String>();
        map1.put("k1", "");
        map1.put("k2", "");
        final List<Map<String, String>> listOfMaps = List.of(map, map1);
        // when
        removeKeyFromListOfMaps(listOfMaps, "k2");
        // then
        assertThat(listOfMaps, hasSize(2));
        assertThat(listOfMaps.get(0), aMapWithSize(1));
        assertThat(listOfMaps.get(0), hasKey("k1"));
        assertThat(listOfMaps.get(1), aMapWithSize(1));
        assertThat(listOfMaps.get(1), hasKey("k1"));
        // map is null test
        removeKeyFromListOfMaps(null, "k2");
    }

    @Test
    void testRemoveKeyFromListOfMapsEmptyMap() {
        // given
        final var map = new HashMap<String, String>();
        final var map1 = new HashMap<String, String>();
        final List<Map<String, String>> listOfMaps = List.of(map, map1);
        // when
        removeKeyFromListOfMaps(listOfMaps, "k2");
        // then
        assertThat(listOfMaps, hasSize(2));
        assertThat(listOfMaps.get(0), aMapWithSize(0));
        assertThat(listOfMaps.get(1), aMapWithSize(0));
    }

    @Test
    void testRemoveIfKeyIsEmptyOrNull() {
        removeIfKeyIsNullOrEmpty(null);
        // key with empty string
        Map<String, String> map = new HashMap<>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("", "three");
        removeIfKeyIsNullOrEmpty(map);
        assertThat(map.size(), is(2));
        assertThat(map.get("1"), is("one"));
        assertThat(map.get("2"), is("two"));
        // key with null
        map.clear();
        map.put("1", "one");
        map.put(null, "three");
        removeIfKeyIsNullOrEmpty(map);
        assertThat(map.size(), is(1));
        assertThat(map.get("1"), is("one"));
    }

    @Test
    void testCalculateDoubleSumByMember() {
        var cc1 = new CostCenter("KS 1", 100.0, "");
        var cc2 = new CostCenter("KS 2", 200.0, "");
        var imMap = ImmutableMap.copyOf(Map.of("KS1", cc1, "KS2", cc2));
        double sum = calculateDoubleSumByMember(imMap, CostCenter::reimbursementNeeds);
        assertThat(sum, is(300.0));
        sum = calculateDoubleSumByMember(null, CostCenter::reimbursementNeeds);
        assertThat(sum, is(0.0));
    }

    @Test
    void testGetMapByMapKey() {
        final Map<String, Map<String, String>> map = Map.of(
                "CC-1", Map.of("BAB-CC-1", "BAB-CC-1", "BAB-CC-100", "BAB-CC-100"),
                "CC-2", Map.of("BAB-CC-2", "BAB-CC-2", "BAB-CC-200", "BAB-CC-200")
        );

        assertThat(map.size(), is(2));

        var innerMap = MapUtils.getMapValueByMapKey(map, "9999");
        assertThat(innerMap.size(), is(0));

        innerMap = MapUtils.getMapValueByMapKey(map, "CC-1");
        assertThat(innerMap.size(), is(2));

        innerMap = MapUtils.getMapValueByMapKey(map, "");
        assertThat(innerMap.size(), is(0));

        innerMap = MapUtils.getMapValueByMapKey(null, null);
        assertThat(innerMap.size(), is(0));
    }

    @Test
    void testGetSetByMapKey() {
        final Map<String, Set<String>> map = Map.of(
                "CC-1", Set.of("BAB-CC-1", "BAB-CC-100"),
                "CC-2", Set.of("BAB-CC-2", "BAB-CC-200")
        );
        assertThat(map.size(), is(2));

        var set = MapUtils.getSetValueByMapKey(map, "9999");
        assertThat(set.size(), is(0));

        set = MapUtils.getSetValueByMapKey(map, "");
        assertThat(set.size(), is(0));

        set = MapUtils.getSetValueByMapKey(null, null);
        assertThat(set.size(), is(0));
    }
}
