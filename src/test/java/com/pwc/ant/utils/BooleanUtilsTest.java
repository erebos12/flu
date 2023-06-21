package com.erebos.ant.utils;

import org.junit.jupiter.api.Test;

import static com.erebos.ant.utils.BooleanUtils.getNullableBoolean;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BooleanUtilsTest {

    @Test
    void testGetNullableBoolean() {
        assertThat(getNullableBoolean(Boolean.FALSE), is(false));
        assertThat(getNullableBoolean(Boolean.TRUE), is(true));
        assertThat(getNullableBoolean(null), is(false));
    }
}
