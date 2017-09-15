CREATE TABLE offer (
  id          BIGSERIAL PRIMARY KEY,
  event_id    BIGINT REFERENCES event (id),
  description VARCHAR(100) NOT NULL,
  coefficient FLOAT        NOT NULL,
  result      BOOLEAN
);

CREATE OR REPLACE FUNCTION select_all_offers()
  RETURNS SETOF OFFER AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 event_id,
                 description,
                 coefficient,
                 result
               FROM offer
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_offers_by_event_id(offer_event_id BIGINT)
  RETURNS SETOF OFFER AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 event_id,
                 description,
                 coefficient,
                 result
               FROM offer
               WHERE event_id = offer_event_id
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_offer_by_id(offer_id BIGINT)
  RETURNS SETOF OFFER AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 event_id,
                 description,
                 coefficient,
                 result
               FROM offer
               WHERE id = offer_id
               LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION insert_offer(offer_event_id BIGINT, offer_description VARCHAR(100), offer_coefficient FLOAT,
                                        offer_result   BOOLEAN)
  RETURNS SETOF OFFER AS $$
BEGIN
  RETURN QUERY
  INSERT INTO offer (event_id, description, coefficient, result)
  VALUES (offer_event_id, offer_description, offer_coefficient, offer_result)
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION delete_offer(offer_id BIGINT)
  RETURNS VOID AS $$
BEGIN
  DELETE FROM offer
  WHERE id = offer_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_offer(offer_id          BIGINT, offer_event_id BIGINT, offer_description VARCHAR(100),
                                        offer_coefficient FLOAT, offer_result BOOLEAN)
  RETURNS SETOF OFFER AS $$
BEGIN
  RETURN QUERY
  UPDATE offer
  SET event_id = offer_event_id, description = offer_description, coefficient = offer_coefficient, result = offer_result
  WHERE id = offer_id
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;
