package io.cdap.plugin.staterestore.action;

import io.cdap.cdap.api.TxRunnable;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.common.Bytes;
import io.cdap.cdap.api.data.DatasetContext;
import io.cdap.cdap.api.dataset.lib.KeyValueTable;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.action.Action;
import io.cdap.cdap.etl.api.action.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Action <></>o restore state from a tracking table.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name("StateRestore")
@Description("Action plugin that restores state from a tracking table and passes as a runtime argument " +
               "${state} to be used in subsequent stages")
public class StateRestore extends Action {

  private StateRestoreActionConfig config;
  public static final Logger LOG = LoggerFactory.getLogger(StateRestore.class);

  public StateRestore(StateRestoreActionConfig config) {
    this.config = config;
  }

  @Override
  public void run(final ActionContext actionContext) throws Exception {
    actionContext.execute(new TxRunnable() {
      @Override
      public void run(DatasetContext datasetContext) throws Exception {
        KeyValueTable trackingTable  = (KeyValueTable) datasetContext.getDataset(config.trackingTable);
        byte[] value = trackingTable.read(config.key);
        String state = value == null? config.defaultValue :  Bytes.toString(value);
        actionContext.getArguments().set("state", state);
        LOG.debug("State: {}", state);
      }
    });
  }


  public class StateRestoreActionConfig extends PluginConfig {
    @Description("Tracking table where state is stored")
    @Macro
    private String trackingTable;

    @Description("Key to retrieve state stored in tracking table")
    @Macro
    private String key;

    @Description("Default value used if state is not present in tracking table")
    @Macro
    @Nullable
    private String defaultValue;

    StateRestoreActionConfig(String trackingTable, String key) {
      this(trackingTable, key, "");
    }

    StateRestoreActionConfig(String trackingTable, String key, String defaultValue) {
      this.trackingTable = trackingTable;
      this.key = key;
      this.defaultValue = defaultValue;
    }
  }
}
