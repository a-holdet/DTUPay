// package eventservice;

// import messaging.EventSender;
// import messaging.rabbitmq.RabbitMqListener;
// import messaging.rabbitmq.RabbitMqSender;
// import eventservice.Service1;

// public class Service1Factory {

//     public Service1 service = null;

// 	public Service1 getService() {
//         System.out.println("service1: I WENT!");

// 		if (service != null) {
// 			return service;
// 		}

// 		EventSender es = new RabbitMqSender();
// 		service = new Service1(es);
// 		RabbitMqListener r = new RabbitMqListener(service);
// 		try {
// 			r.listen();
// 		} catch (Exception e) {
// 			System.out.println("service1: Jeg virker ikke!");
// 			throw new Error(e);
//         }
//         System.out.println("service1: Jeg virker!");
// 		return service;
//     }
// }
