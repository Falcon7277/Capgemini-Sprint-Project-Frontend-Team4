package com.example.ProjectSprintFrontend.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Maps the Spring Data REST HAL response for GET /orders:
 * {
 *   "_embedded": { "orders": [...] },
 *   "_page":     { "size":20, "totalElements":326, "totalPages":17, "number":0 }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersPageResponse {

    @JsonProperty("_embedded")
    private Map<String, List<OrderDto>> embedded;

    @JsonProperty("page")
    private PageMetaDto page;

    public List<OrderDto> getOrders() {
        if (embedded == null) return Collections.emptyList();
        // Spring Data REST uses the entity name (plural) as the key
        return embedded.getOrDefault("orders", Collections.emptyList());
    }

    public PageMetaDto getPage() { return page; }
    public void setPage(PageMetaDto v) { this.page = v; }

    public void setEmbedded(Map<String, List<OrderDto>> v) { this.embedded = v; }
}

