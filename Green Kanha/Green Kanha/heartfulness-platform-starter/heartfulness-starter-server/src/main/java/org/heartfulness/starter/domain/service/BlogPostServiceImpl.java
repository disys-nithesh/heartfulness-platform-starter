package org.heartfulness.starter.domain.service;

import org.heartfulness.starter.domain.exception.ErrorCode;
import org.heartfulness.starter.domain.exception.InvalidRequestException;
import org.heartfulness.starter.domain.model.BlogPost;
import org.heartfulness.starter.domain.model.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogPostServiceImpl implements  BlogPostService {

    @Autowired
    BlogPostRepository blogPostRepository;

    @Override
    public BlogPost add(BlogPost post) {
        validate(post);

        return blogPostRepository.save(post);
    }

    private void validate(BlogPost post) {
        if(StringUtils.isEmpty(post.getText())) {
            throw new InvalidRequestException(ErrorCode.BLOGPOST_TEXT_IS_REQUIRED, "Blog post text is required");
        }

        if(post.getText().length() > 1000) {
            throw new InvalidRequestException(ErrorCode.BLOGPOST_TEXT_EXCEEDING_MAX_LENGTH, "Blog post text should be less than 1000 chars");
        }
    }

    @Override
    public BlogPost delete(String postId) {
        Optional<BlogPost> blogPostToBeDeleted =  blogPostRepository.findById(postId);

        if(blogPostToBeDeleted == null || !blogPostToBeDeleted.isPresent()) {
            throw new InvalidRequestException(ErrorCode.BLOGPOST_NOT_FOUND, "Blog post doesnt exist.");
        }

        blogPostRepository.delete(blogPostToBeDeleted.get());
        return blogPostToBeDeleted.get();
    }

    @Override
    public BlogPost get(String postId) {
        Optional<BlogPost> blogPostToBeDeleted =  blogPostRepository.findById(postId);

        if(blogPostToBeDeleted == null || !blogPostToBeDeleted.isPresent()) {
            throw new InvalidRequestException(ErrorCode.BLOGPOST_NOT_FOUND, "Blog post doesnt exist.");
        }

        return blogPostToBeDeleted.get();
    }

    @Autowired
    CloudStorageService cloudStorageService;

    @Override
    @Transactional
    public String updateProfilePicture(String blogPostId,
                                       byte[] picture,
                                       String fileExtension) throws IOException, ParserConfigurationException, SAXException {
        BlogPost blogPost = get(blogPostId);

        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
        cloudStorageService.uploadToCloudStorage(fileName, new ByteArrayInputStream(picture), true);

        String url = cloudStorageService.getFileURL(fileName);
        blogPost.setBlogCoverImgUrl(url);
        blogPostRepository.save(blogPost);
        return url;
    }
}
