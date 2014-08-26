
/****
 * Agent code below here:
 */
var gameOfLifeAgent = {RPCfunctions: {}};

// mandatory init function
gameOfLifeAgent.init = function () {
  console.log(this.agentName + " added");
  this.alive = this.options.alive == "-" ? false : true;
  this.getNeighbours();
  this.cycle = 1;
  this.maxCycles = this.options.maxCycles || 100;

  this.amountAlive = 0;
  this.cycleMatchCount = 0;
};

// get the agent names of all the neighbours
gameOfLifeAgent.getNeighbours = function() {
  var gw = this.options.width;
  var gh = this.options.height;

  var N = Number(this.agentName.replace("Agent_",""));
  var neighbours = {};

  neighbours['topLeft']     = N - gw - 1; // 0:top left
  neighbours['top']         = N - gw;     // 1:top
  neighbours['topRight']    = N - gw + 1; // 2:top right
  neighbours['left']        = N - 1;       // 3:left
  neighbours['right']       = N + 1;       // 4:right
  neighbours['bottomLeft']  = N + gw - 1;  // 5:bottom left
  neighbours['bottom']      = N + gw;      // 6:bottom
  neighbours['bottomRight'] = N + gw + 1;  // 7:bottom right


  if (N % gw == 0) { // on left side:
    neighbours['topLeft'] = N - 1;
    neighbours['left'] = N + gw - 1;
    neighbours['bottomLeft'] = N + 2 * gw - 1;

    if (N < gw) {
      neighbours['topLeft'] = N + gw*gh - 1;
      neighbours['top'] = N + gw*(gh - 1);
      neighbours['topRight'] = N + gw*(gh - 1) + 1;
    }
    else  if (N >= gw*(gh - 1)) { // on bottom
      neighbours['bottomLeft'] = N % gw - 1 + gw;
      neighbours['bottom'] = N % gw;
      neighbours['bottomRight'] = N % gw + 1;
    }
  }
  else if (N % gw == gw-1) { // on right side:
    neighbours['topRight'] = N - 2*gw + 1;
    neighbours['right'] = N - gw + 1;
    neighbours['bottomRight'] = N + 1;

    if (N < gw) { // on top
      neighbours['topLeft'] = N + gw*(gh - 1) - 1;
      neighbours['top'] = N + gw*(gh - 1);
      neighbours['topRight'] = gw*(gh - 1);
    }
    else  if (N >= gw*(gh - 1)) { // on bottom
      neighbours['bottomLeft'] = N % gw - 1;
      neighbours['bottom'] = N % gw;
      neighbours['bottomRight'] = 0;
    }
  }
  else if (N < gw) { // on top
    neighbours['topLeft'] = N + gw*(gh - 1) - 1;
    neighbours['top'] = N + gw*(gh - 1);
    neighbours['topRight'] = N + gw*(gh - 1) + 1;
  }

  else if (N >= gw*(gh - 1)) { // on bottom
    neighbours['bottomLeft'] = N % gw - 1;
    neighbours['bottom'] = N % gw;
    neighbours['bottomRight'] = N % gw + 1;
  }

  this.neighbours = [];
  for (var nId in neighbours) {
    if (neighbours.hasOwnProperty(nId)) {
      this.neighbours.push("Agent_" + neighbours[nId]);
    }
  }
};

// this function is called by the neighbours to notify this agent of their state
gameOfLifeAgent.RPCfunctions.collect = function (params) {
  if (params.cycle == this.cycle) {
    this.cycleMatchCount += 1;
    if (params.alive == true) {
      this.amountAlive += 1;
    }
  }

  if (this.cycleMatchCount == 8) {
    this.processRules();
  }
};

// determine the state
gameOfLifeAgent.processRules = function () {
  this.cycle += 1;
  if (this.amountAlive < 2 || this.amountAlive > 3) {
    this.alive = false;
  }
  else if (this.amountAlive == 3) {
    this.alive = true;
  }
  this.amountAlive = 0;
  this.cycleMatchCount = 0;
  if (this.cycle <= this.maxCycles) {
    this.updateNeighbours();
  }
  else {
    this.send("manager", {method: "end", params: {}});
  }
};

// update the neighbours of my state
gameOfLifeAgent.updateNeighbours = function () {
  for (var i = 0; i < 8;i++){
    this.send(this.neighbours[i],{method:"collect",params:{cycle:this.cycle, alive:this.alive}});
  }
};

// start the game of life
gameOfLifeAgent.RPCfunctions.start = function() {
  this.updateNeighbours();
};

module.exports = gameOfLifeAgent;