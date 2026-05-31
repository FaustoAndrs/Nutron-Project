-- init.sql
USE auth_db;

CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_email` (`email`),
  UNIQUE KEY `uk_users_username` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
SELECT * FROM auth_db.users;

CREATE TABLE `users_settings` (
  `user_id` varchar(255) NOT NULL,
  `activity` varchar(255) NOT NULL,
  `age` varchar(255) NOT NULL,
  `diet` varchar(255) DEFAULT NULL,
  `formula` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `goal` varchar(255) NOT NULL,
  `height` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_settings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Usuarios de prueba
INSERT INTO auth_db.users (id, email, full_name, password, user_name)
VALUES
('08fb6649-7978-4860-8e17-82e6a3b3efa2', 'usu01@mail.com', 'usuario cero uno', '$2a$10$cua.pD95aV6NbMFdFV20.O4qdT0hxk16HqrAKh4zRb81W2O5TKhwW', 'usuario 1'),
('477d87e6-a7d5-47cc-b6b7-af8776ef6e18', 'usu02@mail.com', 'usuario cero dos', '$2a$10$5PwW/DtoP/OZZg0LqQWtluCeinyyEr3PhZ6Id/GIfOOCSzfSmCzfm', 'usuario 2');


INSERT INTO users_settings (user_id, activity, age, diet, formula, gender, goal, height, weight) 
VALUES
('08fb6649-7978-4860-8e17-82e6a3b3efa2', 'LOW', '25', 'standard', 'Harris-Benedict', 'MAN', 'MAINTAIN', '180', '75'),
('477d87e6-a7d5-47cc-b6b7-af8776ef6e18', 'LOW', '39', 'standard', 'Harris-Benedict', 'MAN', 'MAINTAIN', '199', '89');


