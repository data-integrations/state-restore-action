package io.cdap.plugin.staterestore.action;

import io.cdap.cdap.api.TxRunnable;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.common.Bytes;
import io.cdap.cdap.api.data.DatasetContext;
import io.cdap.cdap.api.dataset.lib.KeyValueTable;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.cdap.etl.api.PipelineConfigurer;
import io.cdap.cdap.etl.api.action.Action;
import io.cdap.cdap.etl.api.action.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action <></>o restore state from a tracking table.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name("StateRestore")
@Description("Action plugin that restores state from a tracking table and passes as a runtime argument " +
               "${state} to be used in subsequent stages")
public class StateRestore extends Action {
  private static final Logger LOG = LoggerFactory.getLogger(StateRestore.class);
  private StateRestoreConfig config;

  public StateRestore(StateRestoreConfig config) {
    this.config = config;
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
    FailureCollector collector = pipelineConfigurer.getStageConfigurer().getFailureCollector();
    config.validate(collector);
    collector.getOrThrowException();
  }

  @Override
  public void run(final ActionContext actionContext) throws Exception {
    actionContext.execute(new TxRunnable() {
      @Override
      public void run(DatasetContext datasetContext) {
        KeyValueTable trackingTable = datasetContext.getDataset(config.getTrackingTable());
        byte[] value = trackingTable.read(config.getKey());
        String state = value == null ? config.getDefaultValue() : Bytes.toString(value);
        actionContext.getArguments().set("state", state);
        LOG.debug("State: {}", state);
      }
    });
  }
}
