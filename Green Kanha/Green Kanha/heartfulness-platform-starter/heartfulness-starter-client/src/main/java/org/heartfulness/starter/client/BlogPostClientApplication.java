package org.heartfulness.starter.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogPostClientApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(BlogPostClientApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(BlogPostClientApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Autowired
    BlogPostClient blogPostClient;

    @Override
    public void run(String... args) throws Exception {
        blogPostClient.uploadCoverPicture();
    }
}
