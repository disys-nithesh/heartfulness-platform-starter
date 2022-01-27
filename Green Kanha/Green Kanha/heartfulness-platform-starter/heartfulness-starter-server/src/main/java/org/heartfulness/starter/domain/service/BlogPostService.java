package org.heartfulness.starter.domain.service;

import org.heartfulness.starter.domain.model.BlogPost;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface BlogPostService {
    BlogPost add(BlogPost post);

    BlogPost delete(String postId);

    BlogPost get(String postId);

    String updateProfilePicture(String blogPostId,
                                byte[] picture,
                                String fileExtension) throws IOException, ParserConfigurationException, SAXException;
}
