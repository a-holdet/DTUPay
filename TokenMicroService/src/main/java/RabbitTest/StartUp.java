package RabbitTest;

import messaging.rmq.event.EventQueue;

public class StartUp {

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		EventService service = new EventService();
	}
}
