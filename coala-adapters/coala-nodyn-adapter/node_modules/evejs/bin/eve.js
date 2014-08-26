/**
 * Created by Alex on 4/24/14.
 */

module.exports = Eve;
var agentBase = require("../lib/modules/agentBase.js");

/**
 *  Eve handles the composition of messages, routing, delivering and implementing callbacks.
 *  local and http protocols are supported.
 *
 *  The local is loaded by default since some internal processes use this layer. (http over local when available, pubsub.. etc)
 *  @param {object} options |-{Array of objects}  options.transports
 *                            |_ {protocol: "protocolName"[, options: {transportSpecificOptions}]}
 *                          |-{Array of objects}  options.agents
 *                            |_ {agentClass: "path to *.js to require", name: "agentName"}
 *                          |-[{Array of Strings} options.agentModules]
 *                            |_ strings to require for agent modules
 */
function Eve(options) {
  this.agents = {};
  this.callbacks = {};
  this.callbackTimeout = 1000; // ms
  this.transports = {};
  this.agentModules = options.agentModules;
  this.sendCallCounter = 0; // debug

  // load the required transports and add them to Eve
  if (options.hasOwnProperty("transports")) {
    for (var i = 0; i < options.transports.length; i++) {
      this.addTransport(options.transports[i]);
    }
  }
  // always load the peer 2 peer protocol
  if (this.transports['local'] === undefined) {
    this.addTransport({protocol:"local"});
  }

  // set the default protocol
  if (options.transports.length > 0) {
    this.defaultTransport = options.transports[0].protocol; // the first transport is the default one.
  }
  else {
    this.defaultTransport = "local";
  }

  // add agents
  if (options.hasOwnProperty("agents")) {
    for (i = 0; i < options.agents.length; i++) {
      this.addAgent(options.agents[i]);
    }
  }
}

/**
 * This function loads the required transport functions into Eve.
 * The input object contains a protocol and possibly options.
 *
 * @param {object} transport
 */
Eve.prototype.addTransport = function(transport) {
  console.log("Implementing transport protocol:", transport.protocol);
  if (transport.options === undefined) {
    transport.options = {};
  }

  this.transports[transport.protocol] = {};
  var implementation = require("../lib/transports/" + transport.protocol + ".js");
  for (var fn in implementation) {
    if (implementation.hasOwnProperty(fn)) {
      this.transports[transport.protocol][fn] = implementation[fn];
    }
  }

  // launching the init function runs the initial config of the transport layer
  this.transports[transport.protocol].init(transport.options, this);
};


/**
 * This function loads the agent implementation from the filename and initializes it.
 *
 * @param {object} agentDescription | {agentClass: "path to *.js to require", name: "agentName"}
 * @param {Boolean} noModules       | do not load modules
 */
Eve.prototype.addAgent = function(agentDescription, noModules) {
  var agentName = agentDescription.name;
  var agentImplementation = agentDescription.agentClass;
  var options = agentDescription.options || {};
  var module;

  if (this.agents[agentName] !== undefined) {
    console.log("ERROR: ", agentName, " already exists!");
  }
  else {
    var agentClass = this.requireFromPaths("../../../", "../lib/agents/", agentImplementation);
    var agent = agentBase(agentClass);
    this.agents[agentName] = agent(agentName,options, this);
    this.callbacks[agentName] = {};

    if (this.agentModules !== undefined && noModules != true) {
      for (var i = 0; i < this.agentModules.length; i++) {
        module = this.requireFromPaths("../../../", "../lib/modules/", this.agentModules[i]);
        module(this.agents[agentName], this);
      }
    }

    // initialize the agent
    this.agents[agentName].init.call(this.agents[agentName],this);
  }
};


Eve.prototype.requireFromPaths = function(path1, path2, filename) {
  var required;
  try {
    required = require(path1 + filename);
  }
  catch(e) {
    try {
      required = require(path2 + filename);
    }
    catch(e2) {
      console.error("Cannot find: " + filename);
      process.exit(e2.code);
    }
  }
  return required;
};

/**
 * Remove an agent specified with the agentId.
 *
 * @param {String} agentName | ID (== name) of agent to delete.
 */
Eve.prototype.removeAgent = function(agentName) {
  this.agents[agentName].stopRepeatingAll();
  delete this.callbacks[agentName];
  delete this.agents[agentName];
  console.log(agentName, "deleted.");
};


/**
 * once a message has been routed, this function delivers it. This means that the RPC function
 * defined in the method will be called with the params.
 *
 * This is only for direct messages, replies are handled differently.
 *
 * @param message
 * @param agentId
 * @param senderId
 */
Eve.prototype.deliverMessage = function(message, agentId, senderId) {
  var agent = this.agents[agentId];
  if (agent.RPCfunctions[message.method] !== undefined) {
    return {
      result:agent.RPCfunctions[message.method].apply(agent,[message.params, senderId]),
      error:0
    };
  }
  else {
    console.log("This agent (" + agentId + ") does not have function: ", message.method);
    return {
      result:"This agent (" + agentId + ") does not have function: " + message.method,
      error:1
    };
  }
};


/**
 * If a callback message is received, it is delivered as a reply to the correct agent.
 * Once the callback has been executed, the callback is removed.
 *
 * @param {object} reply   | reply json object: {result: *, error: 0 || 1}
 * @param {String} agentId | agentId (== agant name) of the agent who sent the original JSON-RPC call
 */
Eve.prototype.deliverReply = function (reply, agentId) {
  if (this.callbacks[agentId] !== undefined) {
    // if a callback function was bound to the reply of this message id, run it once.
    if (this.callbacks[agentId][reply.id] !== undefined) {
      this.callbacks[agentId][reply.id].call(this.agents[agentId], reply);
      delete this.callbacks[agentId][reply.id];
    }
  }
};


/**
 * This function determines the transport protocol based on the address prefix.
 * If there is no prefix (as denoted as everything before ://), the default transport is used.
 * It also adds a callback (if it is supplied) to listen for a reply on this message.
 *
 * @param {String} address            | with or without prefix
 * @param {Object} message            | JSON-RPC
 * @param {String} senderId           | without prefix
 * @param {function, null} [callback] | function, null or undefined
 */
Eve.prototype.sendMessage = function(address, message, senderId, callback) {
  this.sendCallCounter += 1;
  var me = this;
  var transportType, receiverAddress;
  var transportTypIdx = address.indexOf("://");
  if (transportTypIdx != -1) {
    receiverAddress = address;
    transportType = address.substring(0,transportTypIdx);;
  }
  else {
    transportType = this.defaultTransport;
    receiverAddress = this.transports[this.defaultTransport].prefix + address;
  }

  // only register callback if the message is not a response, also add a timeout to the callback
  if (message["method"] !== undefined && callback !== undefined && callback !== null) {
    this.callbacks[senderId][message.id] = callback;
    setTimeout(function() {me.timeoutCallback(senderId, message.id)}, this.callbackTimeout);
  }

  process.nextTick(function() {me.transports[transportType].sendMessage(receiverAddress, message, senderId);});
};


/**
 * This times out a callback. It is removed from the list.
 *
 * @param {String} agentId   | agentId (== agant name) of the agent who sent the original JSON-RPC call
 * @param {String} messageId | unique ID of the message, used to keep track of the 'conversation'
 */
Eve.prototype.timeoutCallback = function(agentId, messageId) {
  if (this.callbacks[agentId] !== undefined) {
    // if a callback function was bound to the reply of this message id, delete it.
    if (this.callbacks[agentId][messageId] !== undefined) {
      delete this.callbacks[agentId][messageId];
    }
  }
};

