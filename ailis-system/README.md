# AILIS 차세대 시스템 (파일럿 프로젝트)

## 프로젝트 개요

cancerch-project를 Clean Architecture 기반으로 재개발한 차세대 LIMS 시스템입니다.
현재 **Worklist 모듈**을 파일럿 프로젝트로 구현했습니다.

## 아키텍처

### 백엔드 (Cancerch Service)
- **Framework**: Spring Boot 3.5.5
- **Language**: Kotlin + JDK 21
- **Architecture**: Clean Architecture (Hexagonal Architecture)
- **Database**: PostgreSQL 14 + R2DBC + jOOQ
- **Reactive**: Kotlin Coroutines
- **Testing**: JUnit 5 + MockK + Testcontainers

### 프론트엔드 (Bo-ui)
- **Framework**: React 18 + TypeScript
- **Build Tool**: Vite 5
- **UI Library**: PrimeReact 10
- **State Management**: Zustand 5
- **Data Fetching**: SWR 2 + Axios
- **Styling**: Tailwind CSS

### 인프라
- **Container**: Docker Compose
- **Database**: PostgreSQL 14
- **Message Broker**: Kafka + Zookeeper
- **Cache**: Redis 7
- **Monitoring**: Prometheus + Grafana

## 디렉토리 구조

```
ailis-system/
├── backend/
│   └── cancerch-service/      # Clean Architecture 백엔드
│       ├── src/
│       │   ├── main/kotlin/com/idrsys/ailis/cancerch/
│       │   │   ├── domain/           # 비즈니스 로직 (프레임워크 독립)
│       │   │   ├── application/      # 유스케이스
│       │   │   ├── adapter/          # 외부 인터페이스
│       │   │   └── infrastructure/   # 설정
│       │   └── test/                 # 테스트 (80%+ 커버리지)
│       └── build.gradle.kts
│
├── frontend/
│   └── bo-ui/                 # React 18 프론트엔드
│       ├── src/
│       │   ├── components/    # 재사용 컴포넌트
│       │   ├── pages/         # 페이지
│       │   ├── hooks/         # 커스텀 훅
│       │   ├── store/         # Zustand 스토어
│       │   └── types/         # TypeScript 타입
│       └── package.json
│
└── infrastructure/
    └── docker-compose.yml     # 로컬 개발 환경
```

## Clean Architecture 레이어

### 1. Domain Layer (핵심 비즈니스 로직)
```
domain/
├── model/              # 도메인 모델 (Worklist, Sample, etc.)
├── service/            # 도메인 서비스
├── repository/         # 리포지토리 인터페이스 (Port)
└── exception/          # 도메인 예외
```

**특징**:
- 프레임워크 독립적 (순수 Kotlin)
- 비즈니스 규칙만 포함
- 100% 테스트 커버리지 목표

### 2. Application Layer (유스케이스)
```
application/
├── usecase/            # 유스케이스 인터페이스
├── service/            # 유스케이스 구현
├── dto/                # DTO (Request/Response)
│   ├── request/        # Command, Query
│   └── response/       # Response
└── required/           # 외부 의존성 인터페이스 (Outbound Port)
```

**특징**:
- 도메인 로직 조율
- 트랜잭션 관리
- DTO 변환

### 3. Adapter Layer (기술 구현)
```
adapter/
├── web/                # REST 컨트롤러
├── persistence/        # DB 구현 (jOOQ + R2DBC)
├── repository/         # 리포지토리 구현
└── messaging/          # Kafka 이벤트
```

**특징**:
- 프레임워크 종속
- 외부 시스템 연동
- 교체 가능

## 빠른 시작

### 1. 인프라 시작 (Docker Compose)

```bash
cd infrastructure
docker-compose up -d

# 확인
docker-compose ps
```

실행되는 서비스:
- PostgreSQL: `localhost:5432`
- Kafka: `localhost:9092`
- Redis: `localhost:6379`
- Prometheus: `localhost:9090`
- Grafana: `localhost:3001` (admin/admin)

### 2. 백엔드 실행

```bash
cd backend/cancerch-service

# 빌드
./gradlew build

# 테스트 실행 (커버리지 80%+)
./gradlew test

# 애플리케이션 실행
./gradlew bootRun
```

API 서버: http://localhost:8080

**주요 엔드포인트**:
- `GET /api/worklists` - 워크리스트 목록 조회
- `POST /api/worklists` - 워크리스트 생성
- `GET /api/worklists/{id}` - 워크리스트 상세 조회
- `PATCH /api/worklists/{id}` - 워크리스트 수정

### 3. 프론트엔드 실행

```bash
cd frontend/bo-ui

# 의존성 설치
npm install

# 개발 서버 시작 (Vite HMR)
npm run dev
```

웹 애플리케이션: http://localhost:3000

## 개발 가이드

### 새로운 기능 추가 (TDD 방식)

**1단계: Domain Layer**
```kotlin
// 1. 테스트 먼저 작성
class WorklistDomainServiceTest {
    @Test
    fun `should validate worklist`() {
        // given
        val worklist = Worklist(...)

        // when
        val result = service.validate(worklist)

        // then
        assertTrue(result.isValid)
    }
}

// 2. 구현
class WorklistDomainService {
    fun validate(worklist: Worklist): ValidationResult {
        // 비즈니스 규칙
    }
}
```

**2단계: Application Layer**
```kotlin
// 1. UseCase 인터페이스 정의
interface CreateWorklistUseCase {
    suspend fun execute(command: CreateWorklistCommand): WorklistResponse
}

// 2. 테스트
class CreateWorklistServiceTest {
    @Test
    fun `should create worklist`() { ... }
}

// 3. 구현
class CreateWorklistService : CreateWorklistUseCase {
    override suspend fun execute(command: CreateWorklistCommand): WorklistResponse {
        // 도메인 서비스 활용
        // 리포지토리 호출
    }
}
```

**3단계: Adapter Layer**
```kotlin
// 1. Controller 테스트
@WebFluxTest(WorklistController::class)
class WorklistControllerTest {
    @Test
    fun `should create worklist via API`() { ... }
}

// 2. Controller 구현
@RestController
@RequestMapping("/api/worklists")
class WorklistController(
    private val createWorklistUseCase: CreateWorklistUseCase
) {
    @PostMapping
    suspend fun create(@RequestBody request: CreateWorklistRequest): WorklistResponse {
        return createWorklistUseCase.execute(request.toCommand())
    }
}
```

### 프론트엔드 컴포넌트 추가

```typescript
// 1. 타입 정의
export interface Worklist {
  id: string
  name: string
  status: WorklistStatus
  createdAt: string
}

// 2. API 훅
export function useWorklists() {
  const { data, error, isLoading } = useSWRApi<Worklist[]>('/api/worklists')
  return { worklists: data ?? [], error, isLoading }
}

// 3. 컴포넌트
export function WorklistTable() {
  const { worklists, isLoading } = useWorklists()

  return (
    <DataTable value={worklists} loading={isLoading}>
      <Column field="name" header="Name" />
      <Column field="status" header="Status" />
    </DataTable>
  )
}
```

## 테스트 전략

### 백엔드
- **Domain Layer**: 단위 테스트 (100% 커버리지)
- **Application Layer**: 단위 테스트 + Mock (90%+)
- **Adapter Layer**: 통합 테스트 + Testcontainers (70%+)
- **E2E**: 주요 시나리오 (100%)

### 프론트엔드
- **Components**: React Testing Library
- **Hooks**: @testing-library/react-hooks
- **Integration**: Cypress (예정)

## 현재 구현 상태

### ✅ 완료
- [x] Clean Architecture 구조
- [x] Worklist 도메인 모델
- [x] CRUD 유스케이스
- [x] REST API
- [x] jOOQ + R2DBC 연동
- [x] 단위/통합 테스트
- [x] React 18 + TypeScript 프로젝트
- [x] Worklist 목록/상세 UI
- [x] SWR 데이터 페칭
- [x] Docker Compose 인프라

### 🚧 진행 중
- [ ] Kafka 이벤트 발행
- [ ] Redis 캐싱
- [ ] Prometheus 메트릭
- [ ] Grafana 대시보드

### 📋 향후 계획
- [ ] Analysis 모듈 마이그레이션
- [ ] Report 모듈 마이그레이션
- [ ] Publish 모듈 마이그레이션
- [ ] Legacy 시스템 통합

## 기술 부채 해소

### cancerch-project → AILIS 개선 사항

| 항목 | 기존 시스템 | AILIS | 개선 효과 |
|------|-----------|-------|----------|
| **아키텍처** | 레이어 혼재 | Clean Architecture | 테스트 가능, 유지보수 용이 |
| **테스트 커버리지** | 1.9% | 80%+ | 품질 보장 |
| **UI 기술** | GWT (빌드 5-10분) | React + Vite (즉시 HMR) | 생산성 300배 향상 |
| **데이터 접근** | QueryDSL | jOOQ | SQL 우선, 성능 최적화 |
| **상태 관리** | 수동 | SWR + Zustand | 자동 캐싱/동기화 |

## 참고 자료

### Clean Architecture
- [Clean Architecture (Robert C. Martin)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

### 기술 문서
- [Spring Boot 3.5 Reference](https://docs.spring.io/spring-boot/docs/3.5.x/reference/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [jOOQ Manual](https://www.jooq.org/doc/latest/manual/)
- [React 18](https://react.dev/)
- [PrimeReact](https://primereact.org/)

## 라이선스

Proprietary - GCGenome AILIS System

## 문의

- 개발팀: dev-team@gcgenome.com
- 이슈 트래킹: [Internal Jira]
