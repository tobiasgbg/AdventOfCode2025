/*
--- Day 4: Printing Department ---
You ride the escalator down to the printing department. They're clearly getting ready for Christmas; they have lots of large rolls of paper everywhere, and there's even a massive printer in the corner (to handle the really big print jobs).

Decorating here will be easy: they can make their own decorations. What you really need is a way to get further into the North Pole base while the elevators are offline.

"Actually, maybe we can help with that," one of the Elves replies when you ask for help. "We're pretty sure there's a cafeteria on the other side of the back wall. If we could break through the wall, you'd be able to keep moving. It's too bad all of our forklifts are so busy moving those big rolls of paper around."

If you can optimize the work the forklifts are doing, maybe they would have time to spare to break through the wall.

The rolls of paper (@) are arranged on a large grid; the Elves even have a helpful diagram (your puzzle input) indicating where everything is located.

For example:

..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.

The forklifts can only access a roll of paper if there are fewer than four rolls of paper in the eight adjacent positions. If you can figure out which rolls of paper the forklifts can access, they'll spend less time looking and more time breaking down the wall to the cafeteria.

In this example, there are 13 rolls of paper that can be accessed by a forklift (marked with x):

..xx.xx@x.
x@@.@.@.@@
@@@@@.x.@@
@.@@@@..@.
x@.@@@@.@x
.@@@@@@@.@
.@.@.@.@@@
x.@@@.@@@@
.@@@@@@@@.
x.x.@@@.x.

Consider your complete diagram of the paper roll locations. How many rolls of paper can be accessed by a forklift?
 */

class PaperRollGrid {

    /**
     * Count the number of paper rolls (@) in the 8 adjacent positions.
     * @param grid The grid of paper rolls
     * @param row The row position
     * @param col The column position
     * @return The count of adjacent paper rolls
     */
    static int countAdjacentRolls(List<String> grid, int row, int col) {
      [[-1,-1],[-1,0],[-1,1],[0,-1],[0,1],[1,-1],[1,0],[1,1]].count { dr, dc ->
          def r = row + dr
          def c = col + dc
          r >= 0 && r < grid.size() && c >= 0 && c < grid[r].length() && grid[r][c] == '@'      
      }
    }

    /**
     * Determine if a paper roll at the given position is accessible.
     * A roll is accessible if it has fewer than 4 adjacent rolls.
     * @param grid The grid of paper rolls
     * @param row The row position
     * @param col The column position
     * @return True if accessible, false otherwise
     */
    static boolean isAccessible(List<String> grid, int row, int col) {
        grid[row][col] == '@' && countAdjacentRolls(grid, row, col) < 4
    }

    /**
     * Count the total number of accessible paper rolls in the grid.
     * @param grid The grid of paper rolls
     * @return The count of accessible rolls
     */
    static int countAccessibleRolls(List<String> grid) {
      (0..<grid.size()).sum { row ->
          (0..<grid[row].length()).count { col ->
              isAccessible(grid, row, col)
          }
      } ?: 0
    }

    /**
     * Parse the input text into a grid.
     * @param input The input text
     * @return List of strings representing the grid
     */
    static List<String> parseGrid(String input) {
        input.split('\n')*.trim().findAll { it }
    }

    // Part 2 methods - iterative removal

    /**
     * Find all accessible positions in the grid.
     * @param grid The grid of paper rolls
     * @return List of [row, col] positions that are accessible
     */
    static List<List<Integer>> findAccessiblePositions(List<String> grid) {
      (0..<grid.size()).collectMany { row ->
          (0..<grid[row].length()).findAll { col ->
              isAccessible(grid, row, col)
          }.collect { col -> [row, col] }
      }
    }

    /**
     * Remove all currently accessible rolls from the grid.
     * @param grid The grid of paper rolls (will be modified)
     * @return Tuple of [newGrid, removedCount]
     */
    static List removeAccessibleRolls(List<String> grid) {
      def positions = findAccessiblePositions(grid)

      def newGrid = (0..<grid.size()).collect { row ->
          (0..<grid[row].length()).collect { col ->
              positions.contains([row, col]) ? '.' : grid[row][col]
          }.join('')
      }

      [newGrid, positions.size()]
    }

    /**
     * Repeatedly remove accessible rolls until none remain.
     * @param grid The grid of paper rolls
     * @return Total number of rolls removed
     */
    static int removeAllAccessibleRolls(List<String> grid) {
        int totalRemoved = 0
        while(true) {
            def result = removeAccessibleRolls(grid)
            def newGrid = result[0]
            def removedCount = result[1]
            totalRemoved += removedCount
            if (removedCount == 0) {
                break
            }
            grid = newGrid
        }
        totalRemoved
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day4.txt').text
    def grid = PaperRollGrid.parseGrid(input)

    // Part 1: Count accessible paper rolls
    def accessibleCount = PaperRollGrid.countAccessibleRolls(grid)
    println("Part 1 - Accessible paper rolls: ${accessibleCount}")

    // Part 2: Remove all accessible rolls iteratively
    def totalRemoved = PaperRollGrid.removeAllAccessibleRolls(grid)
    println("Part 2 - Total rolls removed: ${totalRemoved}")
}
