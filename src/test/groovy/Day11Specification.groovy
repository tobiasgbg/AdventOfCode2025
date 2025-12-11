import spock.lang.Specification

class Day11Specification extends Specification {

    def "should parse device connections"() {
        given: "a simple input"
        def input = """you: bbb ccc
bbb: out
ccc: out"""

        when: "we parse it"
        def reactor = new Reactor(input)

        then: "graph is built correctly"
        reactor.graph['you'] == ['bbb', 'ccc']
        reactor.graph['bbb'] == ['out']
        reactor.graph['ccc'] == ['out']
    }

    def "should count paths in simple case"() {
        given: "a simple graph with 2 paths"
        def input = """you: bbb ccc
bbb: out
ccc: out"""

        when: "we count paths"
        def reactor = new Reactor(input)
        def result = reactor.countPathsFrom('you')

        then: "should be 2"
        result == 2
    }

    def "should count paths in example"() {
        given: "the example input"
        def input = """aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out"""

        when: "we count paths from you to out"
        def reactor = new Reactor(input)
        def result = reactor.countPathsFrom('you')

        then: "should be 5"
        result == 5
    }

    def "should handle single path"() {
        given: "a linear path"
        def input = """you: aaa
aaa: bbb
bbb: out"""

        when: "we count paths"
        def reactor = new Reactor(input)
        def result = reactor.countPathsFrom('you')

        then: "should be 1"
        result == 1
    }

    def "should handle diamond pattern"() {
        given: "a diamond graph (converging paths)"
        def input = """you: aaa bbb
aaa: ccc
bbb: ccc
ccc: out"""

        when: "we count paths"
        def reactor = new Reactor(input)
        def result = reactor.countPathsFrom('you')

        then: "should be 2"
        result == 2
    }

    // Part 2 tests

    def "should count paths through required nodes in example"() {
        given: "the Part 2 example input"
        def input = """svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out"""

        when: "we count paths from svr to out through both dac and fft"
        def reactor = new Reactor(input)
        def result = reactor.countPathsThrough('svr', ['dac', 'fft'])

        then: "should be 2"
        result == 2
    }

    def "should count all paths without required nodes"() {
        given: "the Part 2 example input"
        def input = """svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out"""

        when: "we count all paths from svr to out (no required nodes)"
        def reactor = new Reactor(input)
        def result = reactor.countPathsThrough('svr', [])

        then: "should be 8 (all paths)"
        result == 8
    }

    def "should count paths through single required node"() {
        given: "a simple graph"
        def input = """start: aaa bbb
aaa: mid
bbb: out
mid: out"""

        when: "we count paths through 'mid'"
        def reactor = new Reactor(input)
        def result = reactor.countPathsThrough('start', ['mid'])

        then: "should be 1 (only start->aaa->mid->out)"
        result == 1
    }
}
