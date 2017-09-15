CREATE TABLE bet (
  id        BIGSERIAL PRIMARY KEY,
  person_id BIGINT REFERENCES person (id),
  offer_id  BIGINT REFERENCES offer (id),
  amount    BIGINT NOT NULL,
  gain      FLOAT
);

CREATE OR REPLACE FUNCTION select_all_bets()
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 person_id,
                 offer_id,
                 amount,
                 gain
               FROM bet
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_bets_by_person_id(bet_person_id BIGINT)
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 person_id,
                 offer_id,
                 amount,
                 gain
               FROM bet
               WHERE person_id = bet_person_id
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_bets_by_offer_id(bet_offer_id BIGINT)
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 person_id,
                 offer_id,
                 amount,
                 gain
               FROM bet
               WHERE offer_id = bet_offer_id
               ORDER BY id;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION select_bet_by_id(bet_id BIGINT)
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY SELECT
                 id,
                 person_id,
                 offer_id,
                 amount,
                 gain
               FROM bet
               WHERE id = bet_id
               LIMIT 1;
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION insert_bet(bet_person_id BIGINT, bet_offer_id BIGINT, bet_amount BIGINT, bet_gain FLOAT)
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY
  INSERT INTO bet (person_id, offer_id, amount, gain)
  VALUES (bet_person_id, bet_offer_id, bet_amount, bet_gain)
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION delete_bet(bet_id BIGINT)
  RETURNS VOID AS $$
BEGIN
  DELETE FROM bet
  WHERE id = bet_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_bet(bet_id   BIGINT, bet_person_id BIGINT, bet_offer_id BIGINT, bet_amount BIGINT,
                                      bet_gain FLOAT)
  RETURNS SETOF BET AS $$
BEGIN
  RETURN QUERY
  UPDATE bet
  SET person_id = bet_person_id, offer_id = bet_offer_id, amount = bet_amount, gain = bet_gain
  WHERE id = bet_id
  RETURNING *;
END;
$$ LANGUAGE plpgsql VOLATILE;
