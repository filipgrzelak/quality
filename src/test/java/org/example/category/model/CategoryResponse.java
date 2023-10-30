package org.example.category.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryResponse(String id, String parent_id, String name, String slug) {
}
