CREATE TABLE league (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL
);

CREATE OR REPLACE FUNCTION select_all_leagues()
  RETURNS SETOF LEAGUE AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 name
               FROM league
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_league_by_id(league_id BIGINT)
  RETURNS SETOF LEAGUE AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 name
               FROM league
               WHERE id = league_id
               LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION insert_league(league_name VARCHAR(100))
  RETURNS VOID AS $$
BEGIN
  INSERT INTO league (name) VALUES (league_name);
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION delete_league(league_id BIGINT)
  RETURNS VOID AS $$
BEGIN
  DELETE FROM league
  WHERE id = league_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_league(league_id BIGINT, league_name VARCHAR(100))
  RETURNS VOID AS $$
BEGIN
  UPDATE league
  SET name = league_name
  WHERE id = league_id;
END;
$$ LANGUAGE plpgsql VOLATILE;