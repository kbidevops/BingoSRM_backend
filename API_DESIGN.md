# BingoSRM API 설계 (bingosrm_src)

<aside>
📢 아래 아이콘을 모든 Request/Response에 포함  
🚩 = Updating / 🧨 = To be updated / 👍 = Service Ready / ✔️ = Completed  
모든 Request/Response에 담당자 `@username`을 포함합니다.
</aside>

# Common 🚩

<aside>
‼️ DEV API URL = http://localhost:8081  
DEV Client URL = http://localhost:3000  
TEST API URL = <TBD>
</aside>

## 공통 헤더
```
Content-Type: application/json
X-User-Ty-Code: R001
X-User-Id: api_test   # 선택
```
> `/api/v1/auth/**`, `/api/v1/hist/**` 제외 모든 `/api/**` 요청에 `X-User-Ty-Code` 필요

## 공통 에러 응답
```json
{
  "error": "Missing X-User-Ty-Code header."
}
```

## 페이지네이션 규칙
요청:
```
pageIndex=1
recordCountPerPage=15
```
응답:
```json
{
  "resultList": [],
  "pagination": {
    "pageIndex": 1,
    "recordCountPerPage": 15,
    "totalCount": 0
  }
}
```

---

# JWT 설계 (🧨 To be updated) @backend

<aside>
현재 구현은 JWT 미사용. 아래는 설계(예정)입니다.
</aside>

## Login (JWT)
### REQUEST 🧨
> POST /api/v1/auth/login
```json
{
  "userId": "api_test",
  "userPassword": "Test1234!"
}
```

### RESPONSE 🧨
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "eyJ...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Refresh Token
### REQUEST 🧨
> POST /api/v1/auth/refresh
```
Authorization: Bearer <refreshToken>
```

### RESPONSE 🧨
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "eyJ...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Logout
### REQUEST 🧨
> POST /api/v1/auth/logout
```
Authorization: Bearer <accessToken>
```

### RESPONSE 🧨
```json
{ "result": "SUCCESS" }
```

## Access Token Expired Response
```json
{
  "error": "911",
  "message": "Access Token is expired"
}
```

---

# Auth ✔️ @backend

**필드 스키마**: UserVO (부록 참조)

## Login ✔️
### REQUEST
> POST /api/v1/auth/login
```json
{
  "userId": "api_test",
  "userPassword": "Test1234!"
}
```

### RESPONSE
```json
{
  "authenticated": true,
  "userId": "api_test",
  "userTyCode": "R001",
  "userSttusCode": "U002",
  "passwordExpired": false,
  "status": "OK"
}
```

---

# Common Codes ✔️ @backend

**필드 스키마**: CmmnCodeTyVO, CmmnCodeVO (부록 참조)

## Code Types ✔️
### REQUEST
> GET /api/v1/code-types

### RESPONSE
```json
[
  { "cmmnCodeTy": "T0", "cmmnCodeTyNm": "Asset Type" }
]
```

## Codes by Type ✔️
### REQUEST
> GET /api/v1/code-types/{codeType}/codes

### RESPONSE
```json
[
  { "cmmnCode": "T001", "cmmnCodeNm": "HW" }
]
```

---

# Users ✔️ @backend

**필드 스키마**: BaseVO + UserVO (부록 참조)

## List (paged) ✔️
### REQUEST
> GET /api/v1/users?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [
    { "userId": "api_test", "userNm": "API Test User", "userTyCode": "R001" }
  ],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 1 }
}
```

## Detail ✔️
### REQUEST
> GET /api/v1/users/{userId}

### RESPONSE
```json
{ "userId": "api_test", "userNm": "API Test User", "userTyCode": "R001" }
```

## Create ✔️
### REQUEST
> POST /api/v1/users
```json
{
  "userId": "user01",
  "userPassword": "Test1234!",
  "userNm": "Tester",
  "userTyCode": "R003",
  "userSttusCode": "U002"
}
```

### RESPONSE
```json
{ "created": true }
```

## Update ✔️
### REQUEST
> PUT /api/v1/users/{userId}
```json
{
  "userNm": "Tester Updated",
  "changePasswordYN": "N"
}
```

### RESPONSE
```
204 No Content
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/users/{userId}

### RESPONSE
```
204 No Content
```

---

# Programs ✔️ @backend

**필드 스키마**: BaseVO + ProgrmVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/programs

### RESPONSE
```json
[
  { "progrmSn": "1", "progrmNm": "API Assets", "progrmUri": "/api/v1/assets" }
]
```

## Tree ✔️
### REQUEST
> GET /api/v1/programs/tree

### RESPONSE
```json
[
  { "progrmSn": "1", "progrmNm": "API Root", "progrmUri": "/api" }
]
```

## Detail ✔️
### REQUEST
> GET /api/v1/programs/{progrmSn}

### RESPONSE
```json
{ "progrmSn": "1", "progrmNm": "API Assets", "progrmUri": "/api/v1/assets" }
```

## Create ✔️
### REQUEST
> POST /api/v1/programs
```json
{
  "progrmNm": "Sample Menu",
  "progrmUri": "/sample",
  "upperProgrmSn": "0",
  "sortNo": "1",
  "menuIndictYn": "Y"
}
```

### RESPONSE
```json
{ "created": true, "progrmSn": "123" }
```

## Update ✔️
### REQUEST
> PUT /api/v1/programs/{progrmSn}
```json
{ "progrmNm": "Sample Menu (Updated)" }
```

### RESPONSE
```json
{ "updated": true, "progrmSn": "123" }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/programs/{progrmSn}

### RESPONSE
```json
{ "deleted": true, "progrmSn": "123" }
```

---

# Program Access ✔️ @backend

**필드 스키마**: ProgrmAccesAuthorVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/program-access?authorCode=R001

### RESPONSE
```json
[
  { "progrmSn": "1", "progrmNm": "API Assets", "progrmAccesAuthorCode": "R001" }
]
```

## Assigned ✔️
### REQUEST
> GET /api/v1/program-access/{authorCode}/assigned

### RESPONSE
```json
[
  { "progrmSn": "1", "progrmNm": "API Assets", "progrmAccesAuthorCode": "R001" }
]
```

## Update ✔️
### REQUEST
> PUT /api/v1/program-access/{authorCode}
```json
{ "progrmSns": ["1", "2", "3"] }
```

### RESPONSE
```json
{ "updated": true, "authorCode": "R001" }
```

---

# Assets ✔️ @backend

**필드 스키마**: BaseVO + AssetsVO (부록 참조)

## List (paged) ✔️
### REQUEST
> GET /api/v1/assets?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [
    { "assetsSn": 1, "assetsNo": "12345", "assetsSe1Nm": "HW" }
  ],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 1 }
}
```

---

# Attachments ✔️ @backend

**필드 스키마**: AtchmnflVO (부록 참조)

## Upload ✔️
### REQUEST
> POST /api/v1/attachments (multipart/form-data)
```
file: <file>
atchmnflId: <optional>
```

### RESPONSE
```json
{ "atchmnflId": "uuid", "resultList": [] }
```

## List ✔️
### REQUEST
> GET /api/v1/attachments/{atchmnflId}

### RESPONSE
```json
[
  { "atchmnflId": "uuid", "atchmnflSn": "1", "orginlFileNm": "file.txt" }
]
```

## Download ✔️
### REQUEST
> GET /api/v1/attachments/{atchmnflId}/{atchmnflSn}

### RESPONSE
```
file stream
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/attachments/{atchmnflId}/{atchmnflSn}

### RESPONSE
```json
{ "deleted": true }
```

---

# Business Info ✔️ @backend

**필드 스키마**: BsnsInfoVO, ChargerUserInfoVO (부록 참조)

## Get ✔️
### REQUEST
> GET /api/v1/bsns-info

### RESPONSE
```json
{ "bsnsNm": "BingoSRM", "chargerNm": "관리자" }
```

## Update ✔️
### REQUEST
> POST /api/v1/bsns-info
```json
{ "bsnsNm": "BingoSRM" }
```

### RESPONSE
```json
{ "updated": true }
```

---

# Service Response (SR) ✔️ @backend

**필드 스키마**: BaseVO + SrvcRsponsVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/sr?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [
    { "srvcRsponsNo": "SR-2401-001", "srvcRsponsSj": "제목" }
  ],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 1 }
}
```

## Requests / Receives / Processes / Verifications / Finishes / Evaluations ✔️
### REQUEST
> GET /api/v1/sr/requests (동일: /receives, /processes, /verifications, /finishes, /evaluations)

### RESPONSE
```json
{
  "resultList": [
    { "srvcRsponsNo": "SR-2401-001", "srvcRsponsSj": "제목" }
  ],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 1 }
}
```

## Detail ✔️
### REQUEST
> GET /api/v1/sr/{srvcRsponsNo}

### RESPONSE
```json
{ "srvcRsponsNo": "SR-2401-001", "srvcRsponsSj": "제목" }
```

## Create ✔️
### REQUEST
> POST /api/v1/sr
```json
{
  "trgetSrvcCode": "A001",
  "srvcRsponsBasisCode": "S306",
  "srvcRsponsSj": "요청 제목",
  "srvcRsponsCn": "요청 내용"
}
```

### RESPONSE
```json
{ "created": true, "srvcRsponsNo": "SR-2401-002" }
```

## Create (Manager) ✔️
### REQUEST
> POST /api/v1/sr/manager
```json
{ "srvcRsponsSj": "관리자 등록" }
```

### RESPONSE
```json
{ "created": true, "srvcRsponsNo": "SR-2401-003" }
```

## Update (Partial) ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}
```json
{ "srvcRsponsSj": "제목 수정" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Request Update ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/request
```json
{ "srvcRsponsCn": "요청 수정" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Receive ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/receive
```json
{ "chargerId": "admin" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Response First ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/response-first
```json
{ "srvcRsponsClCode": "S102" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Process ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/process
```json
{ "srvcProcessDtls": "처리내용" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Verify ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/verify
```json
{ "verifyYn": "Y" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Finish ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/finish
```json
{ "finishYn": "Y" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Evaluation ✔️
### REQUEST
> PUT /api/v1/sr/{srvcRsponsNo}/evaluation
```json
{ "satisfyCode": "S901" }
```

### RESPONSE
```json
{ "updated": true, "srvcRsponsNo": "SR-2401-002" }
```

## Re-request ✔️
### REQUEST
> POST /api/v1/sr/{srvcRsponsNo}/re-request
```json
{ "srvcRsponsCn": "재요청" }
```

### RESPONSE
```json
{ "created": true, "srvcRsponsNo": "SR-2401-010", "reSrvcRsponsNo": "SR-2401-002" }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/sr/{srvcRsponsNo}

### RESPONSE
```json
{ "deleted": true, "srvcRsponsNo": "SR-2401-002" }
```

---

# Infra Operations ✔️ @backend

**필드 스키마**: InfraOpertVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/infra-operations?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 0 }
}
```

## Detail ✔️
### REQUEST
> GET /api/v1/infra-operations/{infraOpertNo}

### RESPONSE
```json
{ "infraOpertNo": "IO-2401-001" }
```

## Create ✔️
### REQUEST
> POST /api/v1/infra-operations
```json
{
  "infraOpert": { "infraOpertNo": "IO-2401-001" },
  "srvcRsponsNos": ["SR-2401-001"]
}
```

### RESPONSE
```json
{ "created": true, "infraOpertNo": "IO-2401-001" }
```

## Update ✔️
### REQUEST
> PUT /api/v1/infra-operations/{infraOpertNo}
```json
{
  "infraOpert": { "infraOpertNo_sub": "IO-2401-001" },
  "srvcRsponsNos": ["SR-2401-001"]
}
```

### RESPONSE
```json
{ "updated": true, "infraOpertNo": "IO-2401-001" }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/infra-operations/{infraOpertNo}
```json
{ "srvcRsponsNos": ["SR-2401-001"] }
```

### RESPONSE
```json
{ "deleted": true, "infraOpertNo": "IO-2401-001" }
```

---

# Functional Improvements ✔️ @backend

**필드 스키마**: FuncImprvmVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/func-improvements?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 0 }
}
```

## Check ✔️
### REQUEST
> GET /api/v1/func-improvements/check?processMt=202601

### RESPONSE
```json
{ "exists": false }
```

## Detail ✔️
### REQUEST
> GET /api/v1/func-improvements/{fnctImprvmNo}

### RESPONSE
```json
{ "fnctImprvmNo": "FI-2401-001" }
```

## Create ✔️
### REQUEST
> POST /api/v1/func-improvements
```json
{ "srvcRsponsNo": "SR-2401-001", "fiCl": "S001" }
```

### RESPONSE
```json
{ "created": true, "fnctImprvmNo": "FI-2401-001" }
```

## Update ✔️
### REQUEST
> PUT /api/v1/func-improvements/{fnctImprvmNo}
```json
{ "fiPlan": "Updated plan" }
```

### RESPONSE
```json
{ "updated": true, "fnctImprvmNo": "FI-2401-001" }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/func-improvements/{fnctImprvmNo}

### RESPONSE
```json
{ "deleted": true, "fnctImprvmNo": "FI-2401-001" }
```

---

# WDTB ✔️ @backend

**필드 스키마**: WdtbVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/wdtb?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 0 }
}
```

## Check ✔️
### REQUEST
> GET /api/v1/wdtb/check?processMt=202601

### RESPONSE
```json
{ "exists": false }
```

## Detail ✔️
### REQUEST
> GET /api/v1/wdtb/{wdtbCnfirmNo}

### RESPONSE
```json
{ "wdtb": { "wdtbCnfirmNo": "R-2401-001" } }
```

## Create ✔️
### REQUEST
> POST /api/v1/wdtb
```json
{ "wdtb": { "wdtbSe": "S001" }, "srvcRsponsNos": ["SR-2401-001"] }
```

### RESPONSE
```json
{ "created": true, "wdtbCnfirmNo": "R-2401-001" }
```

## Update ✔️
### REQUEST
> PUT /api/v1/wdtb/{wdtbCnfirmNo}
```json
{ "wdtb": { "wdtbEtc": "Updated" } }
```

### RESPONSE
```json
{ "updated": true, "wdtbCnfirmNo": "R-2401-001" }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/wdtb/{wdtbCnfirmNo}
```json
{ "srvcRsponsNos": ["SR-2401-001"] }
```

### RESPONSE
```json
{ "deleted": true, "wdtbCnfirmNo": "R-2401-001" }
```

---

# Reports ✔️ @backend

**필드 스키마**: RepMasterVO, RepDetailVO, RepDetailVO2, RepMasterVO2 (부록 참조)

## Report Master ✔️
### REQUEST
> POST /api/v1/reports
```json
{ "repTyCode": "B001", "reportDtDisplay": "2026-01-20" }
```

### RESPONSE
```json
{ "created": true, "repSn": 1 }
```

## Report Master List ✔️
### REQUEST
> GET /api/v1/reports?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 0 }
}
```

## Report Master Detail ✔️
### REQUEST
> GET /api/v1/reports/{repSn}

### RESPONSE
```json
{ "repSn": 1, "repTyCode": "B001" }
```

## Report Master Update ✔️
### REQUEST
> PUT /api/v1/reports/{repSn}
```json
{ "sttusCode": "B301" }
```

### RESPONSE
```json
{ "updated": true, "repSn": 1 }
```

## Report Master Delete ✔️
### REQUEST
> DELETE /api/v1/reports/{repSn}

### RESPONSE
```json
{ "deleted": true, "repSn": 1 }
```

## Report Detail ✔️
### REQUEST
> GET /api/v1/report-details?repSn=1

### RESPONSE
```json
[
  { "repSn": 1, "sysCode": "B100" }
]
```

## Report Detail Paged ✔️
### REQUEST
> GET /api/v1/report-details/paged?pageIndex=1&recordCountPerPage=15

### RESPONSE
```json
{
  "resultList": [],
  "pagination": { "pageIndex": 1, "recordCountPerPage": 15, "totalCount": 0 }
}
```

## Report Detail Create ✔️
### REQUEST
> POST /api/v1/report-details
```json
{ "details": [{ "repSn": 1, "sysCode": "B100" }] }
```

### RESPONSE
```json
{ "created": true }
```

## Report Detail Update ✔️
### REQUEST
> PUT /api/v1/report-details
```json
{ "details": [{ "repSn": 1, "sysCode": "B100" }] }
```

### RESPONSE
```json
{ "updated": true }
```

## Report Detail Delete ✔️
### REQUEST
> DELETE /api/v1/report-details?repSn=1&sysCode=B100

### RESPONSE
```json
{ "deleted": true }
```

## Report Detail v2 ✔️
### REQUEST
> GET /api/v1/report-details/v2/last?repTyCode=B001&reportDt=2026-01-20

### RESPONSE
```json
{ "lastRepSn": 1 }
```

---

# Report Attendance ✔️ @backend

**필드 스키마**: RepAttdVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/report-attendance?attdDtDisplay=2026-01-20

### RESPONSE
```json
[
  { "userId": "api_test", "attdDtDisplay": "2026-01-20" }
]
```

## Upsert ✔️
### REQUEST
> POST /api/v1/report-attendance
```json
{ "userId": "api_test", "attdDtDisplay": "2026-01-20" }
```

### RESPONSE
```json
{ "updated": true }
```

## Delete ✔️
### REQUEST
> DELETE /api/v1/report-attendance?attdDtDisplay=2026-01-20&userId=api_test

### RESPONSE
```json
{ "deleted": true }
```

---

# Report Charger ✔️ @backend

**필드 스키마**: RepChargerVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/report-chargers?userId=api_test

### RESPONSE
```json
[
  { "userId": "api_test", "sysCode": "B100" }
]
```

## Assigned ✔️
### REQUEST
> GET /api/v1/report-chargers/assigned?userId=api_test

### RESPONSE
```json
[
  { "userId": "api_test", "sysCode": "B100" }
]
```

## Users ✔️
### REQUEST
> GET /api/v1/report-chargers/users?userLocat=L001&reportCharger=true

### RESPONSE
```json
[
  { "userId": "api_test", "userNm": "API Test User" }
]
```

## Update ✔️
### REQUEST
> PUT /api/v1/report-chargers/{userId}
```json
{ "sysCodes": ["B100"] }
```

### RESPONSE
```json
{ "updated": true, "userId": "api_test" }
```

---

# System Charger ✔️ @backend

**필드 스키마**: SysChargerVO (부록 참조)

## List ✔️
### REQUEST
> GET /api/v1/sys-chargers?userId=api_test

### RESPONSE
```json
[
  { "userId": "api_test", "sysCode": "A001" }
]
```

## Assigned ✔️
### REQUEST
> GET /api/v1/sys-chargers/assigned?userId=api_test

### RESPONSE
```json
[
  { "userId": "api_test", "sysCode": "A001" }
]
```

## Chargers ✔️
### REQUEST
> GET /api/v1/sys-chargers/chargers?sysCode=A001

### RESPONSE
```json
[
  { "userId": "api_test", "moblphon": "010-0000-0000" }
]
```

## Update ✔️
### REQUEST
> PUT /api/v1/sys-chargers/{userId}
```json
{ "sysCodes": ["A001"] }
```

### RESPONSE
```json
{ "updated": true, "userId": "api_test" }
```

---

# SMS ✔️ @backend

**필드 스키마**: SmsVO, TalkVO, TalkCodesVO (부록 참조)

## Send ✔️
### REQUEST
> POST /api/v1/sms/send
```json
{ "destel": "01012341234", "msg": "테스트 메시지" }
```

### RESPONSE
```json
{ "sent": true, "result": "OK" }
```

---

# History ✔️ @backend

**필드 스키마**: HistLoginVO, HistUseVO (부록 참조)

## Login History ✔️
### REQUEST
> POST /api/v1/hist/login
```json
{ "sessionId": "SESSION123" }
```

### RESPONSE
```json
{ "created": true, "sessionId": "SESSION123" }
```

## Logout History ✔️
### REQUEST
> PUT /api/v1/hist/login/logout
```json
{ "sessionId": "SESSION123", "logoutSttusCode": "A101" }
```

### RESPONSE
```json
{ "updated": true, "sessionId": "SESSION123" }
```

## Use History ✔️
### REQUEST
> POST /api/v1/hist/use
```json
{ "requestUri": "/api/v1/assets", "requestMethod": "GET" }
```

### RESPONSE
```json
{ "created": true, "sessionId": "SESSION123" }
```

---

<aside>
📌 문서 수정 규칙  
- 변경 사항은 해당 섹션에 🚩 표시  
- 담당자 변경 시 `@username` 업데이트
</aside>

## 부록: 전체 필드 스키마

BaseVO 필드는 BaseVO를 상속하는 모든 VO에 포함됩니다.

### BaseVO
- rowNum
- searchCondition
- searchKeyword
- searchUseYn
- pageIndex
- pageUnit
- pageSize
- firstIndex
- lastIndex
- recordCountPerPage
- saveToken
- creatDt
- creatId
- creatUserNm
- updtDt
- updtId
- updtUserNm
- deleteYn
- createDtDisplay
- updtDtDisplay
- decorator
- returnListMode

### AssetsVO
- assetsSn
- assetsNo
- assetsSe1
- assetsSe1Nm
- assetsSe2
- assetsSe2Nm
- purchsMthd
- maker
- productNm
- dlvgbiz
- assetQy
- qyUnit
- assetPrpos
- assetYn
- mntnceYn
- tchnlgySprt
- instlLc
- instlLcNm
- useYn
- indcDt
- indcDtDisplay
- indcUntpc
- indcAmount
- mntnceTariff
- mntnceAmount
- cnSdytrn
- assetsRm
- chargerNm
- chargerCttpc
- chargerEmail
- chargerClsf
- licenseAtchmnflId
- manualAtchmnflId

### AtchmnflVO
- atchmnflId
- atchmnflSn
- orginlFileNm
- streAllCours
- fileSize
- files
- multipartFile

### BsnsInfoVO
- infoLogoA
- infoLogoB
- infoBsnsName
- infoAgency
- infoBsnsPerson
- infoBsnsDepart
- infoOpCharger
- infoBsnsCharger
- infoBsnsManager
- infoBsnsPeriod

### CRRepAttachmentVO
- repSn
- userId
- requiredFile
- additionalFile

### CRepAttitudeVO
- userId
- attitudeCode
- attitudeDt

### CRepDetailVO
- repSn
- sysCode
- userId
- currentDescription
- planDescription

### CRepMasterVO
- repTyCode
- reportDt
- userId

### ChargerUserInfoVO
- userId
- userName
- position
- clsf

### CmmnCodeTyVO
- cmmnCodeTy
- cmmnCodeTyNm
- cmmnCodeTyDc

### CmmnCodeVO
- cmmnCode
- cmmnCodeNm
- cmmnCodeDc
- cmmnCodeSubNm1
- cmmnCodeSubNm2
- cmmnCodeSubNm3
- sortNo

### FuncImprvmVO
- conectSysYn
- conectSys
- conectSysNm
- fiCl
- fiClNm
- applyPlanDt
- applyPlanDtDisplay
- applyRDt
- applyRDtDisplay
- cnfrmYn
- noCnfrmResn
- fiRunSvrYn
- fiDevSvrYn
- fiDbYn
- fiVmYn
- fiEtcYn
- fiPlan
- rquireDfnYn
- rquireSpcYn
- rquireTrcYn
- pckgProgrmListYn
- uiDsignYn
- progrmDsignYn
- tableDsignYn
- progrmListYn
- userMnualYn
- admnMnualYn
- unitTestYn
- unionTestYn
- chngePlan
- backupPlan
- rstorePlan
- constrnt
- consder
- navigation
- fiRquire
- fiCn
- fiAtchmnflId
- asisAtchmnflId
- tobeAtchmnflId
- confirmUsr

### HistLoginVO
- sessionId
- loginDt
- loginDtDisplay
- logoutDt
- logoutDtDisplay
- logoutSttusCode

### HistUseVO
- logSn
- sessionId
- userId
- userTyCode
- useDt
- requestUri
- requestMethod
- title
- menuTitle
- subMenuTitle

### InfraOpertVO
- infraOpertNo
- infraOpertNo_sub
- infraPlanAtchmnflId
- infraResultAtchmnflId
- infraPlanEtcAtchmnflId
- infraResultEtcAtchmnflId

### ProgrmAccesAuthorVO
- progrmAccesAuthorCode
- progrmSns
- progrmList

### ProgrmVO
- progrmSn
- progrmNm
- progrmUri
- upperProgrmSn
- sortNo
- menuIndictYn
- progrmAccesAuthorVOList

### RepAssignVO
- assignCode
- assignName

### RepAttachmentNameAndSizeVO
- requiredFileId
- requiredFileName
- requiredFileSize
- additionalFileId
- additionalFileName
- additionalFileSize
- repSn
- userId

### RepAttdVO
- userId
- attdCode
- attdCodeNm
- userLocat
- userNm
- attdDt
- attdDtDisplay

### RepAttitudeVO
- attitudeCode
- attitudeName
- attitudePick

### RepChargerVO
- sysCode
- sysCodeNm
- sysCodeSubNm1
- userId
- userLocat
- userNm
- repChargerId
- sysCodes
- repChargerVOList
- reportCharger

### RepDescVO
- code
- currentDescription
- nextDescription

### RepDetailVO
- repSn
- sysCode
- sysCodeNm
- sysCodeSubNm1
- userId
- userNm
- execDesc
- planDesc
- dailyReportName

### RepDetailVO2
- assignCode
- assignName
- execDesc
- planDesc

### RepFormVO
- repDetailVO
- repMasterVO
- searchMasterVO
- searchDetailVO
- repDetailVOList
- registerFlag
- repSns
- sysCodes
- planDescs
- execDescs
- search

### RepFormVO2
- repSn
- date
- type
- currentAttitude
- planAttitude
- descriptions

### RepMasterVO
- repSn
- repNm
- repTyCode
- execAttdDj
- execNmDj
- planAttdDj
- planNmDj
- sttusCode
- sttusNm
- confirmUsr
- returnResn
- reportDt
- reportDtDisplay

### RepMasterVO2
- repName
- repTyCode
- reportDt

### SmsVO
- sendType
- memberSeq
- subject
- msg
- destel
- srctel

### SrvcRsponsVO
- userTyCode
- userId
- srvcRsponsNo
- requstDt
- requstDtDateDisplay
- requstDtTimeDisplay
- rqester1stNm
- rqester1stPsitn
- rqester1stCttpc
- rqester1stEmail
- rqesterId
- rqesterNm
- rqesterPsitn
- rqesterCttpc
- rqesterEmail
- trgetSrvcCode
- trgetSrvcCodeNm
- trgetSrvcDetailCode
- trgetSrvcDetailCodeNm
- trgetSrvcCodeSubNm1
- trgetSrvcCodeSubNm2
- trgetSrvcCodeSubNm3
- srvcRsponsSj
- srvcRsponsCn
- requstAtchmnflId
- rspons1stDt
- rspons1stDtDateDisplay
- rspons1stDtTimeDisplay
- processMt
- changeDfflyCode
- changeDfflyCodeNm
- srvcRsponsClCode
- srvcRsponsClCodeNm
- processStdrCode
- processStdrCodeNm
- processTerm
- srvcProcessDtls
- etc
- srvcRsponsBasisCode
- srvcRsponsBasisCodeNm
- rsponsAtchmnflId
- processDt
- processDtDateDisplay
- processDtTimeDisplay
- dataUpdtYn
- progrmUpdtYn
- stopInstlYn
- noneStopInstlYn
- instlYn
- infraOpertYn
- chargerId
- chargerUserNm
- cnfrmrId
- cnfrmrUserNm
- orderLevel
- priorLevel
- smsChk
- excludeprocessYn
- requstAtchmnflAt
- fnctImprvmNo
- wdtbCnfirmNo
- srvcWdtbDt
- srvcWdtbDtDateDisplay
- srvcWdtbDtTimeDisplay
- infraOpertNo
- reSrvcRsponsNo
- reRequestDt
- reRequestDtDateDisplay
- reRequestDtTimeDisplay
- verifyYn
- verifyDt
- verifyDtDateDisplay
- verifyDtTimeDisplay
- finishDt
- finishDtDateDisplay
- finishDtTimeDisplay
- verifyId
- verifyUserNm
- srvcVerifyDtls
- srvcFinDtls
- refIds
- srcRqesterId
- srcRqesterNm
- finishId
- finishUserNm
- reSrvcRsponsSj
- reSrvcRsponsCn

### SysChargerVO
- sysCode
- sysCodeNm
- sysCodeSubNm1
- sysCodes

### TalkCodesVO
- uri
- apiKey
- userId
- profileKey
- templateCode

### TalkVO
- uri
- apiKey
- userId
- profileKey
- templateCode
- receiver
- message

### UserVO
- userId
- userPassword
- userNm
- userTyCode2
- userTyCode
- userTyCodeNm
- userSttusCode
- userSttusCodeNm
- psitn
- clsf
- email
- moblphon
- acntReqstResn
- changePasswordYN
- conectIp
- comboName
- sysChargerVOList
- userLocat
- userSignature
- userSignatureFileName
- userSignatureFileSize
- deleteSignature

### WdtbVO
- wdtbCnfirmNo
- wdtbDt
- wdtbDtDateDisplay
- opertReason
- imprvmMatter
- wdtbSe
- wdtbIp
- wdtbNoOne
- wdtbNoTwo
- wdtbDtOne
- wdtbDtOneDateDisplay
- wdtbDtOneTimeDisplay
- wdtbDtTwo
- wdtbDtTwoDateDisplay
- wdtbDtTwoTimeDisplay
- wdtbCoOne
- wdtbCoTwo
- errorReasonOne
- errorReasonTwo
- wdtbEtc
- partclrMatter
- solutConectflId
- opertResultflId
- loginResultflId
- serverOneLogflId
- serverTwoLogflId
- wdtbNavigation
- confirmUsr
