# Pipifax - The Language

Pipifax is a small procedural programming language. It was designed as a
demo vehicle for a compiler construction class at the [Duale Hochschule Baden-Württemberg](https://www.dhbw-stuttgart.de).

## Trivia

The [Wiktionary](https://de.wiktionary.org/wiki/Wiktionary:Hauptseite) defines [Pipifax](https://de.wiktionary.org/wiki/Pipifax) as "unimportant thing, superfluous accessory, (ridiculous) trifle, nonsense".

## Language specification

A Pipifax program is one file containing function definitions and global variable declarations. 

### Encoding

The character encoding can be chosen to fit the compiler construction tools, e.g., UTF-8 with Java-based tools, ASCII or ISO 8859-1 with flex/bison.

### Comments

Comments start with the #-character and last until the end of the line.

```
# This is a comment line
var a int    # This is another comment
```

### Identifiers

Identifiers for functions and variables consist of an arbitrary number of letters, digits, and underscores. The first character must be a letter or underscore. Identifiers are case-sensitive.

### Data types

Pipifax has these data types:

- signed 32-bit integers implemented as two's complement
- 64-bit floating point numbers in the IEEE 754 encoding
- Strings (see section Notes below)
- Arrays of fixed length (the first element is the element with index 0)

```
var i int               # i is a variable of type integer
var d double            # d is a variable of type double
var a_15 [15] int       # a_15 is an array of 15 elements of type integer
var ad [3][5] double    # ad is an array of 3 arrays of 5 doubles
```

Like in C, boolean values are implemented as integers:

- 0 is interpreted as false

- not 0 is interpreted as true

### Literals

- Integer literals: 0, 1, 122345. Note that leading zeros are not allowed (01 is illegal). Signs are treated as operators (-1, --2).
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

Declaring more than one global variable with the same name is illegal.

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
func j(wrong [] int) {}          # This is illegal!
func k(wrong [12]*int) {}        # This is illegal!
```

If a function has a return value, its type is declared following the parameter list:

```
func f() int {}
func g(a int) double {}
func h() string {}
func j() [12] double {}
```

The function body is a block enclosed in curly brackets.

If a function returns a value, the return type is declared after the parameters:

```
func f(a int) int {...}    # A function returning an integer
func g() double {...}      # A function returning a double
func h() string {...}      # A function returning a string
func j() [12] int {...}    # A function returning an array of 12 integers
func k() *int {...}        # Illegal! Only parameters can be references
func l() [] double {...}   # Illegal! Arrays of unknown size are only allowed as parameters passed by reference
```

The function name is implicitly declared as a variable to store return values:

```
# Calculcates sum of two integers
func sum(a int, b int) int {
    sum = a + b    # sum is implicitly declared as an integer variable
}
```

It is allowed to assign and access the return value multiple times in a function:

```
func f(c int, d double) double {
    f = 0.0        # Return value is set to 0.0
    while c > 0 {
        f = f + d  # Return value is read and set
        c = c - 1
    }
}
```

Note that it is not possible to define functions inside of function bodies:

```
func f1() {
    func f2() {}    # Illegal!
}
```

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

### Local variable declarations

Local variables are declared in the same way as global variables, except that the declaration happens inside of a block. Declaring more than one local variable with the same name in the same scope is illegal.

### Statements

#### Assignment

Assignment statements assign a value defined by an expression to a left value. Left values can be variables, parameters, return value, or array elements:

```
var g int
func f(p int) int {
    var a [5] int
    var b [2][5] int
    g = p
    f = g
    p = 1
    a[2] = g
    b[0] = a    # Legals, as b[0] is an array of 5 integers
    b[1][2] = 2
}
```



#### Branching

For branching, Pipifax has to two version of if-statements:

```
if cond {
    # Code executed when cond is not 0
}

if cond {
    # Code executed when cond is true (cond!=0)
}
else {
     # Code executed when cond is false (cond==0)
}
```



#### Iteration

For iteration, there is a while-statement:

```
while cond {
    # Repeat the loop body while cond != 0
}
```



#### Function calls

When function calls are used as statements, the return value is ignored.

```
func f() {
    print("Hello world!)    # Calling print ignoring possible return values
}
```



### Operators and operator precedences

Operators connect Variables (`ab`), array accesses (`ab[i]`, `x[12][3454]`), literals (`3.14,0`,`"foo"`), function calls (`sqrt(2)`), and subexpressions in expressions. Expressions can be grouped using parentheses.

The following operators exist in Pipifax. Logical or has the lowest precedence, unary operators have the highest precedence.

- Logical **or** (`||`) returns 1 (true) or 0 (false). The operands must be integers. Short-circuit evaluation is applied, i.e. if the first operand is true, the second is not evaluated.
- Logical **and** (`&&`) returns 1 (true) or 0 (false). The operands must be integers. Short-circuit evaluation is applied, i.e. if the first operand is false, the second is not evaluated.
- Comparison operators return 1 (true) or 0 (false). Numeric comparison operators are `<`, `<=`, `>`, `>=`, `==`, and `!=`. The comparison operator for strings (`<=>`) returns -1 if the first operand is lexicalically smaller than the second operator, 0 if the operands are lexically equals, and 1 of the first operand is lexically greater than the second operand (cf. `strcmp()`  function in C).
- Addition (`+`) and subtraction (`-`) of numeric values (integer or double)
- Multiplication (`*`) and division (`/`) of numeric values (integer and double)
- Logical inversion (not, `!`) returning 1 of operand is false or 0 otherwise; negation (`-`). Double-to-integer cast (`(int)`) and integer-to-double cast (`(double)`). Note that inversion is defined for integers and negation is defined for integers and doubles.

Note that unlike in C, `=`  is not an operator.

### Scoping

Function names and variable names reside in different namespaces. It is possible to declare a variable with a name and a function with the same name:

```
var f double        # Declaration of global variable f
func f() int {}     # Definition of function f
```

Global variables and functions reside in a global scope. The order of function definitions and global variable declaration does not matter, i.e., a function can call functions and use global variables that are defined/declared before or
after the function.

```
var a int   # Declaration of global variable a before its use in f1
func f1() { # Definition of function f1 before its use in f2
}
func f2() { # Definition of function 2
  f1()      # It's legal to call functions defined before
  f3()      # It's legal to call functions defined later
  a = 1     # It's legal to use global variable declared before
  b = 2     # It's legal to use global variable declared later
}
func f3() { # Definition of function f3 after its use in f2
}
var b int   # Declaration of global variable b, after its use in f1
```

The scope of a local variable is limited by the block in which it has been declared. It is visible only *after* its declaration.

```
{
    {
        a = 1        # Illegal! a is not visible before its declaration
        var a int
        a = 2        # a is declared and visible here

    }  # Scope of a ends here
    a = 3    # Illegal! a is not visible after containing block
}
```

```
func f() {
    var a int
    if 1 {
        var a double    # It's legal to declare a variable with the same
                        # name and even a different type in another scope
    }
    var a string    # Illegal! Local variable a is declared twice
                    # in the same scope
}
```

Parameters and the return value reside in the same scope. Therefore, parameter names must differ from the function name, which is implicitly declared as variable for the return value.

## Notes

- Strings are not specified in detail. This gives more freedom to choose a simple implementation, e.g., with respect to character set and length limitations.
