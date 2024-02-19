package com.api.article;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.api.article_comment.ArticleComment;
import com.api.category.Category;
import com.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
@Table(name = "article")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "introduction_text", nullable = true)
    @JsonProperty("introduction_text")
    private String introductionText;

    @Column(nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    @JsonProperty("image_url")
    private String imageUrl;

    @Column
    @Builder.Default
    private Long views = Long.valueOf(0);

    @Column(name = "is_public", nullable = true)
    @JsonProperty("is_public")
    @Builder.Default
    private Boolean isPublic = false;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = true)
    @JsonProperty("is_longreads")
    @Builder.Default
    private Boolean isLongreads = false;

    @Column(nullable = true)
    @JsonProperty("comment_count")
    @Builder.Default
    private Integer commentCount = 0;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @JsonIgnore
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", insertable = false)
    @JsonIgnore
    private Long lastModifiedBy;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ArticleComment> comments;
}
