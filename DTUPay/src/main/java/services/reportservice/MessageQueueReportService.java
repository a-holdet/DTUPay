package services.reportservice;

import DTO.*;

import java.util.List;
import com.google.gson.reflect.TypeToken;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.MessageQueueBase;

/***
 * @Author Martin Hemmingsen, s141887
 */

public class MessageQueueReportService extends MessageQueueBase implements IReportService, IEventReceiver {

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

    private enum AccountType {
        CUSTOMER(generateReportForCustomer), MERCHANT(generateReportForMerchant);
        public EventType eventType;
        AccountType(EventType eventType) {
            this.eventType = eventType;
        }
    }

    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime) throws UserDoesNotExistsException {
        ReportCreationDTO reportCreationDTO = new ReportCreationDTO(customerId, startTime, endTime);
        if (customerId == null)
            throw new UserDoesNotExistsException("The customer does not exists in DTUPay");
        return generateReport(AccountType.CUSTOMER, reportCreationDTO);
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime)
            throws UserDoesNotExistsException {
        if (merchantId == null){
            throw new UserDoesNotExistsException("The merchant does not exists in DTUPay");
        }
        return generateReport(AccountType.MERCHANT, new ReportCreationDTO(merchantId, startTime, endTime));
    }

    private UserReport generateReport(AccountType accountType, ReportCreationDTO reportCreationDTO) throws UserDoesNotExistsException {
        var responseEvent = sendRequestAndAwaitResponse(reportCreationDTO, accountType.eventType);
        if (responseEvent.isSuccessReponse()){
            return responseEvent.getPayloadAs(UserReport.class);
        }
        throw new UserDoesNotExistsException(responseEvent.getErrorMessage());
    }

    @Override
    public List<Transaction> generateManagerOverview() {
        Event responseEvent = sendRequestAndAwaitResponse("", generateManagerOverview);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(new TypeToken<>(){});

        throw new Error(responseEvent.getErrorMessage());
    }

}
