/*
--- Day 8: Playground ---
Equipped with a new understanding of teleporter maintenance, you confidently step onto the repaired teleporter pad.

You rematerialize on an unfamiliar teleporter pad and find yourself in a vast underground space which contains a giant playground!

Across the playground, a group of Elves are working on setting up an ambitious Christmas decoration project. Through careful rigging, they have suspended a large number of small electrical junction boxes.

Their plan is to connect the junction boxes with long strings of lights. Most of the junction boxes don't provide electricity; however, when two junction boxes are connected by a string of lights, electricity can pass between those two junction boxes.

The Elves are trying to figure out which junction boxes to connect so that electricity can reach every junction box. They even have a list of all of the junction boxes' positions in 3D space (your puzzle input).

For example:

162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689

This list describes the position of 20 junction boxes, one per line. Each position is given as X,Y,Z coordinates. So, the first junction box in the list is at X=162, Y=817, Z=812.

To save on string lights, the Elves would like to focus on connecting pairs of junction boxes that are as close together as possible according to straight-line distance. In this example, the two junction boxes which are closest together are 162,817,812 and 425,690,689.

By connecting these two junction boxes together, because electricity can flow between them, they become part of the same circuit. After connecting them, there is a single circuit which contains two junction boxes, and the remaining 18 junction boxes remain in their own individual circuits.

Now, the two junction boxes which are closest together but aren't already directly connected are 162,817,812 and 431,825,988. After connecting them, since 162,817,812 is already connected to another junction box, there is now a single circuit which contains three junction boxes and an additional 17 circuits which contain one junction box each.

The next two junction boxes to connect are 906,360,560 and 805,96,715. After connecting them, there is a circuit containing 3 junction boxes, a circuit containing 2 junction boxes, and 15 circuits which contain one junction box each.

The next two junction boxes are 431,825,988 and 425,690,689. Because these two junction boxes were already in the same circuit, nothing happens!

This process continues for a while, and the Elves are concerned that they don't have enough extension cables for all these circuits. They would like to know how big the circuits will be.

After making the ten shortest connections, there are 11 circuits: one circuit which contains 5 junction boxes, one circuit which contains 4 junction boxes, two circuits which contain 2 junction boxes each, and seven circuits which each contain a single junction box. Multiplying together the sizes of the three largest circuits (5, 4, and one of the circuits of size 2) produces 40.

Your list contains many junction boxes; connect together the 1000 pairs of junction boxes which are closest together. Afterward, what do you get if you multiply together the sizes of the three largest circuits?
*/

class JunctionBoxCircuits {

    /**
     * Parse junction box coordinates from input.
     * @param input The input text
     * @return List of [x, y, z] coordinates
     */
    static List<List<Integer>> parseJunctionBoxes(String input) {
        input.split('\n')*.trim().findAll { it }.collect { it.split(',')*.toInteger() }
    }

    /**
     * Calculate Euclidean distance between two 3D points.
     * @param box1 First junction box [x, y, z]
     * @param box2 Second junction box [x, y, z]
     * @return Distance between the two boxes
     */
    static double distance(List<Integer> box1, List<Integer> box2) {
        // TODO: Calculate sqrt((x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2)
        Math.sqrt(Math.pow(box2[0] - box1[0], 2) + Math.pow(box2[1] - box1[1], 2) + Math.pow(box2[2] - box1[2], 2))
    }

    /**
     * Find all pairs of junction boxes sorted by distance.
     * @param boxes List of junction box coordinates
     * @return List of pairs with their distances, sorted by distance
     */
    static List<Map> findClosestPairs(List<List<Integer>> boxes) {
        boxes.withIndex().collectMany { box1, i ->
            boxes.drop(i + 1).withIndex().collect { box2, j ->
                [box1Index: i, box2Index: i + j + 1, distance: distance(box1, box2)]
            }
        }.sort { it.distance }
    }

    /**
     * Connect the shortest pairs of junction boxes using Union-Find.
     * @param input The input text
     * @param numConnections Number of connections to make
     * @return Map with circuitSizes and circuitCount
     */
    static Map connectShortestPairs(String input, int numConnections) {
        def boxes = parseJunctionBoxes(input)
        def pairs = findClosestPairs(boxes)

        // Union-Find: parent[i] = parent of node i
        def parent = (0..<boxes.size()).collect { it }

        // Find root with path compression
        def find
        find = { int x ->
            if (parent[x] != x) {
                parent[x] = find(parent[x])
            }
            parent[x]
        }

        // Union two nodes
        def union = { int x, int y ->
            parent[find(x)] = find(y)
        }

        // Connect shortest pairs
        pairs.take(numConnections)*.with { union(box1Index, box2Index) }

        // Count circuit sizes
        def sizes = (0..<boxes.size()).groupBy { find(it) }.values()*.size().sort().reverse()

        [circuitSizes: sizes, circuitCount: sizes.size()]
    }

    /**
     * Calculate the product of the three largest circuits.
     * @param input The input text
     * @param numConnections Number of connections to make
     * @return Product of three largest circuit sizes
     */
    static long calculateCircuitProduct(String input, int numConnections) {
        connectShortestPairs(input, numConnections).circuitSizes.take(3).inject(1L) { acc, size -> acc * size }
    }

    /**
     * Find the last connection that creates a single circuit.
     * @param input The input text
     * @return Map with box1Index, box2Index, and distance
     */
    static Map findLastConnectionForSingleCircuit(String input) {
        // TODO: Keep connecting pairs until only 1 circuit remains
        // Return the last connection made
        def boxes = parseJunctionBoxes(input)
        def pairs = findClosestPairs(boxes)

        // Union-Find setup
        def parent = (0..<boxes.size()).collect { it }
        def find
        find = { int x ->
            if (parent[x] != x) {
                parent[x] = find(parent[x])
            }
            parent[x]
        }

        def union = { int x, int y ->
            def rootX = find(x)
            def rootY = find(y)
            if (rootX != rootY) {
                parent[rootX] = rootY
                return true
            }
            false
        }

        // Count circuits
        def circuitCount = { ->
            (0..<boxes.size()).collect { find(it) }.toSet().size()
        }

        // Connect until single circuit
        def lastConnection = null
        for (pair in pairs) {
            if (union(pair.box1Index, pair.box2Index)) {
                lastConnection = pair
                if (circuitCount() == 1) {
                    break
                }
            }
        }

        lastConnection
    }

    /**
     * Calculate product of X coordinates for last connection to single circuit.
     * @param input The input text
     * @return Product of X coordinates
     */
    static long calculateLastConnectionXProduct(String input) {
        def boxes = parseJunctionBoxes(input)
        def lastConnection = findLastConnectionForSingleCircuit(input)

        def box1 = boxes[lastConnection.box1Index]
        def box2 = boxes[lastConnection.box2Index]

        (long)box1[0] * box2[0]
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day8.txt').text

    // Part 1: Connect 1000 shortest pairs and find product of three largest circuits
    def product = JunctionBoxCircuits.calculateCircuitProduct(input, 1000)
    println("Part 1 - Product of three largest circuits: ${product}")

    // Part 2: Find last connection for single circuit and multiply X coordinates
    def xProduct = JunctionBoxCircuits.calculateLastConnectionXProduct(input)
    println("Part 2 - Product of X coordinates for last connection: ${xProduct}")
}
