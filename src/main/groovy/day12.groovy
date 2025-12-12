/*
--- Day 12: Christmas Tree Farm ---
You're almost out of time, but there can't be much left to decorate. Although there are no stairs, elevators, escalators, tunnels, chutes, teleporters, firepoles, or conduits here that would take you deeper into the North Pole base, there is a ventilation duct. You jump in.

After bumping around for a few minutes, you emerge into a large, well-lit cavern full of Christmas trees!

There are a few Elves here frantically decorating before the deadline. They think they'll be able to finish most of the work, but the one thing they're worried about is the presents for all the young Elves that live here at the North Pole. It's an ancient tradition to put the presents under the trees, but the Elves are worried they won't fit.

The presents come in a few standard but very weird shapes. The shapes and the regions into which they need to fit are all measured in standard units. To be aesthetically pleasing, the presents need to be placed into the regions in a way that follows a standardized two-dimensional unit grid; you also can't stack presents.

As always, the Elves have a summary of the situation (your puzzle input) for you. First, it contains a list of the presents' shapes. Second, it contains the size of the region under each tree and a list of the number of presents of each shape that need to fit into that region. For example:

0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2

The first section lists the standard present shapes. For convenience, each shape starts with its index and a colon; then, the shape is displayed visually, where # is part of the shape and . is not.

The second section lists the regions under the trees. Each line starts with the width and length of the region; 12x5 means the region is 12 units wide and 5 units long. The rest of the line describes the presents that need to fit into that region by listing the quantity of each shape of present; 1 0 1 0 3 2 means you need to fit one present with shape index 0, no presents with shape index 1, one present with shape index 2, no presents with shape index 3, three presents with shape index 4, and two presents with shape index 5.

Presents can be rotated and flipped as necessary to make them fit in the available space, but they have to always be placed perfectly on the grid. Shapes can't overlap (that is, the # part from two different presents can't go in the same place on the grid), but they can fit together (that is, the . part in a present's shape's diagram does not block another present from occupying that space on the grid).

The Elves need to know how many of the regions can fit the presents listed. In the above example, there are six unique present shapes and three regions that need checking.

The first region is 4x4. In it, you need to fit two presents that have shape index 4:
###
#..
###

After some experimentation, it turns out that you can fit both presents in this region:
AAA.
ABAB
ABAB
.BBB

The second region, 12x5: 1 0 1 0 2 2, can fit all presents:
....AAAFFE.E
.BBBAAFFFEEE
DDDBAAFFCECE
DBBB....CCC.
DDD.....C.C.

The third region, 12x5: 1 0 1 0 3 2, cannot fit all presents (one additional shape 4).

So, in this example, 2 regions can fit all of their listed presents.

Consider the regions beneath each tree and the presents the Elves would like to fit into each of them. How many of the regions can fit all of the presents listed?
*/

class Shape {
    int index
    Set<List<Integer>> cells = [] as Set  // Set of [x, y] coordinates where # appears
    List<int[][]> cachedOrientations = null  // Cache orientations as int arrays

    Shape(int index, List<String> lines) {
        this.index = index
        cells = lines.withIndex().collectMany { line, y ->
            line.toList().withIndex().findAll { ch, x -> ch == '#' }
                                     .collect { ch, x -> [x, y] }
        } as Set
    }

    /**
     * Generate all unique orientations as int[][] arrays for fast iteration
     */
    List<int[][]> getAllOrientations() {
        if (cachedOrientations != null) return cachedOrientations

        def orientations = [] as Set

        // Generate all 8 possible transformations
        [false, true].each { flip ->
            [0, 90, 180, 270].each { degrees ->
                def transformed = transform(cells, flip, degrees)
                orientations << normalize(transformed)
            }
        }

        // Convert to int[][] for faster iteration
        cachedOrientations = orientations.collect { cellSet ->
            cellSet.collect { [it[0] as int, it[1] as int] as int[] } as int[][]
        }
        cachedOrientations
    }

    /**
     * Transform cells by flipping and rotating
     */
    private Set<List<Integer>> transform(Set<List<Integer>> cells, boolean flip, int degrees) {
        cells.collect { coord ->
            def (x, y) = coord
            if (flip) x = -x

            switch (degrees) {
                case 90:  return [-y, x]
                case 180: return [-x, -y]
                case 270: return [y, -x]
                default:  return [x, y]
            }
        } as Set
    }

    /**
     * Normalize shape to start at (0,0)
     */
    private Set<List<Integer>> normalize(Set<List<Integer>> cells) {
        if (!cells) return cells

        def minX = cells*.get(0).min()
        def minY = cells*.get(1).min()

        cells.collect { coord ->
            [coord[0] - minX, coord[1] - minY]
        } as Set
    }
}

class Region {
    int width
    int height
    List<Integer> presentCounts  // Count of each shape needed

    Region(String line) {
        // Trim to remove any trailing \r or whitespace
        def cleanLine = line.trim()
        def matcher = cleanLine =~ /(\d+)x(\d+):\s*(.*)/
        if (matcher.matches()) {
            width = matcher[0][1] as int
            height = matcher[0][2] as int
            presentCounts = matcher[0][3].trim().split(/\s+/)*.toInteger()
        }
    }
}

class ChristmasTreeFarm {
    List<Shape> shapes = []
    List<Region> regions = []

    ChristmasTreeFarm(String input) {
        def lines = input.split('\n')
        def shapeLines = []
        int currentIndex = -1

        lines.eachWithIndex { line, idx ->
            def cleanLine = line.trim()  // Remove \r and whitespace

            if (cleanLine =~ /^\d+:$/) {
                // New shape definition
                if (currentIndex >= 0 && shapeLines) {
                    shapes.add(new Shape(currentIndex, shapeLines))
                }
                currentIndex = (cleanLine - ':') as int
                shapeLines = []
            } else if (cleanLine =~ /^\d+x\d+:/) {
                // Region definition
                if (currentIndex >= 0 && shapeLines) {
                    shapes.add(new Shape(currentIndex, shapeLines))
                    currentIndex = -1
                    shapeLines = []
                }
                def region = new Region(cleanLine)
                regions.add(region)
            } else if (cleanLine && currentIndex >= 0) {
                // Shape line
                shapeLines.add(cleanLine)
            }
        }

        // Add last shape if any
        if (currentIndex >= 0 && shapeLines) {
            shapes.add(new Shape(currentIndex, shapeLines))
        }
    }

    /**
     * Part 1: Count how many regions can fit all their required presents
     */
    int countFittableRegions() {
        def fittable = 0
        regions.eachWithIndex { region, idx ->
            def pieces = []
            region.presentCounts.eachWithIndex { count, shapeIdx ->
                count.times {
                    def shape = shapes.find { it.index == shapeIdx }
                    if (shape) pieces << shape
                }
            }
            def totalCells = pieces.sum(0) { it.cells.size() }

            println "Checking region ${idx+1}/${regions.size()}: ${region.width}x${region.height}, ${pieces.size()} pieces, ${totalCells} cells..."
            long startTime = System.currentTimeMillis()
            def fits = canFitPresentsFast(region)
            long elapsed = System.currentTimeMillis() - startTime

            println "  -> ${fits ? 'FITS' : 'DOES NOT FIT'} (${elapsed}ms)"
            if (fits) fittable++
        }
        println "\nTotal regions that fit: $fittable out of ${regions.size()}"
        fittable
    }

    /**
     * Optimized solver using BitSet for O(1) collision detection
     */
    boolean canFitPresentsFast(Region region) {
        int width = region.width
        int height = region.height
        int totalCells = width * height

        // Build list of pieces to place
        def pieces = []
        region.presentCounts.eachWithIndex { count, shapeIndex ->
            count.times {
                def shape = shapes.find { it.index == shapeIndex }
                if (shape) pieces << shape
            }
        }

        if (pieces.isEmpty()) return true

        // Quick area check
        int neededCells = pieces.sum { it.cells.size() }
        if (neededCells > totalCells) return false

        // Pre-compute all valid placements as BitSets for each piece
        List<List<BitSet>> allPlacements = pieces.collect { shape ->
            def placements = [] as Set  // Use Set to deduplicate
            for (int[][] orientation : shape.getAllOrientations()) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        BitSet bits = new BitSet(totalCells)
                        boolean valid = true
                        for (int[] cell : orientation) {
                            int px = cell[0] + x
                            int py = cell[1] + y
                            if (px < 0 || px >= width || py < 0 || py >= height) {
                                valid = false
                                break
                            }
                            bits.set(py * width + px)
                        }
                        if (valid) placements << bits
                    }
                }
            }
            placements as List
        }

        // Check if any piece has no valid placements
        if (allPlacements.any { it.isEmpty() }) return false

        // Sort pieces by number of placements (most constrained first)
        def indices = (0..<pieces.size()).toList()
        indices.sort { allPlacements[it].size() }

        // Reorder placements according to sorted indices
        def sortedPlacements = indices.collect { allPlacements[it] }

        // Backtrack with BitSet
        BitSet occupied = new BitSet(totalCells)
        backtrackBitSet(sortedPlacements, 0, occupied, totalCells, 0, null, false)
    }

    /**
     * Fast backtracking using BitSet for collision detection
     */
    private long deadline = 0
    private int iterations = 0

    private boolean backtrackBitSet(List<List<BitSet>> allPlacements, int pieceIdx, BitSet occupied, int totalCells,
                                    int width = 0, List<BitSet> solution = null, boolean debug = false) {
        if (pieceIdx >= allPlacements.size()) return true

        // Check timeout periodically
        if (++iterations % 10000 == 0 && deadline > 0 && System.currentTimeMillis() > deadline) {
            return false
        }

        // Pruning: check remaining space
        int usedCells = occupied.cardinality()
        int neededCells = 0
        for (int i = pieceIdx; i < allPlacements.size(); i++) {
            if (!allPlacements[i].isEmpty()) {
                neededCells += allPlacements[i][0].cardinality()
            }
        }
        if (usedCells + neededCells > totalCells) return false

        // Filter valid placements for current piece
        def validPlacements = allPlacements[pieceIdx].findAll { !occupied.intersects(it) }
        if (validPlacements.isEmpty()) return false

        // Try each valid placement
        for (BitSet placement : validPlacements) {
            // Set bits (Groovy's BitSet.or() doesn't modify in place!)
            for (int bit = placement.nextSetBit(0); bit >= 0; bit = placement.nextSetBit(bit + 1)) {
                occupied.set(bit)
            }
            if (solution != null) solution[pieceIdx] = placement
            if (backtrackBitSet(allPlacements, pieceIdx + 1, occupied, totalCells, width, solution, debug)) {
                return true
            }
            // Clear the bits we set
            for (int bit = placement.nextSetBit(0); bit >= 0; bit = placement.nextSetBit(bit + 1)) {
                occupied.clear(bit)
            }
        }

        false
    }

    /**
     * Debug: solve and print the solution
     */
    boolean canFitPresentsFastDebug(Region region) {
        int width = region.width
        int height = region.height
        int totalCells = width * height

        def pieces = []
        region.presentCounts.eachWithIndex { count, shapeIndex ->
            count.times {
                def shape = shapes.find { it.index == shapeIndex }
                if (shape) pieces << shape
            }
        }

        if (pieces.isEmpty()) return true

        int neededCells = pieces.sum { it.cells.size() }
        if (neededCells > totalCells) return false

        List<List<BitSet>> allPlacements = pieces.collect { shape ->
            def placements = [] as Set
            for (int[][] orientation : shape.getAllOrientations()) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        BitSet bits = new BitSet(totalCells)
                        boolean valid = true
                        for (int[] cell : orientation) {
                            int px = cell[0] + x
                            int py = cell[1] + y
                            if (px < 0 || px >= width || py < 0 || py >= height) {
                                valid = false
                                break
                            }
                            bits.set(py * width + px)
                        }
                        if (valid) placements << bits
                    }
                }
            }
            placements as List
        }

        if (allPlacements.any { it.isEmpty() }) return false

        println "Pieces to place: ${pieces.size()}"
        allPlacements.eachWithIndex { placements, idx ->
            def cellCount = placements.isEmpty() ? 0 : placements[0].cardinality()
            println "  Piece $idx (shape ${pieces[idx].index}): ${placements.size()} placements, $cellCount cells each"
        }

        def indices = (0..<pieces.size()).toList()
        indices.sort { allPlacements[it].size() }
        def sortedPlacements = indices.collect { allPlacements[it] }
        def sortedPieces = indices.collect { pieces[it] }

        BitSet occupied = new BitSet(totalCells)
        List<BitSet> solution = new ArrayList<>(sortedPlacements.size())
        sortedPlacements.size().times { solution.add(null) }

        boolean found = backtrackBitSet(sortedPlacements, 0, occupied, totalCells, width, solution, true)

        if (found) {
            println "Found solution:"
            println "Solution list size: ${solution.size()}"
            solution.eachWithIndex { bs, idx ->
                if (bs != null) {
                    def bits = []
                    for (int bit = bs.nextSetBit(0); bit >= 0; bit = bs.nextSetBit(bit + 1)) {
                        bits << "[${bit % width},${bit / width as int}]"
                    }
                    println "  Piece $idx: ${bits.join(', ')}"
                } else {
                    println "  Piece $idx: NULL"
                }
            }
            char[][] grid = new char[height][width]
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    grid[y][x] = '.'
                }
            }
            solution.eachWithIndex { bs, idx ->
                if (bs != null) {
                    char label = (char)('A'.charAt(0) + idx)
                    for (int bit = bs.nextSetBit(0); bit >= 0; bit = bs.nextSetBit(bit + 1)) {
                        int x = bit % width
                        int y = bit / width
                        grid[y][x] = label
                    }
                }
            }
            for (int y = 0; y < height; y++) {
                println new String(grid[y])
            }
        }

        found
    }
}

static void main(String[] args) {
    def input = new File("../../../input/day12.txt").text
    def farm = new ChristmasTreeFarm(input)

    println "Part 1: ${farm.countFittableRegions()}"
    println "Part 2: Not yet implemented"
}
