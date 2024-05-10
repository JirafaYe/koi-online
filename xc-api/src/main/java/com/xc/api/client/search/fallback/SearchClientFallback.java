package com.xc.api.client.search.fallback;

import com.xc.api.client.search.SearchClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class SearchClientFallback implements FallbackFactory<SearchClient> {
    @Override
    public SearchClient create(Throwable cause) {
        return null;
    }
}
