/**
 * Created by Alex on 7/23/14.
 */

var proxyAgent = {RPCfunctions: {}};

// mandatory init function
proxyAgent.init = function() {
  this.myAddress = this.options.myAddress;
  this.registerAddress = this.options.registerAddress;
  this.repeat(this.register,1000);
  this.agentData = {};
};

proxyAgent.register = function() {
  var me = this;
  this.send(
    this.registerAddress,
    {method:"register", params:{address:this.myAddress, method:"incoming", expectedParam:"event"}},
    function (data) {console.log(data); me.stopRepeatingAll();}
  )
};

proxyAgent.forwardMessage = function(managerReply, agentName, message) {
  var me = this;
  if (managerReply == "created" || managerReply == "yes") {
    this.send(
      "local://" + agentName,
      {method:"receiveEvent", params:{event:message}},
      function(reply) {
        me.storeData(reply.result);
      }
    );
  }
};

proxyAgent.storeData = function(reply) {
  this.agentData[reply.name] = {name: reply.name, type: reply.type, data:reply.data, meta: reply.meta};
};

proxyAgent.RPCfunctions.incoming = function(params) {
  var message = params.event;
  var me = this;
  if ( typeof message === 'object' && message.about !== undefined) {
    var name = message.about;
    this.send(
      "local://manager",
      {method:"agentExists", params:{name:name}},
      function(reply) {
        me.forwardMessage(reply.result, name, message);
      }
    );
  }
};

proxyAgent.RPCfunctions.getData = function() {
  var aggregatedData = [];
  for (var agentId in this.agentData) {
    if (this.agentData.hasOwnProperty(agentId)) {
      aggregatedData.push(this.agentData[agentId]);
    }
  }
  return aggregatedData;
};



module.exports = proxyAgent;