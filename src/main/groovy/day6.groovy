/*
--- Day 6: Trash Compactor ---
After helping the Elves in the kitchen, you were taking a break and helping them re-enact a movie scene when you over-enthusiastically jumped into the garbage chute!

A brief fall later, you find yourself in a garbage smasher. Unfortunately, the door's been magnetically sealed.

As you try to find a way out, you are approached by a family of cephalopods! They're pretty sure they can get the door open, but it will take some time. While you wait, they're curious if you can help the youngest cephalopod with her math homework.

Cephalopod math doesn't look that different from normal math. The math worksheet (your puzzle input) consists of a list of problems; each problem has a group of numbers that need to either be either added (+) or multiplied (*) together.

However, the problems are arranged a little strangely; they seem to be presented next to each other in a very long horizontal list. For example:

123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +

Each problem's numbers are arranged vertically; at the bottom of the problem is the symbol for the operation that needs to be performed. Problems are separated by a full column of only spaces. The left/right alignment of numbers within each problem can be ignored.

So, this worksheet contains four problems:

123 * 45 * 6 = 33210
328 + 64 + 98 = 490
51 * 387 * 215 = 4243455
64 + 23 + 314 = 401

To check their work, cephalopod students are given the grand total of adding together all of the answers to the individual problems. In this worksheet, the grand total is 33210 + 490 + 4243455 + 401 = 4277556.

Of course, the actual worksheet is much wider. You'll need to make sure to unroll it completely so that you can read the problems clearly.

Solve the problems on the math worksheet. What is the grand total found by adding together all of the answers to the individual problems?
 */

class MathWorksheet {

    /**
     * Check if a row contains operators (+ or *).
     * @param row The row string
     * @return True if the row contains operators
     */
    static boolean isOperatorRow(String row) {
        row.find(/[*+]/)
    }

    /**
     * Parse the worksheet into vertical columns.
     * @param input The worksheet text
     * @return List of columns, where each column is a list of strings
     */
    static List<List<String>> parseColumns(String input) {
      input.split('\n').with { rows ->
          def maxWidth = rows*.length().max()
          (0..<maxWidth)
              .collect { i -> rows*.padRight(maxWidth)*.getAt(i) }
              .inject([[]]) { acc, col ->
                  col.every { it == ' ' } ? acc << [] : acc[-1] << col
                  acc
              }
              .findAll { it }
              .collect { cols -> rows.indices.collect { cols*.getAt(it).join('') } }
      }
  }

    /**
     * Extract a problem from a column.
     * @param column List of strings representing a vertical column
     * @return Map with 'numbers' (List<Integer>) and 'operator' (Character)
     */
    static Map extractProblem(List<String> column) {
      [
          numbers: column.findAll { !isOperatorRow(it) }*.trim().findAll { it }*.toLong(),
          operator: column.find { isOperatorRow(it) }.find(/[*+]/) as Character
      ]
  }

    /**
     * Solve a single problem.
     * @param problem Map with 'numbers' and 'operator'
     * @return The result of the calculation
     */
    static long solveProblem(Map problem) {
        problem.operator == '+' ? problem.numbers.sum() : problem.numbers.inject(1L, { a, b -> a * b })
    }

    /**
     * Calculate the grand total by solving all problems.
     * @param input The worksheet text
     * @return The sum of all problem results
     */
    static long calculateGrandTotal(String input) {
        parseColumns(input).collect { extractProblem(it) }.sum { solveProblem(it) }
    }

    // Part 2 methods - Cephalopod math (right-to-left reading)

    /**
     * Transpose problem columns to get digit rows (without operator row).
     * @param columns The problem columns
     * @return List of rows (without the operator row)
     */
    static List<String> transposeProblemColumns(List<String> columns) {
        columns.findAll { !isOperatorRow(it) }
    }

    /**
     * Extract a cephalopod problem by reading right-to-left.
     * @param columns List of column strings (as parsed left-to-right)
     * @return Map with 'numbers' (List<Long>) and 'operator' (Character)
     */
    static Map extractCephalopodProblem(List<String> columns) {
      def digitRows = columns.findAll { !isOperatorRow(it) }
      def operator = columns.find { isOperatorRow(it) }.find(/[*+]/) as Character

      def maxLen = digitRows*.length().max()
      def paddedRows = digitRows*.padRight(maxLen)

      def numbers = (maxLen-1..0).collect { colIdx ->
          paddedRows*.getAt(colIdx).join('').trim()
      }.findAll { it }*.toLong()

      [numbers: numbers, operator: operator]
    }

    /**
     * Calculate grand total using cephalopod math (Part 2).
     * @param input The worksheet text
     * @return The sum of all problem results
     */
    static long calculateCephalopodGrandTotal(String input) {
        parseColumns(input).collect { extractCephalopodProblem(it) }.sum { solveProblem(it) }
    }
}

static void main(String[] args) {
    def input = new File('../../../input/day6.txt').text

    // Part 1: Calculate grand total
    def grandTotal = MathWorksheet.calculateGrandTotal(input)
    println("\nPart 1 - Grand total: ${grandTotal}")

    // Part 2: Calculate cephalopod grand total
    def cephalopodTotal = MathWorksheet.calculateCephalopodGrandTotal(input)
    println("Part 2 - Cephalopod grand total: ${cephalopodTotal}")
}
