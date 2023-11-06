package org.example.product.model;

public record ProductResponse(
        String id,
        String name,
        String description,
        double price,
        int is_location_offer,
        int is_rental) {
}

