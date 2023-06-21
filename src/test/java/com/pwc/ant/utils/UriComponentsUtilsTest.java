package com.erebos.ant.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

import java.util.Map;

import static com.erebos.ant.utils.UriComponentsUtils.getUriComponents;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UriComponentsUtilsTest {

    @Test
    void testGetUriComponents_withQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fileType", "JOURNAL");
        params.add("clientId", Integer.toString(12));

        UriComponents uriComponents = getUriComponents("http://rave", "/test", params);

        assertThat(uriComponents.toUri().toString(), is("http://rave/test?fileType=JOURNAL&clientId=12"));
    }


    @Test
    void testGetUriComponents_withPathParams() {
        Map<String, String> params = Map.of("fileType", "JOURNAL", "clientId", Integer.toString(12));

        UriComponents uriComponents = getUriComponents("http://rave", "/test/fileType/{fileType}/clientId/{clientId}",
                new LinkedMultiValueMap<>(), params);

        assertThat(uriComponents.toUri().toString(), is("http://rave/test/fileType/JOURNAL/clientId/12"));
    }
}
