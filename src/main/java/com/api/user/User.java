package com.api.user;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.api.article.Article;
import com.api.article_comment.ArticleComment;
import com.api.article_reply_comment.ArticleReplyComment;
import com.api.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    @JsonIgnore
    private String hashedPassword;

    @Column(name = "image_url", nullable = true)
    @JsonProperty("image_url")
    @Builder.Default
    private String imageUrl = "";

    @Column(name = "twitter_url", nullable = true)
    @JsonProperty("twitter_url")
    @Builder.Default
    private String twitterUrl = "";

    @Column(name = "linkedin_url", nullable = true)
    @JsonProperty("linkedin_url")
    @Builder.Default
    private String linkedinUrl = "";

    @Column(name = "github_url", nullable = true)
    @JsonProperty("github_url")
    @Builder.Default
    private String githubUrl = "";

    @Column(nullable = true)
    @Builder.Default
    private String introduction = "";

    @Column(name = "youtube_url", nullable = true)
    @JsonProperty("youtube_url")
    @Builder.Default
    private String youtubeUrl = "";

    @Column(name = "pinterest_url", nullable = true)
    @JsonProperty("pinterest_url")
    @Builder.Default
    private String pinterestUrl = "";

    @Column(name = "facebook_url", nullable = true)
    @JsonProperty("facebook_url")
    @Builder.Default
    private String facebookUrl = "";

    @Column(nullable = true)
    private String career;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonProperty("created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Article> articles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ArticleComment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ArticleReplyComment> replyComments;

    @OneToMany(mappedBy = "refUser", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ArticleReplyComment> refReplyComments;

    @JsonProperty("full_name")
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
