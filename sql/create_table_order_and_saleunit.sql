CREATE TABLE order_and_saleunit (
	order_id INT NOT NULL,
	saleunit_id INT NOT NULL,
    counts INT NOT NULL DEFAULT 1,
    price INT,

    UNIQUE KEY (order_id, saleunit_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (saleunit_id) REFERENCES saleunits(id) ON DELETE CASCADE ON UPDATE CASCADE
);