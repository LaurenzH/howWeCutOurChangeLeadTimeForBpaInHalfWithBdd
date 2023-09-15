package cucumber;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

public class TestStepDefs {
    private Map<String, Object> variables;
    private ProcessInstance processInstance;
    private static WireMockServer wireMockServer;

    private String brand;

    private String model;
    String informCustomerUrl = "/informCustomer";
    String createNewContractUrl = "/createNewContract";
    String forwardToManualProcessingUrl = "/forwardToManualProcessing";
    private static boolean wireMockServerStarted;

    @Given("Camunda is ready")
    public void camundaIsReady() {
        if (!wireMockServerStarted) {
            wireMockServer = new WireMockServer(options().port(1000));
            wireMockServer.start();
            wireMockServerStarted = true;
        }
        variables = new HashMap<>();
        wireMockServer.stubFor(post(urlPathEqualTo(informCustomerUrl)).willReturn(ok()));
        wireMockServer.stubFor(post(urlPathEqualTo(createNewContractUrl)).willReturn(ok()));
        wireMockServer.stubFor(post(urlPathEqualTo(forwardToManualProcessingUrl)).willReturn(ok()));

    }

    @After
    public void resetWireMock() {
        wireMockServer.resetAll();
    }

    @Given("the car is a {string}")
    public void theCarIsA(String carType) throws Exception {
        switch (carType) {
            case "new car" -> variables.put("carType", "newCar");
            case "vintage car" -> variables.put("carType", "vintageCar");
            default -> throw new Exception("car type '" + carType + "' not implemented in test yet, please add it.");
        }
    }


    @Given("the car brand is a {string}")
    public void theCarBrandIsA(String carBrand) {
        variables.put("carBrand", carBrand);
        brand = carBrand;
    }

    @Given("the car model is {string}")
    public void theCarModelIs(String carModel) {
        variables.put("carModel", carModel);
        model = carModel;
    }

    @When("the process is started")
    public void theProcessIsStarted() {
        processInstance = runtimeService().startProcessInstanceByKey("CarInsurance", variables);
    }

    @Then("the process is completed")
    public void theProcessIsCompleted() {
        waitUntilProcessIsEnded();
    }

    private void waitUntilProcessIsEnded() {
        final int maxRetries = 100;
        int counter = 0;
        final int waitMilliSeconds = 300;
        while (counter < maxRetries) {
            try {
                assertThat(processInstance).isEnded();
                return;
            } catch (final AssertionError e) {
                counter++;
                try {
                    Thread.sleep(waitMilliSeconds);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        assertThat(processInstance).isEnded();
    }

    @Given("the backend for automatic insurance contract creation is not reachable")
    public void theBackendForAutomaticInsuranceContractCreationIsNotReachable() {
        wireMockServer.stubFor(post(urlPathEqualTo(createNewContractUrl)).willReturn(serverError()));
    }

    @Then("a new contract {string} created for the given brand and model")
    public void aNewContractCreatedForTheGivenBrandAndModel(String state) throws Exception {

        if ("was".equals(state)) {
            wireMockServer.verify(postRequestedFor(urlPathMatching(createNewContractUrl))
                    .withRequestBody(containing(brand).and(containing(model))));
        } else if ("was not".equals(state)) {
            wireMockServer.verify(0, postRequestedFor(urlPathMatching(createNewContractUrl)));
        } else if ("could not have been".equals(state)) {
            wireMockServer.verify(postRequestedFor(urlPathMatching(createNewContractUrl))
                    .withRequestBody(containing(brand).and(containing(model))));
            assertThat(processInstance).variables().containsEntry("successfulCreation", false);
        } else {
            throw new Exception("state '" + state + "' not implemented in test yet, please add it.");
        }

    }

    @Then("the costumer {string} informed about the new insurance policy")
    public void theCostumerInformedAboutTheNewInsurancePolicy(String state) throws Exception {
        boolean informed;
        switch (state) {
            case "was" -> informed = true;
            case "was not" -> informed = false;
            default -> throw new Exception("state '" + state + "' not implemented in test yet, please add it.");
        }
        if (informed) {
            wireMockServer.verify(postRequestedFor(urlPathMatching(informCustomerUrl)));
        } else {
            wireMockServer.verify(0, postRequestedFor(urlPathMatching(informCustomerUrl)));
        }
    }

    @Then("{string} job for a clerk was created")
    public void jobForAClerkWasCreated(String state) throws Exception {
        boolean isManualJob;
        switch (state) {
            case "a" -> isManualJob = true;
            case "no" -> isManualJob = false;
            default -> throw new Exception("state '" + state + "' not implemented in test yet, please add it.");
        }
        if (isManualJob) {
            wireMockServer.verify(postRequestedFor(urlPathMatching(forwardToManualProcessingUrl)));
        } else {
            wireMockServer.verify(0, postRequestedFor(urlPathMatching(forwardToManualProcessingUrl)));
        }
    }
}
