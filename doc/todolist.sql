CREATE TABLE `web`.`todolist` (
  `id` INT NOT NULL,
  `info` VARCHAR(255) NULL,
  `state` INT(11) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));

ALTER TABLE `web`.`todolist` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;


ALTER TABLE `todolist` CHANGE `info` `info` VARCHAR( 45 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL;


ALTER TABLE `todolist` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;


ALTER TABLE `web` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;


insert into todolist (info,state) value("第一条测试",0);

return this.promotionJdbcTemplate.queryForObject(sql, new Object[] {gid, uid},Integer.class);

	



config.setMaxTotal


config.setMaxWaitMillis
