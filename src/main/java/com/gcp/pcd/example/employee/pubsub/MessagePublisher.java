package com.gcp.pcd.example.employee.pubsub;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessagePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);

    private final GcpConfig gcpConfig;

    public MessagePublisher(GcpConfig gcpConfig) {
        this.gcpConfig = gcpConfig;
    }

    public void publishMessageToTopic(String topicId, String messageInString) {
        TopicName topicName = TopicName.of(gcpConfig.projectId, topicId);
        try {
            Publisher publisher = Publisher.newBuilder(topicName)
                    .setCredentialsProvider(GoogleCredentials::getApplicationDefault)
                    .build();
            PubsubMessage message = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8(messageInString))
                    .build();
            publisher.publish(message);
            LOGGER.info("message published to topic {}", topicId);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            LOGGER.error("error while publishing message to topic {}", topicId);
        }
    }

}
