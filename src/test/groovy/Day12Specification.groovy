import spock.lang.Specification

class Day12Specification extends Specification {

    def exampleInput = """0:
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
12x5: 1 0 1 0 3 2"""

    def "should parse shapes"() {
        when: "we parse the example input"
        def farm = new ChristmasTreeFarm(exampleInput)

        then: "shapes are parsed correctly"
        farm.shapes.size() == 6
        farm.shapes[0].index == 0
        farm.shapes[4].index == 4
    }

    def "should parse shape cells"() {
        when: "we parse the example input"
        def farm = new ChristmasTreeFarm(exampleInput)

        then: "shape 4 has correct cells"
        // Shape 4:
        // ###
        // #..
        // ###
        def shape4 = farm.shapes[4]
        shape4.cells.size() == 7
        shape4.cells.contains([0, 0])
        shape4.cells.contains([1, 0])
        shape4.cells.contains([2, 0])
        shape4.cells.contains([0, 1])
        shape4.cells.contains([0, 2])
        shape4.cells.contains([1, 2])
        shape4.cells.contains([2, 2])
    }

    def "should parse regions"() {
        when: "we parse the example input"
        def farm = new ChristmasTreeFarm(exampleInput)

        then: "regions are parsed correctly"
        farm.regions.size() == 3
        farm.regions[0].width == 4
        farm.regions[0].height == 4
        farm.regions[1].width == 12
        farm.regions[1].height == 5
    }

    def "should parse region present counts"() {
        when: "we parse the example input"
        def farm = new ChristmasTreeFarm(exampleInput)

        then: "present counts are correct"
        // 4x4: 0 0 0 0 2 0 means 2 presents of shape 4
        farm.regions[0].presentCounts == [0, 0, 0, 0, 2, 0]
        // 12x5: 1 0 1 0 2 2
        farm.regions[1].presentCounts == [1, 0, 1, 0, 2, 2]
        // 12x5: 1 0 1 0 3 2
        farm.regions[2].presentCounts == [1, 0, 1, 0, 3, 2]
    }

    def "should generate shape orientations"() {
        when: "we get orientations for shape 4"
        def farm = new ChristmasTreeFarm(exampleInput)
        def shape4 = farm.shapes[4]
        def orientations = shape4.orientations

        then: "each orientation has 7 cells"
        orientations.every { it.length == 7 }
    }

    def "should count fittable regions in example"() {
        when: "we count fittable regions"
        def farm = new ChristmasTreeFarm(exampleInput)
        def result = farm.countFittableRegions()

        then: "should be 2 (first two regions fit, third doesn't)"
        result == 2
    }
}
