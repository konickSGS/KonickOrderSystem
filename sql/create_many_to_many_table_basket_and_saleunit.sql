DROP TABLE IF EXISTS basket_and_saleunit;

CREATE TABLE basket_and_saleunit (
	user_id INT NOT NULL,
    saleunit_id INT NOT NULL UNIQUE,
    count INT NOT NULL DEFAULT 1,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (saleunit_id) REFERENCES saleunits(id) ON DELETE CASCADE ON UPDATE CASCADE
);