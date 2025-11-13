# AWS Tests
Functional tests for AWS SDK

## Kinesis
The Kinesis workflow in the example adapter configuration demonstrates how to configure a producer to a Kinesis stream.
The example can be triggered by calling the endpoint:

```
/aws2/kinesis?stream=<stream>&partition=<partition>

Where:
<stream> is the name of the configured stream
<partition> is the name of the partition
```
By default, a stream called "stream1" is automatically created in LocalStack.