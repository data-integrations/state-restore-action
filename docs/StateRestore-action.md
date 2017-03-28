# State Restore Action

  Description
  -----------
  Action plugin that fetches a stored state from a tracking table and passes it as runtime argument which can
  be used for the rest of the pipeline.


  Use Case
  --------

  This action is used when a pipeline needs to perform any operation that relies on previous run of the pipeline.
  As an example, for incremental data ingestion an identifier that represents an increment (ex: update_time) will
  need to be stored on successful pipeline run, which then can be used to restore for the next run of the pipeline.


  Properties
  ----------

  **trackingTable:** Name of the tracking table to retrieve state 
  **key:** Key to store the state. Note, multiple pipelines can share same tracking table if writing different keys to store state
  **defaultValue:** Default value to use if the key is not present in tracking table

