package co.cask.cdap.hydrator.plugin.action;

import co.cask.cdap.api.TxRunnable;
import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.common.Bytes;
import co.cask.cdap.api.data.DatasetContext;
import co.cask.cdap.api.dataset.lib.KeyValueTable;
import co.cask.cdap.api.plugin.PluginConfig;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.action.Action;
import co.cask.cdap.etl.api.action.ActionContext;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action To Read the Offset from a keyValue Table.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name("OffsetReader")
@Description("Action plugin that reads the offset that is tracked for incremental processing")
public class OffsetReader extends Action {

  private OffsetReaderActionConfig config;
  public static final Logger LOG = LoggerFactory.getLogger(OffsetReader.class);

  public OffsetReader(OffsetReaderActionConfig config) {
    this.config = config;
  }

  @Override
  public void run(final ActionContext actionContext) throws Exception {
    actionContext.execute(new TxRunnable() {
      @Override
      public void run(DatasetContext datasetContext) throws Exception {
        KeyValueTable trackingTable  = (KeyValueTable) datasetContext.getDataset(config.offsetTable);
        byte[] value = trackingTable.read(config.offsetKey);
        String offset = value == null? "1" :  Bytes.toString(value);
        actionContext.getArguments().set("offset", offset);
        LOG.debug("offset {}", offset);
      }
    });
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) throws IllegalArgumentException {
    // Create Dataset
//    pipelineConfigurer.createDataset(config.offsetTable, KeyValueTable.class);
//
  }


  public class OffsetReaderActionConfig extends PluginConfig {
    @Description("Table to Track offset")
    private String offsetTable;

    @Description("offsetKey")
    private String offsetKey;

    @VisibleForTesting
    OffsetReaderActionConfig(String offsetTable, String offsetKey) {
      this.offsetTable = offsetTable;
      this.offsetKey = offsetKey;
    }
  }
}
