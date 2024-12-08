


## 기능 - 회원  

### 3-1. 회원 가입

- 가입되지 않은 유저는 회원 가입을 할 수 있다.
- HttpMethod : post
- endpoint : /api/v1/member/signup
- 이메일이 중복되면 가입을 할 수 없다.
- 비밀번호는 PasswordEncoder를 통해 암호화되어 저장된다.
- request

```jsx
{
    "email" : "test",
    "password" : "zz",
    "name" : "건호김",
    "address" : "금정구",
    "addressDetail" : "수림로",
    "phoneNumber" : "01020492170"
}
```

- data jpa method에 mapping되는 SQL
    - 두 테이블에 insert를 하기 때문에 transaction 처리를 하였다.

```jsx
SELECT * FROM member WHERE email = '유저 입력' LIMIT 1;
만약 Optional의 값이 null이라면 (찾지 못했다면)

BEGIN;

INSERT INTO member_profile (id, name, address, address_detail, phone_number)
VALUES ($1, $2, $3, $4);

INSERT INTO member (id, member_profile_id, email, password, role, is_deleted, is_verify, created_at, updated_at)
VALUES ($5, temp_member_profile_id, $6, $7, $8, false, false, now(), now());

COMMIT;
```

- response → 200 ok 만약 존재하는 회원이라면 403 http confilct+ exception

### 2. 로그인

- 가입 된 유저의 이메일과 비밀번호를 통해, JWT access token을 발급받을 수 있다.
- HttpMethod : post
- endpoint : /api/login
- request

```jsx
{
	"email" : "test@test.com",
	"password" : "password"
}
```

- data jpa method에 mapping되는 SQL

```jsx
SELECT * FROM member WHERE email = 'email' and password = 'encoding(password)';
```

- response : 문자열 타입이다. 9번 index부터 끝까지 파싱하면 access token을 얻을 수 있다.

```jsx
access : eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BTk9OWU1PVVMiLCJpZCI6MSwidHlwZSI6IkFDQ0VTU19UT0tFTiIsImVtYWlsIjoidGVzdCIsInN1YiI6IjEiLCJpYXQiOjE3MzMxMzkxOTQsImV4cCI6MTczMzE0NzgzNH0.9wJonOV4R3pP0U7jEmLlUTTDcg9En0Hg74UtyM0BN38
```

---

### 3. 회원 정보 조회 (완료)

- 가입 된 회원은 정보를 조회할 수 있다.
- HttpMethod : GET
- endpoint : /api/v1/member
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
    - access token을 파싱하여 email을 얻을 수 있다.
- request : x
- data jpa method에 mapping되는 SQL

```jsx
SELECT m.*, mp.*
FROM member m
JOIN member_profile mp ON m.member_profile_id = mp.id
WHERE m.email = 'email';
```

- response

```jsx
{
    "email": "test",
    "role": "ROLE_ANONYMOUS",
    "isDeleted": false,
    "name": "건호김",
    "address": "금정구",
    "addressDetail": "수림로",
    "phoneNumber": "01020492170"
}
```

### 4. 회원 정보 수정

- 가입된 회원은 정보 수정을 할 수 있다.
- HttpMethod : PUT
- endpoint : /api/v1/member/edit
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- request : 만약 email 변경 안하고 싶으면 그냥 body에 넣지 않으면 된다.

```jsx
ex1)
{
	  "name": "건호김",
    "password" : "changepassword"
    "address": "금정구",
    "addressDetail": "수림로",
    "phoneNumber": "01020492170"
} <- 전부 다 바꾸는 경우

ex2) 비밀번호만 바꾸는 경우
{
	  "password": "change",
}
```

- data jpa method에 mapping되는 SQL
    - 두 테이블의 값을 동시에 업데이트 하기 때문에 transaction을 사용했다.

```jsx
BEGIN;

UPDATE member m
SET email = 'new zzzzzz', updated_at = now()
WHERE m.email = 'new';

UPDATE member_profile mp
SET address = 'new address 22222'
FROM member m
WHERE m.member_profile_id = mp.id

COMMIT;
```

---

### 회원 인증 시나리오

1. serverIP:8080/api/v1/member/try-verify로 요청을 보낸다.
2. 회원 가입된 이메일 기반으로 메일을 받는다..
3. 5분 안에 serverIP:8080/api/v1/email-code-verify에 body로 인증 코드 넣어서 전달한다.

### 5. 회원 인증시도

- 5분동안 인증 코드를 테이블에 저장한다.
    - 인증 코드는 5분의 만료 시간을 가진다.
    - 1시간에 한 번씩 @Scheduled를 통해 만료된 인증 코드를 삭제한다.
- HttpMethod : Post
- endpoint : /api/v1/member/try-verify
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 인증이 된 회원이라면 예외를 발생시킨다.
- data jpa method에 mapping되는 SQL
    - 데이터를 찾아오지 못 했으면(인증이 된 회원이라면) 예외를 발생시킨다.

```jsx
SELECT m.*, mp.*
FROM member m
JOIN member_profile mp
ON m.member_profile_id = mp.id
WHERE m.is_verify = false
LIMIT 1;

INSERT INTO email_verification (id, member_id, code, expired_at, created_at, updated_at, is_deleted)
VALUES (1, 1, 'ABC123', now() + INTERVAL '5 minutes', now(), now(), false);
```

- response : 200 ok 및 아래와 같은 인증 번호를 받을 수 있다.

![image](https://github.com/user-attachments/assets/ae016792-e5e6-497e-9117-5d9ecd3e5bf2)

### 6. 회원 코드 증명

- HttpMethod : Post
- endpoint : /api/v1/member/email-code-verify
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 인증이 된 회원이라면 예외를 발생시킨다.
- request

```jsx
{
	"code" : "288243" << 발급받은 여섯자리 문자열로
}
```

- data jpa method에 mapping되는 SQL

```jsx
SELECT *
FROM email_verification ev
WHERE ev.code = 'code'
LIMIT 1;

UPDATE member m
SET Role = 'ROLE_USER'
WHERE m.email = 'email';
```

- response : 200 ok
    - 만약 잘못되면 403 conflict exception

---

### 7. 회원 탈퇴

- 실제로 db에선 삭제되지 않고 논리적으로 삭제된다
- HttpMethod :  DELETE
- endpoint : /api/v1/member/remove
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- request : x
- data jpa method에 mapping되는 SQL

```jsx
UPDATE member m
SET is_deleted = true
WHERE m.email = 'email';
```

- response : isDeleted field가 true로 변경. 이후 검색에선 isDeleted = true이므로 검색을 허용하지 않는다.

```jsx
{
    "email": "amm0124@naver.com",
    "role": "ROLE_ANONYMOUS",
    "isDeleted": true,
    "name": "건호김",
    "address": "금정구",
    "addressDetail": "수림로",
    "phoneNumber": "01020492170"
}
```

###
