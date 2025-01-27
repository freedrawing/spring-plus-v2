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