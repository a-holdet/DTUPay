package messaging.rmq.event.objects;

/***
 * Christian Dan Hjelmslund, s164412
 */
public class Result<S, F> {

    public static enum ResultState {
        FAILURE, SUCCESS
    }
    public S successValue;
    public F failureValue;
    public ResultState state;

    public Result(S successValue, F failureValue, ResultState state) {
        this.successValue = successValue;
        this.failureValue = failureValue;
        this.state = state;
    }
}
