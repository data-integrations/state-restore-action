/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.staterestore.action;

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.FailureCollector;

import javax.annotation.Nullable;

/**
 * Config class for {@link StateRestore}.
 */
public class StateRestoreConfig extends PluginConfig {
  public static final String TRACKING_TABLE = "trackingTable";
  public static final String KEY = "key";
  public static final String DEFAULT_VALUE = "defaultValue";

  @Name(TRACKING_TABLE)
  @Description("Tracking table where state is stored")
  @Macro
  private final String trackingTable;

  @Name(KEY)
  @Description("Key to retrieve state stored in tracking table")
  @Macro
  private final String key;

  @Name(DEFAULT_VALUE)
  @Description("Default value used if state is not present in tracking table")
  @Macro
  @Nullable
  private final String defaultValue;

  StateRestoreConfig(String trackingTable, String key, String defaultValue) {
    this.trackingTable = trackingTable;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public String getTrackingTable() {
    return trackingTable;
  }

  public String getKey() {
    return key;
  }

  @Nullable
  public String getDefaultValue() {
    return defaultValue;
  }

  public void validate(FailureCollector collector) {
    if (!containsMacro(TRACKING_TABLE) && Strings.isNullOrEmpty(trackingTable)) {
      collector.addFailure("Tracking table must be specified.", null)
        .withConfigProperty(TRACKING_TABLE);
    }

    if (!containsMacro(KEY) && Strings.isNullOrEmpty(key)) {
      collector.addFailure("Key must be specified.", null)
        .withConfigProperty(KEY);
    }
  }
}
