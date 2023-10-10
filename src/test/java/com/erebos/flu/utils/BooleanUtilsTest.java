package com.erebos.flu.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.erebos.flu.utils.BooleanUtils.getNullableBoolean;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BooleanUtilsTest {

    @Test
    void testGetNullableBoolean() {
        MatcherAssert.assertThat(getNullableBoolean(Boolean.FALSE), Matchers.is(false));
        MatcherAssert.assertThat(getNullableBoolean(Boolean.TRUE), Matchers.is(true));
        MatcherAssert.assertThat(getNullableBoolean(null), Matchers.is(false));
    }
}
