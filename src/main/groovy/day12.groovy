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
    List<Set<List<Integer>>> cachedOrientations = null  // Cache orientations

    Shape(int index, List<String> lines) {
        this.index = index
        cells = lines.withIndex().collectMany { line, y ->
            line.toList().withIndex().findAll { ch, x -> ch == '#' }
                                     .collect { ch, x -> [x, y] }
        } as Set
    }

    /**
     * Generate all unique orientations (rotations and flips) of this shape
     */
    List<Set<List<Integer>>> getAllOrientations() {
        if (cachedOrientations != null) return cachedOrientations

        def orientations = [] as Set

        // Generate all 8 possible transformations
        [false, true].each { flip ->
            [0, 90, 180, 270].each { degrees ->
                def transformed = transform(cells, flip, degrees)
                orientations << normalize(transformed)
            }
        }

        cachedOrientations = orientations as List
        cachedOrientations
    }

    /**
     * Transform cells by flipping and rotating
     */
    private Set<List<Integer>> transform(Set<List<Integer>> cells, boolean flip, int degrees) {
        def result = cells.collect { coord ->
            def (x, y) = coord
            if (flip) x = -x

            switch (degrees) {
                case 90:  return [-y, x]
                case 180: return [-x, -y]
                case 270: return [y, -x]
                default:  return [x, y]
            }
        } as Set
        result
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

        // Parsed ${shapes.size()} shapes and ${regions.size()} regions
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
            def fits = canFitPresents(region, 5000) // 5 second timeout per region
            long elapsed = System.currentTimeMillis() - startTime

            println "  -> ${fits ? 'FITS' : 'DOES NOT FIT'} (${elapsed}ms)"
            if (fits) fittable++
        }
        println "\nTotal regions that fit: $fittable out of ${regions.size()}"
        fittable
    }

    /**
     * Check if a region can fit all its required presents
     */
    boolean canFitPresents(Region region, long timeoutMs = 10000) {
        // Build list of presents to place
        def presentsToPlace = []
        region.presentCounts.eachWithIndex { count, shapeIndex ->
            count.times {
                // Find the shape with matching index (not array position!)
                def shape = shapes.find { it.index == shapeIndex }
                if (shape) {
                    presentsToPlace << shape
                }
            }
        }

        if (presentsToPlace.isEmpty()) return true  // No presents to place

        // Start backtracking with empty grid and all pieces unplaced
        def occupied = [] as Set
        def placed = new boolean[presentsToPlace.size()]
        long deadline = System.currentTimeMillis() + timeoutMs
        backtrackIndexed(presentsToPlace, placed, region, occupied, deadline)
    }

    /**
     * Indexed backtracking - faster than creating new lists
     */
    private boolean backtrackIndexed(List<Shape> allPieces, boolean[] placed, Region region, Set<List<Integer>> occupied, long deadline) {
        // Check timeout every so often
        if (System.currentTimeMillis() > deadline) {
            return false  // Timeout - assume doesn't fit
        }

        // Count how many pieces are left (manually to avoid potential issues with boolean[])
        def remaining = 0
        for (int i = 0; i < placed.length; i++) {
            if (!placed[i]) remaining++
        }

        if (remaining == 0) {
            return true  // All pieces successfully placed
        }

        // Pruning: check if enough space remains
        def remainingCells = region.width * region.height - occupied.size()
        def neededCells = 0
        for (int i = 0; i < allPieces.size(); i++) {
            if (!placed[i]) neededCells += allPieces[i].cells.size()
        }
        if (remainingCells < neededCells) return false

        // Find most-constrained unplaced piece
        def bestIdx = -1
        def bestPlacements = null
        def minPlacements = Integer.MAX_VALUE

        for (int i = 0; i < allPieces.size(); i++) {
            if (placed[i]) continue  // Skip already placed pieces

            def placements = getValidPlacements(allPieces[i], region, occupied)
            if (placements.size() < minPlacements) {
                minPlacements = placements.size()
                bestIdx = i
                bestPlacements = placements
            }
            if (minPlacements == 0) break  // Early exit
        }

        if (bestIdx == -1 || bestPlacements.isEmpty()) return false

        // Try each valid placement for the chosen piece
        for (placement in bestPlacements) {
            occupied.addAll(placement)
            placed[bestIdx] = true

            if (backtrackIndexed(allPieces, placed, region, occupied, deadline)) {
                return true
            }

            occupied.removeAll(placement)
            placed[bestIdx] = false
        }

        false
    }

    /**
     * Select the piece with fewest valid placements (most-constrained-first heuristic)
     */
    private Tuple2 selectMostConstrainedPiece(List<Shape> pieces, Region region, Set<List<Integer>> occupied) {
        def bestPiece = null
        def bestPlacements = null
        def minPlacements = Integer.MAX_VALUE

        for (piece in pieces) {
            def placements = getValidPlacements(piece, region, occupied)
            if (placements.size() < minPlacements) {
                minPlacements = placements.size()
                bestPiece = piece
                bestPlacements = placements
            }
            // Early exit if we find a piece with no placements
            if (minPlacements == 0) break
        }

        new Tuple2(bestPiece, bestPlacements ?: [])
    }

    /**
     * Get all valid placements for a piece
     */
    private List<Set<List<Integer>>> getValidPlacements(Shape shape, Region region, Set<List<Integer>> occupied) {
        def placements = []

        for (orientation in shape.getAllOrientations()) {
            for (y in 0..<region.height) {
                for (x in 0..<region.width) {
                    def placedCells = orientation.collect { coord ->
                        [coord[0] + x, coord[1] + y]
                    } as Set

                    if (isValidPlacement(placedCells, region, occupied)) {
                        placements << placedCells
                    }
                }
            }
        }

        placements
    }

    /**
     * Forward checking: verify each remaining piece has at least one valid placement
     */
    private boolean forwardCheck(List<Shape> remaining, Region region, Set<List<Integer>> occupied) {
        // Quick check: is there enough space?
        def remainingCells = region.width * region.height - occupied.size()
        def neededCells = remaining.sum(0) { it.cells.size() }
        if (remainingCells < neededCells) return false

        // More thorough check: verify we can place at least one more piece
        // (checking all pieces would be too expensive, this is a compromise)
        if (!remaining.isEmpty() && remaining.size() <= 3) {
            // For small remaining sets, check all pieces have placements
            for (shape in remaining) {
                if (!hasValidPlacement(shape, region, occupied)) {
                    return false
                }
            }
        }

        true
    }

    /**
     * Check if a shape has at least one valid placement
     */
    private boolean hasValidPlacement(Shape shape, Region region, Set<List<Integer>> occupied) {
        for (orientation in shape.getAllOrientations()) {
            for (y in 0..<region.height) {
                for (x in 0..<region.width) {
                    def placedCells = orientation.collect { coord ->
                        [coord[0] + x, coord[1] + y]
                    } as Set

                    if (isValidPlacement(placedCells, region, occupied)) {
                        return true  // Found at least one valid placement
                    }
                }
            }
        }
        false
    }

    /**
     * Check if placement is valid (within bounds and no overlap)
     */
    private boolean isValidPlacement(Set<List<Integer>> cells, Region region, Set<List<Integer>> occupied) {
        cells.every { coord ->
            def (x, y) = coord
            // Check bounds
            x >= 0 && x < region.width && y >= 0 && y < region.height &&
            // Check no overlap
            !occupied.contains(coord)
        }
    }
}

static void main(String[] args) {
    def input = new File("../../../input/day12.txt").text
    def farm = new ChristmasTreeFarm(input)

    println "Part 1: ${farm.countFittableRegions()}"
    println "Part 2: Not yet implemented"
}
