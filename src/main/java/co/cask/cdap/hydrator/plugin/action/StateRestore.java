package co.cask.cdap.hydrator.plugin.action;

import co.cask.cdap.api.TxRunnable;
import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.common.Bytes;
import co.cask.cdap.api.data.DatasetContext;
import co.cask.cdap.api.dataset.lib.KeyValueTable;
import co.cask.cdap.api.plugin.PluginConfig;
import co.cask.cdap.etl.api.action.Action;
import co.cask.cdap.etl.api.action.ActionContext;
import com.google.common.annotations.VisibleForTesting;
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
        LOG.trace("State: {}", state);
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
