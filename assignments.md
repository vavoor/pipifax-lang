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

## 2. Global variables of type int

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

## 3. Name checks to prevent duplicate variable names

Add a semantic check to ensure global variables have a unique name, i.e.
add a visitor that checks for duplicates. If duplicate names are discovered,
an error message should be printed and no output file should be generated.


## 4. Abstract syntax tree

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


## 7. Addition

Add the capability to add expressions. Pipifax code like this should be compiled
into assembly code:

```
var a int
var b int

a = 1+2
b = 3 + a + 4

```

You need to pay attention to the register usage!


## 8. Functions without parameters

In Pipifax, all statements are actually contained in functions. In this
assigment, change the compiler so that a Pipifax program consists of
functions definitions and global variable declarations, i.e. the
assignment statement that was created earlier is now only allowed in a
function body.

In addition to the assignment, the compiler should also implement
function calls.

In particular, the compiler should support

- Declaring functions without parameters and without return value
- Calling functions without arguments
- No local variables

Note that the order of function definitions and global variable
declarations does not matter. A function can call a function or access
a global variabls that is defined/declared later in the program code.

The compile should also check whether a function `main` exists and
report an error if not. It should also generate code that `main` is
called when the execution starts.

```
func main () {
  f()
}

func f() {
  a = 2
}

var a int

```

## 9. Arrays

In addition to integeer types, the compiler should support arrays of integers,
e.g.

```
var i int
var a [3] int
var aa [3] int

func main() {
  i = 1
  a[0] = 0
  a[i] = i
  a[1+i] = 2 + a[0]
  aa = a
}

```

This requires a number of semantical checks:

- In an assignment, types need to be compatible
- The dimension of an array must be a positive constant integer numbers
- The index of an array must be an integer numbers

The grammar and the code generator need to support arrays on the lefts side
and on the right side of an assignment.

As assignments of arrays is allowed in Pipifax, we need to decide how array
values are represented. Hint: It's the base address of the array.

When implementing these features, it's adviceable to proceed in small steps,
e.g.

1. Introduce types (i.e. a Type class in the AST) with IntType as only
   possible types
2. Add lvalues and lvalue expressions, but only allow names - no arrays
   yet.
3. Add a type to each expression and a type calculator.
4. Finally, add the arrays.


## 10. Local variables

Functions can contain local variables. Unlike global variables, local
variables must have been declared before use.

This is legal code:

```
func main() {
  var l int

  l = 1
  g = l + l + 2
}
var g int
```

This is illegal:

```
func main() {
  a = 1
  var a int
}
```


## 11. Function parameters and return values

Extend the compiler to allow functions having parameters and returning
values.
