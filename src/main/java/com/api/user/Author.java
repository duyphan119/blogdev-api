package com.api.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("twitter_url")
    private String twitterUrl;

    @JsonProperty("linkedin_url")
    private String linkedinUrl;

    @JsonProperty("github_url")
    private String githubUrl;

    private String introduction;

    @JsonProperty("youtube_url")
    private String youtubeUrl;

    @JsonProperty("pinterest_url")
    private String pinterestUrl;

    @JsonProperty("facebook_url")
    private String facebookUrl;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private String career;

}
