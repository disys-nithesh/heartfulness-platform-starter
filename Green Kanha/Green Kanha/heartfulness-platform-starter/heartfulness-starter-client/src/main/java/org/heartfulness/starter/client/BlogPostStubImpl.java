package org.heartfulness.starter.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.heartfulness.starter.interfaces.grpc.BlogPostServiceGrpc;
import org.heartfulness.unifiedplatform.common.util.StrUtil;
import org.heartfulness.unifiedplatform.infrastructure.utils.auth.AuthenticationHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.auth.MoreCallCredentials;
import io.grpc.stub.MetadataUtils;

@Component
public class BlogPostStubImpl implements BlogPostStub {
    static final Logger logger = LoggerFactory.getLogger(BlogPostStubImpl.class);

    private ManagedChannel channel;

    @Value("${blogpost.service.use.firebase.id.token:false}")
    boolean useFirebaseIdToken;

    @Value("${add.auth.headers:true}")
    boolean addAuthHeaders;

    @Value("${blogpost.service.firebase.id.token:blogpost-service-grpc.dev.heartfulnessinstitute.in}")
    String firebaseIdToken;

    @Value("${blogpost.service.host:blogpost-service-grpc.dev.heartfulnessinstitute.in}")
    String blogPostServiceHost;

    @Value("${blogpost.service.port:443}")
    int blogPostServicePort;

    @Value("${unifiedplatform.service.key:service.account.keys/service-developer-unifiedplatform-dev.json}")
    String serviceKey;

    @Value("${blogpost.service.endpoint:blogpost-service-grpc.dev.heartfulnessinstitute.in}")
    String blogPostServiceEndpoint;

    @Value("${blogpost.service.use.ssl:false}")
    boolean useSSL;

    private BlogPostServiceGrpc.BlogPostServiceBlockingStub blogPostServiceBlockingStub;
    private volatile CallCredentials gcpCC;

    @PostConstruct
    void initialize() throws IOException, URISyntaxException {
        if (!useSSL) {
            channel = ManagedChannelBuilder.forAddress(blogPostServiceHost, blogPostServicePort).usePlaintext(true)
                    .build();
        } else {
            channel = ManagedChannelBuilder.forAddress(blogPostServiceHost, blogPostServicePort).build();
            generateGoogleCredentials();

            TimerTask generateAccessTokenTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        generateGoogleCredentials();
                        logger.info("Using Endpoint- " + blogPostServiceEndpoint);
                    } catch (Exception e) {
                        logger.error("Error occurred while getting credentials for blogPostService ", e);
                    }
                }
            };

            Timer timer = new Timer("BlogPostGRPCService-AccessTokenTimer");
            timer.scheduleAtFixedRate(generateAccessTokenTask, 45 * 60 * 1000L, 45 * 60 * 1000L);
        }

        blogPostServiceBlockingStub = BlogPostServiceGrpc.newBlockingStub(channel);
    }

    private void generateGoogleCredentials() throws FileNotFoundException, IOException, URISyntaxException {
        logger.info("Generating New Credentials for BlogPostService");

        if(useFirebaseIdToken) {
            GoogleCredentials gc = GoogleCredentials.create(new AccessToken(firebaseIdToken, null));
            gcpCC = MoreCallCredentials.from(gc);
        } else {
            File keyJson = StrUtil.getFile(serviceKey);
            gcpCC = MoreCallCredentials.from(AuthenticationHelper
                    .getJWTServiceAccountToken(new FileInputStream(keyJson), blogPostServiceEndpoint));
        }
        logger.info("Generated New Credentials for BlogPostService");
    }

    private static final Metadata.Key<String> X_ENDPOINT_API_USERINFO = Metadata.Key.of("x-endpoint-api-userinfo",
            Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> AUTHORIZATION = Metadata.Key.of("authorization",
            Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public BlogPostServiceGrpc.BlogPostServiceBlockingStub get() {
        BlogPostServiceGrpc.BlogPostServiceBlockingStub blockingStub = useSSL ? blogPostServiceBlockingStub.withCallCredentials(gcpCC)
                : blogPostServiceBlockingStub;

        if (useFirebaseIdToken && addAuthHeaders) {
            Metadata header = new Metadata();

            try {
                header.put(X_ENDPOINT_API_USERINFO, getEndpointApiUserInfoKey());
            } catch (Exception e) {
                logger.error("Error occurred in preparing headers from firebaseIdToken for grpc call", e);
            }

            header.put(AUTHORIZATION, "Bearer " + firebaseIdToken);
            return MetadataUtils.attachHeaders(blockingStub, header);
        }

        return blockingStub;
    }

    private String getEndpointApiUserInfoKey() throws ParseException {
        JSONObject payload = (JSONObject) new JSONParser()
                .parse(new String(Base64.getUrlDecoder().decode(firebaseIdToken.split("\\.")[1])));

        JSONObject userInfo = new JSONObject();
        userInfo.put("id", payload.get("user_id"));
        userInfo.put("email", payload.get("email"));

        // Hack to make usage simple when connecting to localhost.
        // This hack will not work, if we are connecting to ESP. As ESP will authenticate using the call credentials
        //
        // In order to make firebase user act as service account/Super user, we are sending
        // empty string. If we want to make it non-service account, replace "" with payload.get("iss")
        userInfo.put("issuer", "");

        return new String(Base64.getUrlEncoder().encode(userInfo.toJSONString().getBytes()));
    }
}

