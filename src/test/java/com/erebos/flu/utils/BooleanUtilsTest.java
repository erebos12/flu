package com.erebos.flu.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;


import static com.erebos.flu.utils.BooleanUtils.getNullableBoolean;
import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;


class BooleanUtilsTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(BooleanUtils.class);
    }

    @Test
    void testGetNullableBoolean() {
        MatcherAssert.assertThat(getNullableBoolean(Boolean.FALSE), Matchers.is(false));
        MatcherAssert.assertThat(getNullableBoolean(Boolean.TRUE), Matchers.is(true));
        MatcherAssert.assertThat(getNullableBoolean(null), Matchers.is(false));
    }
}
