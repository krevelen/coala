/**
 * Created by Alex on 4/24/14.
 */

var util = require("../util");

/**
 * This function returns a constructor for an agent. It mixes in standard functions all agents can use.
 *
 * @param agentImplementation
 * @returns {Function}
 * @constructor
 */
function AgentBase(agentImplementation) {

  return function(agentName, options, EveSystem) {

    var newAgent = Object.create(agentImplementation);

    // this mixes in utility functions
    // initializing some fields that may be useful
    newAgent.agentName = String(agentName);
    newAgent.options = options;
    newAgent.repeatIds = [];
    newAgent.scheduleIds = [];

    // send a message to agent: destination, message is JSON-RPC object, callback is fired on callback, message ID used to identify callback.
    //    message structure = {
    //      address: transportPrefix + agentId,
    //      origin:  transportPrefix + agentId,
    //      content: {
    //        id: ###,
    //        method: "functionName",
    //        params: {}
    //      }
    //    }
    newAgent.send = function (destination, message, callback, messageId) {
      if (messageId === undefined) {message["id"] = util.getUID();}
      else                         {message['id'] = messageId;}

      EveSystem.sendMessage(destination, message, newAgent.agentName, callback);
    };

    // do something later
    newAgent.schedule = function(callback, time) {
      if (time == 0 || (typeof time != "number")) {
        setTimeout(callback.apply(newAgent),0);
      }
      else {
        var id = setTimeout(function() {
          callback.apply(newAgent);
          newAgent.scheduleIds.splice(newAgent.scheduleIds.indexOf(id), 1);
        }, time);
        this.scheduleIds.push(id);
        return id;
      }
    };

    newAgent.clearSchedule = function(id) {
      if (id === undefined) {
        for (var i = 0; i < this.scheduleIds.length; i++) {
          clearTimeout(this.scheduleIds[i]);
        }
      }
      else {
        clearTimeout(id);
      }
    }

    newAgent.clearAllSchedules = function() {
      for (var i = 0; i < this.scheduleIds.length; i++) {
        clearTimeout(this.scheduleIds[i]);
      }
    }


    // repeat a function. If this function has arguments, wrap it in an anonymous function!
    newAgent.repeat = function(callback, time) {
      if (time == 0 || (typeof time != "number")) {
        console.log("Cannot set repeat without a valid time (in ms).");
      }
      else {
        var repeatId = setInterval(function() { callback.apply(newAgent); }, time);
        newAgent.repeatIds.push(repeatId);
        return repeatId;
      }
    };

    // stop repeating a certain repeat
    newAgent.stopRepeating = function (id) {
      clearInterval(id);
      this.repeatIds.splice(this.repeatIds.indexOf(id), 1);
    };

    // stop repeating all repeating functions
    newAgent.stopRepeatingAll = function () {
      for (var i = 0; i < newAgent.repeatIds.length; i++) {
        clearInterval(newAgent.repeatIds[i]);
        newAgent.repeatIds.shift();
        i--;
      }
    };

    // delete agent
    newAgent.die = function() {
      EveSystem.removeAgent(this.agentName);
    }

    // warnings
    newAgent.publish = function() {
      console.log("No publish/subscribe module has been loaded.")
    }
    newAgent.subscribe = function() {
      console.log("No publish/subscribe module has been loaded.")
    }

    // initialize the agent.
    return newAgent;
  };
}

module.exports = AgentBase;