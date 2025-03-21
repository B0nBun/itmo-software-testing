package my.beloved.subject.broker;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MessageBrokerClient {
    private volatile SocketChannel channel = null;
    private Listeners listeners = null;
    private AtomicBoolean connected = new AtomicBoolean(true);

    public MessageBrokerClient() {}

    public void connect(SocketAddress address, ProtocolFamily protocol) throws IOException {
        this.channel = SocketChannel.open(protocol);
        this.channel.configureBlocking(false);
        this.channel.connect(address);
        this.listeners.onConnect();

        try {
            while (this.connected.get()) {
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES); 
                int read = channel.read(buffer);
                buffer.flip();
                if (read > 0) {
                    int val = buffer.getInt();
                    this.listeners.onMessage(val);
                } else if (read == -1) {
                    this.listeners.onDisconnect();
                    this.connected.set(false);
                }
            }
        } finally {
            this.channel.close();
        }
    }

    public void setListeners(Listeners listeners) {
        this.listeners = listeners;
    }

    public void send(int val) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES).putInt(val);
        buffer.flip();
        int written = channel.write(buffer);
        assert written == Integer.BYTES;
    }

    public void disconnect() throws IOException {
        this.connected.set(false);
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isConnected() && this.channel.isOpen();
    }

    public static interface Listeners {
        public void onConnect();
        public void onDisconnect();
        public void onMessage(int val);
    }

    public static class SimpleListeners implements Listeners {
        private Runnable connect;
        private Runnable disconnect;
        private Consumer<Integer> message;
        
        public SimpleListeners() {}

        public SimpleListeners whenConnected(Runnable connect) {
            this.connect = connect;
            return this;
        }

        public SimpleListeners whenDisconnected(Runnable disconnect) {
            this.disconnect = disconnect;
            return this;
        }

        public SimpleListeners whenMessaged(Consumer<Integer> message) {
            this.message = message;
            return this;
        }

        public void onConnect() {
            if (this.connect != null) {
                this.connect.run();
            }
        }

        public void onDisconnect() {
            if (this.disconnect != null) {
                this.disconnect.run();
            }
        }

        public void onMessage(int val) {
            if (this.message != null) {
                this.message.accept(val);
            }
        }
    }
}
