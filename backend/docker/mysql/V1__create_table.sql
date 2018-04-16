CREATE TABLE feed (
  id              INT            AUTO_INCREMENT NOT NULL PRIMARY KEY
  , title         VARCHAR(5000)  NOT NULL
  , description   TEXT
  , url           VARCHAR(1000)  NOT NULL
  , feed_url      VARCHAR(255)   NOT NULL
  , feed_type     VARCHAR(50)    NOT NULL
  , icon          VARCHAR(1000)
  , image         VARCHAR(1000)
  , favicon       VARCHAR(1000)
  , published_at DATETIME       DEFAULT NULL
  , created_at   DATETIME       DEFAULT NULL
  , updated_at   DATETIME       DEFAULT NULL
);


CREATE TABLE subscription (
  id           INT      AUTO_INCREMENT NOT NULL PRIMARY KEY
  , user_id    INT      NOT NULL
  , feed_id    INT      NOT NULL
  , created_at DATETIME DEFAULT NULL
  , updated_at DATETIME DEFAULT NULL
);
CREATE UNIQUE INDEX uidx_subscription ON subscription (user_id, feed_id);
CREATE INDEX idx_sub_user_id ON subscription (user_id);
CREATE INDEX idx_sub_feed_id ON subscription (feed_id);


CREATE TABLE item (
  id             INT            AUTO_INCREMENT NOT NULL PRIMARY KEY
  , feed_id      INT            NOT NULL
  , title        VARCHAR(5000)  NOT NULL
  , description  MEDIUMTEXT
  , url          VARCHAR(1000)  NOT NULL
  , author       VARCHAR(255)
  , published_at DATETIME       DEFAULT NULL
  , created_at   DATETIME       DEFAULT NULL
  , updated_at   DATETIME       DEFAULT NULL
);
CREATE INDEX idx_item_feed_id ON item (feed_id);


CREATE TABLE user_item (
  user_id   INT     NOT NULL
  , item_id INT     NOT NULL
  , feed_id INT     NOT NULL
  , unread  BOOLEAN NOT NULL
);
CREATE INDEX idx_user_item_user_id ON user_item (user_id);


CREATE TABLE pin (
  id             INT            AUTO_INCREMENT NOT NULL PRIMARY KEY
  , user_id      INT            NOT NULL
  , title        VARCHAR(5000)  NOT NULL
  , url          VARCHAR(1000)  NOT NULL
  , created_at   DATETIME       DEFAULT NULL
  , updated_at   DATETIME       DEFAULT NULL
);
CREATE INDEX idx_pin_user_id ON pin (user_id);
