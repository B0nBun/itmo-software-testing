package my.beloved.subject;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

import my.beloved.subject.broker.MessageBrokerServer;

public class Server {
    public static void main(String[] args) throws IOException {
        var server = new MessageBrokerServer();
        var socketFile = Path.of("./msgbrok.socket");
        Files.deleteIfExists(socketFile);
        var address = UnixDomainSocketAddress.of(socketFile);
        server.bind(address, StandardProtocolFamily.UNIX);
    }
}
