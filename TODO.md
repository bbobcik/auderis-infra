# Project roadmap

## Semantic annotations
  * [X] Annotations that define (and document) how the annotated element
        interfaces with the outer world (e.g. user-oriented endpoints, 
        filesystem interfaces)
  * [ ] Test qualifiers (e.g. regression tests, smoke tests)

## Simple tools
  * [ ] Helper that maintains series of `AutoCloseable` objects
  * [X] Provider of string separators (used when constructing a list piece-by-piece) 

## Logging
  * [ ] Fluent logging API
  * [ ] LogBack buffering appender
  * [ ] Test-time appender that crashes the test if a message of a certain
        level is logged

## Feature Toggle
  * [X] Annotation-based feature toggle logic
  * [ ] Build-time code instrumentation of feature toggles
    * [ ] Dynamic feature toggle logic
    * [ ] Generation of feature toggle manifest
    * [ ] Prevent compilation of code with expired feature toggles
    * [ ] Documentation of feature toggles
  * [ ] Run-time feature management
    * [ ] JMX interface
    * [ ] Dump initial state of all feature toggles at startup
    * [ ] Monitor which feature toggles were accessed
