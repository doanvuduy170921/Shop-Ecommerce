CREATE TABLE ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review TEXT,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO ratings (user_id, product_id, rating, review, created_at) VALUES
    (1, 1, 5, 'Great product!', '2021-01-01 12:00:00'),
    (1, 2, 4, 'Good product!', '2021-01-02 12:00:00'),
    (2, 1, 3, 'Average product!', '2021-01-03 12:00:00'),
    (2, 2, 2, 'Not bad!', '2021-01-04 12:00:00'),
    (3, 1, 1, 'Terrible product!', '2021-01-05 12:00:00'),
    (3, 2, 5, 'Excellent product!', '2021-01-06 12:00:00');