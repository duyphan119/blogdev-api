package com.api.category;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.api.article.Article;
import com.api.category_parent.CategoryParent;
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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String slug;

    @Column(name = "image_url", nullable = true)
    @JsonProperty("image_url")
    private String imageUrl;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", insertable = false)
    @JsonIgnore
    private Long lastModifiedBy;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Article> articles;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private CategoryParent parent;

    @Column(name = "is_public", nullable = true)
    @JsonProperty("is_public")
    @Builder.Default
    private Boolean isPublic = true;
}
