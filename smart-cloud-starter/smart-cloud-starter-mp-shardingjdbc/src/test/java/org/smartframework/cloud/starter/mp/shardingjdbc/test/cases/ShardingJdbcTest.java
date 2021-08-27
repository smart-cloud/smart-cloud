package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.ShardingJdbcApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcApp.class, args = "--spring.profiles.active=shardingjdbc")
@Transactional
@Rollback
public class ShardingJdbcTest {
}