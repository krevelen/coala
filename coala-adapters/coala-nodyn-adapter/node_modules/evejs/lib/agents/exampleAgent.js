/**
 * This is an example agent.
 * It calls it's own method once and publishes a message to itself.
 *
 * It uses it's default transport by not specifying a transport type (ie. local://, http://)
 */

var mathAgent = {RPCfunctions: {}};

// mandaory init function
mathAgent.init = function () {
  console.log(this.agentName + " added");
  this.schedule(this.sendMessageToSelf,1000); // wait a second, then send a message to itself
  var me = this;
  this.repeat(function() {me.publish("topic","hello world!");},1000); // repeat the publishing of "hello world" every second
  this.subscribe("topic",function(data) {console.log("From subscription:",data);}); // subscribe to the topic it is publishing on
  this.schedule(this.stopRepeatingAll,5000); // cancel the publishing of hello world.
};

/**
 * Send a message to itself.
 */
mathAgent.sendMessageToSelf = function () {
  this.send(this.agentName, {method: "add", params: {a: 71, b: 12} },
    function (answer) {
      console.log('I, ', this.agentName,', have the answer: ', answer.result);
    });
};

/**
 * A RPC function that can be called by other agents or from an external HTTP source.
 *
 * @param {object} params | {a:Number, b:Number}
 * @returns {Number}
 */
mathAgent.RPCfunctions.add = function (params) {
  return params.a + params.b; // this returned value is automatically loaded into a callback to the sender.
};


module.exports = mathAgent;