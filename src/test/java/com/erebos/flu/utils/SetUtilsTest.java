package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.CostCenter;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static com.erebos.flu.utils.SetUtils.extractMembersAsSet;
import static com.erebos.flu.utils.SetUtils.getNullableSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

class SetUtilsTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(SetUtils.class);
    }

    @Test
    void testExtractMembersAsSet() {
        Set<CostCenter> costCenters =
                Set.of(new CostCenter("cc1", 100.0, "cc1-long-name"),
                        new CostCenter("cc2", 200.0, "cc2-long-name"),
                        new CostCenter("cc3", 233.9, "cc3-long-name"));
        Set<String> onlyShortNames = extractMembersAsSet(costCenters, CostCenter::shortName);
        assertThat(onlyShortNames.size(), is(3));
        assertThat(onlyShortNames, containsInAnyOrder("cc1", "cc2", "cc3"));
        Set<String> onlyLongNames = extractMembersAsSet(costCenters, CostCenter::longName);
        assertThat(onlyLongNames.size(), is(3));
        assertThat(onlyLongNames, containsInAnyOrder("cc1-long-name", "cc2-long-name", "cc3-long-name"));
        Set<Double> onlyReimbursementNeeds = extractMembersAsSet(costCenters, CostCenter::reimbursementNeeds);
        assertThat(onlyReimbursementNeeds.size(), is(3));
        assertThat(onlyReimbursementNeeds, containsInAnyOrder(100.0, 200.0, 233.9));
        Set<Double> empty = extractMembersAsSet(null, CostCenter::reimbursementNeeds);
        assertThat(empty.size(), is(0));
    }

    @Test
    void testGetNullableSet() {
        assertThat(getNullableSet(null), is(Set.of()));
        final var set = Set.of("1", "2");
        assertThat(getNullableSet(set), is(set));
    }
}
