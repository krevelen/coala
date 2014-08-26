/**
 * Created by Alex on 7/23/14.
 */

var graph2d;
var graph2dGroups;
var graph2dDatapoints;

var timeline;
var timelineDatapoints;
var timelineGroups;

var fitted = false;

function onLoad() {
  // create a dataSet with groups
  graph2dGroups = new vis.DataSet();
  graph2dDatapoints = new vis.DataSet();

  timelineGroups = new vis.DataSet();
  timelineDatapoints = new vis.DataSet();
  var graph2dContainer = document.getElementById('graph2dDiv');
  var timelineContainer = document.getElementById('timelineDiv');

  var graph2dOptions = {
    catmullRom: false,
    dataAxis: {showMinorLabels: false}
  };

  var timelineOptions = {minHeight:400};
  graph2d = new vis.Graph2d(graph2dContainer, graph2dDatapoints, graph2dOptions, graph2dGroups);

  timeline = new vis.Timeline(timelineContainer);
  timeline.setOptions(timelineOptions);
  timeline.setGroups(timelineGroups);
  timeline.setItems(timelineDatapoints);

  setInterval(refreshData.bind(this,updateVis), 2000);
}


function refreshData(callback) {
  askAgent("http://127.0.0.1:3000/agents/proxy", "getData", null, callback, true);
}


function updateVis(reply) {
  var data = reply.result;
  var graph2dPoints = [];
  var timelinePoints = [];
  for (var i = 0; i < data.length; i++) {
    var agentData = data[i];
    var graph2dUsed = false;
    var timelineUsed = false;
    console.log(agentData);
    var groupName = agentData.type[0] + agentData.name.substring(agentData.name.indexOf(":"), agentData.name.length);
    for (var dataId in agentData.data) {
      if (agentData.data.hasOwnProperty(dataId)) {
        for (var j = 0; j < agentData.data[dataId].length; j++) {
          var point = agentData.data[dataId][j].data;;
          var processedGraph2DPoint =  {x:point.startTime,     y:0, group:groupName};
          var processedTimelinePoint = {start:point.startTime, title: dataId,  content: point.dataPoint,  group:groupName};

//          if (point.dataPoint.toLowerCase() == "on" || point.dataPoint.toLowerCase() == "open") {
//            processedGraph2DPoint.y = 1;
//          }
//          else if (point.dataPoint.toLowerCase() == "off" || point.dataPoint.toLowerCase() == 'closed') {
//            processedGraph2DPoint.y = 0;
//          }
//          else {
            processedGraph2DPoint.y = Number(point.dataPoint);
//          }

          if (isNaN(processedGraph2DPoint.y) == true) {timelinePoints.push(processedTimelinePoint); timelineUsed = true;}
          else                                        {graph2dPoints.push(processedGraph2DPoint);   graph2dUsed = true;}
        }
      }
    }

    if (timelineUsed == true) {
      if (groupExists(timelineGroups, groupName) == false) {
        timelineGroups.add({id:groupName,  content: groupName})
      }
    }
    if (graph2dUsed == true) {
      if (groupExists(graph2dGroups, groupName) == false) {
        graph2dGroups.add({id:groupName, content: groupName})
      }
    }
  }

  if (timelinePoints.length > 0) {
    timelineDatapoints.clear();
    timelineDatapoints.add(timelinePoints);
    if (fitted == false) {
      timeline.fit();
    }
  }

  if (graph2dPoints.length > 0) {
    graph2dDatapoints.clear();
    graph2dDatapoints.add(graph2dPoints);
    if (fitted == false) {
      graph2d.fit();
    }
  }
  fitted = true;

  populateExternalLegend();
}

function groupExists(groups, group) {
  var groupsData = groups.get();
  for (var i = 0; i < groupsData.length; i++) {
    if (groupsData[i].id == group) {
      return true;
      break;
    }
  }
  return false;
}


/**
 * this function fills the external legend with content using the getLegend() function.
 */
function populateExternalLegend() {
  var groupsData = graph2dGroups.get();
  var legendDiv = document.getElementById("Legend");
  legendDiv.innerHTML = "";

  // get for all groups:
  for (var i = 0; i < groupsData.length; i++) {
    // create divs
    var containerDiv = document.createElement("div");
    var iconDiv = document.createElement("div");
    var descriptionDiv = document.createElement("div");

    // give divs classes and Ids where necessary
    containerDiv.className = 'legendElementContainer';
    containerDiv.id = groupsData[i].id + "_legendContainer"
    iconDiv.className = "iconContainer";
    descriptionDiv.className = "descriptionContainer";

    // get the legend for this group.
    var legend = graph2d.getLegend(groupsData[i].id,15,15);

    // append class to icon. All styling classes from the vis.css have been copied over into the head here to be able to style the
    // icons with the same classes if they are using the default ones.
    legend.icon.setAttributeNS(null, "class", "legendIcon");

    // append the legend to the corresponding divs
    iconDiv.appendChild(legend.icon);
    descriptionDiv.innerHTML = legend.label;

    // determine the order for left and right orientation
    if (legend.orientation == 'left') {
      descriptionDiv.style.textAlign = "left";
      containerDiv.appendChild(iconDiv);
      containerDiv.appendChild(descriptionDiv);
    }
    else {
      descriptionDiv.style.textAlign = "right";
      containerDiv.appendChild(descriptionDiv);
      containerDiv.appendChild(iconDiv);
    }

    if (graph2d.isGroupVisible(groupsData[i].id) == false) {
      containerDiv.className = containerDiv.className + " hidden";
    }
    else { // if invisible, show
      containerDiv.className = containerDiv.className.replace("hidden","");
    }

    // append to the legend container div
    legendDiv.appendChild(containerDiv);

    // bind click event to this legend element.
    containerDiv.onclick = toggleGraph.bind(this,groupsData[i].id);
  }
}

/**
 * This function switchs the visible option of the selected group on an off.
 * @param groupId
 */
function toggleGraph(groupId) {
  // get the container that was clicked on.
  var container = document.getElementById(groupId + "_legendContainer")
  // if visible, hide
  if (graph2d.isGroupVisible(groupId) == true) {
    graph2dGroups.update({id:groupId, visible:false});
    container.className = container.className + " hidden";
  }
  else { // if invisible, show
    graph2dGroups.update({id:groupId, visible:true});
    container.className = container.className.replace("hidden","");
  }
}