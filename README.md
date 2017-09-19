# Zoo(Au)ditor
Zooditor is a tool to log changes on ZooKeeper znodes.

!!! EXPERIMENTAL TEST TEST TEST !!!


## Build
```
sbt compile
```

## Package
Create a fat-jar with all dependencies.
```
sbt assembly
```

## Run the application
### Using sbt
```
sbt -Dconfig.file=config/app.conf run
```

### Using Java
Make sure to package the fat-jar first.
```
java -jar -Dconfig.file=config/app.conf target/scala-2.12/zooditor.jar
```

## Configuration
Zooditor uses the HOCON notation for its configuration file.

```
app {
  zk {
    connect = "localhost:2181"
    timeout = 60000
  }
  out {
    file = "/tmp/zk-audit.log"
    format = "plain"
  }
  paths = [
    "/test/path/one"
    "/test/path/two"
  ]
}
```

### Property description
| Name | Description | Type | Options |
|------|-------------|------|---------|
| zk.connect | ZooKeeper connection string, a comma separated list of ZooKeeper nodes | string | |
| zk.timeout | ZooKeeper connection timeout in milliseconds | int ||
| out.file   | Audit log for znode changes | string ||
| out.format | Log format | string | [plain, json] |
| paths | A list of znode paths to observe | list | | |
