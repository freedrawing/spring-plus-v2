-- PasswordEncoder.encode("123") -> $2a$04$gCuz1q96Ogg5fIT07PS0PeTfYmxoZT.FUmsmJc8OkStnAMx.zSRzO
INSERT INTO users (created_at, updated_at, email, nickname, password, role)
VALUES
    (NOW(), NOW(), 'user1@example.com', 'UserOne', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'USER'),
    (NOW(), NOW(), 'user2@example.com', 'UserTwo', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'USER'),
    (NOW(), NOW(), 'manager1@example.com', 'ManagerOne', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'MANAGER'),
    (NOW(), NOW(), 'manager2@example.com', 'ManagerTwo', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'MANAGER'),
    (NOW(), NOW(), 'admin1@example.com', 'AdminOne', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'ADMIN'),
    (NOW(), NOW(), 'admin2@example.com', 'AdminTwo', '$2a$10$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO', 'ADMIN');

INSERT INTO todo (created_at, updated_at, user_id, content, title, weather, id)
VALUES
    -- UserOne's todos (user_id: 1)
    (NOW(), NOW(), 1, '개인 프로젝트 진행하기', '프로젝트 진행', '맑음', 1),
    (NOW(), NOW(), 1, '새로운 고객 미팅 준비', '미팅 준비', '비', 2),
    (NOW(), NOW(), 1, '주간 업무 계획 수립', '업무 계획', '흐림', 3),
    (NOW(), NOW(), 1, '이메일 응답 및 정리', '이메일 응답', '맑음', 4),
    (NOW(), NOW(), 1, '팀 목표 리뷰 작성', '목표 리뷰', '안개', 5),

    -- UserTwo's todos (user_id: 2)
    (NOW(), NOW(), 2, '업무 일지 작성', '업무 일지', '흐림', 6),
    (NOW(), NOW(), 2, '팀원 피드백 제공', '피드백 제공', '맑음', 7),
    (NOW(), NOW(), 2, '주간 미팅 준비', '미팅 준비', '비', 8),
    (NOW(), NOW(), 2, '고객 문의 정리', '문의 정리', '맑음', 9),
    (NOW(), NOW(), 2, '데이터 분석 보고서 작성', '분석 보고서', '안개', 10),

    -- ManagerOne's todos (user_id: 3)
    (NOW(), NOW(), 3, '팀 회의 자료 준비', '회의 준비', '안개', 11),
    (NOW(), NOW(), 3, '프로젝트 일정 조정', '일정 조정', '맑음', 12),
    (NOW(), NOW(), 3, '팀 목표 설정', '목표 설정', '비', 13),
    (NOW(), NOW(), 3, '부서 업무 리뷰', '업무 리뷰', '흐림', 14),
    (NOW(), NOW(), 3, '보고서 작성', '보고서 작성', '맑음', 15),

    -- ManagerTwo's todos (user_id: 4)
    (NOW(), NOW(), 4, '고객 데이터 분석', '데이터 분석', '맑음', 16),
    (NOW(), NOW(), 4, '팀 미팅 준비', '미팅 준비', '비', 17),
    (NOW(), NOW(), 4, '프로젝트 보고서 작성', '보고서 작성', '흐림', 18),
    (NOW(), NOW(), 4, '업무 일정 계획', '일정 계획', '맑음', 19),
    (NOW(), NOW(), 4, '팀 목표 검토', '목표 검토', '안개', 20);