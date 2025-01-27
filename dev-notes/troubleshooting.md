## 🐛 트러블 슈팅

### `Spring Security` 인증 로직의 이해

FORM 기반 인증을 사용하는 경우 `UsernamePasswordAuthenticationFilter`에서 `UserDetailService.loadUserByUsername()`를 사용해 DB로부터`username`을 확인하는 로직을 호출한다. 그러나 현재 프로젝트는 FORM 기반 인증이 아닌 JWT 기반 인증을 사용하기에 FORM 기반 인증을 disable 했는데, FORM 기반 인증을 disable 하면 `UsernamePasswordAuthenticationFilter` 역시 disable 되는 걸 너무 뒤늦게 알았다. 그렇기에 `UserDetailService`를 구현은 해놓았는데 호출이 안 되니 인증이 되지 않아 애를 먹었다. 그래서 `UserDeatailService`를 사용하는 대신, JwtFilter에서 토큰 값으로부터 유저 정보를 추출한 후 Authentication 객체에 주입하는 식으로 구현했다.

---

### Kotlin와 JPA Entity 철학의 충돌

`kotlin`으로 `Entity`를 설계할 때, null 처리를 어떻게 할지 고민이 됐다. Kotlin의 장점이 null-safe한 언어인데, JPA Entity를 설계할 때 다른 필드 값은 객체가 초기화 될 때 바로 함께 초기화를 하거나, 아예 nullable한 필드 값을 설정해주면 됐는데 ID 값만큼은 null-safe하게 만들 수 없는 게 아쉬웠다.

```kotlin
@Entity
@Table(name = "users")
class User(
    email: String,
    password: String,
    nickname: String,
    role: Role,
    profileImgUrl: String?
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null
        protected set

    @Column(name = "email", updatable = false, nullable = false, unique = true)
    var email: String = email
        protected set

    @Column(name = "nickname", nullable = false)
    var nickname: String = nickname
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = role
        protected set

    @Column(name = "profile_img_url")
    var profileImgUrl: String? = profileImgUrl

    fun changePassword(newPassword: String) {
        this.password = newPassword
    }

    fun changeRole(newRole: Role) {
        this.role = newRole
    }
}
```

위 엔티티를 보면, `id`와 DB에서 null이 가능한 값인 `profileImgUrl`을 제외하고 모두 생성자에서 값을 받아와 초기화를 하는 것을 알 수 있는데, id 같은 경우는 가장 null-safe하게 생성해야 함에도 불구하고 DB에서 ID 값을 받아와야 하기 때문에 null로 초기화를 할 수밖에 없다는 점이 아쉬웠다.

또한, 각 필드마다 `protected set`을 해놨는데, 코틀린은 클래스 필드 멤버에 대하여 자동으로 `getter/setter`를 만들어주기 때문에 getter는 가능하게 하고, setter만 막기 위해서 `protected set`으로 처리했다. `private set`으로 하고 싶었지만 `private set`은 final class만 설정 가능하다. Entity가 JPA 기능을 사용하기 위해서 상속 가능한 open 클래스 열어놔야 했기에 `private set`이 아닌, `protected set`으로 설정했다.

\+ <ins>튜터님의 해설 세션을 듣고 추가</ins>

-> 과제 해설 세션에서 튜터님의 구현 방식은 `id` 값을 `var`로 하지 않고 `val id: Long = 0` 방식이었다 . 이해가 안 됐다. '`val`는 `final` 개념이라 한 번 할당이 되면 변경이 안 될 텐데?' 같은 생각이 순간적으로 머릿속에 맴돌았지만, `val`로 할당이 돼도 `JPA`가 리플렉션을 사용해서 `id`값을 바꿔주겠거니 생각했다. 실제로 테스트를 해보니 `val id: Long = 0`으로 사용해도 정상적으로 `id`값이 변경됐다. 그래서 아직 `id`를 부여받지 못했다는 걸 조금 더 명확하게 하기 위해서 `val id: Long = -1`로 초기화를 해주었더니 이번에는 아래와 같은 예외가 발생했다.

![예외](https://github.com/user-attachments/assets/fdb5c2c7-98d5-41c0-bf44-5e9c2592aaf9)

`ObjectOptimisticLockingFailureException` <- 요 예외는 `JPA`가 `-1`이라는 ID 값을 가진 엔티티를 기존에 존재하는 엔티티라고 판단했기 때문에 발생하는 예외다. `JPA`는 ID가 `null or 0`인 경우 새로운 엔티티라고 인식하고 INSERT를 수행한다. 하지만 현재 엔티티 `id`에 다른 값(`-1`)이 있어 이미 존재하고 있는 엔티티라고 인식하고 UPDATE를 수행했지만 `id` 값이 `-1`인 엔티티가 없어서 `ObjectOptimisticLockingFailureException` 예외가 발생한 것이다.

(\+ 예외명에 `OptimisticLocking~~`이 있길래 최근애 배운 낙관적 락(Optimisitic Lock: 서로 다른 트랜잭션이 같은 데이터를 읽으려고 할 때 예외 발생시킴)과 헷갈렸다. 현재 상황은 서로 다른 트랜잭션이 동일한 데이터를 읽으려고 한 게 아니기 때문이다. 알아보니 여기서 말하는 `OptimisticLocking`은 내가 알고 있는 낙관적 락과 별 관련은 없다고 한다)


### ECR을 이용해 EC2에 배포할 때 컨테이너 분리 문제

처음에는 프로젝트 빌드를 EC2 인스턴스 내에서 하려고 했다. 그런데 현재 내가 사용하는 EC2 인스턴스가 프리티어 스펙이기에 프로젝트를 빌드하면 서버가 죽는 문제가 발생했다. 가상램을 조금 설정해주면 다행히 서버는 죽지 않았지만, 빌드를 EC2에서 하는 것보다 마켓 스토어에서 안정된 빌드 환경을 제공하는 깃허브 액션 컴퓨팅 환경이 빌드하기에 더 적절한 환경이라고 생각이라고 생각했다. 그래서 프로젝트 빌드 및 이미지 배포 작업을 모두 깃허브 액션이 제공하는 컴퓨팅 환경에서 진행하고, 최종 결과만 ECR에 푸시한 후 EC2에서는 컨테이너만 실행하는 방식으로 변경했다.

---

### `Spring Security` 필터 중복 등록 문제

```kotlin

    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(
                "auth/**"
            )
        }
    }
  
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicySessionCreationPolicy.STATELESS)
            } 
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .requestCache { it.disable() } 
            .logout { it.disable() }
            .addFilterBefore(jwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
  
            .
            .
            .
    }

    @Bean
    fun jwtFilter(tokenProvider: tokenProvider) = JwtAuthenticationFilter(tokenProvider)

```

내가 한 시큐리티 설정은 위와 같다. 현재 코드를 보면 `JwtAuthenticationFilter`를 빈으로 만들어주고, 해당 필터를 `UsernamePasswordAuthenticationFilter` 이전에 추가함으로써 스프링 시큐리티 필터 체인에 등록했음을 알 수 있다. 그러나, 나는 `/auth/**`로 접근하는 URL은 시큐리티에서 제공하는 필터 체인 무시하고 싶었다. 그렇기에 스프링 시큐리티 필터체인을 무시할 수 있는 `WebSecurityCustomizer`를 사용해 `/auth`로 시작하는 모든 요청을 통과하게 하고 싶었으나, 자꾸만 `JwtAuthenticationFilter`에서 요청이 걸렸다.

문제는 `@Bean fun jwtAuthenticationFilter() = JwtAuthenticationFilter()` <- 이 부분에서 발생했다. `WebSecurityCustomizer`는 분명히 설정한 URL에 대해서 시큐리티 필터 체인을 완전히 무시하도록 설정한다. 그러나 내가 `JwtAutenticationFilter`를 `@Bean`으로 등록하는 바람에 필터가 이중으로 등록되어 버린 것이다. 즉, `addFilterBefore()` 메서드를 이용해 시큐리티 체인에 `JwtAuthenticationFilter`을 등록함과 동시에 시큐리티 필터 체인이 아닌, 일반 `ApplicationFilterChain`에 `JwtAuthenticationFilter`를 등록해버린 것이다. 다시 말하면, 커스텀 필터를 `@Bean`으로 등록하면 자동으로 해당 필터가 `ApplicationFilterChain`에 등록되기 때문에 스프링 시큐리티 필터 체인에 커스텀 필터를 등록할 때는 해당 필터를 `@Bean`으로 등록하면 안 된다.

위 코드에서 `WebSecurityCustomizer`를 사용한 이유는, `permitAll()` 메서드로 특정 경로를 허용해 줘도, `permitAll()`은 스프링 시큐리티 체인을 안 타는 게 아니라, (내가 이해한 바에 따르면) '인증 객체가 없어도 통과시켜줄게'의 방식이라서 커스텀 필터에 따로 인증 제외 로직을 추가해야 했기에 화이트리스트를 시큐리티 필터체인에서 한 번, 커스텀 필터에서 한 번, 총 두 번 설정해주는 게 다소 마음에 들지 않았기 때문이다. 이렇게 할 거면, 굳이 `permitAll()`로 허용해준 의미가 퇴색되는 느낌이라... 🌵
