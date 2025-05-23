package pl.edu.streamfinder.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "services")
public class Service {
    @Id
    private int id;
    private String name;
    private String logoURL;
    List<Plan> plans;
}
