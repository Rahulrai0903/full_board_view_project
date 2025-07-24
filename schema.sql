
-- board.sql

CREATE TABLE board (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE column (
    id SERIAL PRIMARY KEY,
    board_id INT REFERENCES board(id),
    name VARCHAR(100),
    order_on_board INT
);

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    column_id INT REFERENCES column(id),
    title VARCHAR(100),
    order_in_column INT
);
