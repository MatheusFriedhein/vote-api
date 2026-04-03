ALTER TABLE voting_sessions
    ADD COLUMN total_yes BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN total_no BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN total_votes BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

CREATE INDEX idx_voting_sessions_agenda_closes_at ON voting_sessions (agenda_id, closes_at);
