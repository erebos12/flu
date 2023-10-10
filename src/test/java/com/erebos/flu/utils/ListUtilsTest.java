package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.Person;
import com.google.common.collect.ImmutableList;
import com.erebos.flu.utils.pojo.AccountBase;
import com.erebos.flu.utils.pojo.CostCenter;
import com.erebos.flu.utils.pojo.RevenueAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.erebos.flu.utils.ListUtils.*;
import static com.erebos.flu.utils.StringUtils.isStringNullOrEmpty;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ListUtilsTest {

    private static final List<Person> persons =
            List.of(new Person("Small", "Smally", 25, 166, 60),
                    new Person("Big", "Biggy", 35, 192, 94),
                    new Person("VeryBig", "VeryBiggy", 32, 201, 110));

    private static Stream<Arguments> provideElementIsPresentInListTest() {
        return Stream.of(
                Arguments.of(List.of("picard"), null, false),
                Arguments.of(emptyList(), null, false),
                Arguments.of(null, "riker", false),
                Arguments.of(List.of("picard", "riker"), "RIKER", false),
                Arguments.of(List.of("picard", "riker"), "data", false),
                Arguments.of(Arrays.asList("picard", "riker", null), "picard", true),
                Arguments.of(Arrays.asList("picard", "riker", null), "", false),
                Arguments.of(List.of(1, 1000), 2, false),
                Arguments.of(List.of(1, 1000), 1, true),
                Arguments.of(List.of(1, 1000), null, false),
                Arguments.of(List.of(1, 1000), "dummy", false),
                Arguments.of(List.of(1.0, 3.14), null, false),
                Arguments.of(List.of(1L, 3L), 1L, true),
                Arguments.of(List.of(1L, 3L), 1, false)
        );
    }

    private static Stream<Arguments> provideIsListNullOrEmptyList() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(emptyList(), true),
                Arguments.of(List.of("something"), false)
        );
    }


    @ParameterizedTest
    @MethodSource("provideElementIsPresentInListTest")
    void testCheckIfElementIsPresentInList(final List<Object> listOfElements,
                                           final Object searchingElement,
                                           final boolean isFoundInList) {
        final boolean result = checkIfElementIsPresentInList(listOfElements, searchingElement);
        assertThat(result, is(isFoundInList));
    }

    @ParameterizedTest
    @MethodSource("provideIsListNullOrEmptyList")
    void testIsListNullOrEmpty(final List<String> listToCheck, final boolean isListNullOrEmptyCorrectResult) {
        final boolean result = isListNullOrEmpty(listToCheck);
        assertThat(result, is(isListNullOrEmptyCorrectResult));
    }

    @Test
    void testIsListNullOrEmpty() {
        final List<String> l1 = null;
        final List<String> l2 = List.of();
        final List<String> l3 = List.of("");
        assertThat(isListNullOrEmpty(l1), is(true));
        assertThat(isListNullOrEmpty(l2), is(true));
        assertThat(isListNullOrEmpty(l3), is(false));
    }

    @Test
    void testIsListNotNullAndNotEmpty() {
        final List<String> l1 = null;
        final List<String> l2 = List.of();
        final List<String> l3 = List.of("");
        assertThat(isListNotNullAndNotEmpty(l1), is(false));
        assertThat(isListNotNullAndNotEmpty(l2), is(false));
        assertThat(isListNotNullAndNotEmpty(l3), is(true));
    }

    @Test
    void testListIntersection() {
        final var l1 = List.of(1, 2, 3, 4);
        final var l2 = List.of(3, 4, 5, 6);
        final var l3 = List.of(13, 40);
        List<Integer> intersection = intersectLists(l1, l2);

        assertThat(intersection.size(), is(2));
        assertThat(intersection, containsInAnyOrder(3, 4));
        // empty intersection
        intersection = intersectLists(l1, l3);
        assertThat(intersection.size(), is(0));

        // use string lists
        final var l4 = List.of("A", "B", "C");
        final var l5 = List.of("A", "D");
        final var l6 = List.of("E", "F");
        List<String> intersectionStr = intersectLists(l4, l5);
        assertThat(intersectionStr.size(), is(1));
        // empty intersection
        intersectionStr = intersectLists(l6, l5);
        assertThat(intersectionStr.size(), is(0));
    }

    @Test
    void testListIntersectionByMatchingFunction() {
        // use CostCenter with String matching function
        final CostCenter cc1 = new CostCenter("CC1", 0, "CC1");
        final CostCenter cc2 = new CostCenter("CC2", 0, "CC1");
        final CostCenter cc3 = new CostCenter("CC3", 0, "CC1");
        final CostCenter cc4 = new CostCenter("CC4", 0, "CC1");
        final var l1 = List.of(cc1, cc2, cc3, cc4);
        final var l2 = List.of("CC1", "CC3");
        final var l3 = List.of("CC5", "CC6");

        List<CostCenter> intersection = intersectListsByMatchingFunction(l1, l2, CostCenter::shortName);

        assertThat(intersection.size(), is(2));
        assertThat(intersection, containsInAnyOrder(cc1, cc3));
        // empty intersection
        intersection = intersectListsByMatchingFunction(l1, l3, CostCenter::shortName);
        assertThat(intersection.size(), is(0));

        // use AccountsBase with Long matching function
        final AccountBase ab1 = new AccountBase(10L);
        final AccountBase ab2 = new AccountBase(20L);
        final AccountBase ab3 = new AccountBase(30L);
        final AccountBase ab4 = new AccountBase(40L);

        final var l4 = List.of(ab1, ab2, ab3, ab4);
        final var l5 = List.of(20L, 40L);
        final var l6 = List.of(50L, 60L);
        var intersectionAccounts = intersectListsByMatchingFunction(l4, l5, AccountBase::id);
        assertThat(intersectionAccounts.size(), is(2));
        assertThat(intersectionAccounts, containsInAnyOrder(ab2, ab4));
        // empty intersection
        intersectionAccounts = intersectListsByMatchingFunction(l4, l6, AccountBase::id);
        assertThat(intersectionAccounts.size(), is(0));
    }

    @Test
    void testSumOfDoubleMemberOverAllItems() {
        final var listOfCCs = List.of(
                new CostCenter("CC1", 100.00, "CC1"),
                new CostCenter("CC2", 200.00, "CC1"),
                new CostCenter("CC3", 300.00, "CC1")
        );
        final var sum = sumOfDoubleMemberOverAllItems(listOfCCs, CostCenter::reimbursementNeeds);
        assertThat(sum, is(600.00));
    }

    @Test
    void testFilterListByPredicate() {
        List<CostCenter> ccs = List.of(
                new CostCenter("KS1", 0, "Long name"),
                new CostCenter("KS2", 0, null)
        );
        final Predicate<CostCenter> shortNameIsNull = cs -> isStringNullOrEmpty(cs.longName());
        var result = filterListByPredicate(ccs, shortNameIsNull);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).shortName(), is("KS2"));

        ccs = List.of(
                new CostCenter("KS1", 0, ""),
                new CostCenter("KS2", 0, null)
        );
        final Predicate<CostCenter> shortNameIsKS1 = cs -> cs.shortName().equalsIgnoreCase("KS1");
        final Predicate<CostCenter> logNameIsEmpty = cs -> isStringNullOrEmpty(cs.longName());
        result = filterListByPredicate(ccs, shortNameIsKS1.and(logNameIsEmpty));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).shortName(), is("KS1"));
        assertThat(result.get(0).longName(), is(""));
    }

    @Test
    void testFindFirstByPredicate() {
        final List<CostCenter> ccs = List.of(
                new CostCenter("KS1", 0, ""),
                new CostCenter("KS2", 0, "")
        );
        final Predicate<CostCenter> longNameIsNull = cs -> isStringNullOrEmpty(cs.longName());
        final Optional<CostCenter> result = findFirstByPredicate(ccs, longNameIsNull);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().shortName(), is("KS1"));
    }

    @Test
    void testGetNullableList() {
        assertThat(getNullableList(null), is(List.of()));
        final var list = List.of("1", "2");
        assertThat(getNullableList(list), is(list));
    }

    @Test
    void testSortList() {
        final var accounts = List.of(
                new RevenueAccount(100),
                new RevenueAccount(1000),
                new RevenueAccount(900),
                new RevenueAccount(300),
                new RevenueAccount(500)
        );
        var result = sortList(accounts, Comparator.comparingLong(RevenueAccount::id));
        assertThat(result.size(), is(5));
        assertThat(result.get(0).id(), is(100L));
        assertThat(result.get(1).id(), is(300L));
        assertThat(result.get(2).id(), is(500L));
        assertThat(result.get(3).id(), is(900L));
        assertThat(result.get(4).id(), is(1000L));

        result = sortList(List.of(), Comparator.comparingLong(RevenueAccount::id));
        assertThat(result.size(), is(0));

        result = sortList(null, Comparator.comparingLong(RevenueAccount::id));
        assertThat(result.size(), is(0));
    }

    @Test
    void testImmutabilityForFinalList() {
        final List<String> finalList = new ArrayList<>();
        // Compiler error when assigning a new list : list = new ArrayList<>();
        // BUT mutating the list still works
        finalList.add("star wars");
        assertThat(finalList.size(), is(1));
        finalList.set(0, "start trek");// overwriting possible
        assertThat(finalList.get(0), is("start trek"));
        finalList.remove(0);
        assertThat(finalList.size(), is(0));
    }

    @Test
    void testImmutabilityForImmutableList() {
        final List<String> list = new ArrayList<>();
        list.add("string");
        ImmutableList<String> immutableList = ImmutableList.copyOf(list);
        assertThat(immutableList.size(), is(1));
        // immutableList cannot be modified via add or remove
        // BUT overwriting possible
        immutableList = ImmutableList.copyOf(List.of("1", "2")); // overwriting possible
        assertThat(immutableList.size(), is(2));
        final ImmutableList<String> finalImmutableList = ImmutableList.copyOf(list);
        // Compiler error: finalImmutableList = ImmutableList.copyOf(List.of("1", "2")); // overwriting NOT possible
    }

    @Test
    void testFilterItemsWhereStringMemberIsNotNullOrEmpty() {
        List<CostCenter> list =
                List.of(new CostCenter(null, 0.0, null),
                        new CostCenter("", 0.0, null),
                        new CostCenter("A", 0.0, null));
        var result = filterItemsWhereStringMemberIsNotNullOrEmpty(ImmutableList.copyOf(list), CostCenter::shortName);
        assertSame(1, result.size());
        assertSame("A", result.get(0).shortName());
    }

    @Test
    void testFilterItemsWhereStringMemberIsNullOrEmpty() {
        List<CostCenter> list =
                List.of(new CostCenter(null, 0.0, null),
                        new CostCenter("", 0.0, null),
                        new CostCenter("A", 0.0, null));
        var result = filterItemsWhereStringMemberIsNullOrEmpty(ImmutableList.copyOf(list), CostCenter::shortName);
        assertSame(2, result.size());
        assertSame(null, result.get(0).shortName());
        assertSame("", result.get(1).shortName());
    }

    @Test
    void testFilterItemsWhereMemberIsNull() {
        List<CostCenter> list =
                List.of(new CostCenter(null, 0.0, null),
                        new CostCenter("", 0.0, null),
                        new CostCenter("A", 0.0, "longname"));
        var result = filterItemsWhereMemberIsNull(ImmutableList.copyOf(list), CostCenter::shortName);
        assertSame(1, result.size());
        assertSame(null, result.get(0).shortName());

        result = filterItemsWhereMemberIsNull(ImmutableList.copyOf(list), CostCenter::longName);
        assertSame(2, result.size());
        assertSame(null, result.get(0).shortName());
        assertSame("", result.get(1).shortName());
    }

    @Test
    void testFilterItemsWhereMemberIsNotNull() {
        List<CostCenter> list =
                List.of(new CostCenter(null, 0.0, null),
                        new CostCenter("", 0.0, null),
                        new CostCenter("A", 0.0, "longname"));
        List<CostCenter> result = filterItemsWhereMemberIsNotNull(ImmutableList.copyOf(list), CostCenter::shortName);
        assertSame(2, result.size());
        assertSame("", result.get(0).shortName());
        assertSame("A", result.get(1).shortName());

        result = filterItemsWhereMemberIsNotNull(ImmutableList.copyOf(list), CostCenter::longName);
        assertSame(1, result.size());
        assertSame("A", result.get(0).shortName());
    }

    @Test
    void testMapAttributesByFunctionWithPredicateFilter() {
        // gimme all cc-shortnames that have reimbursementNeeds > 100.0
        List<CostCenter> costCenters =
                List.of(new CostCenter("cc1", 100.0, "cc1-long-name"),
                        new CostCenter("cc2", 200.0, "cc2-long-name"),
                        new CostCenter("cc3", 233.99, "cc3-long-name"));
        Predicate<CostCenter> greaterThan100 = cc -> cc.reimbursementNeeds() > 100.0;
        List<String> onlyShortNamesWithGreaterThan100 =
                mapAttributesWithPredicate(ImmutableList.copyOf(costCenters), greaterThan100, CostCenter::shortName);
        assertSame(2, onlyShortNamesWithGreaterThan100.size());
        assertSame("cc2", onlyShortNamesWithGreaterThan100.get(0));
        assertSame("cc3", onlyShortNamesWithGreaterThan100.get(1));
    }

    @Test
    void testMapAttributesWithPredicate_big_guys() {
        // gimme all lastnames from persons who are greater than 170cm and heavier than 80kg
        // Then
        Predicate<Person> isTall = p -> p.height() > 190;
        Predicate<Person> isHeavy = p -> p.weight() > 90;
        List<String> bigGuys =
                mapAttributesWithPredicate(ImmutableList.copyOf(persons), isTall.and(isHeavy), Person::lastname);
        // Expect
        assertSame(2, bigGuys.size());
        assertSame("Biggy", bigGuys.get(0));
        assertSame("VeryBiggy", bigGuys.get(1));
    }

    @Test
    void testMapAttributesWithPredicate_young_guys() {
        // gimme all lastnames from persons who are young which is younger than 30 years old
        // Then
        Predicate<Person> isYoung = p -> p.age() < 30;
        List<String> youngGuys =
                mapAttributesWithPredicate(ImmutableList.copyOf(persons), isYoung, Person::lastname);
        // Expect
        assertSame(1, youngGuys.size());
        assertSame("Smally", youngGuys.get(0));
    }

    @Test
    void mapAttributesByFunctionWithPredicateFilter_null_args() {
        Predicate<CostCenter> greaterThan100 = cc -> cc.reimbursementNeeds() > 100.0;
        List<String> onlyShortNames =
                mapAttributesWithPredicate(null, greaterThan100, CostCenter::shortName);
        assertSame(0, onlyShortNames.size());
    }

    @Test
    void testExtractMembersAsList() {
        List<CostCenter> costCenters =
                List.of(new CostCenter("cc1", 100.0, "cc1-long-name"),
                        new CostCenter("cc2", 200.0, "cc2-long-name"),
                        new CostCenter("cc3", 233.99, "cc3-long-name"));
        List<String> onlyShortNames = extractMembersAsList(ImmutableList.copyOf(costCenters), CostCenter::shortName);
        assertEquals(3, onlyShortNames.size());
        assertEquals(List.of("cc1", "cc2", "cc3"), onlyShortNames);
        List<String> onlyLongNames = extractMembersAsList(ImmutableList.copyOf(costCenters), CostCenter::longName);
        assertEquals(3, onlyLongNames.size());
        assertEquals(List.of("cc1-long-name", "cc2-long-name", "cc3-long-name"), onlyLongNames);
        List<Double> onlyReimbursementNeeds = extractMembersAsList(ImmutableList.copyOf(costCenters), CostCenter::reimbursementNeeds);
        assertEquals(3, onlyReimbursementNeeds.size());
        assertEquals(List.of(100.0, 200.0, 233.99), onlyReimbursementNeeds);
        List<Double> empty = extractMembersAsList(null, CostCenter::reimbursementNeeds);
        assertEquals(0, empty.size());
    }

    @Test
    void testConcatenateListsOfTheSameType() {
        // given
        final List<String> list1 = new ArrayList<>();
        list1.add("StringsList1");
        final List<String> list2 = new ArrayList<>();
        list2.add("StringsList2");
        list2.add("StringsList2");
        // when
        final List<String> concatenatedLists = concatenateLists(list1, list2);
        // then
        assertThat(concatenatedLists.size(), is(3));
    }

    @Test
    void testFindDuplicates() {
        Set<String> stringDups = findDuplicates(List.of("1", "2", "2", "3", "4", "2"));
        assertThat(stringDups.size(), is(1));
        assertThat(stringDups.contains("2"), is(true));

        Set<Integer> intDups = findDuplicates(List.of(1, 2, 3, 3, 3, 5, 6, 6));
        assertThat(intDups.size(), is(2));
        assertThat(intDups.contains(3), is(true));
        assertThat(intDups.contains(6), is(true));
    }

    @Test
    void testDistinctSetFromListProperties() {
        List<CostCenter> costCenters =
                List.of(new CostCenter("cc1", 100.0, "cc1-long-name"),
                        new CostCenter("cc2", 200.0, "cc2-long-name"),
                        new CostCenter("cc3", 200.0, "cc3-long-name"));
        var resultSet = createDistinctSetFromListProperty(costCenters, CostCenter::reimbursementNeeds);
        assertThat(resultSet.size(), is(2));
        assertThat(resultSet.contains(100.0), is(true));
        assertThat(resultSet.contains(200.0), is(true));

        assertThat(createDistinctSetFromListProperty(emptyList(), CostCenter::reimbursementNeeds).size(), is(0));
        assertThat(createDistinctSetFromListProperty(null, CostCenter::reimbursementNeeds).size(), is(0));
    }
}
