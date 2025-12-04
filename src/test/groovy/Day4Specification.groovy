import spock.lang.Specification

class Day4Specification extends Specification {

    def "should count adjacent paper rolls at position"() {
        given: "a grid with paper rolls"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        ]

        when: "we count adjacent rolls at a specific position"
        def count = PaperRollGrid.countAdjacentRolls(grid, row, col)

        then: "should return correct count"
        count == expected

        where:
        row | col | expected
        0   | 2   | 3        // Position (0,2) has 3 adjacent @ symbols
        0   | 3   | 3        // Position (0,3) has 3 adjacent @ symbols
        1   | 0   | 3        // Position (1,0) has 3 adjacent @ symbols at edge
    }

    def "should determine if a roll is accessible by forklift"() {
        given: "a grid with paper rolls"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        ]

        expect: "correct accessibility determination"
        PaperRollGrid.isAccessible(grid, row, col) == accessible

        where:
        row | col | accessible
        0   | 2   | true      // Has 3 adjacent (< 4)
        0   | 3   | true      // Has 2 adjacent (< 4)
        0   | 7   | false     // Has 4 or more adjacent
    }

    def "should count accessible paper rolls in example grid"() {
        given: "the example grid"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        ]

        when: "we count accessible rolls"
        def count = PaperRollGrid.countAccessibleRolls(grid)

        then: "should be 13"
        count == 13
    }

    def "should parse grid from input"() {
        given: "input text"
        def input = """..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@"""

        when: "we parse the grid"
        def grid = PaperRollGrid.parseGrid(input)

        then: "should have correct dimensions"
        grid.size() == 3
        grid[0] == "..@@.@@@@."
        grid[1] == "@@@.@.@.@@"
        grid[2] == "@@@@@.@.@@"
    }

    // Part 2 tests - iterative removal
    def "should remove accessible rolls from grid"() {
        given: "a grid with accessible rolls"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        ]

        when: "we remove accessible rolls once"
        def (newGrid, removedCount) = PaperRollGrid.removeAccessibleRolls(grid)

        then: "should remove 13 rolls"
        removedCount == 13
        // Check that some positions are now '.'
        newGrid[0][2] == '.'
        newGrid[0][3] == '.'
    }

    def "should repeatedly remove rolls until none are accessible"() {
        given: "the example grid"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@",
            "@.@@@@..@.",
            "@@.@@@@.@@",
            ".@@@@@@@.@",
            ".@.@.@.@@@",
            "@.@@@.@@@@",
            ".@@@@@@@@.",
            "@.@.@@@.@."
        ]

        when: "we remove all accessible rolls iteratively"
        def totalRemoved = PaperRollGrid.removeAllAccessibleRolls(grid)

        then: "should remove 43 rolls in total"
        totalRemoved == 43
    }

    def "should find all accessible positions in grid"() {
        given: "a grid with paper rolls"
        def grid = [
            "..@@.@@@@.",
            "@@@.@.@.@@",
            "@@@@@.@.@@"
        ]

        when: "we find all accessible positions"
        def positions = PaperRollGrid.findAccessiblePositions(grid)

        then: "should return list of [row, col] positions"
        positions.size() > 0
        positions.every { it instanceof List && it.size() == 2 }
    }
}
