package com.erebos.flu.utils;


import org.junit.jupiter.api.Test;

import java.util.List;

import static com.erebos.flu.utils.SetTheoryUtils.leftOuterJoin;
import static com.erebos.flu.utils.SetTheoryUtils.union;
import static org.junit.jupiter.api.Assertions.assertEquals;


class SetTheoryUtilsTest {

    @Test
    void testUnionWithCommonElements() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of("A", "C", "D");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C", "D"), result);
    }

    @Test
    void testUnionWithNoCommonElements() {
        // Given
        List<String> leftList = List.of("E", "F", "G");
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C", "E", "F", "G"), result);
    }

    @Test
    void testUnionWithDuplicateElementsWithinLists() {
        // Given
        List<String> leftList = List.of("A", "B", "B", "C");
        List<String> rightList = List.of("C", "C", "D", "D");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C", "D"), result);
    }

    @Test
    void testUnionWithEmptyLeftList() {
        // Given
        List<String> leftList = List.of();
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testUnionWithEmptyRightList() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of();

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testUnionWithBothListsEmpty() {
        // Given
        List<String> leftList = List.of();
        List<String> rightList = List.of();

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of(), result);
    }

    @Test
    void testUnionWithNestedDataStructures() {
        // Given
        List<List<Integer>> leftList = List.of(List.of(1, 2), List.of(3, 4));
        List<List<Integer>> rightList = List.of(List.of(3, 4), List.of(5, 6));

        // When
        List<List<Integer>> result = union(leftList, rightList);

        // Then
        assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6)), result);
    }

    @Test
    void testUnionWithSingleElementLists() {
        // Given
        List<String> leftList = List.of("A");
        List<String> rightList = List.of("B");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B"), result);
    }

    @Test
    void testUnionWithIdenticalLists() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = union(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testUnionWithLargeLists() {
        // Given
        List<Integer> leftList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> rightList = List.of(6, 7, 8, 9, 10, 11, 12);

        // When
        List<Integer> result = union(leftList, rightList);

        // Then
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), result);
    }

    @Test
    void testLeftOuterJoinWithCommonElements() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of("A", "C", "D");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("B", "A", "C"), result);
    }

    @Test
    void testLeftOuterJoinWithNoCommonElements() {
        // Given
        List<String> leftList = List.of("E", "F", "G");
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("E", "F", "G"), result);
    }

    @Test
    void testLeftOuterJoinWithAllCommonElements() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testLeftOuterJoinWithEmptyLeftList() {
        // Given
        List<String> leftList = List.of();
        List<String> rightList = List.of("A", "B", "C");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of(), result);
    }

    @Test
    void testLeftOuterJoinWithEmptyRightList() {
        // Given
        List<String> leftList = List.of("A", "B", "C");
        List<String> rightList = List.of();

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testLeftOuterJoinWithDuplicateElementsWithinLists() {
        // Given
        List<String> leftList = List.of("A", "B", "B", "C");
        List<String> rightList = List.of("C", "C", "D", "D");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("A", "B", "B", "C"), result);
    }

    @Test
    void testLeftOuterJoinWithNestedDataStructures() {
        // Given
        List<List<Integer>> leftList = List.of(List.of(1, 2), List.of(3, 4));
        List<List<Integer>> rightList = List.of(List.of(3, 4), List.of(5, 6));

        // When
        List<List<Integer>> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of(List.of(1, 2), List.of(3, 4)), result);
    }

    @Test
    void testLeftOuterJoinWithSingleElementLists() {
        // Given
        List<String> leftList = List.of("A");
        List<String> rightList = List.of("A");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("A"), result);
    }

    @Test
    void testLeftOuterJoinWithPartialOverlap() {
        // Given
        List<String> leftList = List.of("X", "Y", "Z");
        List<String> rightList = List.of("Y", "Z", "A");

        // When
        List<String> result = leftOuterJoin(leftList, rightList);

        // Then
        assertEquals(List.of("X", "Y", "Z"), result);
    }

    @Test
    void testRightOuterJoin() {
        // Given
        List<String> leftList = List.of("X", "Y", "Z");
        List<String> rightList = List.of("Y", "Z", "A");

        // When - JUST FLIP THE ARGUMENTS
        List<String> result = leftOuterJoin(rightList, leftList);

        // Then
        assertEquals(List.of("A", "Y", "Z"), result);
    }


}
