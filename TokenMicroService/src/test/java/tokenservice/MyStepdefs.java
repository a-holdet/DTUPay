package tokenservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.objects.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {

    @When("yo")
    public void iReceiveEvent() throws Exception {

    }

    @Then("whatup")
    public void iHaveSentEvent() {

    }
}
