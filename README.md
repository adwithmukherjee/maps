# README

## MAPS
###REPL
The REPL takes a list of Events, which are actions that can be called from the commandline by the
user. A BufferedReader reads all user input, parses it, and then searches to see if any of the 
events it knows matches with the user's first inputted term. If it does, then it calls that action,
and passes the rest of the user input into that action as it's arguments. The REPL also catches all 
errors from the actions and prints them before continuing the user-input loop.


###KDTree, KDNode,
**KDNode** stores a node's coords key (a list of doubles) and value (in this case a Star), children, parent, 
and dimension in the KDTree. KDTree stores the tree root, a list of all the 
contained nodes, and some methods for calculating the nearest objects. Each KDNode stores a reference to 
its children, which are used to recur down the KDTree for the different queries. 

###Graph, Node, Edge
Node and Edge are interfaces for graph nodes and edges, respectfully. In Graph, Node, and 
Edge, the generics N and E are used to generically describe classes that implement the 
Node and Edge interfaces. The Graph class performs djikstra's and other graph operations on
a set of generic Ns and Es

###MapGraph, MapTree, DBReference
MapGraph and MapTree are where nodes and edges are stored for querying. DBReference
contains a MapGraph, MapTree, and Cache to easily pass these data structures between 
REPL Events. Since both MapGraph and MapTree are EntryLists, we can generically populate
them with data from a single SQLParser class. MapTree's addEntry method is used 
to load nodes, MapGraph's addEntry method is used to add edges. Since MapGraph is associated
with MapTree, adding nodes to the MapTree also adds nodes to the MapGraph. 

###MapNode, Way
MapNode implements Node, Way implements Edge. MapGraph, in turn, adds MapNodes and Ways to 
a Graph that can be efficiently queried for route details. 

Property-Based Testing:
You could use property-based testing in this assignment by utilizing some hard facts we know about
what the answer should look like. For example, a property of a properly done shortest path 
algorithm will result in an answer for which there are no paths which are shorter than the answer.
In this way, you could "fuzz test" a result for your graph by simply looping through every other
path and making sure that there is not even one that is shorter. This uses a property we know about
dijkstra's (or its A* version) to avoid having to know exact answers when we fuzz test. This is
helpful because in an example like this, it is difficult to randomly generate Maps for which we
know the right answer without having to do it out by hand (or trust that our implementation is 
correct -- this would defeat the purpose of testing!). This doesn't mean that propety based testing
should ever completely replace unit tests, it is just a helpful way to "fuzz" and search for errors.

Division of Labor:
Richard combined Adwith's KDTree implementation with Richard's REPL at the beginning of the project.
Adwith handled the caching and SQL queries for this project. Richard did a lot of dijkstra's,
although Adwith helped out with this, too. Richard implemented A* search. We split the testing
roughly evenly. 

We don't know of any bugs.

Run: ./run

Run with gui: ./run --gui [-port]

Test Script:


