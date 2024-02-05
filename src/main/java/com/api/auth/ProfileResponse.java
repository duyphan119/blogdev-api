package com.api.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("image_url")
    private String imageUrl;

    private Long id;
}
