package org.heartfulness.starter.client;

import com.google.protobuf.ByteString;
import org.heartfulness.starter.interfaces.grpc.BlogPostOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BlogPostClient {
    @Autowired
    BlogPostStub blockingStub;

    public void uploadCoverPicture() {
        BlogPostOuterClass.UpdateCoverPictureRequest updateCoverPictureReq = null;
        try {
            updateCoverPictureReq = BlogPostOuterClass.UpdateCoverPictureRequest.newBuilder()
                    .setBlogPostId("u8SR5k4kDFTCD3hUDQF6z4AH71J2")
                    .setPictureName("SampleJpg.jpg")
                    .setPictureData(ByteString.readFrom(getClass().getResourceAsStream("/profile/SampleJpg.jpg"))).build();

            String profilePicUrl = blockingStub.get().updateCoverPicture(updateCoverPictureReq).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
