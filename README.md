
## 기능 - 회원  

> JWT access token 방식으로 인증 및 인가를 진행한다. 
 
### 1. 회원 가입

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

## 기능 - 게시글 관련

> 게시글 본문 (posting) + 댓글 (comment) + 매칭(토너먼트, 매칭)으로 이루어져 있다.

### 1. 게시판 글 쓰기(자유/팁/공지 게시판) 

- HttpMethod : post
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- endpoint
    - 자유게시판  : /api/v1/posting/free
    - 팁게시판 : /api/v1/posting/tip
    - 공지 게시판 : /api/v1/posting/notice
- 권한
    - 자유, 팁 게시판 : ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
    - 공지 게시판 : ROLE_ADMIN, ROLE_MANAGER
- request

```jsx
{
    "title" : "titie",
    "game" : "LOL",
    "content" : "content"
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO posting (
    is_deleted, likes_count, created_at, id, member_id, updated_at, content, game, posting_type, title
) 
VALUES (
    'false',
    0,
    NOW(),
    DEFAULT, -- 프레임워크에서 자동으로 id를 생성해준다.
    123, -- 접속중인 member의 id
    NOW(),
    'content',
    'LOL',
    'FREE', --자유면 FREE, 팁이면 TIP, 공지면 NOTICE
    'title'
);
```

- response
    - 만약 권한이 맞지 않으면 403 Forbidden error를 받는다.

```jsx
{
    "postingResponse": {
        "postingId": 4,
        "title": "titie",
        "createdAt": "2024-12-03T14:48:35.7936511",
        "memberResponse": {
            "email": "test@email.com",
            "role": "ROLE_USER",
            "isDeleted": false,
            "name": "건호김",
            "address": "금정구",
            "addressDetail": "수림로",
            "phoneNumber": "xxxxxxx"
        },
        "content": "content",
        "postingType": "FREE"
    },
    "postingCommentResponseList": null,
    "matchingResponse": null
}
```

### 2. 매칭 게시판 글 쓰기

- 게임 매칭을 만들 때, 자기 자신을 포함시킨다.
- HttpMethod : POST
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- endpoint : /api/v1/posting/matching
- 권한  :  ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
- request

```jsx
{
    "title" : "게임 할 사람 구함~",
    "content" : "보드게임 고수 구해요",
    "game" : "보드게임", 
    "when" : "12월 4일 데이터베이스 시간",
    "place" : "컴공관 5층",
    "limit" : 100
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO posting (
    is_deleted, likes_count, created_at, id, member_id, updated_at, content, game, posting_type, title
) 
VALUES (
    'false',
    0,
    NOW(),
    DEFAULT, -- 프레임워크에서 자동으로 id를 생성해준다.
    123, -- 접속중인 member의 id
    NOW(),
    '보드게임 고수 구해요',
    '보드게임',
    'MATCHING', --자유면 FREE, 팁이면 TIP, 공지면 NOTICE
    '게임 할 사람 구함 ~'
)

INSERT INTO matching (
    capacity, is_deleted, is_full, now, created_at, id, posting_id, updated_at, event_time, place
) 
VALUES (
    100,         -- capacity
    'false',     -- is_deleted
    'false',     -- is_full
    NOW(),       -- now (현재 시간)
    NOW(),       -- created_at (현재 시간)
    DEFAULT,     -- 프레임워크에서 자동으로 id를 생성해준다.
    1,           -- posting_id (앞서 insert한 posting의 id)
    NOW(),       -- updated_at (현재 시간)
    '12월 4일 데이터베이스 시간', 
    '컴공관 5층'   
);

INSERT INTO matching_join (
    count, is_deleted, created_at, id, matching_id, member_id, updated_at
) 
VALUES (
    1,          -- count
    'false',     -- is_deleted
    NOW(),       -- created_at (현재 시간)
    DEFAULT,     -- 프레임워크에서 자동으로 id를 생성해준다.
    123,         -- matching_id (앞서 insert한 matching의 id)
    456,         -- 접속중인 member의 id
    NOW()        -- updated_at (현재 시간)
);
```

- response

```jsx
{
    "postingResponse": {
        "postingId": 1,
        "title": "게임 할 사람 구함~",
        "game": "보드게임",
        "createdAt": "2024-12-04T14:59:12.9846963",
        "memberResponse": {
            "email": "amm0124@naver.com",
            "role": "ROLE_ANONYMOUS",
            "isDeleted": false,
            "name": "건호김",
            "address": "금정구",
            "addressDetail": "수림로",
            "phoneNumber": "01020492170"
        },
        "content": "보드게임 고수 구해요",
        "postingType": "MATCHING"
    },
    "CommentResponseList": null,
    "matchingResponse": {
        "matchingId": 1,
        "postingId": 1,
        "eventTime": "12월 4일 데이터베이스 시간",
        "place": "컴공관 5층",
        "now": 1,
        "capacity": 100
    }
}
```

### 3. 토너먼트 게시판 글 쓰기 

- 게임 매칭을 만들 때, 자기 자신을 포함시키지 않는다.
- HttpMethod : POST
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- endpoint : /api/v1/posting/tournament
- 권한  :   ROLE_MANAGER
- 토너먼트 장소는 manager의 사업지를 사용한다.
- request

```jsx
{
    "title" : "xx pc방 게임 대회",
    "content" : "롤 대회 합니다 인원 100명",
    "game" : "롤", 
    "when" : "12월 4일 오전 11시",
    "limit" : 100
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO posting (
    is_deleted, likes_count, created_at, id, member_id, updated_at, content, game, posting_type, title
) 
VALUES (
    'false',
    0,
    NOW(),
    DEFAULT, -- 프레임워크에서 자동으로 id를 생성해준다.
    123, -- 접속중인 member의 id
    NOW(),
    'xx pc방 게임 대회',
    '롤',
    'TOURNAMENT', --자유면 FREE, 팁이면 TIP, 공지면 NOTICE
    '롤 대회 합니다 인원 100명'
)

INSERT INTO matching (
    capacity, is_deleted, is_full, now, created_at, id, posting_id, updated_at, event_time, place
) 
VALUES (
    100,         -- capacity
    'false',     -- is_deleted
    'false',     -- is_full
    NOW(),       -- now (현재 시간)
    NOW(),       -- created_at (현재 시간)
    DEFAULT,     -- 프레임워크에서 자동으로 id를 생성해준다.
    1,           -- posting_id (앞서 insert한 posting의 id)
    NOW(),       -- updated_at (현재 시간)
    '12월 4일 오전 11시', 
    '컴공관 5층'   
);
```

- response

```jsx
{
    "postingResponse": {
        "postingId": 2,
        "title": "xx pc방 게임 대회",
        "game": "롤",
        "createdAt": "2024-12-04T17:34:07.682276",
        "memberResponse": {
            "email": "amm0124@naver.com",
            "role": "ROLE_MANAGER",
            "isDeleted": false,
            "name": "건호김",
            "address": "금정구",
            "addressDetail": "수림로",
            "phoneNumber": "01020492170"
        },
        "content": "롤 대회 합니다 인원 100명",
        "postingType": "TOURNAMENT"
    },
    "CommentResponseList": null,
    "matchingResponse": {
        "matchingId": 1,
        "postingId": 2,
        "eventTime": "12월 4일 오전 11시",
        "place": "금정구 비비pc방",
        "now": 0,
        "capacity": 100,
        "matchingJoinResponseList": []
    }
}
```

### 4.  게시판 글 보기

- 권한
    - 자유/팁/공지 게시판은 인증이 되지 않은 유저도 볼 수 있다.
    - 매칭/토너먼트 게시판은 인증이 된 유저만 볼 수 있다.
        - 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
            - Authorization: Bearer <accesstoken>
        - ROLE_ADMIN, ROLE_USER, ROLE_MANAGER
- HttpMethod : GET
- endpoint
    - 자유게시판 : /api/v1/posting/free
    - 팁게시판 : /api/v1/posting/tip
    - 공지 게시판 : /api/v1/posting/notice
    - 매칭 게시판 : /api/v1/posting/matching
    - 토너먼트 게시판 : /api/v1/posting/tournament
- request : x
- data jpa method에 mapping되는 SQL

```jsx
SELECT
    p1.id,
    p1.content,
    p1.created_at,
    p1.game,
    p1.is_deleted,
    p1.likes_count,
    p1.member_id,
    p1.posting_type,
    p1.title,
    p1.updated_at
FROM
    posting p1
WHERE
    p1.is_deleted = false
    AND p1.posting_type = ? --찾고자 하는 게시판 type
ORDER BY
    p1.likes_count DESC;

-- 글 쓴 사람을 찾아오기 위한 쿼리

SELECT
    m.id,
    m.created_at,
    m.email,
    m.is_deleted,
    m.is_verify,
    mp.id AS member_profile_id,
    mp.address,
    mp.address_detail,
    mp.name,
    mp.phone_number,
    m.password,
    m.role,
    m.updated_at
FROM
    member m
LEFT JOIN
    member_profile mp ON mp.id = m.member_profile_id
WHERE
    m.id = ?;

-- 댓글의 깊이 순으로 정렬해서 select

SELECT
    c.id,
    c.content,
    c.created_at,
    c.depth,
    c.is_deleted,
    c.member_id,
    c.parent_comment_id,
    c.posting_id,
    c.updated_at
FROM
    comment c
WHERE
    c.posting_id = ?
ORDER BY
    c.depth;
    
    
-- 삭제되지 않은 매칭을 가져온다
SELECT
    m.id,
    m.capacity,
    m.created_at,
    m.event_time,
    m.is_deleted,
    m.is_full,
    m.now,
    m.place,
    m.posting_id,
    m.updated_at
FROM
    matching m
WHERE
    m.is_deleted = false
    AND m.posting_id = ?;

-- 삭제되지 않는 멤버를 가져온다
SELECT
    mj.id,
    mj.count,
    mj.created_at,
    mj.is_deleted,
    mj.matching_id,
    mj.member_id,
    mj.updated_at
FROM
    matching_join mj
WHERE
    mj.is_deleted = false
    AND mj.matching_id = ?;
```

- response

```jsx
[
    {
        "postingResponse": {
            "postingId": 1,
            "title": "게임 할 사람 구함~",
            "game": "보드게임",
            "createdAt": "2024-12-08T21:52:03.29021",
            "memberResponse": {
                "memberId": 4,
                "email": "zz",
                "role": "ROLE_ADMIN",
                "isDeleted": false,
                "name": '건호김',
                "address": '금정구',
                "addressDetail": '수림로',
                "phoneNumber": '01012345678'
            },
            "content": "보드게임 고수 구해요",
            "postingType": "MATCHING",
            "LikesCount": 0
        },
        "CommentResponseList": [],
        "matchingResponse": {
            "matchingId": 1,
            "postingId": 1,
            "eventTime": "12월 4일 데이터베이스 시간",
            "place": "컴공관 5층",
            "now": 1,
            "capacity": 100,
            "matchingJoinResponseList": [
                {
                    "matchingJoinId": 1,
                    "memberId": 4,
                    "memberName": null,
                    "count": 1
                }
            ]
        }
    }
]
```

### 5. 팁/자유/공지/매칭/토너먼트 게시글 삭제 

- is_deleted column을 true로 둠으로 논리적 삭제를 할 수 있다.
- 이후 삭제된 글은 조회할 수 없다.
- HttpMethod : DELETE
- token 필요
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : 자신의 글만 삭제 할 수 있다.
    - 팁/자유/공지 게시판 : ROLE_ADMIN, ROLE_USER, ROLE_MANAGER
    - 매칭/토너먼트 게시판 : ROLE_ADMIN, ROLE_MANAGER
- data jpa method에 mapping되는 SQL

```jsx
UPDATE posting p
SET is_deleted = true
WHERE p.id = posting_id; --유저 입력
```

- request

```jsx
{
    "postingId" : "4"
}
```

- resposne : 204 No Content

### 6. 팁/자유/공지/매칭/토너먼트 게시판 내용 업데이트

- HttpMethod : PUT
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : 자신의 글만 업데이트 할 수 있다.
    - 팁/자유/공지 게시판 : ROLE_ADMIN, ROLE_USER, ROLE_MANAGER
    - 매칭/토너먼트 게시판 : ROLE_ADMIN, ROLE_MANAGER
- request

```jsx
{
    "postingId" : 2,
    "title" : "updatex2",
    "game" : "LOL update x2",
    "content" : "update contentx2"
}
```

- data jpa method에 mapping되는 SQL

```jsx
UPDATE posting p
SET 
    title = 'updatex2',
    game = 'LOL update x2',
    content = 'update contentx2',
    updated_at = NOW()  
WHERE posting_id = 2;
```

- response

```jsx
[
    {
        "postingResponse": {
            "postingId": 2,
            "title": "updatex2"
            "game": "LOL update x2",
            "createdAt": "2024-12-08T21:52:03.29021",
            "memberResponse": {
                "memberId": 4,
                "email": "zz",
                "role": "ROLE_ADMIN",
                "isDeleted": false,
                "name": '건호김',
                "address": '금정구',
                "addressDetail": '수림로',
                "phoneNumber": '01012345678'
            },
            "content": "update contentx2",
            "postingType": "MATCHING",
            "LikesCount": 0
        },
        "CommentResponseList": [],
        "matchingResponse": {
            "matchingId": 1,
            "postingId": 1,
            "eventTime": "12월 4일 데이터베이스 시간",
            "place": "컴공관 5층",
            "now": 1,
            "capacity": 100,
            "matchingJoinResponseList": [
                {
                    "matchingJoinId": 1,
                    "memberId": 4,
                    "memberName": null,
                    "count": 1
                }
            ]
        }
    }
]
```

### 7. 매칭 게시판/토너먼트 게시판의 매칭 정보 수정 

- HttpMethod : PUT
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : 자신의 글만 업데이트 할 수 있다.
    - 팁/자유/공지 게시판 : ROLE_ADMIN, ROLE_USER, ROLE_MANAGER
    - 매칭/토너먼트 게시판 : ROLE_ADMIN, ROLE_MANAGER
- endpoint
    - 매칭 : /api/v1/posting/matching
    - 토너먼트 : /api/v1/posting/tournament
- request
    - 현재 등록된 인원보다 더 적게 최대 수용 인원(capacity)를 변경하려 하면 exception 발생

```jsx
{
    "matchingId" : 2,
    "eventTime" : "12월 4일 데이터베이스 시간 - update",
    "place" : "컴공관 5층 - update",
    "capacity" : 10000
}
```

- response

```jsx
"matchingResponse": {
        "matchingId": 2,
        "postingId": 1,
        "eventTime": "12월 4일 데이터베이스 시간 - update",
        "place": "컴공관 5층 - update",
        "now": 1,
        "capacity": 10000
    }
```

- 예시
    - 아래 상태일 때 (최대 수용 인원이 13232명)
    
![image](https://github.com/user-attachments/assets/9c831f92-c861-467b-a700-cc29db880081)

- 현재 인원보다 더 적은 인원으로 업데이트 요청을 보내면 아래와 같은 error를 response로 받는다.

![image](https://github.com/user-attachments/assets/fff7cb37-f5fa-4eab-8404-ec337fed8e4a)



## 기능 - 댓글 관련

> 게시글에 댓글을 달 수 있다. 또한 댓글에 대댓글을 중첩해서 계속 달 수 있다. 댓글 삭제 시, 내용은 '삭제된 댓글입니다'로 변경한다. 사람은 '삭제`로 변경된다.
> 회원 탈퇴 시, 댓글 내용과 사람 또한 '삭제된 내용입니다' 및 '삭제'로 변경된다.

### 1. 댓글 달기

- HttpMethod : Post
- endpoint : /api/v1/comment
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
- request

```jsx
{
    "postingId" : 1,
    "content" : "댓글 comment1"
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO comment (
    depth,
    is_deleted,
    created_at,
    id,
    member_id,
    parent_comment_id,
    posting_id,
    updated_at,
    content
)
VALUES (
    1,   
    false,  
    NOW(),
    DEFAULT, --프레임워크에서 계산한다.
    null,  -- 로그인 한 회원의 id
    ?,  -- 만약 null이라면 첫 번째 댓글이다. 
    1, -- 유저 입력  
    NOW(), 
    '댓글 comment1'
);

```

- response :

```jsx
{
    "commentId": 1,
    "postingId": 1,
    "memberId": 1,
    "parentCommentId": null,
    "commentContent": "comment1",
    "isDeleted": false,
    "memberName": "건호김",
    "replyList": []
}
```

### 2. 대댓글 달기

- HttpMethod : Post
- endpoint : /api/v1/comment/reply
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
- request

```jsx
{
    "parentCommentId" : 1,
    "content" : "대댓글 comment"
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO comment (
    depth,
    is_deleted,
    created_at,
    id,
    member_id,
    parent_comment_id,
    posting_id,
    updated_at,
    content
)
VALUES (
    1,   
    false,  
    NOW(),
    DEFAULT, --프레임워크에서 계산한다.
    null,  -- 로그인 한 회원의 id
    1,  
    1, -- 유저 입력  
    NOW(), 
    '대댓글 comment'
);

```

- response

```jsx
[
	{
	    "commentId": 1,
	    "postingId": 1,
	    "memberId": 1,
	    "parentCommentId": null,
	    "commentContent": "comment1",
	    "isDeleted": false,
	    "memberName": "건호김",
	    "replyList": [
			    {
					    "commentId": 4,
					    "postingId": 1,
					    "memberId": 1,
					    "parentCommentId": 1,
					    "commentContent": "대댓글 comment",
					    "isDeleted": false,
					    "memberName": "건호김",
					    "replyList": []
						}
	    ]
	}
]
```

### 3. 댓글 수정

- HttpMethod : PUT
- endpoint : /api/v1/comment/edit
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
- 자신의 댓글만 수정 가능하다
- request

```jsx
{
    "commentId" : "1",
    "content" : "수정사항입니다"
}
```

- data jpa method에 mapping되는 SQL

```jsx
UPDATE comment
SET 
		updated_at = NOW(),  -- updated_at을 현재 시간으로 갱신
    content = '수정사항입니다'         -- 새로운 content 값
WHERE
    parent_comment_id = 1;              -- 수정할 댓글의 ID
```

- response

```jsx
[
    {
        "commentId": 1,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "수정사항입니다",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": [
            {
                "commentId": 6,
                "postingId": 1,
                "memberId": 1,
                "parentCommentId": 1,
                "commentContent": "대댓글 comment",
                "isDeleted": false,
                "memberName": "건호김",
                "replyList": []
            },
            {
                "commentId": 7,
                "postingId": 1,
                "memberId": 1,
                "parentCommentId": 1,
                "commentContent": "대댓글 comment",
                "isDeleted": false,
                "memberName": "건호김",
                "replyList": []
            }
        ]
    },
    {
        "commentId": 2,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    },
    {
        "commentId": 3,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    },
    {
        "commentId": 4,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    },
    {
        "commentId": 5,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    }
]
```

### 4. 댓글 삭제 (완)

- HttpMethod : DELETE
- endpoint : /api/v1/comment/remove
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_ADMIN, ROLE_MANAGER, ROLE_USER
- 자신의 댓글만 수정 가능하다
- request

```jsx
{
	"commentId" : 1
}
```

- data jpa method에 mapping되는 SQL

```jsx
UPDATE comment
SET 
		is_deleted = true;
WHERE
    comment_id = 1;              -- 수정할 댓글의 ID
```

- response :  게시글에 해당하는 전체 댓글

```jsx
[
    {
        "commentId": 1,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "삭제된 댓글입니다.",
        "isDeleted": true,
        "memberName": "삭제",
        "replyList": [
            {
                "commentId": 6,
                "postingId": 1,
                "memberId": 1,
                "parentCommentId": 1,
                "commentContent": "대댓글 comment",
                "isDeleted": false,
                "memberName": "건호김",
                "replyList": []
            },
            {
                "commentId": 7,
                "postingId": 1,
                "memberId": 1,
                "parentCommentId": 1,
                "commentContent": "대댓글 comment",
                "isDeleted": false,
                "memberName": "건호김",
                "replyList": []
            }
        ]
    },
    {
        "commentId": 2,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "삭제된 댓글입니다.",
        "isDeleted": true,
        "memberName": "삭제",
        "replyList": []
    },
    {
        "commentId": 3,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    },
    {
        "commentId": 4,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    },
    {
        "commentId": 5,
        "postingId": 1,
        "memberId": 1,
        "parentCommentId": null,
        "commentContent": "comment1",
        "isDeleted": false,
        "memberName": "건호김",
        "replyList": []
    }
]
```

## 기능 - 매니저 시설 등록

> 매니저는 자신의 영업지를 등록 할 수 있다. 토너먼트 개최 시, 매니저의 영업지를 설정한다.

### 1. 시설 등록 api

- HttpMethod : POST
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- endpoint : /api/v1/facilities/register
- 권한 : ROLE_MANAGER
- request

```jsx
{
		"storeName" : "금정구 비비pc방",
		"address" : "금정구",
		"addressDetail" : "금정구 상세주소 어쩌고"
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO facilities(
    is_deleted,
    created_at,
    id,
    member_id,
    updated_at,
    address,
    address_detail,
    phone,
    store_name
)
VALUES (
    'false',    -- is_deleted
    NOW(),      -- created_at (현재 시간)
    DEFAULT,    -- id (자동 증가 컬럼일 경우 DEFAULT 사용)
    1,        -- 현재 접속된 유저
    NOW(),      -- updated_at (현재 시간)
    '금정구 어쩌고',  -- address (예시로 주소)
    '금정구 상세주소',     -- address_detail (예시로 상세 주소)
    '555-1234',    -- phone (예시로 전화번호)
    '금정구 비비pc방'     -- store_name (예시로 상호명)
);
```

- response

```jsx
{
		"facilitiesId": 1, 
    "storeName": "금정구 비비pc방",
    "address": "금정구 어쩌고",
    "addressDetail": "금정구 상세주소 어쩌고",
    "phone": "01020492170"
}
```

### 2. 시설 조회 api

- HttpMethod : GET
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_MANAGER
- endpoint : /api/v1/facilities
- request 없음 : x
- data jpa method에 mapping되는 SQL

```jsx
SELECT * 
FROM facilities
WHERE member_id = 접속된 회원 id;
```

- response

```jsx
{
		"facilitiesId": 1, 
    "storeName": "금정구 비비pc방",
    "address": "금정구 어쩌고",
    "addressDetail": "금정구 상세주소 어쩌고",
    "phone": "01020492170"
}
```

### 3. 시설 수정 api

- HttpMethod : PUT
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_MANAGER
- endpoint : /api/v1/facilities/edit
- request

```jsx
{
	"storeName" : "금정구 수정",
	"address" : "금정구 수정",
	"addressDetail" : "금정수정 어쩌고"
}
```

- data jpa method에 mapping되는 SQL

```jsx
UPDATE table_name
SET
    store_name = '금정구 수정',
    address = '금정구 수정',
    address_detail = '금정수정 어쩌고',
    updated_at = NOW()  -- 업데이트된 시간 기록
WHERE member_id = 접속된 회원 id;
```

- response

```jsx
{
    "facilitiesId": 1,
    "storeName": "금정구 수정",
    "address": "금정구 수정",
    "addressDetail": "금정수정 어쩌고",
    "phone": "01020492170"
}
```


## 기능 - 매칭/토너먼트(대회) 참여

> 매칭은 기본적으로 1명으로 등록된다. 또한 한 번 등록한 매칭/토너먼트는 다시 등록 할 수 없다. 또한 최대 인원을 넘어서는 등록 할 수 없다.
> 예외적인 요청을 할 시, 아래와 같은 예외가 발생한다.
> ![image](https://github.com/user-attachments/assets/625f5a65-6bca-4f90-a84b-611a82976115)

### 1. 게임 매칭 참여

- 게임 매칭은 자동으로 1명으로 등록된다.
- HttpMethod : POST
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- endpoint : /api/v1/facilities/register
- 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
- endpoint : /api/v1/join
- request

```jsx
{
    "matchingId" : 1
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO matching_join(
    count, 
    is_deleted, 
    created_at, 
    id, 
    matching_id, 
    member_id, 
    updated_at
) 
VALUES (
    1,         -- 기본값 1
    'false',    -- is_deleted (예시로 false)
    NOW(),      -- created_at (현재 시간)
    DEFAULT,    -- id (자동 증가 컬럼일 경우 DEFAULT 사용)
    1,        -- matching_id 
    2,        -- 현재 접속된 member의 id
    NOW()       -- updated_at (현재 시간)
);
```

- response

```jsx
[
    {
		    "matchingJoinId" : 1,
        "memberId": 1,
        "memberName": "건호김",
        "count": 1
    },
    {
		    "matchingJoinId" : 2,
        "memberId": 2,
        "memberName": "건호박",
        "count": 1
    }
]
```

- 만약 참여 한 매칭에 다시 참여하려 하면 아래와 같은 error 발생.

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/a3437d6e-ded7-452e-b098-e1c53c9cd606/00326f73-2111-4716-b1b1-d31ed16c598d/image.png)

- 게임 매칭 하고 난 후 매칭 게시글 response
    - matchingJoinResponseList에 매칭 한 사람의 정보와 수가 들어있다.
    - 매칭은 기본적으로 자기 자신. 즉, 한 명만 가능하다.

```jsx
{
    "postingResponse": {
        "postingId": 1,
        "title": "게임 할 사람 구함~",
        "game": "보드게임",
        "createdAt": "2024-12-04T17:08:27.9493952",
        "memberResponse": {
            "email": "amm0124@naver.com",
            "role": "ROLE_ANONYMOUS",
            "isDeleted": false,
            "name": "건호김",
            "address": "금정구",
            "addressDetail": "수림로",
            "phoneNumber": "01020492170"
        },
        "content": "보드게임 고수 구해요",
        "postingType": "MATCHING"
    },
    "CommentResponseList": null,
    "matchingResponse": {
        "matchingId": 1,
        "postingId": 1,
        "eventTime": "12월 4일 데이터베이스 시간",
        "place": "컴공관 5층",
        "now": 1,
        "capacity": 100,
        "matchingJoinResponseList": [
            {
		            "matchingJoinId" : 1,
                "memberId": 1,
                "memberName": "건호김",
                "count": 1
            }
        ]
    }
}
```

### 2. 토너먼트 참여

- 토너먼트는 여러 명 참여 가능하다. 하지만 기본 등록(capacity) 넘어가면 등록할 수 없다
- endpoint : /api/v1/join
- HttpMethod : POST
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
- request

```jsx
{
    "matchingId" : 1,
    "count" : 4
}
```

- data jpa method에 mapping되는 SQL

```jsx
INSERT INTO matching_join(
    count, 
    is_deleted, 
    created_at, 
    id, 
    matching_id, 
    member_id, 
    updated_at
) 
VALUES (
    4,         -- 유저 입력값
    'false',    -- is_deleted (예시로 false)
    NOW(),      -- created_at (현재 시간)
    DEFAULT,    -- id (자동 증가 컬럼일 경우 DEFAULT 사용)
    1,        -- matching_id 
    2,        -- 현재 접속된 member의 id
    NOW()       -- updated_at (현재 시간)
);
```

- response

```jsx
[
    {
		    "matchingJoinId" : 3,
        "memberId": 1,
        "memberName": "건호김",
        "count": 4
    } 
]
```

### 3. 게임 매칭 취소

- HttpMethod : PUT
- endpoint : /api/v1/join/cancel
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
- 자신의 게임 매칭만 취소할 수 있다.
- request

```jsx
{
		"matchingJoinId" : 2
}
```

- data jpa method에 해당하는 SQL

```jsx
UPDATE matching_join
SET is_deleted = 'true',  -- is_deleted 값을 true로 설정
    updated_at = NOW() -- 업데이트된 시간 기록
WHERE matching_join_id = 2  -- 예시로 matching_id가 1인 경우
```

- response

```jsx
[
    {
		    "matchingJoinId" : 3,
        "memberId": 1,
        "memberName": "건호김",
        "count": 4
    } 
]
```

### 4. 토너먼트 매칭 사람 수 변경

- 취소하고 싶으면, 변경하고자 하는 인원을 0으로 입력한다.
- HttpMethod : PUT
- endpoint : /api/v1/join/tournament/edit
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
- 자신의 게임 매칭만 취소할 수 있다.
- request

```jsx
{
		"matchingJoinId" : 2,
		"count" : 6
}
```

- data jpa method에 해당하는 SQL
    - 만약 0으로 입력 시, 게임 매칭 취소와 동일한 로직이 수행된다.

```jsx
UPDATE matching_join
SET count = 6,        -- 유저가 입력한 새로운 count 값
    updated_at = NOW() -- 업데이트된 시간 기록
WHERE matching_join_id = 2  
```

- response

```jsx
[
    {
		    "matchingJoinId" : 2,
        "memberId": 1,
        "memberName": "건호김",
        "count": 6
    } 
]
```


## 기능 - 게시글 좋아요 및 싫어요

> 게시글을 조회할 때, 작성된 좋아요를 기준으로 정렬된다.
> 또한, 하나의 게시글당 한 명의 회원은 좋아요 및 싫어요 기능은 한 번만 이용하도록 구현하였다.

 ### 1. 좋아요/ 싫어요 누르기

- HttpMethod : POST
- endpoint
    - 좋아요 : /api/v1/likes/add
    - 싫어요 : /api/v1/likes/subtract
- 좋아요나 싫어요 누른 사람은 다시 누를 수 없다
- 로그인 후 얻은 access token을 아래와 같은 형식으로 header에 넣어 주어야 한다.
    - Authorization: Bearer <accesstoken>
- 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
- request

```jsx
{
		"postingId" : 1
}
```

- data jpa method에 mapping되는 SQL

```jsx
BEGIN;

INSERT INTO likes (
    is_deleted, 
    created_at, 
    id, 
    member_id, 
    posting_id, 
    updated_at, 
    likes_type
) 
VALUES (
    'false',     -- is_deleted (예시로 false)
    NOW(),       -- created_at (현재 시간)
    DEFAULT,     -- id (자동 증가 컬럼일 경우 DEFAULT 사용)
    1,           -- member_id (예시로 1)
    1,           -- posting_id 
    NOW(),       -- updated_at (현재 시간)
    'LIKE'       -- likes_type (만약 싫어요면 DISLIKE)
);

UPDATE likes
SET likes_count = likes_count + 1,  -- 좋아요는 +1, 싫어요는 -1
    updated_at = NOW()              -- updated_at을 현재 시간으로 업데이트
WHERE posting_id = 1;               -- 원하는 posting_id 값을 조건으로 설정

COMMIT;

```

- response

```jsx
{
    "postingResponse": {
        "postingId": 1,
        "title": "첫 번째 자유게시판 글",
        "game": "롤",
        "createdAt": "2024-12-05T23:08:54.244754",
        "memberResponse": {
            "memberId": 1,
            "email": "amm0124@naver.com",
            "role": "ROLE_ANONYMOUS",
            "isDeleted": false,
            "name": "김건호1",
            "address": "금정구",
            "addressDetail": "수림로",
            "phoneNumber": "01020492170"
        },
        "content": "1 글",
        "postingType": "FREE",
        "LikesCount": 1
    },
    "CommentResponseList": [],
    "matchingResponse": null
}
```
