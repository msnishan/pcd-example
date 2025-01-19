package com.gcp.pcd.example.employee.pubsub;

import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Profile("mysql")
public class GcpSubscription implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(GcpSubscription.class);

    private final GcpConfig gcpConfig;
    private Subscriber subscriber;

    public GcpSubscription(GcpConfig gcpConfig) {
        this.gcpConfig = gcpConfig;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            ProjectSubscriptionName projectSubscriptionName = ProjectSubscriptionName.of(gcpConfig.projectId, gcpConfig.subId);
            subscriber = Subscriber.newBuilder(projectSubscriptionName, (MessageReceiver) (message, cons) -> {
                LOGGER.info("message received {}", message.getData());
                cons.ack();
            }).build();
            subscriber.startAsync().awaitRunning();
            LOGGER.info("sub started");
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }

    @PreDestroy
    public void stopSub() {
        if (subscriber != null) {
            subscriber.stopAsync();
        }
    }
}
