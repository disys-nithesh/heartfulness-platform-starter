package org.heartfulness.starter;

import com.google.protobuf.BoolValue;
import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.commons.io.FilenameUtils;
import org.heartfulness.starter.domain.exception.InvalidRequestException;
import org.heartfulness.starter.domain.model.BlogPost;
import org.heartfulness.starter.domain.service.BlogPostService;
import org.heartfulness.starter.interfaces.grpc.BlogPostOuterClass;
import org.heartfulness.starter.interfaces.grpc.BlogPostServiceGrpc;
import org.heartfulness.starter.interfaces.internal.assembler.BlogPostGrpcAssembler;
import org.heartfulness.unifiedplatform.domain.shared.AuthenticationContext;
import org.heartfulness.unifiedplatform.infrastructure.utils.auth.grpc.AuthenticationServerInterceptor;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@GRpcService(interceptors = {AuthenticationServerInterceptor.class})
public class BlogPostGRPCServiceImpl extends BlogPostServiceGrpc.BlogPostServiceImplBase {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BlogPostGRPCServiceImpl.class);

    @Autowired
    BlogPostService blogPostService;

    @Override
    public void addBlogPost(BlogPostOuterClass.BlogPost request, StreamObserver<BlogPostOuterClass.BlogPost> responseObserver) {

        try {
            BlogPost domainModel = blogPostService.add(BlogPostGrpcAssembler.fromGrpc(request));
            responseObserver.onNext(BlogPostGrpcAssembler.toGrpc(domainModel));
            responseObserver.onCompleted();
        } catch (InvalidRequestException ex) {
            responseObserver.onError(ex.toGRPCException());
            return;
        } catch (Exception e) {
            log.error("Error occurred while adding blog post", e);
            responseObserver.onError(Status.INTERNAL.asRuntimeException());
        }
    }

    @Override
    public void deleteBlogPost(StringValue request, StreamObserver<BlogPostOuterClass.BlogPost> responseObserver) {
        try {
            BlogPost domainModel = blogPostService.delete(request.getValue());
            responseObserver.onNext(BlogPostGrpcAssembler.toGrpc(domainModel));
            responseObserver.onCompleted();
        } catch (InvalidRequestException ex) {
            responseObserver.onError(ex.toGRPCException());
            return;
        } catch (Exception e) {
            log.error("Error occurred while adding blog post", e);
            responseObserver.onError(Status.INTERNAL.asRuntimeException());
        }
    }

    @Override
    public void getBlogPost(StringValue request, StreamObserver<BlogPostOuterClass.BlogPost> responseObserver) {
        try {
            BlogPost domainModel = blogPostService.get(request.getValue());
            responseObserver.onNext(BlogPostGrpcAssembler.toGrpc(domainModel));
            responseObserver.onCompleted();
        } catch (InvalidRequestException ex) {
            responseObserver.onError(ex.toGRPCException());
            return;
        } catch (Exception e) {
            log.error("Error occurred while adding blog post", e);
            responseObserver.onError(Status.INTERNAL.asRuntimeException());
        }
    }

    @Override
    public void updateCoverPicture(BlogPostOuterClass.UpdateCoverPictureRequest request, StreamObserver<StringValue> responseObserver) {
        String fileExtension = FilenameUtils.getExtension(request.getPictureName());

        try {
            String url = blogPostService.updateProfilePicture(request.getBlogPostId(),
                    request.getPictureData().toByteArray(),
                    fileExtension);
            responseObserver.onNext(StringValue.newBuilder().setValue(url).build());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            responseObserver.onError(e);
        }

        responseObserver.onCompleted();
    }
}
