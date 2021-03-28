# README

## MAPS Backend Project Structure
### REPL
The REPL takes a list of Events, which are actions that can be called from the commandline by the
user. A BufferedReader reads all user input, parses it, and then searches to see if any of the 
events it knows matches with the user's first inputted term. If it does, then it calls that action,
and passes the rest of the user input into that action as it's arguments. The REPL also catches all 
errors from the actions and prints them before continuing the user-input loop.


### KDTree, KDNode,
**KDNode** stores a node's coords key (a list of doubles) and value, children, parent, 
and dimension in the KDTree. KDTree stores the tree root, a list of all the 
contained nodes, and some methods for calculating the nearest objects. Each KDNode stores a reference to 
its children, which are used to recur down the KDTree for the different queries. 

### Graph, Node, Edge
Node and Edge are interfaces for graph nodes and edges, respectfully. In Graph, Node, and 
Edge, the generics N and E are used to generically describe classes that implement the 
Node and Edge interfaces. The Graph class performs djikstra's and other graph operations on
a set of generic Ns and Es

### MapGraph, MapTree, DBReference
MapGraph and MapTree are where nodes and edges are stored for querying. DBReference
contains a MapGraph, MapTree, and Cache to easily pass these data structures between 
REPL Events. Since both MapGraph and MapTree are EntryLists, we can generically populate
them with data from a single SQLParser class. MapTree's addEntry method is used 
to load nodes, MapGraph's addEntry method is used to add edges. Since MapGraph is associated
with MapTree, adding nodes to the MapTree also adds nodes to the MapGraph. 

### MapNode, Way
MapNode implements Node, Way implements Edge. MapGraph, in turn, adds MapNodes and Ways to 
a Graph that can be efficiently queried for route details. 

## Division of Labor
We worked together to integrate our code to achieve Maps 1-2 functionality. There was also a lot of communication about design decsions and a lot of the code was pair programmed. The parts of the code that were written individually were looked over by both partners and talked about together.

## MAPS Frontend Project Structure
App.js has components MapController and CheckinTable

MapController holds relevent handlers for panning and scrolling, and information important to the setup of the GUI. It also holds the Map Component.

Map holds the canvas component of the map, the logic for creating and caching tiles, and the handlers for selecting nodes and holds the Route Component.

Route holds the 4 TextBox object and 3 buttons, and uses nearest.js and interesction.js to make the appropriate requests to the backend to update information on the map, this component also  creates/finds the shortest route with the current information.

CheckinTable contains all the information and logic for displaying checkins and requesting a users previous checkings



## Known Bugs
There are no known bugs.

## Design Details specific to your code
The project stucture section above explains details specific to the code. We also use caching using localStorage to store ways in the map for display purposes. We are also concious of stroke() calls when drawing on the canvas and only do so when loading in a tile of ways, drawing a new route or a new node.

### Frontend Directions
 We orginally set the server to maps.sqlite3 and focus on the lattitude and longitude of Brown's Campus. To pan across the map click and drag. To zoom in or out please scroll. Double click on a spot of the map to get the nearest node to the clicked spot. The blue circle that appears after double clicking or entering input refers to the starting node of a route. A red circle refers to the ending node of a route. You may find a route by entering street names into the input boxes and hitting submit or by selecting nodes on the map as mentioned, or a combination of the two. Once two node are selected, the shortest path from the start node to the end node will be shown in black. To see the checkins of a user appear in the box below the feed of checkins on the right, click on the users entry in the feed. To clear a route and the node currently on the map click the clear route button. If you load in a new map on the REPL, please hit the reset map button and once you interact with the map again, the new map information will appear from the current viewpoint.

### Integrating Code
For this project we used Adwith's REPL structure and Jadey's Dijkstras method. To ingtegrate the code we made changes to the origial MapGraph class so we could build the graph as we go through dijkstas or A*. This also involved changing the Route command and node caching.

## How to build/run your program with the GUI
We tested the GUI using Chrome. 

To run you will need to run the REPL, run the React component and run the Server.

1) Run the Server (from the maps-3-4-amukhe12-jhagiwar directory)
    run python2.7 cs032_maps_location_tracking 8080 10 -S

2) Run the REPL with the GUI (from the maps-3-4-amukhe12-jhagiwar directory)
    run mvn package 
    run ./run --gui
    You should also load in the desired map through the REPL.

3) Run the react component (from the maps-3-4-amukhe12-jhagiwar/frontend directory)
    in the frontend directory:
    run npm install
    run npm start