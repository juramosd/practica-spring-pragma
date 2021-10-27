package config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.SocketUtils;

import java.io.IOException;


public class MongoDbTestConfiguration {
   // private static final String MONGO_DB_URL = "mongodb://%s:%d";
   // private static final String MONGO_DB_NAME = "embeded_db";
//    private MongodExecutable mongodExecutable;
//    @Bean
//    @Primary
//    public MongoTemplate mongoTemplate1() throws IOException {
//        String ip = "localhost";
//        int randomPort = SocketUtils.findAvailableTcpPort();
//
//        ImmutableMongodConfig mongodConfig = MongodConfig
//                .builder().version(Version.Main.PRODUCTION)
//                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
//                .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongodExecutable.start();
//        return new MongoTemplate(MongoClients.create(String.format(MONGO_DB_URL, ip, randomPort)),MONGO_DB_NAME);
//    }

//    private static final String IP = "localhost";
//    private static final int PORT = 28017;
//    @Bean
//    public ImmutableMongodConfig embeddedMongoConfiguration() throws IOException {
//        return MongodConfig.builder()
//                .version(Version.V4_0_2)
//                .net(new Net(IP, PORT, Network.localhostIsIPv6()))
//                .build();
//    }
}
