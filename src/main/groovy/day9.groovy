/*
--- Day 9: Movie Theater ---
You slide down the firepole in the corner of the playground and land in the North Pole base movie theater!

The movie theater has a big tile floor with an interesting pattern. Elves here are redecorating the theater by switching out some of the square tiles in the big grid they form. Some of the tiles are red; the Elves would like to find the largest rectangle that uses red tiles for two of its opposite corners. They even have a list of where the red tiles are located in the grid (your puzzle input).

For example:

7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3

Showing red tiles as # and other tiles as ., the above arrangement of red tiles would look like this:

..............
.......#...#..
..............
..#....#......
..............
..#......#....
..............
.........#.#..
..............

You can choose any two red tiles as the opposite corners of your rectangle; your goal is to find the largest rectangle possible.

For example, you could make a rectangle (shown as O) with an area of 24 between 2,5 and 9,7:

..............
.......#...#..
..............
..#....#......
..............
..OOOOOOOO....
..OOOOOOOO....
..OOOOOOOO.#..
..............

Or, you could make a rectangle with area 35 between 7,1 and 11,7:

..............
.......OOOOO..
.......OOOOO..
..#....OOOOO..
.......OOOOO..
..#....OOOOO..
.......OOOOO..
.......OOOOO..
..............

You could even make a thin rectangle with an area of only 6 between 7,3 and 2,3:

..............
.......#...#..
..............
..OOOOOO......
..............
..#......#....
..............
.........#.#..
..............

Ultimately, the largest rectangle you can make in this example has area 50. One way to do this is between 2,5 and 11,1:

..............
..OOOOOOOOOO..
..OOOOOOOOOO..
..OOOOOOOOOO..
..OOOOOOOOOO..
..OOOOOOOOOO..
..............
.........#.#..
..............

Using two red tiles as opposite corners, what is the largest area of any rectangle you can make?
*/

class MovieTheater {

    /**
     * Parse red tile coordinates from input.
     * @param input The input text
     * @return List of [x, y] coordinates
     */
    static List<List<Integer>> parseRedTiles(String input) {
        input.split('\n')*.trim().findAll { it }.collect { it.split(',')*.toInteger() }
    }

    /**
     * Calculate area of rectangle with given opposite corners.
     * @param tile1 First corner [x, y]
     * @param tile2 Opposite corner [x, y]
     * @return Area of rectangle
     */
    static long calculateRectangleArea(List<Integer> tile1, List<Integer> tile2) {
        (Math.abs(tile2[0] - tile1[0]) + 1L) * (Math.abs(tile2[1] - tile1[1]) + 1L)
    }

    /**
     * Find the largest rectangle using red tiles as opposite corners.
     * @param input The input text
     * @return Maximum rectangle area
     */
    static long findLargestRectangle(String input) {
        def tiles = parseRedTiles(input)
        long maxArea = 0L
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                maxArea = Math.max(maxArea, calculateRectangleArea(tiles[i], tiles[j]))
            }
        }
        maxArea
    }

    /**
     * Find all green tiles using an optimized scan-line algorithm.
     * @param input The input text
     * @return Set of [x, y] green tile coordinates
     */
    static Set<List<Integer>> findGreenTiles(String input) {
        def redTiles = parseRedTiles(input)
        if (redTiles.size() < 3) return new HashSet<List<Integer>>()
        def greenTiles = new HashSet<List<Integer>>()

        // Add all perimeter tiles
        redTiles.eachWithIndex { tile, i ->
            def nextTile = redTiles[(i + 1) % redTiles.size()]

            if (tile[0] == nextTile[0]) {
                def (minY, maxY) = [tile[1], nextTile[1]].sort()
                (minY..maxY).each { y -> greenTiles.add([tile[0], y]) }
            } else {
                def (minX, maxX) = [tile[0], nextTile[0]].sort()
                (minX..maxX).each { x -> greenTiles.add([x, tile[1]]) }
            }
        }

        // Build lookup maps for fast perimeter queries by row and column
        def perimeterByRow = greenTiles.groupBy { it[1] }
        def perimeterByCol = greenTiles.groupBy { it[0] }

        // Get unique y-coordinates and x-coordinates that have perimeter tiles
        def yCoords = perimeterByRow.keySet().sort()
        def xCoords = perimeterByCol.keySet().sort()

        // Pre-compute vertical enclosure info for all columns
        def verticalEnclosure = [:]
        perimeterByCol.each { x, tiles ->
            def yValues = tiles.collect { it[1] }.sort()
            def minY = yValues.min()
            def maxY = yValues.max()
            verticalEnclosure[x] = [minY: minY, maxY: maxY, yValues: yValues as Set]
        }

        // Use scan-line to fill interior - only scan rows that have perimeter tiles
        // Skip interior filling if gaps are too large (optimization for large inputs)
        yCoords.each { y ->
            def tilesInRow = perimeterByRow[y].collect { it[0] }.sort()

            // For each pair of x-coordinates in this row, check if space between should be filled
            for (int i = 0; i < tilesInRow.size() - 1; i++) {
                int x1 = tilesInRow[i]
                int x2 = tilesInRow[i + 1]
                int gap = x2 - x1 - 1

                // Skip very large gaps (likely not interior tiles)
                if (gap > 1000) continue

                // Check if points between x1 and x2 are enclosed vertically
                for (int x = x1 + 1; x < x2; x++) {
                    def enclosure = verticalEnclosure[x]
                    if (enclosure && enclosure.minY < y && enclosure.maxY > y) {
                        greenTiles.add([x, y])
                    }
                }
            }
        }

        greenTiles
    }

    /**
     * Find largest valid rectangle using only red/green tiles.
     * Two OPPOSITE corners must be RED tiles (the other two can be any valid tile).
     * Optimized to avoid materializing all green tiles for large inputs.
     * @param input The input text
     * @return Maximum valid rectangle area
     */
    static long findLargestValidRectangle(String input) {
        def redTiles = parseRedTiles(input)
        def redTilesSet = redTiles.collect { [it[0], it[1]] } as Set

        // Build perimeter only (don't materialize all interior tiles)
        def perimeterTiles = new HashSet<List<Integer>>()
        redTiles.eachWithIndex { tile, i ->
            def nextTile = redTiles[(i + 1) % redTiles.size()]
            if (tile[0] == nextTile[0]) {
                def (minY, maxY) = [tile[1], nextTile[1]].sort()
                (minY..maxY).each { y -> perimeterTiles.add([tile[0], y]) }
            } else {
                def (minX, maxX) = [tile[0], nextTile[0]].sort()
                (minX..maxX).each { x -> perimeterTiles.add([x, tile[1]]) }
            }
        }

        // Pre-compute min/max for each column and row (O(1) enclosure check)
        def colMinY = [:].withDefault { Integer.MAX_VALUE }
        def colMaxY = [:].withDefault { Integer.MIN_VALUE }
        def rowMinX = [:].withDefault { Integer.MAX_VALUE }
        def rowMaxX = [:].withDefault { Integer.MIN_VALUE }

        perimeterTiles.each { tile ->
            int x = tile[0], y = tile[1]
            if (y < colMinY[x]) colMinY[x] = y
            if (y > colMaxY[x]) colMaxY[x] = y
            if (x < rowMinX[y]) rowMinX[y] = x
            if (x > rowMaxX[y]) rowMaxX[y] = x
        }

        // Sort red tiles by area potential (largest first) for better pruning
        def tilePairs = []
        for (int i = 0; i < redTiles.size(); i++) {
            for (int j = i + 1; j < redTiles.size(); j++) {
                long area = calculateRectangleArea(redTiles[i], redTiles[j])
                if (area > 0) tilePairs.add([i, j, area])
            }
        }
        tilePairs.sort { -it[2] }  // Sort by area descending

        long maxArea = 0L

        for (def pair : tilePairs) {
            long potentialArea = pair[2]
            if (potentialArea <= maxArea) break  // All remaining are smaller

            def tile1 = redTiles[pair[0]]
            def tile2 = redTiles[pair[1]]

            int minX = Math.min(tile1[0], tile2[0])
            int maxX = Math.max(tile1[0], tile2[0])
            int minY = Math.min(tile1[1], tile2[1])
            int maxY = Math.max(tile1[1], tile2[1])

            // O(width + height) validation instead of O(width * height)
            // For interior to be valid: all columns must span y-range, all rows must span x-range
            boolean allValid = true

            // Check all columns in range have perimeter spanning the y-range
            for (int x = minX; x <= maxX && allValid; x++) {
                // Column x needs perimeter at or above minY and at or below maxY
                if (colMinY[x] > minY || colMaxY[x] < maxY) {
                    allValid = false
                }
            }

            // Check all rows in range have perimeter spanning the x-range
            for (int y = minY; y <= maxY && allValid; y++) {
                // Row y needs perimeter at or left of minX and at or right of maxX
                if (rowMinX[y] > minX || rowMaxX[y] < maxX) {
                    allValid = false
                }
            }

            if (allValid) {
                maxArea = potentialArea
                break  // Since sorted by area, first valid is largest
            }
        }
        return maxArea
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day9.txt').text

    // Part 1: Find largest rectangle area
    def maxArea = MovieTheater.findLargestRectangle(input)
    println("Part 1 - Largest rectangle area: ${maxArea}")

    // Part 2: Find largest valid rectangle (only red/green tiles)
    def maxValidArea = MovieTheater.findLargestValidRectangle(input)
    println("Part 2 - Largest valid rectangle area: ${maxValidArea}")
}
