CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255)
);

CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    item_id BIGINT,
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE criterias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    rating INT,
    item_id BIGINT,
    FOREIGN KEY (item_id) REFERENCES items(id)
);
