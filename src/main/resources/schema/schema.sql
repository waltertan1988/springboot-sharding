
CREATE TABLE `T_ORDER` (
  `ORDER_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(11) NOT NULL,
  `STATUS` VARCHAR(50) COLLATE UTF8_BIN DEFAULT NULL,
  PRIMARY KEY (`ORDER_ID`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE=UTF8_BIN;

CREATE TABLE `T_ORDER_ITEM` (
  `ORDER_ITEM_ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ORDER_ID` BIGINT(20) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ORDER_ITEM_ID`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE=UTF8_BIN;
