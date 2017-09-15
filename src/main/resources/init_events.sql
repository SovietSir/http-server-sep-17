CREATE TABLE event (
  id         BIGSERIAL PRIMARY KEY,
  league_id  BIGINT REFERENCES league (id),
  date       TIMESTAMP   NOT NULL,
  home_team  VARCHAR(50) NOT NULL,
  guest_team VARCHAR(50) NOT NULL,
  score      VARCHAR(10)
);

CREATE OR REPLACE FUNCTION select_all_events()
  RETURNS SETOF EVENT AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 league_id,
                 date,
                 home_team,
                 guest_team,
                 score
               FROM event
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_events_by_league_id(event_league_id BIGINT)
  RETURNS SETOF EVENT AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 league_id,
                 date,
                 home_team,
                 guest_team,
                 score
               FROM event
               WHERE league_id = event_league_id
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_event_by_id(event_id BIGINT)
  RETURNS SETOF EVENT AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 league_id,
                 date,
                 home_team,
                 guest_team,
                 score
               FROM event
               WHERE id = event_id
               LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION insert_event(event_league_id  BIGINT, event_date TIMESTAMP, event_home_team VARCHAR(50),
                                        event_guest_team VARCHAR(50), event_score VARCHAR(10))
  RETURNS SETOF EVENT AS $$
BEGIN
  RETURN QUERY INSERT INTO event (league_id, date, home_team, guest_team, score)
  VALUES (event_league_id, event_date, event_home_team, event_guest_team, event_score)
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION delete_event(event_id BIGINT)
  RETURNS VOID AS $$
BEGIN
  DELETE FROM event
  WHERE id = event_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_event(event_id        BIGINT, event_league_id BIGINT, event_date TIMESTAMP,
                                        event_home_team VARCHAR(50), event_guest_team VARCHAR(50),
                                        event_score     VARCHAR(10))
  RETURNS SETOF EVENT AS $$
BEGIN
  RETURN QUERY UPDATE event
  SET league_id = event_league_id, date = event_date, home_team = event_home_team, guest_team = event_guest_team,
    score       = event_score
  WHERE id = event_id RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;
