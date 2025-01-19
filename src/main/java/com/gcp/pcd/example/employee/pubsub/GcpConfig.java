package com.gcp.pcd.example.employee.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcpConfig {


    public final String projectId;

    public final String subId;


    public GcpConfig(@Value("${gcp.project.id}") String projectId, @Value("${gcp.project.sub}") String subId) {
        this.projectId = projectId;
        this.subId = subId;
    }
}
