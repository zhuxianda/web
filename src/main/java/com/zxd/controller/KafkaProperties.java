package com.zxd.controller; /**
 * Created by zhuxianda on 2018/2/13.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaProperties {

    @Value("${log_topic}")
    public String topic;

    @Value("${log_groupId}")
    public String groupId;

    @Value("${bootstrap_servers}")
    public String bootstrapServers;

    @Value("${enableAutoCommit}")
    public String enableAutoCommit;

    @Value("${autoCommitIntervalMs}")
    public String autoCommitIntervalMs;

    @Value("${session_timeout_ms}")
    public String sessionTimeoutMs;

    @Value("${keySerializer}")
    public String keySerializer;

    @Value("${valueSerializer}")
    public String valueSerializer;

}
