CREATE TABLE feeds (
  id            INT            AUTO_INCREMENT NOT NULL PRIMARY KEY
  , title       VARCHAR(5000)  NOT NULL
  , description VARCHAR(5000)
  , url         VARCHAR(255)   NOT NULL
  , feed_url    VARCHAR(255)   NOT NULL
  , feed_type   VARCHAR(50)    NOT NULL
  , created_at  DATETIME       DEFAULT NULL
  , updated_at  DATETIME       DEFAULT NULL
);


CREATE TABLE subscriptions (
  id           INT      AUTO_INCREMENT NOT NULL PRIMARY KEY
  , user_id    INT      NOT NULL
  , feed_id    INT      NOT NULL
  , created_at DATETIME DEFAULT NULL
  , updated_at DATETIME DEFAULT NULL
);
CREATE INDEX idx_subs_user_id ON subscriptions (user_id);
CREATE UNIQUE INDEX uidx_subscriptions ON subscriptions (user_id, feed_id);


CREATE TABLE items (
  id             INT            AUTO_INCREMENT NOT NULL PRIMARY KEY
  , feed_id      INT            NOT NULL
  , title        VARCHAR(5000)  NOT NULL
  , description  VARCHAR(5000)
  , url          VARCHAR(255)   NOT NULL
  , author       VARCHAR(255)
  , published_at DATETIME       DEFAULT NULL
  , created_at   DATETIME       DEFAULT NULL
  , updated_at   DATETIME       DEFAULT NULL
);
