package io.temporal.samples.springboot.dividend;

import io.temporal.activity.ActivityOptions;
import io.temporal.samples.springboot.dividend.model.DividendCorporateAction;
import io.temporal.samples.springboot.dividend.model.Position;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;

@WorkflowImpl(taskQueues = "DividendWorkflowTaskQueue")
public class DividendWorkflowImpl implements DividendWorkflow {
  private Logger logger = Workflow.getLogger(DividendWorkflowImpl.class.getName());
  private PositionActivity positionActivity =
      Workflow.newActivityStub(
          PositionActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.of(2L, ChronoUnit.SECONDS))
              .build());

  private DomainEventSenderActivity domainEventSenderActivity =
      Workflow.newActivityStub(
          DomainEventSenderActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.of(2L, ChronoUnit.SECONDS))
              .build());

  private CreateCashTransactionActivity createCashTransactionActivity =
      Workflow.newActivityStub(
          CreateCashTransactionActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.of(2L, ChronoUnit.SECONDS))
              .build());
  private boolean checkPositions = false;
  private boolean checkCashTransactions = false;

  @Override
  public void execute(DividendCorporateAction dividendCorporateAction) {
    logger.info("Starting dividend workflow");
    while (LocalDateTime.now(ZoneId.systemDefault()).isAfter(dividendCorporateAction.getExDate())) {
      positionActivity.fetchPositions().forEach(it -> logger.info("logging position" + it));
      Workflow.await(
          Duration.of(30L, ChronoUnit.SECONDS),
          () ->
              LocalDateTime.now(ZoneId.systemDefault())
                  .isAfter(dividendCorporateAction.getExDate()));
    }
    logger.info("Waiting for positions");
    Workflow.await(() -> checkPositions);
    logger.info("Positions approved");
    List<Position> positions = positionActivity.fetchPositions();
    createCashTransactionActivity.saveCashTransactions(positions, dividendCorporateAction);
    // TODO How do we cancel await and cancel the whole Workflow execution
    logger.info("Waiting for Cash Transactions");
    Workflow.await(() -> checkCashTransactions);
    domainEventSenderActivity.sendDomainEvent(dividendCorporateAction.getCorporateActionId());
    logger.info("Cash Transactions approved");
  }

  @Override
  public void approvePositions(boolean approve) {
    logger.info("Setting approve positions " + approve);
    this.checkPositions = approve;
  }

  @Override
  public void approveCashTransaction(boolean approve) {
    this.checkCashTransactions = approve;
  }
}
