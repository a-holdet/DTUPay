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
                new EventQueue().registerReceiver(service);
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

    static final String generateReportForCustomer = "generateReportForCustomer";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> generateReportForCustomerCF = new ConcurrentHashMap<>();

    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime)
            throws CustomerDoesNotExistException {
        Event event = new Event(generateReportForCustomer, new Object[] { customerId, startTime, endTime },
                UUID.randomUUID());
        try {
            generateReportForCustomerCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = generateReportForCustomerCF.get(event.getUUID()).join();
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

    static final String generateReportForMerchant = "generateReportForMerchant";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> generateReportForMerchantCF = new ConcurrentHashMap<>();

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime)
            throws MerchantDoesNotExistException {
        Event event = new Event(generateReportForMerchant, new Object[] { merchantId, startTime, endTime });
        try {
            generateReportForMerchantCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = generateReportForMerchantCF.get(event.getUUID()).join();
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

    static final String registerTransaction = "registerTransaction";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> registerTransactionCF = new ConcurrentHashMap<>();

    @Override
    public void registerTransaction(Payment payment, String customerId) {
        Event event = new Event(registerTransaction, new Object[] { payment, customerId });
        try {
            registerTransactionCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }
        registerTransactionCF.get(event.getUUID()).join();
    }

    static final String generateManagerOverview = "generateManagerOverview";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> generateManagerOverviewCF = new ConcurrentHashMap<>();

    @Override
    public List<Transaction> generateManagerOverview() {
        Event event = new Event(generateManagerOverview);
        try {
            generateManagerOverviewCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        Gson gson = new Gson();
        event = generateManagerOverviewCF.get(event.getUUID()).join();
        return Arrays.asList(gson.fromJson(event.getArgument(0, String.class), Transaction[].class));
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        CompletableFuture<Event> cf;
        switch (event.getEventType()) {
            case generateReportForCustomer + "Success":
            case generateReportForCustomer + "Fail":
                cf = generateReportForCustomerCF.getOrDefault(event.getUUID(), null);
                if (cf != null)
                    cf.complete(event);
                break;
            case generateReportForMerchant + "Success":
            case generateReportForMerchant + "Fail":
                cf = generateReportForMerchantCF.getOrDefault(event.getUUID(), null);
                if (cf != null)
                    cf.complete(event);
                break;
            case registerTransaction:
                cf = registerTransactionCF.getOrDefault(event.getUUID(), null);
                if (cf != null)
                    cf.complete(event);
                break;
            case generateManagerOverview:
                cf = generateManagerOverviewCF.getOrDefault(event.getUUID(), null);
                if (cf != null)
                    cf.complete(event);
                break;
            default:
                break;
        }
        System.out.println("--------------------------------------------------------");
    }
}
