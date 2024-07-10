# LoggerSystem

## Objectives

* Create an implementation of a simple logger system with 3 access types
* Access types:
    * ***owner*** - the highest access type, allow to manage all logs
    * ***admin*** - Allow to manage own logs and all logs of ***basic*** access type
    * ***basic*** - Allow to create and view only own logs
* Log object:
    * <i>timestamp of creation</i>
    * <i>creator</i>
    * <i>log text</i>
    * <i>log type</i>
* Logger System:
    * <i>List with all logs</i>
    * <i>List with all deleted logs</i>
    * <i>Feature of creating logs</i>
    * <i>Feature of deleting logs</i>
* Write tests for your implementation
