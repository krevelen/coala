/**
 * This is an example.
 * You can use a POST request to http://localhost:3000/agents/mathAgentFrank using the add RPC function.
 *
 * example JSON-RPC message = {"method": "add","params": { "a":3,"b":5}}
 * response = {"result": 8,"error": 0}
 */

var Eve = require('evejs');

var eveOptions = {
  transports: [
    {protocol: "http", options: {port: 3000, path: "agents/"}}
  ],
  agents: [
    {agentClass: "mathAgent.js", name: "mathAgentFrank"} // adding the agent.
  ]
};
var eve = new Eve(eveOptions);
