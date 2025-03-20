package my.beloved.subject.broker;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageBrokerServer implements AutoCloseable {
    ServerSocketChannel server = null;
    Selector selector = null;
    ArrayList<SocketChannel> clients = new ArrayList<>();
    AtomicBoolean stopped = new AtomicBoolean(false);

    public MessageBrokerServer() {}

    public void bind(SocketAddress address, ProtocolFamily protocol) throws IOException {
        this.server = ServerSocketChannel.open(protocol);
        this.server.configureBlocking(false);
        server.bind(address);
        System.out.println("Server bound to address " + address);
        
        
        this.selector = Selector.open();
        int ops = server.validOps();
        server.register(this.selector, ops, null);

        while (!stopped.get()) {
            this.selector.select();
            Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
            
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                if (key.isAcceptable()) {
                    this.handleAccept(key);
                } else if (key.isReadable()) {
                    this.handleRead(key);
                }
                selectedKeys.remove();
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel client = this.server.accept();
        client.configureBlocking(false);
        client.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.clients.add(client);
        System.out.println("Accepted connection from " + client.getLocalAddress() + " " + client.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        var buffer = ByteBuffer.allocate(Integer.BYTES);
        int read = client.read(buffer);
        boolean closed = read == -1;
        if (closed) {
            System.out.println("Disconnected " + client.getLocalAddress());
            client.register(this.selector, 0);
            client.close();
            return;
        }
        buffer.flip();
        int num = buffer.getInt();
        System.out.println("Recieved " + num + " from " + client.getLocalAddress() + " " + client.getRemoteAddress());

        ArrayList<Integer> disconnected = new ArrayList<>();
        for (int i = 0; i < this.clients.size(); i ++) {
            SocketChannel otherClient = this.clients.get(i);
            if (!otherClient.isConnected()) {
                disconnected.add(i);
                continue;
            }
            if (!channelsEqual(otherClient, client)) {
                System.out.println("Sent " + num + " to " + client.getLocalAddress() + " " + client.getRemoteAddress());
                ByteBuffer buffer_ = ByteBuffer.allocate(Integer.BYTES).putInt(num);
                buffer_.flip();
                otherClient.write(buffer_);
            }
        }

        disconnected.sort(Comparator.reverseOrder());
        for (Integer i : disconnected) {
            this.clients.remove(i.intValue());
        }
    }

    private static boolean channelsEqual(SocketChannel a, SocketChannel b) throws IOException {
        return a.getRemoteAddress() == b.getRemoteAddress() && a.getLocalAddress() == b.getLocalAddress();
    }

    public void stop() throws IOException {
        this.stopped.set(true);
        if (this.server != null) {
            this.server.close();
        }
        if (this.selector != null) {
            this.selector.close();
        }
        for (SocketChannel client : this.clients) {
            client.close();
        }
    }

    public void close() throws IOException {
        this.stop();
    }
}
