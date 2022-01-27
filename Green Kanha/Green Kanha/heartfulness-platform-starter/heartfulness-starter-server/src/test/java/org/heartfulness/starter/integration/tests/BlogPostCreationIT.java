package org.heartfulness.starter.integration.tests;

import org.heartfulness.starter.interfaces.grpc.BlogPostOuterClass;
import org.junit.After;
import org.junit.Test;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;

public class BlogPostCreationIT extends  IntegrationTestBase {

    @After
    public void cleanUp(){
        this.cleanDatabase();
    }

    @Test
    public void blogPostCanBeCreated() {
        BlogPostOuterClass.BlogPost blogPost =
                BlogPostOuterClass.BlogPost.newBuilder()
                .setPostType(BlogPostOuterClass.BlogPostType.MEDIA)
                .setPostText("Some Valid Bolg Text")
                .setIsPublished(true)
                .setPostNo(1)
                .build();

        BlogPostOuterClass.BlogPost createdBlogPost = blogPostGrpc.addBlogPost(blogPost);
        assertNotNull(createdBlogPost);
        assertFalse(StringUtils.isEmpty(createdBlogPost.getPostId()));
        assertEquals(blogPost.getPostText(), createdBlogPost.getPostText());
        assertEquals(blogPost.getPostType(), createdBlogPost.getPostType());
        assertEquals(blogPost.getIsPublished(), createdBlogPost.getIsPublished());
        assertEquals(blogPost.getPostNo(), createdBlogPost.getPostNo());
    }

}
