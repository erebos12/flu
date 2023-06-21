package com.erebos.flu.utils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class UriComponentsUtils {

    private UriComponentsUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static UriComponents getUriComponents(final String url,
                                                 final String path,
                                                 final MultiValueMap<String, String> queryParams,
                                                 final Map<String, ?> pathParams) {
        return UriComponentsBuilder
                .fromHttpUrl(url)
                .path(path)
                .queryParams(queryParams)
                .buildAndExpand(pathParams);
    }


    public static UriComponents getUriComponents(final String url,
                                                 final String path,
                                                 final MultiValueMap<String, String> queryParams) {
        return getUriComponents(url, path, queryParams, emptyMap());
    }
}
