import spock.lang.Specification

class Day9Specification extends Specification {

    def "should parse red tile coordinates"() {
        given: "input with coordinates"
        def input = """7,1
11,1
11,7"""

        when: "we parse the coordinates"
        def tiles = MovieTheater.parseRedTiles(input)

        then: "should have correct coordinates"
        tiles.size() == 3
        tiles[0] == [7, 1]
        tiles[1] == [11, 1]
        tiles[2] == [11, 7]
    }

    def "should calculate rectangle area"() {
        expect: "correct area calculations"
        MovieTheater.calculateRectangleArea([2, 5], [9, 7]) == 24
        MovieTheater.calculateRectangleArea([7, 1], [11, 7]) == 35
        MovieTheater.calculateRectangleArea([7, 3], [2, 3]) == 6
        MovieTheater.calculateRectangleArea([2, 5], [11, 1]) == 50
    }

    def "should find largest rectangle in example"() {
        given: "the example red tiles"
        def input = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"""

        when: "we find the largest rectangle"
        def area = MovieTheater.findLargestRectangle(input)

        then: "should be 50"
        area == 50
    }

    def "should handle simple case"() {
        given: "just two red tiles"
        def input = """0,0
5,5"""

        when: "we find the largest rectangle"
        def area = MovieTheater.findLargestRectangle(input)

        then: "should be 36 (6x6 inclusive)"
        area == 36
    }

    def "should handle collinear tiles"() {
        given: "tiles on same row"
        def input = """0,0
5,0
10,0"""

        when: "we find the largest rectangle"
        def area = MovieTheater.findLargestRectangle(input)

        then: "should be 11 (11x1 between 0,0 and 10,0)"
        area == 11
    }

    def "should identify green tiles on perimeter"() {
        given: "the example red tiles"
        def input = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"""

        when: "we identify green tiles"
        def greenTiles = MovieTheater.findGreenTiles(input)

        then: "should include tiles on the perimeter path"
        greenTiles.contains([8, 1])  // Between 7,1 and 11,1
        greenTiles.contains([11, 2]) // Between 11,1 and 11,7
        greenTiles.contains([10, 7]) // Between 11,7 and 9,7
    }

    def "should find largest valid rectangle in example"() {
        given: "the example red tiles"
        def input = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"""

        when: "we find the largest valid rectangle"
        def area = MovieTheater.findLargestValidRectangle(input)

        then: "the area should be the largest rectangle composed of only red and green tiles"
        // The largest rectangle is from (2,3) to (9,5) with area 24
        area == 24
    }
}
