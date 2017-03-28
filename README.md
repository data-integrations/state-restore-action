State Restore
=============

<a href="https://cdap-users.herokuapp.com/"><img alt="Join CDAP community" src="https://cdap-users.herokuapp.com/badge.svg?t=state-restore-action"/></a>
[![Build Status](https://travis-ci.org/hydrator/state-restore-action.svg?branch=develop)](https://travis-ci.org/hydrator/state-restore-action) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) <img src="https://cdap-users.herokuapp.com/assets/cdap-action.svg"/>


State restore fetches previously store state from a tracking table and sets a run-time argument to be used for rest of the pipeline. State restore is used when a data pipeline needs to perform any operation that relies on previous run of the pipeline.
As an example, to perform incremental data ingestion from a database, an identifier that represents a starting point for the incremental ingest (ex: maximum value of update time in the database table) can be stored
on each successful pipeline run, which then can be used as the starting point for subsequent data ingestion in the database select query. The state is stored in a variable called ${state} which can
 be substituted in any plugin property that is macro enabled.

Usage Notes
-----------

This plugin is used to restore state that is stored in the previous run of the pipeline and set the state as runtime argument in the current run. The plugin uses a tracking table, which is a key-value table to store the state the pipeline state.
The tracking table name can be  specified by configuring the "Tracking Table" property and the key to store the state is configured using the "Key" property. The sink that maintains the pipeline state
should write to a key-value table.

If the key is not found in the tracking table a default that is configured by the user in the "Default Value" configuration will be returned, the default value is typically used when the pipeline is run for the first time. All the plugin configurations are macro enabled and the values can be specified in the
pipeline deployment time or at pipeline runtime.

The plugin does not create the tracking table on deployment, this is because the pipeline assumes the sink which writes the state will create the tracking table.

Plugin Configuration
--------------------

| Configuration | Required | Default | Description |
| :------------ | :------: | :----- | :---------- |
| **Tracking Table Name** | **Y** | trackingTable | Specifies the tracking table to store the pipeline state. This plugin does not create the table, the table creation will be performed by the sink that writes to the tracking table.|
| **State Key** | **Y** | key | Specifies the key in the tracking table to retrieve the state. |
| **Default Value** | **N** | '' | Specifies the default value to return if the state is not present in the tracking table|



Build
-----
To build this plugin:

```
   mvn clean package
```    

The build will create a .jar and .json file under the ``target`` directory.
These files can be used to deploy your plugins.

Deployment
----------
You can deploy your plugins using the CDAP CLI:

    > load artifact <target/state-restore-action-<version>.jar config-file <target/state-restore-action-<version>.json>

## Mailing Lists

CDAP User Group and Development Discussions:

* `cdap-user@googlegroups.com <https://groups.google.com/d/forum/cdap-user>`

The *cdap-user* mailing list is primarily for users using the product to develop
applications or building plugins for appplications. You can expect questions from 
users, release announcements, and any other discussions that we think will be helpful 
to the users.

## Slack Channel

CDAP Slack Channel: http://cdap-users.herokuapp.com/


## License and Trademarks

Copyright © 2017 Cask Data, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language governing permissions 
and limitations under the License.

Cask is a trademark of Cask Data, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. Used with
permission. No endorsement by The Apache Software Foundation is implied by the use of these marks.    