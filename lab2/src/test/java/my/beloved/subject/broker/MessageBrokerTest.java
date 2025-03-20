package my.beloved.subject.broker;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;

public class MessageBrokerTest {
    private SocketAddress address;
    private ProtocolFamily protocol;

    private MessageBrokerServer server;
    private Thread serverThread;
    
    @BeforeEach
    public void setup(
        @TempDir Path tempDir,
        TestInfo testInfo
    ) throws IOException {
        String fileName = "msgbrok-" + testInfo.getDisplayName() + "-" + (int)(Math.random() * 1e6) + ".socket";
        Path socketFile = tempDir.resolve(fileName);
        Files.deleteIfExists(socketFile);
        this.address = UnixDomainSocketAddress.of(socketFile);
        this.protocol = StandardProtocolFamily.UNIX;
        
        this.server = new MessageBrokerServer();
        
        this.serverThread = new Thread(() -> {
            try {
                this.server.bind(this.address, this.protocol);
            } catch (IOException e) {
                throw new RuntimeException(e);   
            }
        });
        this.serverThread.start();

        Awaitility.setDefaultTimeout(Duration.TWO_SECONDS);
        Awaitility.await().until(() -> Files.exists(socketFile));
    }

    @AfterEach
    public void teardown() throws IOException {
        this.server.stop();
    }
    
    
    @Test
    public void basicMessageSending() throws IOException {
        final var clients = setupClients(3);
        final var threads = startClients(clients);

        Awaitility.await().until(() -> {
            for (var client : clients) {
                if (!client.isConnected()) {
                    return false;
                }
            }
            return true;
        });

        clients[0].send(0);
        clients[1].send(1);
        clients[2].send(2);
        
        Awaitility.await().untilAsserted(() -> {
            assertCollectionEquals(List.of(1, 2), clients[0].history);
            assertCollectionEquals(List.of(0, 2), clients[1].history);
            assertCollectionEquals(List.of(0, 1), clients[2].history);
        });

        clients[0].disconnect();

        Awaitility.await().until(() -> threads[0].getState() == Thread.State.TERMINATED);

        clients[1].send(1);
        clients[2].send(2);
    
        Awaitility.await().untilAsserted(() -> {
            assertCollectionEquals(List.of(1, 2), clients[0].history);
            assertCollectionEquals(List.of(0, 2, 2), clients[1].history);
            assertCollectionEquals(List.of(0, 1, 1), clients[2].history);
        });
    }

    @Test
    public void messagesAreOrdered() throws IOException {
        var clients = setupClients(2);
        startClients(clients);

        Awaitility.await().until(() -> clients[0].isConnected() && clients[1].isConnected());

        var range = IntStream.range(0, 50).boxed().collect(Collectors.toList());
        for (Integer i : range) {
            clients[0].send(i);
            clients[1].send(i);
        }

        Awaitility.await().untilAsserted(() -> {
            Assertions.assertIterableEquals(range, clients[0].history);
            Assertions.assertIterableEquals(range, clients[1].history);
        });
    }

    @Test
    public void serverDisconnect() throws IOException {
        var clients = setupClients(1);
        startClients(clients);
        
        Awaitility.await().until(() -> clients[0].isConnected());
        this.server.stop();
        Awaitility.await().until(() -> {
            return this.serverThread.getState() == Thread.State.TERMINATED && !clients[0].isConnected();
        });
    }

    @Test
    public void failedToConnect() throws IOException {
        var clients = setupClients(1);
        this.server.stop();

        Awaitility.await().until(() -> this.server.isStopped());
        Assertions.assertThrows(IOException.class, () -> {
            clients[0].connect(this.address, this.protocol);
        });
    }

    @Test
    public void clientLost() throws IOException {
        var clients = setupClients(1);
        startClients(clients);

        Awaitility.await().until(() -> clients[0].isConnected());
        clients[0].disconnect();
        Awaitility.await().until(() -> !clients[0].isConnected());

        Assertions.assertThrows(IOException.class, () -> {
            clients[0].send(0);
        });
    }

    private TestingClient[] setupClients(int clientsNum) {
        final var clients = new TestingClient[clientsNum];
        for (int i = 0; i < clientsNum; i ++) {
            var client = new TestingClient();
            clients[i] = client;
        }
        return clients;
    }

    private Thread[] startClients(MessageBrokerClient[] clients) {
        final var clientThreads = new Thread[clients.length];
        for (int i = 0; i < clients.length; i ++) {
            var client = clients[i];
            clientThreads[i] = new Thread(() -> {
                try {
                    client.connect(this.address, this.protocol);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            clientThreads[i].start();
        }
        return clientThreads;
    }

    private static <T> void assertCollectionEquals(Collection<T> expected, Collection<T> actual) {
        Assertions.assertEquals(
            expected.size(),
            actual.size(),
            "collections differ in size, expected " + expected.size() + " elements, but got " + actual.size()
        );
        Assertions.assertTrue(
            actual.containsAll(expected),
            "collections differ in content, expected " + expected  + ", but got " + actual
        );
    }
}

class TestingClient extends MessageBrokerClient {
    List<Integer> history = Collections.synchronizedList(new ArrayList<Integer>());
    
    public TestingClient() {
        super();
        this.setListeners(
            new MessageBrokerClient.SimpleListeners()
                .whenMessaged(val -> {
                    this.history.add(val);
                })
        );
    }
}
