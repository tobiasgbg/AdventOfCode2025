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
    Set<List<Integer>> cells = []  // Set of [x, y] coordinates where # appears

    Shape(int index, List<String> lines) {
        this.index = index
        lines.eachWithIndex { line, y ->
            line.eachWithIndex { ch, x ->
                if (ch == '#') cells.add([x, y])
            }
        }
    }

    // TODO: Generate all rotations and flips of this shape
    List<Set<List<Integer>>> getAllOrientations() {
        // Return list of all unique orientations (rotations + flips)
        []
    }
}

class Region {
    int width
    int height
    List<Integer> presentCounts = []  // Count of each shape needed

    Region(String line) {
        def matcher = line =~ /(\d+)x(\d+):\s*(.*)/
        if (matcher.matches()) {
            width = matcher[0][1] as int
            height = matcher[0][2] as int
            presentCounts = matcher[0][3].trim().split(/\s+/).collect { it as int }
        }
    }
}

class ChristmasTreeFarm {
    List<Shape> shapes = []
    List<Region> regions = []

    ChristmasTreeFarm(String input) {
        def sections = input.split(/\n\n/)

        // Parse shapes from first section(s)
        def shapeLines = []
        int currentIndex = -1

        sections.each { section ->
            section.split('\n').each { line ->
                if (line =~ /^\d+:$/) {
                    // New shape definition
                    if (currentIndex >= 0 && shapeLines) {
                        shapes.add(new Shape(currentIndex, shapeLines))
                    }
                    currentIndex = (line - ':') as int
                    shapeLines = []
                } else if (line =~ /^\d+x\d+:/) {
                    // Region definition
                    if (currentIndex >= 0 && shapeLines) {
                        shapes.add(new Shape(currentIndex, shapeLines))
                        currentIndex = -1
                        shapeLines = []
                    }
                    regions.add(new Region(line))
                } else if (line.trim() && currentIndex >= 0) {
                    // Shape line
                    shapeLines.add(line)
                }
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
        regions.count{ region -> canFitPresents(region) }
    }

    /**
     * Check if a region can fit all its required presents
     */
    boolean canFitPresents(Region region) {
        // TODO: Implement backtracking/constraint satisfaction solver
        false
    }
}

static void main(String[] args) {
    try {
        String filePath = "../../../input/day12.txt"
        File file = new File(filePath)
        String input = file.text

        ChristmasTreeFarm farm = new ChristmasTreeFarm(input)
        int result = farm.countFittableRegions()
        println("Part 1: ${result}")

        println("Part 2: Not yet implemented")
    } catch (FileNotFoundException e) {
        println("File not found: " + e.message)
    } catch (IOException e) {
        println("Error reading file: " + e.message)
    }
}
