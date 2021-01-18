package reportservice;

import DTO.DTUPayUser;
import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import merchantservice.*;
import DTO.Payment;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueueReportService implements IReportService, IEventReceiver {

    // Singleton as method due to serviceTest
    private static MessageQueueReportService instance;

    public static MessageQueueReportService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueReportService service = new MessageQueueReportService(ies,
                        MessageQueueAccountService.getInstance(), MessageQueueAccountService.getInstance());
                new EventQueue(service).startListening();
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    IEventSender sender;

    public MessageQueueReportService(IEventSender sender, IMerchantService merchantService,
            ICustomerService customerService) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    private static final EventType generateReportForCustomer = new EventType("generateReportForCustomer");
    private static final EventType generateReportForMerchant = new EventType("generateReportForMerchant");
    private static final EventType registerTransaction = new EventType("registerTransaction");
    private static final EventType generateManagerOverview = new EventType("generateManagerOverview");
    private static final EventType[] supportedEventTypes = {generateReportForCustomer, generateReportForMerchant, registerTransaction, generateManagerOverview};

    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();

    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime) throws CustomerDoesNotExistException {
        Event event = new Event(generateReportForCustomer.getName(), new Object[] { customerId, startTime, endTime });
        try {
            requests.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = requests.get(event.getUUID()).join();
        String type = event.getEventType();

        if (type.equals(generateReportForCustomer + "Success")) {
            // Gson gson = new Gson();
            // UserReport report = new UserReport();
            // List<Payment> payments = Arrays.asList(gson.fromJson(event.getArgument(0,
            // String.class), Payment[].class));
            // DTUPayUser customerAsUser = gson.fromJson(event.getArgument(1, String.class),
            // DTUPayUser.class);
            // report.setPayments(payments);
            // report.setUser(customerAsUser);
            return generateReport(event);
        }
        String exceptionMsg = event.getArgument(1, String.class);
        throw new CustomerDoesNotExistException(exceptionMsg);
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime)
            throws MerchantDoesNotExistException {
        Event event = new Event(generateReportForMerchant.getName(), new Object[] { merchantId, startTime, endTime });
        try {
            requests.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = requests.get(event.getUUID()).join();
        String type = event.getEventType();

        if (type.equals(generateReportForMerchant + "Success")) {
            return generateReport(event);
        }
        String exceptionMsg = event.getArgument(1, String.class);
        throw new MerchantDoesNotExistException(exceptionMsg);
    }

    private UserReport generateReport(Event event) {
        Gson gson = new Gson();
        UserReport report = new UserReport();
        List<Payment> payments = Arrays.asList(gson.fromJson(event.getArgument(0, String.class), Payment[].class));
        DTUPayUser User = gson.fromJson(event.getArgument(1, String.class), DTUPayUser.class);
        report.setPayments(payments);
        report.setUser(User);
        return report;
    }

    @Override
    public void registerTransaction(Payment payment, String customerId) {
        Event event = new Event(registerTransaction.getName(), new Object[] { payment, customerId });
        requests.put(event.getUUID(), new CompletableFuture<>());
        this.sender.sendEvent(event);
        requests.get(event.getUUID()).join();
    }

    @Override
    public List<Transaction> generateManagerOverview() {
        Event event = new Event(generateManagerOverview.getName());
        requests.put(event.getUUID(), new CompletableFuture<>());
        this.sender.sendEvent(event);

        Gson gson = new Gson();
        event = requests.get(event.getUUID()).join();
        return Arrays.asList(gson.fromJson(event.getArgument(0, String.class), Transaction[].class));
    }

    @Override
    public void receiveEvent(Event event) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null)
                cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
