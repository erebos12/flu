# FLU - use*F*ul he*L*per f*U*nctions

## Configurable Rules in JaCoCo

- **Coverage Elements (Targets of the rule):**
    - `BUNDLE`: The entire project or a module.
    - `PACKAGE`: A specific package.
    - `CLASS`: A single class.
    - `METHOD`: A method within a class.

- **Counter Types (Metrics that can be measured):**
    - `INSTRUCTION`: Number of executed bytecode instructions.
    - `BRANCH`: Number of conditional branches (e.g., `if`, `switch`) and their coverage.
    - `LINE`: Number of covered code lines.
    - `COMPLEXITY`: Cyclomatic complexity (number of independent paths through the code).
    - `METHOD`: Coverage of individual methods.
    - `CLASS`: Coverage of individual classes.

- **Values (Measurements to check):**
    - `COVEREDRATIO`: Percentage of covered elements.
    - `MISSEDRATIO`: Percentage of uncovered elements.
    - `COVEREDCOUNT`: Absolute number of covered elements.
    - `MISSEDCOUNT`: Absolute number of uncovered elements.
    - `TOTALCOUNT`: Total number of elements.

- **Limits (Thresholds for rules):**
    - `minimum`: Specifies the minimum required value (e.g., at least 80% line coverage).
    - `maximum`: Specifies the maximum allowed value (e.g., a maximum complexity of 10).
