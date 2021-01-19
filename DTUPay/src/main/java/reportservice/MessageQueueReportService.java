package reportservice;

import DTO.DTUPayUser;
import DTO.Transaction;
import DTO.UserReport;
import DTO.Payment;

import java.util.Arrays;
import java.util.List;

import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;
import com.google.gson.reflect.TypeToken;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventServiceBase;
import messaging.rmq.event.objects.EventType;

import java.util.concurrent.CompletableFuture;


public class MessageQueueReportService extends EventServiceBase implements IReportService, IEventReceiver {

    private static final EventType generateReportForCustomer = new EventType("generateReportForCustomer");
    private static final EventType generateReportForMerchant = new EventType("generateReportForMerchant");
    private static final EventType registerTransaction = new EventType("registerTransaction");
    private static final EventType generateManagerOverview = new EventType("generateManagerOverview");
    private static final EventType[] supportedEventTypes =
            new EventType[] {generateReportForCustomer, generateReportForMerchant, registerTransaction, generateManagerOverview};

    public MessageQueueReportService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime) throws CustomerDoesNotExistException {
        Event event = new Event(generateReportForCustomer.getName(), new Object[] { customerId, startTime, endTime });
        try {
            requests.put(event.getUUID(), new CompletableFuture<>());
            sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = requests.get(event.getUUID()).join();
        String type = event.getEventType();

        if (event.isSuccessReponse()) {
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
        System.out.println("---------------------------------------------");
        Event event = new Event(generateReportForMerchant.getName(), new Object[] { merchantId, startTime, endTime });
        try {
            requests.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }
        System.out.println("---------------------------------------------");

        event = requests.get(event.getUUID()).join();
        String type = event.getEventType();

        if (event.isSuccessReponse()) {
            return generateReport(event);
        }
        String exceptionMsg = event.getArgument(1, String.class);
        throw new MerchantDoesNotExistException(exceptionMsg);
    }

    private UserReport generateReport(Event event) {
        System.out.println("DTUPay successfully received report: " + event.getEventType());
        UserReport report = new UserReport();
        DTUPayUser user = event.getArgument(0,DTUPayUser.class);
        System.out.println("DTUPay report user is " + user.getFirstName() + user.getCprNumber());
        List<Payment> payments = event.getArgument(1, new TypeToken<>(){});
        System.out.println("payments contain " + payments.size());
        report.setPayments(payments);
        report.setUser(user);
        return report;
    }

    @Override
    public List<Transaction> generateManagerOverview() {
        Event event = new Event(generateManagerOverview.getName());
        try {
            requests.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = requests.get(event.getUUID()).join();

        List<Transaction> transactions = event.getArgument(0, new TypeToken<>() {});
        //List<Transaction> transactions = (List<Transaction>) event.getArguments()[0];
        return transactions;
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
