# State Restore

 Description
 -----------
 State restore fetches previously store state from a tracking table and sets a run-time argument to be used for rest of the pipeline. The state is stored in a variable called ${state} which can
 be substituted in any plugin property that is macro enabled.


 Use Case
 --------

State restore is used when a data pipeline needs to perform any operation that relies on previous run of the pipeline.
As an example, to perform incremental data ingestion from a database, an identifier that represents a starting point for the incremental ingest (ex: maximum value of update time in the database table) can be stored
on each successful pipeline run, which then can be used as the starting point for subsequent data ingestion in the database select query.



 Properties
 ----------

**trackingTable:** Tracking table where state is stored

**key:** Key to retrieve state stored in tracking table

**defaultValue:** Default value used if state is not present in tracking table

