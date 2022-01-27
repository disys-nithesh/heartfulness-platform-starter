package org.heartfulness.starter.domain.model;

import org.springframework.data.repository.CrudRepository;

public interface BlogPostRepository extends CrudRepository<BlogPost, String> {

}
