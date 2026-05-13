CREATE TABLE IF NOT EXISTS nodes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    parent_id BIGINT,
    CONSTRAINT fk_nodes_parent
        FOREIGN KEY (parent_id)
        REFERENCES nodes(id)
        ON DELETE CASCADE
);