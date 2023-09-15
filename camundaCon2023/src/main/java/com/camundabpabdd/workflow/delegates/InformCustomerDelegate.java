package com.camundabpabdd.workflow.delegates;

import com.camundabpabdd.workflow.apis.HttpApi;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named("InformCustomer")
public class InformCustomerDelegate implements JavaDelegate {
    @Autowired
    HttpApi httpApi;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        httpApi.informCustomer();

    }
}
