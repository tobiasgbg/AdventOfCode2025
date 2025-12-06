import spock.lang.Specification

class Day6Specification extends Specification {

    def "should parse worksheet into columns"() {
        given: "the example worksheet"
        def input = """123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +  """

        when: "we parse the worksheet"
        def columns = MathWorksheet.parseColumns(input)

        then: "should have 4 columns"
        columns.size() == 4
    }

    def "should extract problem from column"() {
        given: "a column of text"
        def column = ["123", " 45", "  6", "*"]

        when: "we extract the problem"
        def problem = MathWorksheet.extractProblem(column)

        then: "should have numbers and operator"
        problem.numbers == [123, 45, 6]
        problem.operator == '*'
    }

    def "should solve multiplication problem"() {
        given: "a multiplication problem"
        def problem = [numbers: [123, 45, 6], operator: '*']

        when: "we solve the problem"
        def result = MathWorksheet.solveProblem(problem)

        then: "should be 33210"
        result == 33210
    }

    def "should solve addition problem"() {
        given: "an addition problem"
        def problem = [numbers: [328, 64, 98], operator: '+']

        when: "we solve the problem"
        def result = MathWorksheet.solveProblem(problem)

        then: "should be 490"
        result == 490
    }

    def "should calculate grand total for example"() {
        given: "the example worksheet"
        def input = """123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +  """

        when: "we calculate the grand total"
        def grandTotal = MathWorksheet.calculateGrandTotal(input)

        then: "should be 4277556"
        grandTotal == 4277556L
    }

    def "should identify operator row"() {
        given: "a row with operators"
        def row = "*   +   *   +  "

        expect: "should contain operators"
        MathWorksheet.isOperatorRow(row) == true
    }

    def "should identify non-operator row"() {
        given: "a row with numbers"
        def row = "123 328  51 64 "

        expect: "should not contain operators"
        MathWorksheet.isOperatorRow(row) == false
    }

    // Part 2 tests - reading right-to-left
    def "should extract cephalopod problem from columns"() {
        given: "columns for a problem (reading left-to-right as parsed)"
        def columns = ["64 ",
                       "23 ",
                       "314",
                       "+  "]

        when: "we extract as cephalopod math (right-to-left)"
        def problem = MathWorksheet.extractCephalopodProblem(columns)

        then: "should read right-to-left"
        problem.numbers == [4, 431, 623]
        problem.operator == '+'
    }

    def "should calculate grand total for example Part 2"() {
        given: "the example worksheet"
        def input = """123 328  51 64
 45 64  387 23
  6 98  215 314
*   +   *   +  """

        when: "we calculate cephalopod grand total"
        def grandTotal = MathWorksheet.calculateCephalopodGrandTotal(input)

        then: "should be 3263827"
        grandTotal == 3263827L
    }

    def "should transpose problem columns to rows"() {
        given: "problem columns"
        def columns = ["123", " 45", "  6", "*  "]

        when: "we transpose to get digit rows"
        def rows = MathWorksheet.transposeProblemColumns(columns)

        then: "should have rows without operator"
        rows.size() == 3
        rows[0] == "123"
        rows[1] == " 45"
        rows[2] == "  6"
    }
}
