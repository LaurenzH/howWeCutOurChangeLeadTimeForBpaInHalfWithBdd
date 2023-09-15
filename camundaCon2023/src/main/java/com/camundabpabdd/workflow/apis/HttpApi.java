package com.camundabpabdd.workflow.apis;

import com.camundabpabdd.workflow.services.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpApi {
    @Autowired
    HttpService httpService;

    @Value("${informCustomerUrl}")
    String informCustomerUrl;
    @Value("${createNewContractUrl}")
    String createNewContractUrl = "/createNewContract";
    @Value("${forwardToManualProcessingUrl}")
    String forwardToManualProcessingUrl;

    public boolean createNewContract(String carModel, String carBrand) throws Exception {
        final String body = getBody(carModel, carBrand);
        var httpResponse = httpService.httpCall(createNewContractUrl, body);
        return String.valueOf(httpResponse.statusCode()).matches("2\\d\\d");
    }


    public void forwardToManualProcessing(String carModel, String carBrand) throws Exception {
        final String body = getBody(carModel, carBrand);
        httpService.httpCall(forwardToManualProcessingUrl, body);
    }

    public void informCustomer() throws Exception {
        httpService.httpCall(informCustomerUrl, "");
    }

    private static String getBody(String carModel, String carBrand) {
        return "{ \"carModel\": \"" + carModel + "\", \"carBrand\": \" " + carBrand + "\" }";
    }
}
