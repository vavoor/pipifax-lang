# Pipifax - The Language

Pipifax is a small procedural programming language. It was designed as a
demo vehicle for a compiler construction class.

## Trivia

The [Wiktionary](https://de.wiktionary.org/wiki/Wiktionary:Hauptseite) defines [Pipifax](https://de.wiktionary.org/wiki/Pipifax) as "unimportant thing, superfluous accessory, (ridiculous) trifle, nonsense".

## Language specification

### Compilation units

A Pipifax program is one file containing function definitions and
global variable declarations. The order of function definitions and
global variable declaration does not matter, i.e., a function can call
functions and use global variables that are defined/declared before or
after the function.

**Example**

```
var a int   # Declaration of global variable a before its use in f1
func f1() { # Definition of function f1 before its use in f2
}
func f2() { # Definition of function 2
  f1()      # It's legal to call functions defined before
  f3()      # It's legal to call functions defined later
  a = 1     # It's legal to use global variabled declared before
  b = 2     # It's legal to use global variabled declared later
}
func f3() { # Definition of function f3 after its use in f2
}
var b int   # Declaration of global variable b, after its use in f1
```

### Encoding

The character encoding can be chosen to fit to the compiler construction tools, e.g., UTF-8 with Java-based tools, ASCII or ISO 8859-1 with flex/bison.

### Comments

Comments start with the #-character and last until the end of the the compiler construction tools, e.g., UTF-8 with Java-based tools, ASCII,line.

**Exmaple**

```
# This is a comment line
var a int    # This is another comment
```

### Identifiers

Identifiers for functions and variables consist of letters, digits, and underscores of arbitrary length. The first character must be a letter or underscore. Identifiers are case-sensitive.

### Data types

Pipifax has these data types:

- signed 32-bit integers implemented as two's complement
- 64-bit floating point numbers in the IEEE 754 encoding)
- Strings of arbitrary length
- Arrays of fixed length

```
var i int               # i is a variable of type integer
var d double            # d is a variable of type double
var a_15 [15] int       # a_15 is an array of 15 elements of type integer
var ad [3][5] double    # ad is an array of 3 arrays of 5 doubles
```

Like in C, boolean values are implemented as integers:

-  0 is interpreted as false

- not 0 is interpreted as true

### Literals

- Integer literals: 0, 1, 122345. Note that leading zero's are not allowed (01 is illegal). Signs are treated as operators (-1, --2).
- Double literals in scientific notation: 0.0, 0.1, 1.0, 2E2, 123e-12, 0.123e-1, 3.14E+12, 1.2e0. Note that signs are treated as operators (-1.2, -0.0).
- String literals are enclosed with double quotes. They can span more than one line. Quotes in strings are escaped with a backslash, backslashes are escaped with a backslash, like in C: "string", "this is a \"string\" in quotes", "\\"

### Global variable declaration

Global variables are declared with the keyword `var`:

```
var a int
var b double
var s string
var a_r12 [12] string
var xyz [2][3][4] double
```



### Function definition

Functions are defined with the keyword `func`:

```
func f() {}
```

Function parameters are declared in parentheses following the identifier:

```
func f0() {}
func f1(a int) {}
func f2(a double, s2 string, x [17] double) {}
```

Parameters declared, like in the previous examples, are passed _by value_.

To indicate that parameters are passed _by reference_, a `*` preceding the type in the parameter declaration is used:

```
func f(a_by_ref *int, b *[12][23] double) {}
```

For arrays passed by reference, the dimension can be omitted:

```
func f(array_without_dim *[] double) {}
func g(x *[][12] string) {}      # This is legal
func h(wrong *[][] string) {}    # This is illegal!
```

If a function has a return value, its type is declared following the parameter list:

```
func f() int {}
func g(a int) double {}
func h() string {}
func j() [12] double {}
```

The function body is a block enclosed in curly brackets.

### Block

A block is a sequence of statements and local variable declarations enclosed in curly brackets:

```
{
    var x int
    x = 1
    var y double
    y = (double) x
}
```



### Statements

#### Assignment

#### Branching

#### Iteration

#### Function calls

### Operators and precedence

- Logical or: &&

### Scoping

## Notes
