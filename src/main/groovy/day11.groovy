import groovy.transform.Memoized

/*
--- Day 11: Reactor ---
You hear some loud beeping coming from a hatch in the floor of the factory, so you decide to check it out. Inside, you find several large electrical conduits and a ladder.

Climbing down the ladder, you discover the source of the beeping: a large, toroidal reactor which powers the factory above. Some Elves here are hurriedly running between the reactor and a nearby server rack, apparently trying to fix something.

One of the Elves notices you and rushes over. "It's a good thing you're here! We just installed a new server rack, but we aren't having any luck getting the reactor to communicate with it!" You glance around the room and see a tangle of cables and devices running from the server rack to the reactor. She rushes off, returning a moment later with a list of the devices and their outputs (your puzzle input).

For example:

aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out

Each line gives the name of a device followed by a list of the devices to which its outputs are attached. So, bbb: ddd eee means that device bbb has two outputs, one leading to device ddd and the other leading to device eee.

The Elves are pretty sure that the issue isn't due to any specific device, but rather that the issue is triggered by data following some specific path through the devices. Data only ever flows from a device through its outputs; it can't flow backwards.

After dividing up the work, the Elves would like you to focus on the devices starting with the one next to you (an Elf hastily attaches a label which just says you) and ending with the main output to the reactor (which is the device with the label out).

To help the Elves figure out which path is causing the issue, they need you to find every path from you to out.

In this example, these are all of the paths from you to out:

Data could take the connection from you to bbb, then from bbb to ddd, then from ddd to ggg, then from ggg to out.
Data could take the connection to bbb, then to eee, then to out.
Data could go to ccc, then ddd, then ggg, then out.
Data could go to ccc, then eee, then out.
Data could go to ccc, then fff, then out.

In total, there are 5 different paths leading from you to out.

How many different paths lead from you to out?

--- Part Two ---
Thanks in part to your analysis, the Elves have figured out a little bit about the issue. They now know that the problematic data path passes through both dac (a digital-to-analog converter) and fft (a device which performs a fast Fourier transform).

They're still not sure which specific path is the problem, and so they now need you to find every path from svr (the server rack) to out. However, the paths you find must all also visit both dac and fft (in any order).

For example:

svr: aaa bbb
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
hhh: out

This new list of devices contains many paths from svr to out (8 total).
However, only 2 paths from svr to out visit both dac and fft.

Find all of the paths that lead from svr to out. How many of those paths visit both dac and fft?
*/

class Reactor {
    Map<String, List<String>> graph = [:]

    Reactor(String input) {
        input.split('\n').each { line ->
            if (line.trim()) {
                def parts = line.split(':')
                def device = parts[0].trim()
                def outputs = parts[1].trim().split(/\s+/).collect { it.trim() }
                graph[device] = outputs
            }
        }
    }

    @Memoized
    long countPathsFrom(String node) {
        node == 'out' ? 1L : graph[node]?.sum { countPathsFrom(it) } ?: 0L
    }

    /**
     * Part 2: Count paths from start to 'out' that visit ALL required nodes.
     * Uses bitmask to track which required nodes have been visited.
     */
    private Map<String, Integer> nodeToBit = [:]
    private int allVisited

    long countPathsThrough(String start, List<String> requiredNodes) {
        // Map required nodes to bit positions
        requiredNodes.eachWithIndex { node, i -> nodeToBit[node] = i }
        allVisited = (1 << requiredNodes.size()) - 1  // e.g., 0b11 for 2 nodes

        countWithState(start, 0)
    }

    @Memoized
    long countWithState(String node, int visited) {
        if (nodeToBit.containsKey(node)) {
            visited |= (1 << nodeToBit[node])
        }
        if (node == 'out') return visited == allVisited ? 1L : 0L
        graph[node].sum { countWithState(it, visited) } ?: 0L
    }
}

static void main(String[] args) {
    try {
        String filePath = "../../../input/day11.txt"
        File file = new File(filePath)
        String input = file.text

        Reactor reactor = new Reactor(input)
        long result = reactor.countPathsFrom('you')
        println("Part 1: ${result}")

        long result2 = reactor.countPathsThrough('svr', ['dac', 'fft'])
        println("Part 2: ${result2}")
    } catch (FileNotFoundException e) {
        println("File not found: " + e.message)
    } catch (IOException e) {
        println("Error reading file: " + e.message)
    }
}
