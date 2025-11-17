# AILIS Cancer Check Web Frontend

React 18 기반의 AILIS 차세대 시스템 프론트엔드 애플리케이션입니다.

## 기술 스택

- **React 18** - UI 라이브러리
- **TypeScript** - 타입 안전성
- **Vite** - 빌드 도구 및 개발 서버
- **React Router** - 라우팅
- **PrimeReact** - UI 컴포넌트 라이브러리
- **SWR** - 데이터 페칭 및 캐싱
- **Axios** - HTTP 클라이언트
- **Zustand** - 상태 관리 (추후 사용)
- **date-fns** - 날짜 처리

## 시작하기

### 사전 요구사항

- Node.js 18 이상
- npm 또는 yarn

### 설치

```bash
# 의존성 설치
npm install
```

### 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성합니다:

```bash
cp .env.example .env
```

필요한 경우 API 엔드포인트를 수정합니다:

```env
VITE_API_BASE_URL=http://localhost:8080
```

### 개발 서버 실행

```bash
npm run dev
```

브라우저에서 http://localhost:3000 을 열어 애플리케이션을 확인합니다.

### 빌드

```bash
npm run build
```

빌드된 파일은 `dist` 디렉토리에 생성됩니다.

### 프리뷰

빌드된 애플리케이션을 로컬에서 미리 보기:

```bash
npm run preview
```

## 프로젝트 구조

```
src/
├── api/              # API 클라이언트 및 서비스
│   ├── client.ts     # Axios 설정
│   └── worklistApi.ts # Worklist API 함수
├── hooks/            # 커스텀 React 훅
│   └── useWorklists.ts # SWR 기반 데이터 페칭
├── pages/            # 페이지 컴포넌트
│   ├── WorklistList.tsx      # Worklist 목록
│   ├── WorklistDetail.tsx    # Worklist 상세
│   └── CreateWorklist.tsx    # Worklist 생성
├── types/            # TypeScript 타입 정의
│   └── worklist.ts   # Worklist 관련 타입
├── App.tsx           # 메인 앱 컴포넌트
├── main.tsx          # 엔트리 포인트
└── index.css         # 전역 스타일
```

## 주요 기능

### Worklist 관리

- **목록 조회**: 페이지네이션 및 상태별 필터링 지원
- **상세 조회**: Worklist 정보 및 검체 목록 표시
- **생성**: 새로운 Worklist 생성
- **실시간 업데이트**: SWR을 통한 자동 리프레시 (30초 간격)

## API 통신

백엔드 API와의 통신은 Axios를 사용하며, 다음 기능을 지원합니다:

- 자동 요청/응답 인터셉터
- 에러 핸들링
- CORS 설정
- 향후 인증 토큰 지원 준비

## 개발 가이드

### 새로운 페이지 추가

1. `src/pages/` 디렉토리에 컴포넌트 생성
2. `src/App.tsx`에 라우트 추가
3. 필요한 경우 API 함수 및 훅 생성

### API 함수 추가

1. `src/api/` 디렉토리에 API 함수 추가
2. `src/hooks/` 디렉토리에 SWR 훅 생성 (선택사항)
3. `src/types/` 디렉토리에 타입 정의 추가

### 스타일링

- PrimeReact 컴포넌트 사용 권장
- 전역 스타일은 `src/index.css` 수정
- 컴포넌트별 인라인 스타일 또는 CSS 모듈 사용 가능

## 배포

### Production 빌드

```bash
npm run build
```

### Nginx 설정 예시

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 트러블슈팅

### CORS 오류

백엔드 서버의 CORS 설정을 확인하세요. `WebConfig.kt`에서 허용된 오리진을 확인합니다.

### API 연결 실패

1. 백엔드 서버가 실행 중인지 확인
2. `.env` 파일의 `VITE_API_BASE_URL` 확인
3. 브라우저 개발자 도구의 네트워크 탭 확인

### 의존성 오류

```bash
# node_modules 삭제 후 재설치
rm -rf node_modules package-lock.json
npm install
```

## 라이센스

Private - AILIS System
