
CREATE TABLE status (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE orders (
	id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    status_id INT NOT NULL DEFAULT 1,
    total INT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES status(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO status (name) VALUE ('new');
INSERT INTO status (name) VALUE ('approved');
INSERT INTO status (name) VALUE ('in work');
INSERT INTO status (name) VALUE ('delivering');
INSERT INTO status (name) VALUE ('received');