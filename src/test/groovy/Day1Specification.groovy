import spock.lang.Specification

class Day1Specification extends Specification {

    def "should calculate correct password for example input"() {
        given: "the example rotations"
        def rotations = [
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"
        ]

        when: "we count how many times the dial points at 0"
        def password = SafeDial.calculatePassword(rotations)

        then: "the password should be 3"
        password == 3
    }

    def "should rotate left correctly from starting position"() {
        given: "dial starts at 50"
        def dial = new SafeDial()

        when: "we rotate left by 68"
        def newPosition = dial.rotate("L68")

        then: "the dial should point at 82"
        newPosition == 82
    }

    def "should rotate right correctly"() {
        given: "dial is at 52"
        def dial = new SafeDial(52)

        when: "we rotate right by 48"
        def newPosition = dial.rotate("R48")

        then: "the dial should point at 0"
        newPosition == 0
    }

    def "should wrap around when rotating left from 0"() {
        given: "dial is at 5"
        def dial = new SafeDial(5)

        when: "we rotate left by 10"
        def newPosition = dial.rotate("L10")

        then: "the dial should point at 95"
        newPosition == 95
    }

    def "should wrap around when rotating right from 99"() {
        given: "dial is at 95"
        def dial = new SafeDial(95)

        when: "we rotate right by 5"
        def newPosition = dial.rotate("R5")

        then: "the dial should point at 0"
        newPosition == 0
    }

    def "should track zero crossings during rotation sequence"() {
        given: "a sequence of rotations"
        def rotations = ["R48", "L55", "L99"]
        def dial = new SafeDial(52) // Starting at 52 to match the example flow

        when: "we apply the rotations and track zeros"
        def zeroCrossings = 0
        rotations.each { rotation ->
            if (dial.rotate(rotation) == 0) {
                zeroCrossings++
            }
        }

        then: "we should land on zero 1 time"
        zeroCrossings == 1
    }

    // Part 2 Tests
    def "should calculate correct password for example input part 2"() {
        given: "the example rotations"
        def rotations = [
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"
        ]

        when: "we count zero crossings including during rotations"
        def password = SafeDial.calculatePasswordPart2(rotations)

        then: "the password should be 6 (3 landings + 3 during rotations)"
        password == 6
    }

    def "should count zero crossings during large rotation"() {
        given: "dial starts at 50"
        def dial = new SafeDial(50)

        when: "we rotate right by 1000"
        def zeroCrossings = dial.rotateAndCountZeroCrossings("R1000")

        then: "the dial should cross zero 10 times"
        zeroCrossings == 10
    }
}
