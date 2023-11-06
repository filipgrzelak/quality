package org.example.product.model;

public record ProductRequest(
         String name,
         String description,
         double price,
         String category_id,
         String brand_id,
         int product_image_id,
         int is_location_offer,
         int is_rental) {
}
