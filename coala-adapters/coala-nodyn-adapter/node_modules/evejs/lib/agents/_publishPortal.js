/**
 * Created by Alex on 4/24/14.
 */

/**
 * The publish portal is a gateway that received published messages. These are then
 * sent to the corresponding topic agents.
 *
 * This is required in order to publish to multiple instances of Eve.
 *
  * @type {{RPCfunctions: {}}}
 */
var publishPortal = {RPCfunctions: {}};

// required init function
publishPortal.init = function (Eve) {
  this.eve = Eve;
};

/**
 * send the published message to the topic agent if they exist.
 * @param params
 */
publishPortal.RPCfunctions.publish = function (params) {
  var topic = params.topic;
  if (this.eve.agents["_topicAgent_" + topic] !== undefined) {
    this.send("local://_topicAgent_" + topic, {method:'incoming',params:params,id:0}, null);
  }
};


module.exports = publishPortal;