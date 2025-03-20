package my.beloved.subject;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Path;

import my.beloved.subject.broker.MessageBrokerClient;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = new MessageBrokerClient();

        client.setListeners(new MessageBrokerClient.Listeners() {
            @Override
            public void onConnect() {
                System.out.println("connected");
            }

            @Override
            public void onDisconnect() {
                System.out.println("disconnected");
            }

            @Override
            public void onMessage(int val) {
                System.out.println("recieved " + val);
            }
        });

        var thread = new Thread(() -> {
            while (!client.isConnected()) { // Wait for "connected" to become true
                continue;
            }
            while (client.isConnected()) {
                try {
                    Thread.sleep(2000);
                    int val = (int)(Math.random() * 10);
                    client.send(val);
                    System.out.println("sent " + val);
                } catch (IOException | InterruptedException e) {
                    continue;
                }
            }
        });

        thread.start();

        var socketFile = Path.of("./msgbrok.socket");
        var address = UnixDomainSocketAddress.of(socketFile);
        client.connect(address, StandardProtocolFamily.UNIX);

        thread.join();
    }
}
