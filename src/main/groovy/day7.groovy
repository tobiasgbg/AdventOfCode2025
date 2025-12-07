/*
--- Day 7: Laboratories ---
You thank the cephalopods for the help and exit the trash compactor, finding yourself in the familiar halls of a North Pole research wing.

Based on the large sign that says "teleporter hub", they seem to be researching teleportation; you can't help but try it for yourself and step onto the large yellow teleporter pad.

Suddenly, you find yourself in an unfamiliar room! The room has no doors; the only way out is the teleporter. Unfortunately, the teleporter seems to be leaking magic smoke.

Since this is a teleporter lab, there are lots of spare parts, manuals, and diagnostic equipment lying around. After connecting one of the diagnostic tools, it helpfully displays error code 0H-N0, which apparently means that there's an issue with one of the tachyon manifolds.

You quickly locate a diagram of the tachyon manifold (your puzzle input). A tachyon beam enters the manifold at the location marked S; tachyon beams always move downward. Tachyon beams pass freely through empty space (.). However, if a tachyon beam encounters a splitter (^), the beam is stopped; instead, a new tachyon beam continues from the immediate left and from the immediate right of the splitter.

For example:

.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............

In this example, the incoming tachyon beam (|) extends downward from S until it reaches the first splitter, where it splits into two beams going left and right. Those beams continue downward until they reach more splitters, and so on.

To repair the teleporter, you first need to understand the beam-splitting properties of the tachyon manifold. In this example, a tachyon beam is split a total of 21 times.

Analyze your manifold diagram. How many times will the beam be split?
 */

class TachyonManifold {

    /**
     * Find the starting position (S) in the diagram.
     * @param diagram The manifold diagram
     * @return [row, col] position of S
     */
    static List<Integer> findStart(List<String> diagram) {
        [0, diagram[0].indexOf('S')]
    }

    /**
     * Check if a character is a splitter.
     * @param c The character
     * @return True if it's a splitter (^)
     */
    static boolean isSplitter(String c) {
        c == '^'
    }

    /**
     * Count how many times the beam is split in the manifold.
     * @param diagram The manifold diagram
     * @return Total number of beam splits
     */
    static int countBeamSplits(List<String> diagram) {
        def queue = [findStart(diagram)] as Queue
        def visitedSplitters = [] as Set
        def splitCount = 0

        while (queue) {
            def (row, col) = queue.poll()

            // Move beam downward until it hits a splitter or leaves the grid
            def splitterPos = (row..<diagram.size()).find { r ->
                isSplitter(diagram[r][col])
            }

            if (splitterPos != null && visitedSplitters.add([splitterPos, col])) {
                splitCount++

                // Spawn new beams at next row, going left and right
                def nextRow = splitterPos + 1
                queue.addAll(
                    [[nextRow, col - 1], [nextRow, col + 1]]
                        .findAll { r, c ->
                            r in 0..<diagram.size() && c in 0..<diagram[r]?.length()
                        }
                )
            }
        }

        splitCount
    }

    /**
     * Count quantum timelines using many-worlds interpretation.
     * A quantum particle takes BOTH paths at each splitter.
     * Each timeline represents a unique ending position.
     * @param diagram The manifold diagram
     * @return Number of different timelines
     */
    static long countQuantumTimelines(List<String> diagram, List<Integer> start = null, Map cache = [:]) {
        start = start ?: findStart(diagram)
        def (row, col) = start

        // Check cache first
        def key = [row, col]
        if (cache.containsKey(key)) {
            return cache[key]
        }

        // Out of bounds - path exits, count as 1 timeline
        if (col < 0 || col >= diagram[0].length()) {
            return 1L
        }

        // Reached bottom - path exits, count as 1 timeline
        if (row >= diagram.size()) {
            return 1L
        }

        // Find next splitter in this column
        def splitterPos = (row..<diagram.size()).find { r ->
            isSplitter(diagram[r][col])
        }

        // No splitter - particle exits bottom, count as 1 timeline
        if (splitterPos == null) {
            return 1L
        }

        // Hit splitter - quantum split into left and right paths
        def leftCount = countQuantumTimelines(diagram, [splitterPos + 1, col - 1], cache)
        def rightCount = countQuantumTimelines(diagram, [splitterPos + 1, col + 1], cache)

        def result = leftCount + rightCount
        cache[key] = result
        return result
    }

    /**
     * Parse the input text into a diagram.
     * @param input The input text
     * @return List of strings representing the diagram
     */
    static List<String> parseDiagram(String input) {
        input.split('\n')*.trim().findAll { it }.collect { it }
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day7.txt').text
    def diagram = TachyonManifold.parseDiagram(input)

    // Part 1: Count beam splits
    def splits = TachyonManifold.countBeamSplits(diagram)
    println("Part 1 - Total beam splits: ${splits}")

    // Part 2: Count quantum timelines (many-worlds interpretation)
    def timelines = TachyonManifold.countQuantumTimelines(diagram)
    println("Part 2 - Quantum timelines: ${timelines}")
}
