# BingoSRM Backend

이 저장소에는 두 개의 코드베이스가 있습니다.
- `itsm_src(legacy)`: 기존 eGov/JSP 애플리케이션
- `bingosrm_src`: Spring Boot REST 백엔드 (레거시에서 마이그레이션)

## 개발환경
- Java JDK 17
- Maven
- MariaDB 10.4

## 레거시 개발환경 (참고용)
- 전자정부 프레임워크 4.10-64 비트
- eGovFrameDev-4.1.0-64bit

## 레거시 참고
레거시 코드는 `itsm_src(legacy)`에 보관되어 있습니다.

## 프로젝트 구조
- bingosrm_src: Spring Boot REST 백엔드
- itsm_src(legacy): 기존 eGov/JSP (참고용)

## 변경 사항 요약 (Legacy -> Spring Boot)

핵심 변경:
- JSP/서블릿 MVC에서 JSON 기반 REST API(`/api/v1/...`)로 전환
- `itsm_src(legacy)`의 서비스/DAO/매퍼 로직을 `bingosrm_src`로 이관
- 각 모듈에 REST 컨트롤러 추가
- 프로그램 접근 권한 기반의 API 접근 제어 추가

`bingosrm_src`로 마이그레이션된 모듈:
- assets
- attachments
- business info
- common codes
- users/auth
- program management + access author
- service response (SR)
- infra operations
- functional improvements
- WDTB
- reports (master/detail/detail2)
- report attendance
- report charger
- system charger
- SMS
- history (login/use)

미이관(UI 전용):
- JSP 화면, `main`, `oz` 컨트롤러 (레거시에 유지)
  - 서버 렌더링 화면이나 OZ 보고서를 계속 사용한다면 필요합니다.

## 접근 제어(프로그램 권한)

레거시 인터셉터 방식 대신, Boot 앱은 `/api/**`에 대해 권한을 검사합니다.

`bingosrm_src/src/main/resources/application.yml`:
```
itsm:
  security:
    enforce-permissions: true
```

활성화 시:
- `/api/v1/auth/**`, `/api/v1/hist/**`를 제외한 모든 `/api/**` 요청에
  `X-User-Ty-Code` 헤더가 필요합니다.
- 권한은 `TB_PROGRM`, `TB_PROGRM_ACCES_AUTHOR` 기준으로 판정됩니다.

시드 스크립트:
- `bingosrm_src/db/seed_api_programs.sql`: `/api` + `/api/v1` 프로그램 매핑 생성
- `bingosrm_src/db/grant_api_roles.sql`: 역할별 API 접근 권한 부여

## SR 업데이트 (Partial Update 지원)

SR 업데이트는 이제 부분 업데이트를 지원합니다.
요청에 포함한 필드만 업데이트되고 나머지는 유지됩니다.

## 실행 방법

필수:
- Java 17
- MariaDB 10.4
- Maven

실행:
```
cd bingosrm_src
mvn -DskipTests spring-boot:run
```
기본 포트: 8081
