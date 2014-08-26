/**
 * This is an example.
 * The agent calls it's own method once and publishes a message to itself.
 *
 * It uses it's default transport by not specifying a transport type (ie. local://, http://)
 */

var Eve = require('evejs');

var eveOptions = {
  transports: [ // uncomment the HTTP protocall to use the http protocol over the local protocol
//    {protocol: "http", options: {port: 3000, path: "agents/"}}
  ],
  agents: [
    {agentClass: "exampleAgent.js", name: "Frank"} // adding the agent.
  ],
  agentModules: [
    'publishSubscribe' // hooking up the publish subscribe agent module to the agent as an example.
  ]
};
var eve = new Eve(eveOptions);
