package com.freedrawing.springplus.config.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {

    // Basic
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "GENERAL_400", "올바르지 않은 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "GENERAL_405", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GENERAL_500", "서버에 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "GENERAL_404", "존재하지 않는 엔티티입니다."),
    ENTITY_ALREADY_EXISTS(HttpStatus.CONFLICT, "GENERAL_409", "이미 존재하는 엔티티입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "GENERAL_403", "접근 권한이 없습니다."),

    // Role
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "ROLE_400", "유효하지 않은 권한입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "존재하지 않는 사용자입니다."),
    USER_DELETED(HttpStatus.FORBIDDEN, "USER_403", "삭제된 사용자입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409", "이미 존재하는 사용자입니다."),

    // Authentication
    AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_401", "인증에 실패했습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_401", "아이디 혹은 비밀번호가 올바르지 않습니다."),

    // Token
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN_400", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_404", "존재하지 않는 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_401", "만료된 토큰입니다."),

    // Todo
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_404", "존재하지 않는 일정입니다."),
    TODO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "TODO_403", "해당 일정에 대한 접근 권한이 없습니다."),

    // TodoManagement
    TODO_MANAGEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_MANAGEMENT_404", "존재하지 않는 일정 관리입니다."),
    TODO_MANAGEMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "TODO_MANAGEMENT_403", "해당 일정 관리에 대한 접근 권한이 없습니다."),
    TODO_MANAGEMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "TODO_MANAGEMENT_409", "이미 동일 매니저가 할당되어 있습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_404", "존재하지 않는 댓글입니다."),
    COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMENT_403", "해당 댓글에 대한 접근 권한이 없습니다."),


    ;

}