/**
 * Created by Alex on 4/24/14.
 */

//var codein = require("node-codein");
var Eve = require('evejs');

var serverIP = "127.0.0.1";
var registerAddress = "http://rickspc:3000/agents/unit";
var http = {
  protocol: "http",
  options: {
    port: 3000,
    path: "agents/",
    localShortcut: true
  }
};

var eveOptions = {
  transports: [http],
  agents: [
    {agentClass: "src/test/resources/evejs/agents/inertia/proxyAgent", name: "proxy",
      options: {
        myAddress: "http://" + serverIP  + ":" + http.options.port + "/" + http.options.path + "proxy",
        registerAddress: registerAddress}
    },
    {agentClass: "src/test/resources/evejs/agents/inertia/managerAgent", name: "manager"},
    {agentClass: "src/test/resources/evejs/agents/inertia/test", name: "test"}
  ]
};

var myEve = new Eve(eveOptions);
