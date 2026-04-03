CREATE TABLE agendas (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE associates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    CONSTRAINT uk_associate_cpf UNIQUE (cpf)
);

CREATE TABLE voting_sessions (
    id BIGSERIAL PRIMARY KEY,
    agenda_id BIGINT NOT NULL,
    opened_at TIMESTAMP WITH TIME ZONE NOT NULL,
    closes_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_session_agenda FOREIGN KEY (agenda_id) REFERENCES agendas (id),
    CONSTRAINT uk_voting_session_agenda UNIQUE (agenda_id)
);

CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
    agenda_id BIGINT NOT NULL,
    associate_id BIGINT NOT NULL,
    value VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_vote_agenda FOREIGN KEY (agenda_id) REFERENCES agendas (id),
    CONSTRAINT fk_vote_associate FOREIGN KEY (associate_id) REFERENCES associates (id),
    CONSTRAINT uk_vote_agenda_associate UNIQUE (agenda_id, associate_id),
    CONSTRAINT ck_vote_value CHECK (value IN ('SIM', 'NAO'))
);

CREATE INDEX idx_votes_agenda ON votes (agenda_id);
CREATE INDEX idx_votes_agenda_value ON votes (agenda_id, value);
