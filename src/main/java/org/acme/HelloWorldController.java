
package org.acme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.kie.kogito.app.DecisionModels;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.kogito.app.Application;
import org.kie.kogito.dmn.rest.DMNEvaluationErrorException;
import org.kie.kogito.dmn.rest.DMNJSONUtils;
import org.kie.kogito.dmn.rest.KogitoDMNResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;




@org.springframework.stereotype.Component
@RestController
@ComponentScan("org.kie.kogito.app")
public class HelloWorldController {

    @org.springframework.beans.factory.annotation.Autowired() @Qualifier("application")
    Application application;

    @org.springframework.beans.factory.annotation.Autowired()
    DecisionModels models;

    private static final String KOGITO_DECISION_INFOWARN_HEADER = "X-Kogito-decision-messages";

    private static final String KOGITO_EXECUTION_ID_HEADER = "X-Kogito-execution-id";

    private static final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule()).registerModule(new com.fasterxml.jackson.databind.module.SimpleModule().addSerializer(org.kie.dmn.feel.lang.types.impl.ComparablePeriod.class, new org.kie.kogito.dmn.rest.DMNFEELComparablePeriodSerializer())).disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);


    @GetMapping("/say")
    public Map<String, String> sayHello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");

       
        return map;
    }

    @GetMapping("/inventory")
    public Map<String, String> vmgmt() {
        HashMap<String, String> map = new HashMap<>();
 
        System.out.println(models.soledmn);
        System.out.println("yea" + models.solens);
        org.kie.kogito.decision.DecisionModel decision = application.get(org.kie.kogito.decision.DecisionModels.class).  getDecisionModel("https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF", "popme");
        System.out.println(decision.toString());
        System.out.println(decision.getDMNModel().getName());

        Set<DecisionNode> nodes = decision.getDMNModel().getDecisions();

        for (DecisionNode n : nodes)
        {
            map.put(n.getName(), decision.getDMNModel().getNamespace());

        }
        return map;
    }

    @PostMapping(value = "traffic", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(ref = "/dmnDefinitions.json#/definitions/InputSet2")), description = "DMN input")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(ref = "/dmnDefinitions.json#/definitions/OutputSet2")), description = "DMN output")
    public ResponseEntity<?> dmn(@RequestBody(required = false) java.util.Map<String, Object> variables, HttpServletResponse httpResponse) {
        //org.kie.kogito.decision.DecisionModel decision = application.get(org.kie.kogito.decision.DecisionModels.class).getDecisionModel("https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF", "popme");
        org.kie.kogito.decision.DecisionModel decision = application.get(org.kie.kogito.decision.DecisionModels.class).getDecisionModel("http://www.redhat.com/_c7328033-c355-43cd-b616-0aceef80e52a", "50kDecisionTable");
        
        org.kie.dmn.api.core.DMNResult decisionResult = decision.evaluateAll(DMNJSONUtils.ctx(decision, variables));
        enrichResponseHeaders(decisionResult, httpResponse);
        //KogitoDMNResult result = new KogitoDMNResult("https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF", "popme", decisionResult);
        KogitoDMNResult result = new KogitoDMNResult("http://www.redhat.com/_c7328033-c355-43cd-b616-0aceef80e52a", "50kDecisionTable", decisionResult);
        
        return extractContextIfSucceded(result);
    }

    private void enrichResponseHeaders(org.kie.dmn.api.core.DMNResult result, HttpServletResponse httpResponse) {
        if (!result.getMessages().isEmpty()) {
            String infoWarns = result.getMessages().stream().map(m -> m.getLevel() + " " + m.getMessage()).collect(Collectors.joining(", "));
            httpResponse.addHeader(KOGITO_DECISION_INFOWARN_HEADER, infoWarns);
        }
        org.kie.kogito.decision.DecisionExecutionIdUtils.getOptional(result.getContext()).ifPresent(executionId -> httpResponse.addHeader(KOGITO_EXECUTION_ID_HEADER, executionId));
    }

    private ResponseEntity extractContextIfSucceded(KogitoDMNResult result) {
        if (!result.hasErrors()) {
            return ResponseEntity.ok(buildResponse(result.getDmnContext()));
        } else {
            return buildFailedEvaluationResponse(result);
        }
    }

    private ResponseEntity buildFailedEvaluationResponse(KogitoDMNResult result) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    private String buildResponse(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




}

