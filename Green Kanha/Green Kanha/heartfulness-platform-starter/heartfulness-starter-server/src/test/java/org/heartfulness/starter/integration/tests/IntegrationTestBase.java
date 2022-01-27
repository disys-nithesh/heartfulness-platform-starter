package org.heartfulness.starter.integration.tests;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.MetadataUtils;
import org.heartfulness.starter.domain.service.CloudStorageService;
import org.heartfulness.starter.interfaces.grpc.BlogPostServiceGrpc;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Base64;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StarterTestingApp.class}, webEnvironment = DEFINED_PORT)
@TestPropertySource(properties = {"grpc.port:10103"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationTestBase {

    protected static ManagedChannel channel;
    protected static final String DEFAULT_ADDRESS = "localhost:10103";
    protected static BlogPostServiceGrpc.BlogPostServiceBlockingStub blogPostGrpc;

    @MockBean
    public CloudStorageService cloudStorageService;

    @BeforeClass
    public static void beforeClass() throws Exception {
        channel = ManagedChannelBuilder.forTarget(DEFAULT_ADDRESS).usePlaintext(true).build();
        blogPostGrpc = BlogPostServiceGrpc.newBlockingStub(channel);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        channel.shutdown();
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected void cleanDatabase() {
        jdbcTemplate.execute("delete from blog_post");
    }
}
