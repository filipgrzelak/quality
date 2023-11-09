package org.example.contact.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContactResponse(String id, String first_name, String last_name, String email, String subject, String message) { }
