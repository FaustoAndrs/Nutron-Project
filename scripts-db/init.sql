-- init.sql
/*USE auth_db;*z/

-- Asegúrate de que la tabla exista antes de insertar si tu Hibernate/JPA no la crea primero,
-- o simplemente confía en las sentencias de inserción si mapeas bien el orden.

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


-- Inserta tus usuarios de prueba
-- NOTA: Si usas Spring Security, la contraseña debería estar ya encriptada (BCrypt)
INSERT INTO auth_db.users (id, email, full_name, password, user_name)
VALUES ('d0398b43-14f5-43ec-962b-1bf5998d2c01', 'usu01@mail.com',	'Usuario De Prueba',	'$2a$10$Ovg3hiORKMAyL0RJeeUATOxvbSFGrtnaoeYQY4sBeh3f3JO6CKSBu',	'UsuPrueba01');

INSERT INTO users_settings (user_id, activity, age, diet, formula, gender, goal, height, weight) 
VALUES ('d0398b43-14f5-43ec-962b-1bf5998d2c01', 'MODERATE', '54', 'standard', 'Harris-Benedict', 'Hombre', 'GAIN_MUSCLE', '194', '83'
)
*/