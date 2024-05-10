package com.xc.api.client.search;

import com.xc.api.client.search.fallback.SearchClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "search-service", fallbackFactory = SearchClientFallback.class)
public interface SearchClient {
}
