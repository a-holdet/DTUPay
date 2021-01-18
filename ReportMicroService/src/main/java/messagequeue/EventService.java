package messagequeue;


import AccountService.CustomerDoesNotExistException;
import AccountService.Merchant;
import AccountService.MerchantDoesNotExistException;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import reportservice.IReportService;
import reportservice.ReportService;
import reportservice.Transaction;
import reportservice.UserReport;

import java.util.List;
import java.util.UUID;

public class EventService implements IEventReceiver {

	// Singleton as method due to serviceTest
	private static EventService instance;
	public static EventService getInstance() {
		if (instance == null) {
			try {
				var ies = EventExchange.instance.getSender();
				EventService service = new EventService(ies);
				new EventQueue().registerReceiver(service);
				instance = service;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		return instance;
	}

	private static final IReportService reportService = ReportService.instance;
	IEventSender sender;

	public EventService(IEventSender sender) { this.sender = sender; }

    EventType generateReportForCustomer = new EventType("generateReportForCustomer");
    private static final EventType generateReportForMerchant = new EventType("generateReportForMerchant");
    private static final EventType generateManagerOverview = new EventType("generateManagerOverview");


	public void generateReportForCustomer(String customerId, String startTime, String endTime, UUID eventId) throws Exception {
		try {
			UserReport userReport = reportService.generateReportForCustomer(customerId, startTime, endTime);
            Event event = new Event(generateReportForCustomer.succeeded(), new Object[] { userReport }, eventId);
			this.sender.sendEvent(event);
		}
		catch (CustomerDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
            Event event = new Event(generateReportForCustomer.failed(), new Object[] { exceptionType, exceptionMsg }, eventId);
			this.sender.sendEvent(event);
		}
	}

    private void generateReportForMerchant(String merchantId, String startTime, String endTime, UUID eventId) throws Exception{
        try {
            UserReport userReport = reportService.generateReportForMerchant(merchantId, startTime, endTime);
            Event event = new Event(generateReportForMerchant.succeeded(), new Object[] { userReport }, eventId);
            this.sender.sendEvent(event);
        }
        catch (MerchantDoesNotExistException e) {
            String exceptionType = e.getClass().getSimpleName();
            String exceptionMsg = e.getMessage();
            Event event = new Event(generateReportForMerchant.failed(), new Object[] { exceptionType, exceptionMsg }, eventId);
            this.sender.sendEvent(event);
        }
    }

    private void generateManagerOverview(UUID eventId) throws Exception{
        List<Transaction> managerOverview = reportService.generateManagerOverview();
        Event event = new Event(generateReportForMerchant.succeeded(), new Object[] { managerOverview }, eventId);
        this.sender.sendEvent(event);
    }

	@Override
	public void receiveEvent(Event event) throws Exception {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

        if (type.equals(generateReportForCustomer.getName())){
            String customerId = event.getArgument(0, String.class);
            String startTime = event.getArgument(1, String.class);
            String endTime = event.getArgument(2, String.class);
            generateReportForCustomer(customerId, startTime, endTime, eventId);
        } else if (type.equals(generateReportForMerchant.getName())){
            String merchantId = event.getArgument(0, String.class);
            String startTime = event.getArgument(1, String.class);
            String endTime = event.getArgument(2, String.class);
            generateReportForMerchant(merchantId, startTime, endTime, eventId);
        } else if (type.equals(generateManagerOverview.getName())){
            generateManagerOverview(eventId);
        }

		System.out.println("--------------------------------------------------------");
	}




}