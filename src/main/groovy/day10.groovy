/*
--- Day 10: Factory ---
Just across the hall, you find a large factory. Fortunately, the Elves here have plenty of time to decorate. Unfortunately, it's because the factory machines are all offline, and none of the Elves can figure out the initialization procedure.

The Elves do have the manual for the machines, but the section detailing the initialization procedure was eaten by a Shiba Inu. All that remains of the manual are some indicator light diagrams, button wiring schematics, and joltage requirements for each machine.

For example:

[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
The manual describes one machine per line. Each line contains a single indicator light diagram in [square brackets], one or more button wiring schematics in (parentheses), and joltage requirements in {curly braces}.

To start a machine, its indicator lights must match those shown in the diagram, where . means off and # means on. The machine has the number of indicator lights shown, but its indicator lights are all initially off.

So, an indicator light diagram like [.##.] means that the machine has four indicator lights which are initially off and that the goal is to simultaneously configure the first light to be off, the second light to be on, the third to be on, and the fourth to be off.

You can toggle the state of indicator lights by pushing any of the listed buttons. Each button lists which indicator lights it toggles, where 0 means the first light, 1 means the second light, and so on. When you push a button, each listed indicator light either turns on (if it was off) or turns off (if it was on). You have to push each button an integer number of times; there's no such thing as "0.5 presses" (nor can you push a button a negative number of times).

So, a button wiring schematic like (0,3,4) means that each time you push that button, the first, fourth, and fifth indicator lights would all toggle between on and off. If the indicator lights were [#.....], pushing the button would change them to be [...##.] instead.

Because none of the machines are running, the joltage requirements are irrelevant and can be safely ignored.

You can push each button as many times as you like. However, to save on time, you will need to determine the fewest total presses required to correctly configure all indicator lights for all machines in your list.

There are a few ways to correctly configure the first machine:

[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
You could press the first three buttons once each, a total of 3 button presses.
You could press (1,3) once, (2,3) once, and (0,1) twice, a total of 4 button presses.
You could press all of the buttons except (1,3) once each, a total of 5 button presses.
However, the fewest button presses required is 2. One way to do this is by pressing the last two buttons ((0,2) and (0,1)) once each.

The second machine can be configured with as few as 3 button presses:

[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
One way to achieve this is by pressing the last three buttons ((0,4), (0,1,2), and (1,2,3,4)) once each.

The third machine has a total of six indicator lights that need to be configured correctly:

[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
The fewest presses required to correctly configure it is 2; one way to do this is by pressing buttons (0,3,4) and (0,1,2,4,5) once each.

So, the fewest button presses required to correctly configure the indicator lights on all of the machines is 2 + 3 + 2 = 7.

Analyze each machine's indicator light diagram and button wiring schematics. What is the fewest button presses required to correctly configure the indicator lights on all of the machines?

--- Part Two ---
All of the machines are starting to come online! Now, it's time to worry about the joltage requirements.

Each machine needs to be configured to exactly the specified joltage levels to function properly. Below the buttons on each machine is a big lever that you can use to switch the buttons from configuring the indicator lights to increasing the joltage levels. (Ignore the indicator light diagrams.)

The machines each have a set of numeric counters tracking its joltage levels, one counter per joltage requirement. The counters are all initially set to zero.

So, joltage requirements like {3,5,4,7} mean that the machine has four counters which are initially 0 and that the goal is to simultaneously configure the first counter to be 3, the second counter to be 5, the third to be 4, and the fourth to be 7.

The button wiring schematics are still relevant: in this new joltage configuration mode, each button now indicates which counters it affects, where 0 means the first counter, 1 means the second counter, and so on. When you push a button, each listed counter is increased by 1.

So, a button wiring schematic like (1,3) means that each time you push that button, the second and fourth counters would each increase by 1. If the current joltage levels were {0,1,2,3}, pushing the button would change them to be {0,2,2,4}.

You can push each button as many times as you like. However, your finger is getting sore from all the button pushing, and so you will need to determine the fewest total presses required to correctly configure each machine's joltage level counters to match the specified joltage requirements.

Consider again the example from before:

[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}

Configuring the first machine's counters requires a minimum of 10 button presses. One way to do this is by pressing (3) once, (1,3) three times, (2,3) three times, (0,2) once, and (0,1) twice.

Configuring the second machine's counters requires a minimum of 12 button presses. One way to do this is by pressing (0,2,3,4) twice, (2,3) five times, and (0,1,2) five times.

Configuring the third machine's counters requires a minimum of 11 button presses. One way to do this is by pressing (0,1,2,3,4) five times, (0,1,2,4,5) five times, and (1,2) once.

So, the fewest button presses required to correctly configure the joltage level counters on all of the machines is 10 + 12 + 11 = 33.

Analyze each machine's joltage requirements and button wiring schematics. What is the fewest button presses required to correctly configure the joltage level counters on all of the machines?
*/

class MachineFactory {
    List<Machine> machines = []

    MachineFactory(String input) {
        input.split('\n').each { line ->
            if (line.trim()) {
                machines.add(new Machine(line))
            }
        }
    }

    int solve() {
        machines.sum { it.findMinPresses() }
    }

    int solvePart2() {
        machines.sum { it.findMinJoltagePresses() }
    }
}

class Machine {
    List<Integer> targetState
    List<List<Integer>> buttons
    List<Integer> joltageRequirements

    Machine(String line) {
        def lightMatcher = line =~ /\[(.*?)\]/
        def buttonMatcher = line =~ /\((.*?)\)/
        def joltageMatcher = line =~ /\{(.*?)\}/

        this.targetState = lightMatcher[0][1].chars.collect { it == '#' ? 1 : 0 }

        this.buttons = []
        while (buttonMatcher.find()) {
            this.buttons.add(buttonMatcher.group(1).split(',')*.trim()*.toInteger())
        }

        // Parse joltage requirements
        if (joltageMatcher.find()) {
            this.joltageRequirements = joltageMatcher.group(1).split(',')*.trim()*.toInteger()
        } else {
            this.joltageRequirements = []
        }
    }

    int findMinPresses() {
        int numLights = targetState.size()
        int numButtons = buttons.size()

        if (numLights == 0) return 0

        def matrix = (0..<numLights).collect { i ->
            def row = (0..<numButtons).collect { j -> buttons[j].contains(i) ? 1 : 0 }
            row.add(targetState[i])
            row
        }

        int lead = 0, rowCount = numLights, colCount = numButtons + 1
        for (int r = 0; r < rowCount && lead < numButtons; r++) {
            int i = r
            while (matrix[i][lead] == 0) {
                if (++i == rowCount) {
                    i = r
                    if (++lead == numButtons) break
                }
            }
            if (lead == numButtons) break
            Collections.swap(matrix, i, r)
            for (int k = 0; k < rowCount; k++) {
                if (k != r && matrix[k][lead] == 1) {
                    (lead..<colCount).each { col -> matrix[k][col] ^= matrix[r][col] }
                }
            }
            lead++
        }

        for (int i = 0; i < rowCount; i++) {
            if (matrix[i].subList(0, numButtons).every { it == 0 } && matrix[i][numButtons] == 1) {
                return Integer.MAX_VALUE
            }
        }

        def isPivotCol = new boolean[numButtons]
        int rank = 0
        matrix.each { row ->
            int firstOne = row.indexOf(1)
            if (firstOne != -1 && firstOne < numButtons) {
                isPivotCol[firstOne] = true
                rank++
            }
        }

        def freeVarIndices = (0..<numButtons).findAll { !isPivotCol[it] }
        int minPresses = Integer.MAX_VALUE

        for (long i = 0; i < (1L << freeVarIndices.size()); i++) {
            def solution = new int[numButtons]
            int currentPresses = 0

            freeVarIndices.eachWithIndex { idx, j ->
                if (((i >> j) & 1) == 1) {
                    solution[idx] = 1
                    currentPresses++
                }
            }

            for (int r = rank - 1; r >= 0; r--) {
                int pivotCol = matrix[r].indexOf(1)
                if (pivotCol == -1) continue
                int val = matrix[r][numButtons]
                for (int c = pivotCol + 1; c < numButtons; c++) {
                    if (matrix[r][c] == 1) val ^= solution[c]
                }
                solution[pivotCol] = val
                if (val == 1) currentPresses++
            }
            minPresses = Math.min(minPresses, currentPresses)
        }
        return minPresses
    }

    /**
     * Part 2: Find minimum button presses to configure joltage counters.
     * Buttons now increment counters (add 1) instead of toggling.
     *
     * Optimized approach: Solve as system of linear equations Ax = b over non-negative integers.
     * 1. Use Gaussian elimination to find solution space
     * 2. Express solution as particular + null space combination
     * 3. Search over free variables to find minimum sum solution
     */
    int findMinJoltagePresses() {
        int numCounters = joltageRequirements.size()
        int numButtons = buttons.size()

        if (numCounters == 0) return 0
        if (joltageRequirements.every { it == 0 }) return 0

        // Build coefficient matrix A where A[i][j] = 1 if button j affects counter i
        // We want to solve Ax = b where b = joltageRequirements, minimize sum(x)
        double[][] matrix = new double[numCounters][numButtons + 1]
        for (int i = 0; i < numCounters; i++) {
            for (int j = 0; j < numButtons; j++) {
                matrix[i][j] = buttons[j].contains(i) ? 1.0 : 0.0
            }
            matrix[i][numButtons] = joltageRequirements[i]
        }

        // Gaussian elimination with partial pivoting (over rationals)
        int[] pivotCol = new int[numCounters]
        Arrays.fill(pivotCol, -1)
        int rank = 0

        for (int col = 0; col < numButtons && rank < numCounters; col++) {
            // Find pivot
            int maxRow = rank
            for (int row = rank + 1; row < numCounters; row++) {
                if (Math.abs(matrix[row][col]) > Math.abs(matrix[maxRow][col])) {
                    maxRow = row
                }
            }

            if (Math.abs(matrix[maxRow][col]) < 1e-10) continue

            // Swap rows
            double[] temp = matrix[rank]
            matrix[rank] = matrix[maxRow]
            matrix[maxRow] = temp

            // Scale pivot row
            double pivot = matrix[rank][col]
            for (int j = col; j <= numButtons; j++) {
                matrix[rank][j] /= pivot
            }

            // Eliminate column
            for (int row = 0; row < numCounters; row++) {
                if (row != rank && Math.abs(matrix[row][col]) > 1e-10) {
                    double factor = matrix[row][col]
                    for (int j = col; j <= numButtons; j++) {
                        matrix[row][j] -= factor * matrix[rank][j]
                    }
                }
            }

            pivotCol[rank] = col
            rank++
        }

        // Check for inconsistency (0 = nonzero)
        for (int i = rank; i < numCounters; i++) {
            if (Math.abs(matrix[i][numButtons]) > 1e-10) {
                return Integer.MAX_VALUE  // No solution
            }
        }

        // Identify free variables (columns without pivots)
        boolean[] isPivot = new boolean[numButtons]
        for (int i = 0; i < rank; i++) {
            if (pivotCol[i] >= 0) isPivot[pivotCol[i]] = true
        }
        List<Integer> freeVars = (0..<numButtons).findAll { !isPivot[it] }

        // Search over free variable values to find minimum sum non-negative integer solution
        // Upper bound for each variable: max target value (since each press adds at least 1)
        int maxVal = (joltageRequirements.max() ?: 0) + 1
        int minPresses = Integer.MAX_VALUE

        // If too many free variables, fall back to smarter approach
        if (freeVars.size() > 10) {
            // Too many free vars - use iterative improvement starting from a greedy solution
            return findMinJoltagePressesGreedy()
        }

        // Recursive search over free variables with proper bounds
        int maxFreeVal = maxVal  // No artificial cap

        // Recursive search over free variables
        minPresses = searchMinSolution(matrix, pivotCol, rank, freeVars, 0,
                                        new int[numButtons], maxFreeVal, minPresses, numButtons)

        return minPresses
    }

    private int searchMinSolution(double[][] matrix, int[] pivotCol, int rank,
                                   List<Integer> freeVars, int freeIdx,
                                   int[] solution, int maxFreeVal, int currentMin, int numButtons) {
        if (freeIdx == freeVars.size()) {
            // All free vars assigned, compute pivot vars by back substitution
            int[] sol = solution.clone()

            for (int i = rank - 1; i >= 0; i--) {
                int pCol = pivotCol[i]
                double val = matrix[i][numButtons]
                for (int j = pCol + 1; j < numButtons; j++) {
                    val -= matrix[i][j] * sol[j]
                }
                // Check if integer and non-negative
                long rounded = Math.round(val)
                if (Math.abs(val - rounded) > 1e-9 || rounded < 0) {
                    return currentMin  // Invalid solution
                }
                sol[pCol] = (int) rounded
            }

            // Compute sum
            int sum = 0
            for (int v : sol) {
                if (v < 0) return currentMin  // Invalid
                sum += v
            }
            return Math.min(currentMin, sum)
        }

        int freeVar = freeVars[freeIdx]

        // Try values for this free variable
        for (int v = 0; v <= maxFreeVal; v++) {
            // Pruning: current partial sum already exceeds best
            int partialSum = 0
            for (int i = 0; i < freeIdx; i++) {
                partialSum += solution[freeVars[i]]
            }
            if (partialSum + v >= currentMin) break  // Pruning

            solution[freeVar] = v
            currentMin = searchMinSolution(matrix, pivotCol, rank, freeVars, freeIdx + 1,
                                           solution, maxFreeVal, currentMin, numButtons)
        }

        return currentMin
    }

    /**
     * Greedy fallback for cases with many free variables.
     * Iteratively selects the button that makes the most progress toward the goal.
     */
    private int findMinJoltagePressesGreedy() {
        int numCounters = joltageRequirements.size()
        int numButtons = buttons.size()

        int[] current = new int[numCounters]
        int[] target = joltageRequirements as int[]
        int totalPresses = 0

        // Greedy: repeatedly pick the button that reduces total remaining distance most efficiently
        while (!Arrays.equals(current, target)) {
            int bestButton = -1
            double bestEfficiency = -1

            for (int b = 0; b < numButtons; b++) {
                // Check if this button can make progress without overshooting
                boolean valid = true
                int progress = 0
                for (int idx : buttons[b]) {
                    if (idx < numCounters) {
                        if (current[idx] >= target[idx]) {
                            valid = false
                            break
                        }
                        progress++
                    }
                }

                if (valid && progress > 0) {
                    // Efficiency = progress made per press
                    double efficiency = progress
                    if (efficiency > bestEfficiency) {
                        bestEfficiency = efficiency
                        bestButton = b
                    }
                }
            }

            if (bestButton == -1) {
                // No button can make progress - find one that at least helps some counters
                for (int b = 0; b < numButtons; b++) {
                    for (int idx : buttons[b]) {
                        if (idx < numCounters && current[idx] < target[idx]) {
                            bestButton = b
                            break
                        }
                    }
                    if (bestButton != -1) break
                }
            }

            if (bestButton == -1) {
                return Integer.MAX_VALUE  // No solution
            }

            // Press the best button
            for (int idx : buttons[bestButton]) {
                if (idx < numCounters) {
                    current[idx]++
                }
            }
            totalPresses++

            // Safety check
            if (totalPresses > joltageRequirements.sum() * 2) {
                return Integer.MAX_VALUE
            }
        }

        return totalPresses
    }
}

static void main(String[] args) {
    try {
        String filePath = "../../../input/day10.txt"
        File file = new File(filePath)
        String input = file.text

        MachineFactory factory = new MachineFactory(input)
        int result = factory.solve()
        println("Part 1: ${result}")

        int result2 = factory.solvePart2()
        println("Part 2: ${result2}")
    } catch (FileNotFoundException e) {
        println("File not found: " + e.message)
    } catch (IOException e) {
        println("Error reading file: " + e.message)
    }
}