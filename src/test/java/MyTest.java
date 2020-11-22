import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyTest {

    @Container
    private static final DockerComposeContainer CONTAINERS = new DockerComposeContainer(new File("docker-compose.yml"));

    private static final Logger logger = LoggerFactory.getLogger(MyTest.class);

    @BeforeAll
    void prepare() throws InterruptedException {
        // Thread.sleep(1000); << Ensures PostgreSQL is reachable
        logger.info("🚀 MyTest.prepare()");

        Vertx vertx = Vertx.vertx();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        PgPool pgPool = PgPool.pool(vertx, new PgConnectOptions()
                .setHost("localhost")
                .setDatabase("postgres")
                .setUser("postgres")
                .setPassword("vertx-in-action"), new PoolOptions());

        pgPool.preparedQuery("CREATE TABLE IF NOT EXISTS abc (def VARCHAR)").execute(ar -> {
            if (!ar.succeeded()) {
                failure.set(ar.cause());
            }
            latch.countDown();
        });

        latch.await();
        Assertions.assertNull(failure.get(), "There should be no exception");
    }

    @Test
    void test() {
        // Ok
    }
}
