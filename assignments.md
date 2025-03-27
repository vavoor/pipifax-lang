# Assignments


## 1. Accept empty input files

Create an initial version of the Pipifax compiler that

1. Reads one file specified on the command line. This file can only contain
   spaces and comments.
   
2. Emits an assembly output

### Example

```
# This is

  # a file with comments

```

produces

```
# Pipifax compiler V1.0
.data
.text

```

## 2. Compile global variables of type int

Enhance the compiler to accept global variables of type `int` and generate
assembly output.

## Example

```
# Global variables
var a int   # Variable a

var _1 int var abc123 int

```

produces the following output

```
# Pipifax compiler V1.0

.data
a:  .word 0
_1: .word 0
abc123:  .word 0

.text
```
