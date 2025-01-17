package com.freedrawing.springplus.domain.comment.repository

import com.freedrawing.springplus.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>, CommentRepositoryCustom {
}