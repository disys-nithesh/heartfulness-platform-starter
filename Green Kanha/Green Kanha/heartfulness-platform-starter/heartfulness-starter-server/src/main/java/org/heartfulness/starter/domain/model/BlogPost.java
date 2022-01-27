package org.heartfulness.starter.domain.model;

import org.heartfulness.starter.util.InstantToLongConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class BlogPost {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(name="post_no")
    private Integer postNo;

    @Column(name="post_type")
    private BlogPostType postType;

    @Column(name="is_published")
    private boolean isPublished;

    @Column(name="blog_cover_img_url")
    private String blogCoverImgUrl;

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    @Convert(converter = InstantToLongConverter.class)
    private Instant createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @Convert(converter = InstantToLongConverter.class)
    private Instant modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPostNo() {
        return postNo;
    }

    public void setPostNo(Integer postNo) {
        this.postNo = postNo;
    }

    public BlogPostType getPostType() {
        return postType;
    }

    public void setPostType(BlogPostType postType) {
        this.postType = postType;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getBlogCoverImgUrl() {
        return blogCoverImgUrl;
    }

    public void setBlogCoverImgUrl(String blogCoverImgUrl) {
        this.blogCoverImgUrl = blogCoverImgUrl;
    }
}
