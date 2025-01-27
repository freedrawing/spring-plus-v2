## 고민했던 지점들

### 스프링 시큐리티 필터 체인에서 DB를 다녀오는 게 좋은 걸까??
스프링 시큐리티 인증 시점에, 필터 단에서 DB를 다녀오는 게 좋아보이지 않는다. 현재 프로젝트는 사실 JWT 토큰을 통해 인증을 하기 때문에 굳이 DB를 거칠 필요가 없다고 생각해, 토큰에 있는 값을 추출해 `Authentication` 객체에 주입해 인증 처리를 했다. 이렇게 해도 괜찮을까?

---

### Github Repository 파일 공개 범위
Github Repository에 Dockerfile 및 docker-compose.yaml 파일을 public하게 공개해도 되는 걸까? 이를테면 현재 프로젝트에는 프록시 서버 역할을 Nginx 이미지 설정 정보가 들어가 있는데 `[/nginx/nginx.conf, /nginx/Dockerfile]` nginx.conf 같은 경우는 간단한 설정이지만 프록시 서버 포트값이 노출되어 있어 이런 정보를 public하게 올려도 되는지 판단이 안 섰다.
```nginx
server {
    listen 80;

    location / {
        proxy_pass http://app:8080;  # app은 docker-compose에서 정의할 서비스 이름
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## 느낀 점
개인적으로 이번 프로젝트를 하면서 가장 해보고 싶었던 부분이 CI/CD였다. 깃허브에 코드를 푸시하고 자동으로 테스트도 진행하고, 배포까지 된다는 게 너무 신기했기 때문이다. (다만, 이번에 내가 적용한 워크플로우가 흔히 말하는 CI/CD를 제대로 구현한 것인지는 확신이 없다) 워크플로우를 작성하면서 에러가 많이 발생해 우여곡절도 겪고, 그것을 해결해나가면서 깃허브 액션 워크플로우가 어떻게 동작하는지 대략적으로 감은 잡은 것 같다. (어렵지만 너무 신기하다. 무중단 배포까지 흉내내고 싶었지만, 여러 툴을 익히고 개념을 이해하는 데 시간을 너무 많이 사용한 듯?)

프로젝트를 진행하면서 알아야 할 게 너무 많다는 걸 새삼 다시 느꼈다. 배포를 하기 위해서는 docker와 docker-compose가 무엇인지, docker-swam은 또 뭐하는 친구인지 기본적인 개념들을 이해하고 있어야 했다. 마찬가지로 EC2나, RDS 같은 서비스를 사용하기에 앞서 AWS라는 서비스 전반에 대해 더 잘 이해하고 있어야 함을 느꼈다. 어떤 기능을 제공하는지 아는 것과 그 기능을 사용하기 위해 이해하고 있는 것은 전혀 다른 문제였다. EC2나 RDS가 무엇인지, S3가 어떤 뭐하는 친구들인지 정도는 개략적으로 알고 있었지만 이런 서비스들을 적절히 사용하기 위해서는 VPC, IAM 등 AWS 전반에 대해 잘 알고 있어야 한다는 생각을 많이 했다. 항상 느끼는 거지만 전체를 이해하는 게 참 중요한 듯하다...

~~(Redis, Spring Cache, ElasticSearch 등 사용해보고 싶은 기술들이 많았지만 못한 게 아쉽...)~~
