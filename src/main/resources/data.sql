INSERT INTO users (created_at, updated_at, email, nickname, password, role)
VALUES
    (NOW(), NOW(), 'admin1@example.com', 'AdminOne', '$2a$04$gCuz1q96Ogg5fIT07PS0PeTfYmxoZT.FUmsmJc8OkStnAMx.zSRzO', 'ADMIN'),
    (NOW(), NOW(), 'admin2@example.com', 'AdminTwo', '$2a$04$gCuz1q96Ogg5fIT07PS0PeTfYmxoZT.FUmsmJc8OkStnAMx.zSRzO', 'ADMIN'),
    (NOW(), NOW(), 'user1@example.com', 'UserOne', '$2a$04$gCuz1q96Ogg5fIT07PS0PeTfYmxoZT.FUmsmJc8OkStnAMx.zSRzO', 'USER'),
    (NOW(), NOW(), 'user2@example.com', 'UserTwo', '$2a$04$gCuz1q96Ogg5fIT07PS0PeTfYmxoZT.FUmsmJc8OkStnAMx.zSRzO', 'USER');