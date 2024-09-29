DROP TABLE IF EXISTS order_and_saleunit;

CREATE TABLE order_and_saleunit (
	order_id INT NOT NULL,
	saleunit_id INT NOT NULL,
    count INT NOT NULL DEFAULT 1,

    UNIQUE KEY (order_id, saleunit_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (saleunit_id) REFERENCES saleunits(id) ON DELETE CASCADE ON UPDATE CASCADE
);