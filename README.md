COALA
=====

The Common Ontological Abstraction Layer for Agents (COALA) helps reuse agent code across MASs and ABMs

COALA support reusable and reproducible behavior (how to negotiate, learn, coordinate, ...)
across prevalent platforms and middleware (multi-agent systems, application servers, ...) 
through common concepts related to agents and organizations

CAPABILITY BINDING
------------------

Each Agent belongs to a Model and owns a Binder for Capability injection.
A Binder provides Capabilities lazily (as needed) as per the Model configuration.
Capabilities may be extended per domain, may use each other, and may include:

* Access configuration: ConfiguringCapability #getProperty(..)
* Create agents: CreatingCapability #create(..)
- Destroy agents: DestroyingCapability #destroy(..)
- Perform (bodily) actions: ActuatingCapability #perform(..)
- Observe (bodily) percepts: PerceivingCapability #perceive(..)
- Receive messages: ReceivingCapability #receive(..)
- Send messages: SendingCapability #send(..)
- Advertise services: ExposingCapability #expose(..)
- Evaluate scripts: InterpretingCapability #eval(..)
- Persisting state: PersistingCapability #persist(..) #retrieve(..)
- Reasoning with rules and beliefs: ResoningCapability #assert(..)
- Online requests: OnlineCapability #request(..)
- Time awareness: TimingCapability #getTime(..)
- Future initiative: SchedulingCapability #schedule(..)
- Randomization: RandomizingCapability #getRNG(..)
- Replication: ReplicatingCapability #start(..) #pause(..)

EMBRACES
--------

- (pre-Java8) functional reactive programming (RxJava, LambdaJ?, ...)
- test-driven development (Maven, junit, easymock, jacoco, cobertura, ...)
- dependency injection and interception (guice, ...)
- standard Java extension APIs (cdi, stax, jaxb, jax-ws, servlet, jdo, jpa, ...)
- efficient schema support and un/marshalling for XML (...) and JSON (Jackson)
- standard unit APIs, for physics (JSR-275: jscience), time (JSR-310: threetenbp), currency (JSR-354: moneta)
- unified logging (slf4j + injection)
- flexible configuration (Owner API + injection)
- streaming IO (commons-io)
- fluent/builder design patterns
- dynamic beans (...)

TODO
----

- apply owner-api universally, including for configuring Binders, thus deprecating commons-configuration
- update to latest extension APIs and dependencies (RXJava, Joda, Jackson, ...)
- increase test coverage
- add introspection tools (sniffing, ...)
- get examples working, also in Groovy, Scala, etc.
- provide vert.x-based AgentSpeak-like behavioral script interpreter/reasoner (a la Jason, SL, GOAL, 2APL, ...)
