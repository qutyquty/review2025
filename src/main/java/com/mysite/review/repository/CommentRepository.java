package com.mysite.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.review.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
