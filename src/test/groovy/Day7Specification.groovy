import spock.lang.Specification

class Day7Specification extends Specification {

    def "should find starting position"() {
        given: "a manifold diagram"
        def diagram = [
            ".......S.......",
            "...............",
            ".......^......."
        ]

        when: "we find the starting position"
        def start = TachyonManifold.findStart(diagram)

        then: "should be at column 7"
        start == [0, 7]
    }

    def "should detect splitter"() {
        expect: "correct splitter detection"
        TachyonManifold.isSplitter('^') == true
        TachyonManifold.isSplitter('.') == false
        TachyonManifold.isSplitter('S') == false
    }

    def "should count beam splits in example"() {
        given: "the example manifold"
        def diagram = [
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        ]

        when: "we simulate the tachyon beams"
        def splits = TachyonManifold.countBeamSplits(diagram)

        then: "should split 21 times"
        splits == 21
    }

    def "should parse manifold from input"() {
        given: "input text"
        def input = """.......S.......
...............
.......^......."""

        when: "we parse the manifold"
        def diagram = TachyonManifold.parseDiagram(input)

        then: "should have correct structure"
        diagram.size() == 3
        diagram[0] == ".......S......."
        diagram[2] == ".......^......."
    }

    def "should track beam positions"() {
        given: "a simple manifold"
        def diagram = [
            "...S...",
            ".......",
            "...^..."
        ]

        when: "we simulate beams"
        def splits = TachyonManifold.countBeamSplits(diagram)

        then: "should split once"
        splits == 1
    }

    def "should count quantum timelines in example"() {
        given: "the example manifold"
        def diagram = [
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        ]

        when: "we simulate quantum tachyon particle"
        def timelines = TachyonManifold.countQuantumTimelines(diagram)

        then: "should create multiple timelines (expected: 40, currently: verify algorithm)"
        // TODO: Algorithm currently returns 74 - may need to count differently
        // Problem says 40 timelines, need to determine what counts as a "timeline"
        timelines == 40
    }

    def "should count quantum timelines in simple case"() {
        given: "a simple manifold with one splitter"
        def diagram = [
            "...S...",
            ".......",
            "...^...",
            "......."
        ]

        when: "we simulate quantum particle"
        def timelines = TachyonManifold.countQuantumTimelines(diagram)

        then: "should have multiple timeline states"
        timelines > 1
    }
}
