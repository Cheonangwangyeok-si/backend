-- 테스트 데이터 삽입
INSERT INTO account (account_id, login_id, name, role, provider)
VALUES (1, 'john_doe', 'John Doe', 'USER', 'local'),
       (2, 'jane_smith', 'Jane Smith', 'USER', 'local'),
       (3, 'bob_johnson', 'Bob Johnson', 'USER', 'google'),
       (4, 'alice_williams', 'Alice Williams', 'USER', 'facebook');

-- refresh_token 테이블에 새로운 토큰을 추가
INSERT INTO refresh_token (token_id, account_id, token)
VALUES (1, 1, 'john_doe'),
       (2, 2, 'jane_smith'),
       (3, 3, 'bob_johnson'),
       (4, 4, 'alice_williams');
