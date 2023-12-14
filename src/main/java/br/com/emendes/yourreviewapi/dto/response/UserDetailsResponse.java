package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com dados resumidos sobre User.
 */
@Builder
public record UserDetailsResponse(
    Long id,
    String name,
    String email,
    String status,
    LocalDateTime createdAt
) {
}
