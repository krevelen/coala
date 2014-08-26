EveJS
=========

### Introduction

Eve is a multipurpose, web-based agent platform. Eve envisions to be an open and dynamic environment where agents can live and act anywhere: in the cloud, on smartphones, on desktops, in browsers, robots, home automation devices, and others. The agents communicate with each other using simple, existing protocols (JSON-RPC) over existing transport layers, offering a language and platform agnostic solution. 

Eve has a library available in JavaScript, running on Node.js. This library provides a ready-made Node.js server and some example agents. Node.js runs JavaScript, which is a very natural language to handle JSON-RPC messages, as JSON is a subset of the JavaScript language.

Eve for nodeJS is available as an npm package called *evejs*.

---

### How to install
You can install EveJS for Node.js from npm:
```
npm install evejs
```

---

### EveJS Example
This is a small code example showing how initialize an Eve instance.
```
var Eve = require('evejs');

var eveOptions = {
  transports: [
    {
    protocol: "http",
      options: {
        port: 3000,
        path: "agents/"
      }
    }
  ],
  agents: [
    {agentClass: "./agents/mathAgent", name: "mathAgent"}, 
    ...
  ],
  agentModules: [
      "publishSubscribe",
      "./agents/agentModules/myModule"
  ]
};

var myEve = new Eve(eveOptions);
```
The path of the agentClass is relative to the root of your project. That means that the "agents" folder is in the same folder as "node_modules".
####EveJS options:
 name | type | description
:--------|:-----|:-----------
transports|Array|Each object given to the transport array has to consist of a field called ```protocol```. This also needs to be present in the address as ```protocol://AgentName```. If a protocol is not defined in the address when sending a message, the message is automatically sent over the default transport. The default transport is local or the first transport defined: eveOptions.transports[0]. 
agents|Array|Each agent object given to the agents array has to contain the fields ```agentClass```, which is a path to where the agent's javascript module can be found and ```name```, the name of the agent as Eve will identify it.
[agentModules]|Array|Optionally, agents can be extended with agentModules. Some modules, like ```publishSubscribe```, are provided by EveJS. Others can be created by the user. These modules extend the default built-in methods the agents can use.


---
### Transports
EveJS currently supports two transport layers. These are stand-alone and determined by the address to which the message is sent.

- **local** - Local transport is loaded by default for sending messages within the same Eve instance.

```
options = {}; // the local transport layer has no options

example: "local://agentName"
```

- **http** - When http is selected, EveJS automatically creates a native Nodejs server to listen on a pre-/userdefined port. EveJS can be
configured to send a message over the local transport if the agent is detected on the same instance to increase performance.

```
options: {
    port: 3000,
    path: myAgents,
    localShortcut: false
};

example: "http://127.0.0.1:3000/myAgents/agentName"
```

option | type | default | description
:--------|:-----|:--------|:-----------
port|Number|3000|The port EveJS will listen on
path|String|agents|The path after localhost (or 127.0.0.1)
localShortcut|Boolean|false|When true, send message over local if possible


- **default** - The first transport protocol that is given in the options will be the default transport protocol. If no transport protocol is defined in the options, the local protocol will be the default.

```
example: "agentName" 
```
---
### JSON-RPC message structure
Both the local and http transport layers employ the JSON-RPC protocol. The messages in the JSON-RPC protocol are defined as shown below. The function name is the function the receiving agent will perform. Only functions in the this.RPCfunctions object of the agent can be called by other agents. 

```
var message = {
    method: "functionName",
    params: {}
};

var reply = {
    result: "my result",
    error: 0
};
```

---
### Built-in methods of Agents

This is a list of the functions an EveJS agent can use.

 function | arguments | return | description
:-------- |:-----|:--------|:-----------
send      | address, message, callback |  | send a message (reply) {console.log(reply.result);}```
schedule  | function, time | id | Schedule a function to be performed after ```time``` ms.
clearSchedule |  id |  | Remove a specific scheduled function.
clearAllSchedules | |  | Remove all scheduled functions.
repeat    | function, timeInterval | id | Repeat a function after ```timeInterval``` (in ms), every ```timeInterval``` until stopped.
stopRepeating | id |  | Stop repeating all specific repeating function.
stopRepeatingAll |  |  | Stop repeating all repeating functions.
die | |  | Remove the agent.

#### Usage examples
```
this.send(
        "local://agentName",                      // address
        {method:"add",params:{a:49, b:23}},     // message
        function(reply) {                       // callback
            console.log(reply.result);
        }
);

var sID = this.schedule(
            function () {console.log("do this in 5 minutes");},   // function to perform after the timeout
            5*60*1000                                               // timeout in milliseconds
);

this.clearSchedule(sID);                        // do not perform the scheduled function defined by the "sid"

var rID = this.repeat(
            function() {console.log("I'm repeating every 10s!");},  // function to repeat with a timeout interval
            10*1000                                                 // timeout interval in milliseconds
);

this.stopRepeating(rID);                        // stop repeating the message

this.die();                                     // kill the agent
```
---
### Agent Modules

Agent modules can be used to expand the built-in methods the agents can use. These methods are added to all agents.
The modules themselves are quite simple, they contain a function that requires an agent object, add functions and or variables to the agent and finally return the extended agent.
```
module.exports = myAgentModule;

function myAgentModule(newAgent, EveSystem) {
    newAgent.x = 123;
    newAgent.helloWorld = function() {
      console.log('helloWorld!');
    };
    
    newAgent.talkToFrank = function() {
        // this is the "this" of the agent
        this.send("local://frank",{method:"helloFrank",params:{weather:"beautiful", x: this.x}});
    };

    return newAgent;
}
```
This agent module gives all agents a ```this.x``` variable, a ```this.helloWorld()``` functions and a ```this.talkToFrank()``` function.

#### Available Modules:
- **publishSubscribe**
The publishSubscribe adds functions to for publishing and subscribing to all agents.
An extra agent called  _publishPortal will be created. This received all published messages. On a subscription, a _topicAgent will be created. The _publishPortal will forward the data to the corresponding _topicAgent which will then perform all the subscribed callbacks. Here is a list of the functions publishSubscribe contains:

 function | arguments | return | description
:--------|:-----|:--------|:-----------
publish | topic, data |  | Publish the data to a topic.
subscribe | topic, callback | id | Subscribe to a topic. For each message received on the topic, the callback function will be called with one argument containing the published data.
unsubscribe |  topic, [callback] |  | Unsubscribed a callback from a subscribed topic. If no callback is defined, all the callbacks are unsubscribed from this topic.
unsubscribeAll | |  | Unsubscribe from all subscribed topics.


---
### Example agents
There are a few example agents provided. The example below shows a mathAgent, which can do additions. There is also a game of life agent available for benchmarking. To use the examples bundled with EveJS, copy the examples folder to your project root (ie. in the same place as the node_modules containing evejs).

Other examples:
- Game of Life
- Publish Subscibe
- Math agent

```
module.exports = mathAgent;

var mathAgent = {RPCfunctions: {}}; // create the JSON object what will contain all RPC functions. 

// mandatory init function
mathAgent.init = function () {
  // print the creation of this agent to the console.
  console.log(this.agentName + " added"); 
};

/**
 * Defining the RPC function "add". This can be called by other agents.
 * @param {object} params       |   {a:Number, b:Number}
 * @param {String} [senderId]   |   This is the agent name of the sender. EveJS's transport protocols supply this value.
                                    The HTTP protocol does this with an extra header: "x-eve-senderurl".
                                    If this is not the case, senderId == "unknown".
 */
mathAgent.RPCfunctions.add = function (params, senderId) { 
  return params.a + params.b;
};
```


---
### Communication with the browser

If there is a node.js EveJS instance running with the HTTP transport protocol, javascript on the browser side can be used to send either synchronous or asynchronous JSON-RPC messages. The function below can be used for this.
```
/**
 * This function communicates with the agent by an (optionally) asynchronous HTTP POST request.
 *
 * @param {String} url
 * @param {String} method
 * @param {Object} params
 * @param {Function} callback
 */
function askAgent(url,method,params,callback, async) {
  if (async === undefined) {
    async = true;
  }
  // create post request
  var POSTrequest = JSON.stringify({"method": method, "params": params});

  // create XMLHttpRequest object to send the POST request
  var http = new XMLHttpRequest();

  // insert the callback function. This is called when the message has been delivered and a response has been received
  http.onreadystatechange = function () {
    if (http.readyState == 4 && http.status == 200) {
      if (callback === undefined || callback === null) {
      }
      else {
        // launch callback function
        callback(JSON.parse(http.responseText));
      }
    }
    else if (http.readyState == 4 && http.status != 200) {
      console.log("Make sure that the Node server has started.");
    }
  };

  // open an asynchronous POST connection
  http.open("POST", url, async);
  // include header so the receiving code knows its a JSON object
  http.setRequestHeader("Content-type", "application/json");
  // send
  http.send(POSTrequest);
}

// usage example:
askAgent(
    "http://localhost:3000/agents/mathAgent",
    "add",
    {a:1,b:34},
    function (reply) {console.log(reply.result);},
    true
);
```


---
### License

Copyright (C) 2010-2014 Almende B.V.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

----------


> Written with [StackEdit](https://stackedit.io/).