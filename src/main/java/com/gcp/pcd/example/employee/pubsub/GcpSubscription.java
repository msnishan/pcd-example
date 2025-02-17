package com.gcp.pcd.example.employee.pubsub;

import com.google.auth.oauth2.GoogleCredentials;
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
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
            })
            .setCredentialsProvider(GoogleCredentials::getApplicationDefault)
            .build();
            subscriber.startAsync().awaitRunning();
            LOGGER.info("sub started");
            subscriber.awaitTerminated(3, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            LOGGER.warn("sub closing");
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
