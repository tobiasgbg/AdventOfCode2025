import spock.lang.Specification

class Day8Specification extends Specification {

    def "should parse junction box coordinates"() {
        given: "input with coordinates"
        def input = """162,817,812
57,618,57
906,360,560"""

        when: "we parse the coordinates"
        def boxes = JunctionBoxCircuits.parseJunctionBoxes(input)

        then: "should have correct 3D coordinates"
        boxes.size() == 3
        boxes[0] == [162, 817, 812]
        boxes[1] == [57, 618, 57]
        boxes[2] == [906, 360, 560]
    }

    def "should calculate distance between junction boxes"() {
        given: "two junction boxes"
        def box1 = [162, 817, 812]
        def box2 = [425, 690, 689]

        when: "we calculate the distance"
        def distance = JunctionBoxCircuits.distance(box1, box2)

        then: "should calculate Euclidean distance"
        distance > 0
        // sqrt((425-162)^2 + (690-817)^2 + (689-812)^2)
        // sqrt(69169 + 16129 + 15129) = sqrt(100427) ≈ 316.9
        Math.abs(distance - 316.9) < 1
    }

    def "should find closest pairs"() {
        given: "junction boxes"
        def boxes = [
            [162, 817, 812],
            [425, 690, 689],
            [431, 825, 988]
        ]

        when: "we find all pairs sorted by distance"
        def pairs = JunctionBoxCircuits.findClosestPairs(boxes)

        then: "should be sorted by distance"
        pairs.size() == 3  // n*(n-1)/2 pairs
        pairs[0].distance <= pairs[1].distance
        pairs[1].distance <= pairs[2].distance
    }

    def "should connect junction boxes and track circuits"() {
        given: "the example circuit"
        def input = """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"""

        when: "we connect the 10 shortest pairs"
        def result = JunctionBoxCircuits.connectShortestPairs(input, 10)

        then: "should have correct circuit sizes"
        def sizes = result.circuitSizes.sort().reverse()
        sizes[0] == 5  // largest circuit
        sizes[1] == 4  // second largest
        sizes[2] == 2  // third largest (one of two size-2 circuits)
        result.circuitCount == 11
    }

    def "should calculate product of three largest circuits"() {
        given: "the example circuit"
        def input = """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"""

        when: "we connect the 10 shortest pairs"
        def product = JunctionBoxCircuits.calculateCircuitProduct(input, 10)

        then: "should be 5 * 4 * 2 = 40"
        product == 40
    }

    def "should find last connection for single circuit"() {
        given: "the example circuit"
        def input = """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"""

        when: "we find the last connection that creates single circuit"
        def lastConnection = JunctionBoxCircuits.findLastConnectionForSingleCircuit(input)

        then: "should connect boxes at 216,146,977 and 117,168,530"
        def boxes = JunctionBoxCircuits.parseJunctionBoxes(input)
        boxes[lastConnection.box1Index] == [216, 146, 977] || boxes[lastConnection.box1Index] == [117, 168, 530]
        boxes[lastConnection.box2Index] == [216, 146, 977] || boxes[lastConnection.box2Index] == [117, 168, 530]
    }

    def "should calculate X coordinate product for last connection"() {
        given: "the example circuit"
        def input = """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"""

        when: "we calculate X product for last connection"
        def product = JunctionBoxCircuits.calculateLastConnectionXProduct(input)

        then: "should be 216 * 117 = 25272"
        product == 25272
    }
}
