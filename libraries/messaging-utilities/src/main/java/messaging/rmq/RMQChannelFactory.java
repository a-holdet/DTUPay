package messaging.rmq;

public class RMQChannelFactory {
    static RMQChannel channel;
    public RMQChannel getChannel() {
        if(channel == null) {
            channel = new RMQChannel();
        }
        return channel;
    }
}
