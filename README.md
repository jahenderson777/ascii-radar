# ASCII Radar Detection Project

This project provides functionality to scan an ASCII radar for invader patterns and return all detected invaders above a specified detection threshold. The main method runs the detection for all known invaders in the sample radar.

## Overview

The key functions in this project include:

- **`score`**: Calculates the similarity score between two sequences. It returns a floating-point score between 0.0 and 1.0 that represents how closely the sequences match.
- **`add-unknowns`**: Adds borders around the radar data to help with the invader detection.
- **`sub-sample`**: Extracts a sub-region from the radar.
- **`all-locations`**: Generates all possible [x, y] coordinates for a radar, including extra space to account for invaders extending off the edges.
- **`scan-for-invader`**: Scans the radar for occurrences of a given invader pattern, returning all detected invaders with details about their location and match score.
- **`print-results`**: Prints the results of the detection, including the invader's location, score, and the corresponding sub-region of the radar.

The primary goal is to locate invaders within an ASCII radar map, where the invaders are defined as patterns in the radar. The detection process uses a score threshold to determine whether a given match is significant enough to be considered a valid detection.

## Main method

The detection functionality is executed through the `-main` function, which:

1. Loads predefined invader patterns and a sample radar.
2. Runs the detection (`scan-for-invader`) for each invader.
3. Prints the results, including the location, detection score, and radar region where the invader was found.

## Testing

Unit tests are included in the project to validate core functionality. The tests are defined in the `core_test.clj` file, which contains a `(run-tests)` form at the bottom. This form triggers the execution of all defined tests to ensure the correctness of the project.

To run the tests, load the `core_test.clj` file.

## Usage

1. Define the invader patterns as sequences of strings, where each string represents a row of the invader.
2. Define a sample radar as a sequence of strings, where each string represents a row of the radar map.
3. Use the `scan-for-invader` function to search for the invader pattern(s) in the radar.
4. Print the results using the `print-results` function.

## Project Structure

```
src/
  ascii_radar/
    core.clj        # Core logic for scanning invaders
    sample_data.clj # Contains sample radar and invader data
    core_spec.clj   # Specs for important functions
test/
  ascii_radar/
    core_test.clj   # Unit tests for core functions
```


## Building and Running the Project

### Building the Uberjar

To build the standalone JAR for the project, run the following command:

```bash
clj -T:uberjar
```

This will generate a standalone JAR file in the `target` directory.

### Running the Application

Once the Uberjar is built, you can run the application using the following command:

```bash
java -jar target/ascii-radar-1.0.[git revision count]-standalone.jar
```

## Expected Output

When the application runs, it detects invaders in a grid of characters (represented by `o` and `-`) and prints their locations along with detection scores. Below is an example of the expected output format:

```
Invader found at:  [74 1]  detection score:  0.875
--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---

ooo-----o--
o--o-o-o---
--o-ooooo--
oo--ooo-oo-
ooooooo-ooo
oooo--ooo-o
o-o-----o-o
---oo-oo---


Invader found at:  [85 12]  detection score:  0.8636364
--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---

--oo----o--
-------o---
o--oooooo--
-oo--oo--o-
oo-oooooooo
o-ooooooo-o
oo-o----o-o
--ooo-oo--o


Invader found at:  [60 13]  detection score:  0.90909094
--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---

--o-----o--
-------o---
--oooo-oo--
----ooo-oo-
o--oooooo-o
o-o-ooooo-o
o-o-----o-o
---oo-oo---


Invader found at:  [42 0]  detection score:  0.875
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o

---oo---
--ooo-o-
--ooooo-
oo--o-oo
oo-ooooo
-----o--
oo-oo-o-
o-o--ooo


Invader found at:  [35 15]  detection score:  0.84375
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o

---oo---
--oooo--
oooooooo
oo-oo--o
-ooooooo
--o--ooo
-o-oo---
oo--oo-o


Invader found at:  [16 28]  detection score:  0.859375
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o

---o-o--
-ooooo--
-oooooo-
o--oo-oo
oooooooo
-ooo-o--
--ooo-o-
o-o--ooo


Invader found at:  [82 41]  detection score:  0.859375
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o

---oo---
--ooooo-
-oo-ooo-
oo-o-ooo
o-oooooo
--o--o--
oo-oo-o-
--oooo-o
```

