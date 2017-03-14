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
  **name:** Name of the ``PartitionedFileSet`` to which records are written.
  If it doesn't exist, it will be created.

  **schema:** The Avro schema of the record being written to the sink as a JSON Object.

  **basePath:** Base path for the ``PartitionedFileSet``. Defaults to the name of the dataset.

  **compressionCodec:** Optional parameter to determine the compression codec to use on the resulting data.
  Valid values are None, Snappy, GZip, and LZO.

  Example
  -------
  This example will write to a ``PartitionedFileSet`` named ``'users'`` using the original
  ``'create_date'`` of the record as the partition:

      {
          "name": "DynamicPFSParquet",
          "type": "batchsink",
          "properties": {
              "name": "users",
              "fieldNames": "create_date",
              "schema": "{
                  \"type\":\"record\",
                  \"name\":\"user\",
                  \"fields\":[
                      {\"name\":\"id\",\"type\":\"long\"},
                      {\"name\":\"name\",\"type\":\"string\"},
                      {\"name\":\"create_date\",\"type\":\"string\"}
                  ]
              }"
          }
      }

  It will write data in Parquet format using the given schema. Every time the pipeline runs,
  partitions will be determined in the ``PartitionedFileSet`` based on the ``create_date``
  of each record.