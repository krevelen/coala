var mathAgent = {RPCfunctions: {}}; // create the JSON object what will contain all RPC functions.

// mandatory init function
mathAgent.init = function () {
  // print the creation of this agent to the console.
  console.log(this.agentName + " added");
};

/**
 * Defining the RPC function "add". This can be called by other agents and by HTTP POST requests.
 * @param {object} params       |   {a:Number, b:Number}
 * @param {String} [senderId]   |   This is the agent name of the sender. EveJS's transport protocols supply this value.
                                    The HTTP protocol does this with an extra header: "x-eve-senderurl".
                                    If this is not the case, senderId == "unknown".
 */
mathAgent.RPCfunctions.add = function (params, senderId) {
  return params.a + params.b;
};

/**
 * Defining the RPC function "add". This can be called by other agents NOT by external http requests.
 * @param {object} params       |   {a:Number, b:Number}
 * @param {String} [senderId]   |   This is the agent name of the sender.
 */
mathAgent.RPCfunctions.protectedAdd = function (params, senderId) {
  if (senderId != "unknown") {
    return "access denied";
  }
  else {
    return params.a + params.b;
  }
};

module.exports = mathAgent;