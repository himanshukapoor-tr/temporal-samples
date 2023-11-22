package io.temporal.samples.springboot.dividend;

import io.temporal.samples.springboot.dividend.model.DividendCorporateAction;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DividendWorkflow {

  @WorkflowMethod
  void execute(DividendCorporateAction dividendCorporateAction);

  @SignalMethod
  void approvePositions(boolean approve);

  @SignalMethod
  void approveCashTransaction(boolean approve);
}
