/**
 * Created by Alex on 7/23/14.
 */

var managerAgent = {RPCfunctions: {}};


// mandatory init function
managerAgent.init = function(Eve) {
  this.eve = Eve;
}

managerAgent.RPCfunctions.agentExists = function(params) {
  var agentName = params.name;
  if (this.eve.agents[agentName] !== undefined) {
    return "yes";
  }
  else {
    var newAgentParams = {agentClass: "src/test/evejs/deviceAgent", name: agentName};
    this.eve.addAgent.call(this.eve, newAgentParams);
    var newAgent = this.eve.agents[agentName];
    var binder = com.almende.coala.service.interpreter.InterpreterAgentManager.getInstance().getBinderFactory().create(agentName);
    var interpreter = binder.inject(com.almende.coala.service.interpreter.InterpreterService);
    var status = interpreter.init(newAgent);
    // interpreter.subscribe(status, function(nextStatus){console.log(nextStatus);}, null, null);
    newAgent.log.trace("Interfaced, got modelID: "+newAgent.modelID+", proxyStatus: "+newAgent.proxyStatus);
    newAgent.log.trace("Bindings: "+binder.getBindings());
    //status.subscribe({function(var t1){}});
    return "created";
  }
}


module.exports = managerAgent;