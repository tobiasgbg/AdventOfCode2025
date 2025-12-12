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

    def "should count fittable regions in example"() {
        when: "we count fittable regions"
        def farm = new ChristmasTreeFarm(exampleInput)
        def result = farm.countFittableRegions()

        then: "should be 2 (first two regions fit, third doesn't)"
        result == 2
    }

    def "debug third region"() {
        when: "we try to fit the third region"
        def farm = new ChristmasTreeFarm(exampleInput)
        println "Third region: ${farm.regions[2].width}x${farm.regions[2].height}"
        println "Present counts: ${farm.regions[2].presentCounts}"
        def fits = farm.canFitPresentsFastDebug(farm.regions[2])

        then: "debug output"
        !fits  // third region should NOT fit
    }

    def "debug shape orientations"() {
        when: "we check shape 4 orientations"
        def farm = new ChristmasTreeFarm(exampleInput)
        def shape4 = farm.shapes[4]
        println "Shape 4 original cells: ${shape4.cells}"
        println "Shape 4 cell count: ${shape4.cells.size()}"

        def orientations = shape4.getAllOrientations()
        println "Number of orientations: ${orientations.size()}"
        orientations.eachWithIndex { orientation, idx ->
            println "Orientation $idx: ${orientation.length} cells"
            orientation.each { cell ->
                println "  [${cell[0]}, ${cell[1]}]"
            }
        }

        then: "each orientation should have 7 cells"
        orientations.every { it.length == 7 }
    }
}
