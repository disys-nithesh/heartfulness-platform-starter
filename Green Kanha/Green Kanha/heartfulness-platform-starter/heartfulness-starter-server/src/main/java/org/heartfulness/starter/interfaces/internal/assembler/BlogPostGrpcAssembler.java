package org.heartfulness.starter.interfaces.internal.assembler;

import org.heartfulness.starter.domain.model.BlogPost;
import org.heartfulness.starter.domain.model.BlogPostType;
import org.heartfulness.starter.interfaces.grpc.BlogPostOuterClass;
import org.springframework.util.StringUtils;

public class BlogPostGrpcAssembler {
    public static BlogPostOuterClass.BlogPost toGrpc(BlogPost domainModel) {
        return BlogPostOuterClass.BlogPost.newBuilder()
                .setPostId(domainModel.getId())
                .setPostText(domainModel.getText())
                .setPostType(toGrpc(domainModel.getPostType()))
                .setPostNo(domainModel.getPostNo())
                .setIsPublished(domainModel.isPublished()).build();
    }

    public static BlogPost fromGrpc(BlogPostOuterClass.BlogPost grpcModel) {
        BlogPost domainModel = new BlogPost();

        if(!StringUtils.isEmpty(grpcModel.getPostId())) {
            domainModel.setId(grpcModel.getPostId());
        }

        if(!StringUtils.isEmpty(grpcModel.getPostText())) {
            domainModel.setText(grpcModel.getPostText());
        }

        domainModel.setPostType(fromGrpc(grpcModel.getPostType()));
        domainModel.setPostNo(grpcModel.getPostNo());
        domainModel.setPublished(grpcModel.getIsPublished());
        return domainModel;
    }

    private static BlogPostOuterClass.BlogPostType toGrpc(BlogPostType domainModel) {
        return BlogPostOuterClass.BlogPostType.valueOf(domainModel.name());
    }

    private static BlogPostType fromGrpc(BlogPostOuterClass.BlogPostType grpcModel) {
        return BlogPostType.valueOf(grpcModel.name());
    }
}
