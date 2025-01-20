plugins {
    kotlin("jvm") version "1.9.25" // Kotlin을 JVM에서 실행하도록 지원하는 플러그인.
    kotlin("plugin.spring") version "1.9.25" // Spring 프레임워크와 Kotlin 간 통합을 위한 플러그인.
    id("org.springframework.boot") version "3.4.1" // Spring Boot 플러그인으로, 애플리케이션을 실행 가능하게 설정하고, 의존성 관리를 단순화.
    id("io.spring.dependency-management") version "1.1.7" // Spring Boot에서 제공하는 의존성 관리 기능을 별도로 활성화.
    id("org.jetbrains.kotlin.kapt") version "2.0.0" //  Kotlin Annotation Processing Tool (KAPT)을 활성화하여 QueryDSL 및 Lombok과 같은 APT 기반 도구 사용 가능.
    kotlin("plugin.noarg") version "2.1.0" // JPA 엔티티에서 기본 생성자를 요구할 때 사용
    kotlin("plugin.allopen") version "2.1.0" // 특정 애노테이션을 사용해 클래스와 메서드를 자동으로 open 상태로 변경.
}

group = "com.freedrawing" // 프로젝트의 그룹 ID. 배포 시 식별에 사용.
version = "0.0.1-SNAPSHOT" // 프로젝트 버전. 배포 및 종속성 관리 시 사용.

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}

repositories {
    mavenCentral()
}

dependencies {

    // Kotlin basic
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // Kotlin 표준 라이브러리의 JDK 8 버전.
    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin 리플렉션 라이브러리. 리플렉션 기반 작업 필요 시 사용.

    // SpringBoot basic
    implementation("org.springframework.boot:spring-boot-starter-web") // Spring Web 애플리케이션 개발에 필요한 기본 의존성 포함.
    implementation("org.springframework.boot:spring-boot-starter-validation") // javax.validation을 통해 데이터 유효성 검사를 지원.
    developmentOnly("org.springframework.boot:spring-boot-devtools") // 개발 중 핫 리로드 및 기타 편의 기능 제공.

    // Spring Security
    implementation ("org.springframework.boot:spring-boot-starter-security")
    testImplementation ("org.springframework.security:spring-security-test")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // PA와 Hibernate를 기반으로 한 데이터 계층 작업을 지원.

    // DB
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client") // MariaDB 데이터베이스와의 연결을 위한 JDBC 드라이버.

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta") // Querydsl의 JPA 관련 기능 제공 (JPAQueryFactory, BooleanBuilder, PathBuilder ...)
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta") // QueryDSL APT(Annotation Processing Tool)로 컴파일 시점에 @Entity 애노테이션이 붙은 JPA 엔티티 클래스를 기반으로 Q 클래스를 생성
    kapt("jakarta.annotation:jakarta.annotation-api") // QueryDSL APT에서 사용하는 @Generated 애노테이션을 제공.  클래스 생성 시 @Generated 애노테이션이 필요하며, 이를 처리하기 위해 이 의존성이 필요
    kapt("jakarta.persistence:jakarta.persistence-api") // JPA 표준 애노테이션(@Entity, @Id, @Column 등)을 정의. QueryDSL APT가 엔티티 클래스를 분석하고 Q 클래스를 생성하는 데 필수

    // Kotlin-Logging
    implementation ("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // P6Spy
//    implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.2")
    implementation ("p6spy:p6spy:3.9.1")
    implementation ("com.github.gavlyukovskiy:datasource-decorator-spring-boot-autoconfigure:1.9.2")

    // test
    testImplementation(kotlin("test")) // `Kotlin`에서 테스트를 작성하기 위한 표준 라이브러리.
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // h2
    runtimeOnly("com.h2database:h2")

    // BCrypt Encoder (Spring Security 의존성 추가 없이 Encoder 사용할 때)
    implementation("at.favre.lib:bcrypt:0.10.2")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1") // Java JWT library
    implementation("javax.xml.bind:jaxb-api:2.3.1") // XML document와 Java 객체 간 매핑 자동화

}

// JSR-305 애노테이션을 엄격하게 처리. `Nullable` 및 `NonNull`을 더 안전하게 다룸.
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

// JUnit5 플랫폼을 사용해 테스트 실행
tasks.withType<Test> {
    useJUnitPlatform()
}

noArg {
    annotation("jakarta.persistence.Entity")
}

// JPA 애노테이션을 사용하는 클래스들을 자동으로 open 처리. 이는 JPA 프록시 생성 요구사항을 충족하기 위함.
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
