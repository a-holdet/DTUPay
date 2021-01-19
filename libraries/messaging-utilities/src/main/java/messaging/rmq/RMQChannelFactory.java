package messaging.rmq;


/***
 * @Author Simon Borup Lindegren, s185370
 */
public class RMQChannelFactory {
    static RMQChannel channel;
    public RMQChannel getChannel() {
        if(channel == null) {
            channel = new RMQChannel();
        }
        return channel;
    }
}
