package org.example.contact.model;

public record ContactRequest(String first_name, String last_name, String email, String subject, String message) { }