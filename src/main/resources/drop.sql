DROP FUNCTION IF EXISTS select_all_leagues();
DROP FUNCTION IF EXISTS select_league_by_id(BIGINT);
DROP FUNCTION IF EXISTS insert_league(VARCHAR(100));
DROP FUNCTION IF EXISTS delete_league(BIGINT);
DROP FUNCTION IF EXISTS update_league(BIGINT, VARCHAR(100));

DROP FUNCTION IF EXISTS select_all_events();
DROP FUNCTION IF EXISTS select_events_by_league_id(BIGINT);
DROP FUNCTION IF EXISTS select_event_by_id(BIGINT);
DROP FUNCTION IF EXISTS insert_event(BIGINT, TIMESTAMP, VARCHAR(50), VARCHAR(50), VARCHAR(10));
DROP FUNCTION IF EXISTS delete_event(BIGINT);
DROP FUNCTION IF EXISTS update_event(BIGINT, BIGINT, TIMESTAMP, VARCHAR(50), VARCHAR(50), VARCHAR(10));

DROP FUNCTION IF EXISTS select_all_offers();
DROP FUNCTION IF EXISTS select_offer_by_id(BIGINT);
DROP FUNCTION IF EXISTS insert_offer(BIGINT, VARCHAR(100), FLOAT, BOOLEAN);
DROP FUNCTION IF EXISTS delete_offer(BIGINT);
DROP FUNCTION IF EXISTS update_offer(BIGINT, BIGINT, VARCHAR(100), FLOAT, BOOLEAN);
DROP FUNCTION IF EXISTS select_offers_by_event_id(BIGINT);

DROP FUNCTION IF EXISTS select_all_persons();
DROP FUNCTION IF EXISTS select_person_by_id(BIGINT);
DROP FUNCTION IF EXISTS insert_person(VARCHAR(100), INT, BIGINT);
DROP FUNCTION IF EXISTS delete_person(BIGINT);
DROP FUNCTION IF EXISTS update_person(BIGINT, VARCHAR(100), INT, BIGINT);

DROP FUNCTION IF EXISTS select_all_bets();
DROP FUNCTION IF EXISTS select_bet_by_id(BIGINT);
DROP FUNCTION IF EXISTS insert_bet(BIGINT, BIGINT, BIGINT, FLOAT);
DROP FUNCTION IF EXISTS delete_bet(BIGINT);
DROP FUNCTION IF EXISTS update_bet(BIGINT, BIGINT, BIGINT, BIGINT, FLOAT);
DROP FUNCTION IF EXISTS select_bets_by_person_id(BIGINT);
DROP FUNCTION IF EXISTS select_bets_by_offer_id(BIGINT);

DROP TABLE IF EXISTS bet;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS offer;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS league;