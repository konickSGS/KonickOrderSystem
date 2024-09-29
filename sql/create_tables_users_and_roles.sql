
CREATE TABLE roles (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	login VARCHAR(32) NOT NULL UNIQUE,
	hashed_password VARCHAR(255) NOT NULL,
	role_id INT NOT NULL DEFAULT 1,
	email VARCHAR(32) NOT NULL UNIQUE,
	address VARCHAR(32) NOT NULL,
	create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO roles (name) VALUE ('client');
INSERT INTO roles (name) VALUE ('manager');

INSERT INTO users (login, hashed_password, role_id, email, address) VALUE ('admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 2, 'admin@admin.ru', 'somewhere');