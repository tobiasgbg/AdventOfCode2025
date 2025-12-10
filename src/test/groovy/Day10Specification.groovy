import spock.lang.Specification

class Day10Specification extends Specification {

    def "should find minimum presses for first machine"() {
        given: "the first example machine"
        def machine = new Machine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")

        when: "we find minimum presses"
        def result = machine.findMinPresses()

        then: "should be 2"
        result == 2
    }

    def "should find minimum presses for second machine"() {
        given: "the second example machine"
        def machine = new Machine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")

        when: "we find minimum presses"
        def result = machine.findMinPresses()

        then: "should be 3"
        result == 3
    }

    def "should find minimum presses for third machine"() {
        given: "the third example machine"
        def machine = new Machine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")

        when: "we find minimum presses"
        def result = machine.findMinPresses()

        then: "should be 2"
        result == 2
    }

    def "should find total minimum presses for all example machines"() {
        given: "all three example machines"
        def input = """[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"""

        when: "we solve for all machines"
        def factory = new MachineFactory(input)
        def result = factory.solve()

        then: "total should be 7 (2 + 3 + 2)"
        result == 7
    }

    // Part 2 tests - Joltage configuration (increment counters)

    def "should find minimum joltage presses for first machine"() {
        given: "the first example machine"
        def machine = new Machine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")

        when: "we find minimum joltage presses"
        def result = machine.findMinJoltagePresses()

        then: "should be 10"
        result == 10
    }

    def "should find minimum joltage presses for second machine"() {
        given: "the second example machine"
        def machine = new Machine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")

        when: "we find minimum joltage presses"
        def result = machine.findMinJoltagePresses()

        then: "should be 12"
        result == 12
    }

    def "should find minimum joltage presses for third machine"() {
        given: "the third example machine"
        def machine = new Machine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")

        when: "we find minimum joltage presses"
        def result = machine.findMinJoltagePresses()

        then: "should be 11"
        result == 11
    }

    def "should find total minimum joltage presses for all example machines"() {
        given: "all three example machines"
        def input = """[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"""

        when: "we solve Part 2 for all machines"
        def factory = new MachineFactory(input)
        def result = factory.solvePart2()

        then: "total should be 33 (10 + 12 + 11)"
        result == 33
    }
}
