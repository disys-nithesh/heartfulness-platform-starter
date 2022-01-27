package org.heartfulness.starter.client;


import org.heartfulness.starter.interfaces.grpc.BlogPostServiceGrpc;

public interface BlogPostStub {
    BlogPostServiceGrpc.BlogPostServiceBlockingStub get();
}
