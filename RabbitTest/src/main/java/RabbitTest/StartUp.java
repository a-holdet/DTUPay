package RabbitTest;

import messaging.EventSender;
import messaging.channels.EventServiceListener;
import messaging.rabbitmq.RabbitMqSender;

public class StartUp {
    public static void main(String[] args) throws Exception {
    	new StartUp().startUp();
    }

	private void startUp() throws Exception {
		EventSender s = new RabbitMqSender();
		EventService service = new EventService(s);
		new EventServiceListener(service).listen();
	}
}
