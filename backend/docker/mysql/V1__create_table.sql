CREATE TABLE user (
  id                 INT           AUTO_INCREMENT NOT NULL PRIMARY KEY
  , email            VARCHAR(255)  NOT NULL
  , name             VARCHAR(1000) NOT NULL
  , auth_id_google   VARCHAR(1000)
  , auth_id_github   VARCHAR(1000)
  , auth_id_facebook VARCHAR(1000)
  , auth_id_yahoo    VARCHAR(1000)
  , auth_id_line     VARCHAR(1000)
  , created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX uidx_user_email ON user (email);


CREATE TABLE feed (
  id             INT           AUTO_INCREMENT NOT NULL PRIMARY KEY
  , title        VARCHAR(5000) NOT NULL
  , description  TEXT
  , url          VARCHAR(1000) NOT NULL
  , feed_url     VARCHAR(255)  NOT NULL
  , feed_type    VARCHAR(50)   NOT NULL
  , icon         VARCHAR(1000)
  , image        VARCHAR(1000)
  , favicon      VARCHAR(1000)
  , status       VARCHAR(30)   NOT NULL DEFAULT 'NORMAL'
  , published_at DATETIME
  , created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE subscription (
  id           INT      AUTO_INCREMENT NOT NULL PRIMARY KEY
  , user_id    INT      NOT NULL
  , feed_id    INT      NOT NULL
  , created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX uidx_subscription ON subscription (user_id, feed_id);
CREATE INDEX idx_sub_user_id ON subscription (user_id);
CREATE INDEX idx_sub_feed_id ON subscription (feed_id);


CREATE TABLE item (
  id             INT           AUTO_INCREMENT NOT NULL PRIMARY KEY
  , feed_id      INT           NOT NULL
  , title        VARCHAR(5000) NOT NULL
  , description  MEDIUMTEXT
  , url          VARCHAR(1000) NOT NULL
  , author       VARCHAR(255)
  , published_at DATETIME
  , created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
  , created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_pin_user_id ON pin (user_id);


CREATE TABLE import (
  id           INT         AUTO_INCREMENT NOT NULL PRIMARY KEY
  , user_id    INT         NOT NULL
  , status     VARCHAR(30) NOT NULL DEFAULT 'RUNNING'
  , created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
  , updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE INDEX idx_import_user_id ON import (user_id);


CREATE TABLE import_feed (
  id          INT           AUTO_INCREMENT NOT NULL PRIMARY KEY
  , import_id INT           NOT NULL
  , title     VARCHAR(5000)
  , xml_url   VARCHAR(1000) NOT NULL
  , html_url  VARCHAR(1000)
  , status    VARCHAR(30)   NOT NULL
);
CREATE INDEX idx_import_feed_import_id ON import_feed (import_id);
