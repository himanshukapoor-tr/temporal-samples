package io.temporal.samples.springboot;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.springboot.dividend.CashTransactionRepository;
import io.temporal.samples.springboot.dividend.DividendWorkflow;
import io.temporal.samples.springboot.dividend.model.CashTransaction;
import io.temporal.samples.springboot.dividend.model.DividendCorporateAction;
import io.temporal.samples.springboot.dividend.model.Position;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corporate/action")
public class CorporateActionController {

  @Autowired WorkflowClient workflowClient;

  @Autowired CashTransactionRepository cashTransactionRepository;

  private UUID workflowId = UUID.randomUUID();
  private UUID corporateActionId = UUID.randomUUID();

  @GetMapping("/positions")
  public List<Position> getPositions() {
    return Arrays.asList(
        new Position("0001", 1, "ISIN12345"), new Position("0002", 2, "ISIN12345"));
  }

  @PostMapping(
      value = "/dividend/execution",
      produces = {MediaType.TEXT_HTML_VALUE})
  public ResponseEntity createDividendWorkflowExecution() {
    DividendCorporateAction dividendCorporateAction =
        new DividendCorporateAction(
            BigDecimal.valueOf(1.50d),
            LocalDateTime.now(ZoneId.systemDefault()).plus(2L, ChronoUnit.MINUTES),
            corporateActionId);
    DividendWorkflow dividendWorkflow =
        workflowClient.newWorkflowStub(
            DividendWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("DividendWorkflowTaskQueue")
                .setWorkflowId("DividendWorkflow" + workflowId)
                .build());
    WorkflowClient.start(dividendWorkflow::execute, dividendCorporateAction);
    return new ResponseEntity<>("\"" + corporateActionId + "\"", HttpStatus.OK);
  }

  @PostMapping(value = "/dividend/position/approval")
  public ResponseEntity sendPositionsApproval() {
    DividendWorkflow dividendWorkflow =
        workflowClient.newWorkflowStub(
            DividendWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("DividendWorkflowTaskQueue")
                .setWorkflowId("DividendWorkflow" + workflowId)
                .build());
    dividendWorkflow.approvePositions(true);
    return new ResponseEntity("", HttpStatus.OK);
  }

  @PostMapping(value = "/dividend/cash/transactions/approval")
  public ResponseEntity sendCashTransactionApproval() {
    DividendWorkflow dividendWorkflow =
        workflowClient.newWorkflowStub(
            DividendWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("DividendWorkflowTaskQueue")
                .setWorkflowId("DividendWorkflow" + workflowId)
                .build());
    dividendWorkflow.approveCashTransaction(true);
    return new ResponseEntity("", HttpStatus.OK);
  }

  @GetMapping(
      value = "/dividend/cash/transactions/",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<CashTransaction>> getCashTransactions() {
    List<CashTransaction> cashTransactions =
        cashTransactionRepository.findByCorporateActionId(corporateActionId);
    return new ResponseEntity(cashTransactions, HttpStatus.OK);
  }
}
