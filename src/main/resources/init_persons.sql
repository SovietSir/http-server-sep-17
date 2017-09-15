CREATE TABLE person (
  id            BIGSERIAL PRIMARY KEY,
  login         VARCHAR(100) UNIQUE NOT NULL,
  password_hash INT                 NOT NULL,
  balance       BIGINT              NOT NULL
);

CREATE OR REPLACE FUNCTION select_all_persons()
  RETURNS SETOF PERSON AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 login,
                 password_hash,
                 balance
               FROM person
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_person_by_id(person_id BIGINT)
  RETURNS SETOF PERSON AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 login,
                 password_hash,
                 balance
               FROM person
               WHERE id = person_id
               LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION insert_person(person_login VARCHAR(100), person_password_hash INT, person_balance BIGINT)
  RETURNS SETOF PERSON AS $$
BEGIN
  RETURN QUERY
  INSERT INTO person (login, password_hash, balance)
  VALUES (person_login, person_password_hash, person_balance)
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION delete_person(person_id BIGINT)
  RETURNS VOID AS $$
BEGIN
  DELETE FROM person
  WHERE id = person_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_person(person_id      BIGINT, person_login VARCHAR(100), person_password_hash INT,
                                         person_balance BIGINT)
  RETURNS SETOF PERSON AS $$
BEGIN
  RETURN QUERY
  UPDATE person
  SET login = person_login, password_hash = person_password_hash, balance = person_balance
  WHERE id = person_id
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;
