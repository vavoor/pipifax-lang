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

## 3. Prevent duplicate variable names

Add a semantic check to ensure global variables have a unique name, i.e.
add a visitor that checks for duplicates. If duplicate names are discovered,
an error message should be printed and no output file should be generated.


## 4. Create an abstract syntax tree

Instead of generating code from the parse tree, we will create an abstract
syntax tree (AST) from the parse tree and use this for the semantic
analysis and for the code generation.

Create a visitor that creates the nodes of the AST. The AST needs to
represent the program and the global variables.


## 5. Assignment of constant numbers to variables

After completion of this assigment, the compiler should be able to generate
code for variable assignments of this kind:

```
# Assigning constant values to variables
var i int
i = 0

var xy int
xy = 2
i = 123
```

The generated assembly code should look like this:

```
# Pipifax compiler V1.0

.data
i:	.word 0
xy:	.word 0

.text
	la t1,i
	li t2,0
	sw t2,0(t1)
	la t1,xy
	li t2,2
	sw t2,0(t1)
	la t1,i
	li t2,123
	sw t2,0(t1)
```

The compiler should also report an error if undeclared variables are used.

## 6. Assignments of variable values

Add the capability to assign variable values like in the following example:

```
var a int
a = 42

var b int
b = a
```

The compiler should check whether all variables are declared and report
corresponding error messages.

The generated code could look like this:

```
# Pipifax compiler V1.0

.data
a:	.word 0
b:	.word 0

.text
	li t1,42
	la t2,a
	sw t1,0(t2)
	la t2,a
	lw t1,0(t2)
	la t2,b
	sw t1,0(t2)
```

Note that the order of instructions or usage of registers can vary in your
implementation.


## 7. Adding expressions

Add the capability to add expressions. Pipifax code like this should be compiled
into assembly code:

```
var a int
var b int

a = 1+2
b = 3 + a + 4

```

You need to pay attention to the register usage!
