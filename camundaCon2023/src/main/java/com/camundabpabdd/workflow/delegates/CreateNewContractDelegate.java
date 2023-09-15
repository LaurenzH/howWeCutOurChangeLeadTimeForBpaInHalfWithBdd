package com.camundabpabdd.workflow.delegates;

import com.camundabpabdd.workflow.apis.HttpApi;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named("CreateNewContract")
public class CreateNewContractDelegate implements JavaDelegate {
    @Autowired
    HttpApi httpApi;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        var carModel = (String) delegateExecution.getVariable("carModel");
        var carBrand = (String) delegateExecution.getVariable("carBrand");

        final boolean success = httpApi.createNewContract(carModel, carBrand);
        delegateExecution.setVariable("successfulCreation", success);
    }
}
