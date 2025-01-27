# Sparta-Spring-Plus

## 프로젝트 개요
일정 관리 애플리케이션

**(기존의 프로젝트를 `fork`해서 작업해야 했으나, 처음부터 `Kotlin`을 처음부터 사용하고 싶었기에, 아예 `Kotlin`으로 프로젝트를 시작했습니다)**

## 사용한 기술 스택
* `Kotlin`
* `Gradle`
* `SpringBoot`
* `Spring Security`
* `JPA`, `QueryDSL`
* `MySQL`, `MariaDB`, `H2`
* `JWT`
* `EC2`
* `RDS`
* `Docker`
* `Nginx`
* `Github Action`

## 작업 현황

### LV1 (필수기능)
* 1.1 코드 개선 퀴즈 - @Transactional의 이해✅
* 1.2 코드 추가 퀴즈 - JWT의 이해 ✅
* 1.3 코드 개선 퀴즈 - AOP의 이해 ✅
* 1.4 테스트 코드 퀴즈 - 컨트롤러 테스트의 이해 ✅
* 1.5 코드 개선 퀴즈 - JPA의 이해 ✅

### LV2 (필수 기능)
* 2.6 JPA Cascaed ✅
* 2.7 N+1 ✅
* 2.8 QueryDSL ✅
* 2.9 Spring Security ✅

### LV3 (도전 기능)
* 3.10 QueryDSL을 사용하여 검색기능 만들기 ✅
* 3.11 Transaction 심화 - 로그 기능 만들기 ✅
* 3.12 AWS 활용 마스터 ✅
  * 12-1 EC2 ✅
  * 12-2 RDS ✅
  * 12-3 S3
* 3.13 대용량 데이터 처리
* 3.14 `Kotlin` 적용하기 ✅

## AWS Configuration

### EC2 콘솔
![EC2 개요](https://github.com/user-attachments/assets/d1ccbe02-75f1-4bd6-801b-01ef7c9bb041)
![EC2 보안그룹](https://github.com/user-attachments/assets/6b810661-e17f-49c5-9068-0dbac34ce2aa)

### RDS 콘솔
![RDS 개요](https://github.com/user-attachments/assets/939377b7-4752-42f9-9077-1a5952a15832)
![RDS 보안그룹](https://github.com/user-attachments/assets/3190d4f5-f7c8-4a31-8c4d-e360c298d9fc)

## API 문서
https://documenter.getpostman.com/view/14200056/2sAYQggnpF

## CI/CD

## 기타
* [🐛 트러블 슈팅](/dev-notes/troubleshooting.md)
* [💡프로젝트를 하면서 생각했던 점들](/dev-notes/thoughtslog.md)