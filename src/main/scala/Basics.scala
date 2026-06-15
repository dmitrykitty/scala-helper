package com.dnikitin

import scala.annotation.tailrec

object Basics {

  // This is the classic entry point.
  // Java equivalent:
  //
  // public class Basics {
  //     public static void main(String[] args) {
  //         ...
  //     }
  // }
  //
  // In Scala, `Array[String]` is similar to Java's `String[]`.
  def main(args: Array[String]): Unit = {

    // ============================================================
    // 1. VALUES AND VARIABLES
    // ============================================================

    // `val` means immutable reference.
    // Java equivalent:
    // final int number = 42;
    val number: Int = 42

    // Type annotation is optional if the compiler can infer the type.
    // Java equivalent:
    // final boolean aBool = true;
    val aBool = true

    // `var` means mutable variable.
    // Java equivalent:
    // int mutableNumber = 10;
    // mutableNumber = 20;
    var mutableNumber: Int = 10
    mutableNumber = 20

    println(number)
    println(aBool)
    println(mutableNumber)

    // Prefer `val` by default.
    // Use `var` only when mutation is really needed.

    // ============================================================
    // 2. BASIC TYPES
    // ============================================================

    val anInt: Int = 123
    val aLong: Long = 123456789L
    val aDouble: Double = 3.14
    val aFloat: Float = 2.71f
    val aChar: Char = 'A'
    val aBoolean: Boolean = false
    val aShort: Short = 123
    val aByte: Byte = 12

    println(anInt)
    println(aLong)
    println(aDouble)
    println(aFloat)
    println(aChar)
    println(aBoolean)
    println(aShort)
    println(aByte)

    // Similar Java primitive types:
    // int, long, double, float, char, boolean, short, byte
    //
    // In Scala, everything behaves like an object.
    // For example, Int has methods:
    val incremented = anInt.+(1)
    println(incremented)

    // Usually we write:
    val incrementedNormally = anInt + 1
    println(incrementedNormally)

    // ============================================================
    // 3. STRINGS
    // ============================================================

    val aString: String = "I love Scala"
    val anotherString = "very much"

    // String concatenation.
    // Java equivalent:
    // String result = aString + " " + anotherString;
    val concatenated = aString + " " + anotherString
    println(concatenated)

    // String interpolation with `s`.
    val scalaString = s"$aString $anotherString. Boolean value = $aBool"
    println(scalaString)

    val name = "Dima"
    val age = 25

    val introduction = s"My name is $name and I am $age years old"
    println(introduction)

    // You can put expressions inside ${...}.
    val nextYear = s"Next year I will be ${age + 1}"
    println(nextYear)

    // Formatted interpolation with `f`.
    // Similar to Java's String.format.
    val price = 123.4567
    val formattedPrice = f"Price: $price%.2f PLN"
    println(formattedPrice)

    // Raw strings are useful for paths, regexes, etc.
    val rawPath = raw"""C:\Users\Dima\Projects\Scala\n"""
    println(rawPath)

    // Multi-line string.
    val multiLineString =
      """
        |This is line 1
        |This is line 2
        |This is line 3
        |""".stripMargin

    println(multiLineString)

    // ============================================================
    // 4. EXPRESSIONS
    // ============================================================

    // Expression = something that can be reduced to a value.
    val anExpression = 2 + 3
    println(anExpression)

    // In Scala, `if` is an expression.
    // It returns a value.
    //
    // Java equivalent:
    // int ifExpression;
    // if (anExpression > 5) {
    //     ifExpression = 56;
    // } else {
    //     ifExpression = 999;
    // }
    val ifExpression = if (anExpression > 5) 56 else 999
    println(ifExpression)

    val chainedIf =
      if (anExpression < -10) 56
      else if (anExpression < 0) 45
      else 0

    println(chainedIf)

    // Important:
    // Both branches should usually return the same type.
    val goodIf: Int = if (aBool) 1 else 0

    // This compiles, but the inferred type is Any.
    // Any is like Object, but even more general.
    // Usually this is not what we want.
    val badIf = if (aBool) 123 else "not a number"

    println(goodIf)
    println(badIf)

    // Java ternary equivalent:
    // int goodIf = aBool ? 1 : 0;

    // ============================================================
    // 5. CODE BLOCKS
    // ============================================================

    val aCodeBlock = {
      val aLocalValue = 67
      val anotherLocalValue = 3

      // The value of the block is the value of the last expression.
      aLocalValue + anotherLocalValue
    }

    println(aCodeBlock)

    // Java rough equivalent:
    //
    // int aCodeBlock;
    // {
    //     int aLocalValue = 67;
    //     int anotherLocalValue = 3;
    //     aCodeBlock = aLocalValue + anotherLocalValue;
    // }

    val complexBlock = {
      val x = 10
      val y = 20

      if (x < y) "x is smaller"
      else "y is smaller"
    }

    println(complexBlock)

    // ============================================================
    // 6. FUNCTIONS / METHODS
    // ============================================================

    def myFunction(x: Int, y: String): String = {
      y + " " + x
    }

    println(myFunction(42, "Number is"))

    // Shorter version.
    // Return type can often be inferred.
    def add(x: Int, y: Int) = x + y

    println(add(10, 20))

    // For public methods, it is often better to write the return type explicitly.
    def multiply(x: Int, y: Int): Int = x * y

    println(multiply(3, 4))

    // Java equivalent:
    //
    // static String myFunction(int x, String y) {
    //     return y + " " + x;
    // }

    // In Scala, we usually do not write `return`.
    // The last expression is returned automatically.

    def withReturnKeyword(x: Int): Int = {
      return x + 1
    }

    println(withReturnKeyword(10))

    // The version below is preferred:
    def withoutReturnKeyword(x: Int): Int = {
      x + 1
    }

    println(withoutReturnKeyword(10))

    // ============================================================
    // 7. DEFAULT AND NAMED ARGUMENTS
    // ============================================================

    def connect(host: String, port: Int = 8080, useSsl: Boolean = false): String = {
      s"Connecting to host=$host, port=$port, useSsl=$useSsl"
    }

    println(connect("localhost"))
    println(connect("localhost", 9000))
    println(connect("example.com", useSsl = true, port = 443))

    // Java usually needs overloaded methods or builder pattern:
    //
    // connect("localhost");
    // connect("localhost", 9000);
    // connect("example.com", 443, true);

    // ============================================================
    // 8. RECURSION
    // ============================================================

    def factorial(n: Int): Int = {
      if (n <= 1) 1
      else n * factorial(n - 1)
    }

    println(factorial(5))

    // Important:
    // Scala supports loops, but in functional Scala we often prefer:
    // - recursion
    // - map/filter/fold
    // - immutable collections

    // Recursive methods should have explicit return types.
    // This is required in many recursive cases.

    @tailrec
    def factorialTailRecursive(n: Int, accumulator: BigInt = BigInt(1)): BigInt = {
      if (n <= 1) accumulator
      else factorialTailRecursive(n - 1, accumulator * n)
    }

    println(factorialTailRecursive(20))

    // @tailrec asks the compiler to verify that this recursion is optimized.
    // Tail recursion works like a loop internally and avoids stack overflow.

    // Java loop equivalent:
    //
    // static int factorial(int n) {
    //     int result = 1;
    //     for (int i = 2; i <= n; i++) {
    //         result *= i;
    //     }
    //     return result;
    // }

    // ============================================================
    // 9. UNIT TYPE
    // ============================================================

    // Unit means "no meaningful value".
    // Java equivalent: void
    def myUnitFunction(): Unit = {
      println("I do not return anything meaningful")
    }

    myUnitFunction()

    val unitValue = myUnitFunction()
    println(unitValue)

    // This prints (), which is the only value of type Unit.

    // Important difference:
    //
    // Java void means "no value".
    // Scala Unit is a real type with one value: ().

    // ============================================================
    // 10. SIDE EFFECTS
    // ============================================================

    // Side effect = function does something outside returning a value.
    // Examples:
    // - printing
    // - modifying a variable
    // - writing to file
    // - sending HTTP request
    // - changing database state

    def pureAdd(x: Int, y: Int): Int = {
      x + y
    }

    def impurePrint(message: String): Unit = {
      println(message)
    }

    println(pureAdd(1, 2))
    impurePrint("This function has a side effect")

    // Functional programming prefers pure functions when possible.

    // ============================================================
    // 11. WHILE LOOPS
    // ============================================================

    var i = 0

    while (i < 3) {
      println(s"while loop i = $i")
      i += 1
    }

    // Scala has while loops.
    // But they require mutable state, so they are less idiomatic.

    // Java equivalent:
    //
    // int i = 0;
    // while (i < 3) {
    //     System.out.println(i);
    //     i++;
    // }

    // `while` returns Unit.
    val whileResult = while (i < 0) {
      println("Will not happen")
    }

    println(whileResult)

    // ============================================================
    // 12. FOR LOOPS
    // ============================================================

    for (n <- 1 to 3) {
      println(s"for loop n = $n")
    }

    // Java equivalent:
    //
    // for (int n = 1; n <= 3; n++) {
    //     System.out.println(n);
    // }

    for (n <- 1 until 3) {
      println(s"until example n = $n")
    }

    // 1 to 3      -> 1, 2, 3
    // 1 until 3   -> 1, 2

    // For-comprehension can create a new collection with `yield`.
    val squares = for (n <- 1 to 5) yield n * n
    println(squares)

    /*
    Take numbers from 1 to 10,
    keep only even numbers,
    return their squares as a new collection.
     */
    val evenSquares =
      for {
        n <- 1 to 10
        if n % 2 == 0
      } yield n * n

    /*
    the same as
      val evenSquares =
      (1 to 10)
        .filter(n => n % 2 == 0)
        .map(n => n * n)
      or
      val evenSquares =
        for (n <- 1 to 10 if n % 2 == 0) yield n * n
     */
    println(evenSquares)

    // ============================================================
    // 13. COLLECTIONS: LIST, VECTOR, ARRAY
    // ============================================================

    val numbers = List(1, 2, 3, 4, 5)

    println(numbers.head)
    println(numbers.tail)
    println(numbers.reverse)

    // List is immutable.
    // `reverse` returns a new List.
    // Original list is unchanged.
    println(numbers)

    //create a new list with 0 added at the beginning. :: is called cons. It prepends one element to a list.
    val withZeroAtBeginning = 0 :: numbers // numbers.::(0) - prepend
    println(withZeroAtBeginning)

    //create a new list with 6 added at the end.
    val withSixAtEnd = numbers :+ 6 //append
    println(withSixAtEnd)

    // Java equivalent:
    //
    // List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    //
    // But Java's ArrayList is usually mutable.
    // Scala's List is immutable by default.

    val vector = Vector(1, 2, 3)
    println(vector)

    // Vector is also immutable and usually better for indexed access than List.

    val array = Array(1, 2, 3)

    array(0) = 100

    println(array.mkString(", "))

    // Array is mutable.
    // Java equivalent:
    //
    // int[] array = {1, 2, 3};
    // array[0] = 100;

    // In Scala:
    // array(0) means array.apply(0)
    // array(0) = 100 means array.update(0, 100)

    // ============================================================
    // 14. MAP, FILTER, FOREACH, FOLD
    // ============================================================

    val doubled = numbers.map(n => n * 2)
    println(doubled)

    val doubledShort = numbers.map(_ * 2)
    println(doubledShort)

    val evenNumbers = numbers.filter(n => n % 2 == 0)
    println(evenNumbers)

    val evenNumbersShort = numbers.filter(_ % 2 == 0)
    println(evenNumbersShort)

    numbers.foreach(n => println(s"foreach: $n"))

    val sum = numbers.foldLeft(0)((accumulator, n) => accumulator + n)
    println(sum)

    val sumShort = numbers.foldLeft(0)(_ + _)
    println(sumShort)

    // Java Stream API equivalent:
    //
    // List<Integer> doubled = numbers.stream()
    //     .map(n -> n * 2)
    //     .toList();
    //
    // List<Integer> evenNumbers = numbers.stream()
    //     .filter(n -> n % 2 == 0)
    //     .toList();
    //
    // int sum = numbers.stream()
    //     .reduce(0, Integer::sum);

    // ============================================================
    // 15. FUNCTIONS AS VALUES
    // ============================================================

    /*
    plusOne is a function.
    It takes one Int.
    It returns one Int.
    For input x, it returns x + 1.
     */
    val plusOne: Int => Int = x => x + 1

    println(plusOne(10))

    val addTwoNumbers: (Int, Int) => Int = (x, y) => x + y

    println(addTwoNumbers(10, 20))

    // Java equivalent:
    //
    // Function<Integer, Integer> plusOne = x -> x + 1;
    // BiFunction<Integer, Integer, Integer> addTwoNumbers = (x, y) -> x + y;

    def applyFunction(x: Int, function: Int => Int): Int = {
      function(x)
    }

    println(applyFunction(10, plusOne))
    println(applyFunction(10, x => x * 3))
    //x => x * 3
    println(applyFunction(10, _ * 3))

    // ============================================================
    // 16. TUPLES
    // ============================================================

    val person = ("Dima", 25, true)

    println(person._1)
    println(person._2)
    println(person._3)

    // Tuple is useful for grouping a few values quickly.
    //
    // Java rough equivalents:
    // - record PersonData(String name, int age, boolean active)
    // - custom class
    // - Map.Entry for pairs only

    val nameAndAge: (String, Int) = ("Alice", 30)
    println(nameAndAge)

    // Pattern matching / destructuring:
    val (personName, personAge) = nameAndAge

    println(personName)
    println(personAge)

    // ============================================================
    // 17. OPTION INSTEAD OF NULL
    // ============================================================

    def findUserName(id: Int): Option[String] = {
      if (id == 1) Some("Alice")
      else None
    }

    val existingUser = findUserName(1)
    val missingUser = findUserName(999)

    println(existingUser)
    println(missingUser)

    val userNameOrDefault = missingUser.getOrElse("Unknown user")
    println(userNameOrDefault)

    existingUser match {
      case Some(userName) => println(s"Found user: $userName")
      case None           => println("User not found")
    }

    // Java equivalent:
    //
    // Optional<String> findUserName(int id) {
    //     if (id == 1) return Optional.of("Alice");
    //     else return Optional.empty();
    // }

    // In idiomatic Scala, avoid null when possible.

    // ============================================================
    // 18. PATTERN MATCHING BASICS
    // ============================================================

    def describeNumber(n: Int): String = {
      n match {
        case 0 => "zero"
        case 1 => "one"
        case 2 => "two"
        case _ => "something else"
      }
    }

    println(describeNumber(0))
    println(describeNumber(5))

    // `_` means default case.
    // Java equivalent:
    //
    // switch (n) {
    //     case 0 -> "zero";
    //     case 1 -> "one";
    //     case 2 -> "two";
    //     default -> "something else";
    // }

    def describeList(list: List[Int]): String = {
      list match {
        case Nil =>
          "empty list"
          /*
          A list with one element.
          Take the first element and call it head.
          After it, there is empty list Nil.
          list equals to list with [ head elements, Nil ]
           */
        case head :: Nil =>
          s"one element: $head"

        case head :: tail =>
          s"head = $head, tail = $tail"
      }
    }

    println(describeList(List()))
    println(describeList(List(10)))
    println(describeList(List(10, 20, 30)))

    // ============================================================
    // 19. EXCEPTIONS
    // ============================================================

    def parseInt(value: String): Option[Int] = {
      try {
        Some(value.toInt)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

    println(parseInt("123"))
    println(parseInt("abc"))

    // try/catch is also an expression in Scala.
    val parsedValue: String =
      try {
        "123".toInt
        "Parsing succeeded"
      } catch {
        case _: NumberFormatException =>
          "Parsing failed"
      } finally {
        println("Finally block executed")
      }

    println(parsedValue)

    // Java equivalent:
    //
    // String parsedValue;
    // try {
    //     Integer.parseInt("123");
    //     parsedValue = "Parsing succeeded";
    // } catch (NumberFormatException e) {
    //     parsedValue = "Parsing failed";
    // } finally {
    //     System.out.println("Finally block executed");
    // }

    // ============================================================
    // 20. LAZY VALUES
    // ============================================================

    lazy val expensiveValue = {
      println("Computing expensive value...")
      100
    }

    println("Before using lazy val")
    println(expensiveValue)
    println(expensiveValue)

    // lazy val is computed only once, when it is used for the first time.
    //
    // Java rough equivalent:
    // lazy initialization with a private field and a getter.

    // ============================================================
    // 21. CALL-BY-VALUE VS CALL-BY-NAME
    // ============================================================

    def callByValue(x: Long): Unit = {
      println("callByValue:")
      println(x)
      println(x)
    }

    def callByName(x: => Long): Unit = {
      println("callByName:")
      println(x)
      println(x)
    }

    callByValue(System.nanoTime())
    callByName(System.nanoTime())

    // callByValue evaluates the argument once before calling the function.
    // callByName evaluates the argument every time it is used inside the function.
    //
    // Java rough equivalent for call-by-name:
    // Supplier<Long> supplier = () -> System.nanoTime();

    def printTwice(message: => String): Unit = {
      println(message)
      println(message)
    }

    printTwice {
      println("Creating message...")
      "Hello"
    }

    // ============================================================
    // 22. SEMICOLONS
    // ============================================================

    val noSemicolon = 123
    println(noSemicolon)

    // Semicolons are usually not used in Scala.
    // Java requires semicolons almost everywhere.
    //
    // Scala:
    // val x = 10
    //
    // Java:
    // int x = 10;

    // ============================================================
    // 23. EQUALITY
    // ============================================================

    val string1 = "hello"
    val string2 = "he" + "llo"

    println(string1 == string2)

    // In Scala, `==` checks value equality.
    // It is null-safe and usually calls equals internally.
    //
    // Java:
    // string1.equals(string2)

    val object1 = new String("hello")
    val object2 = new String("hello")

    println(object1 == object2) // value equality
    println(object1 eq object2) // reference equality

    // Java equivalent:
    //
    // object1.equals(object2) // value equality
    // object1 == object2      // reference equality

    // ============================================================
    // 24. SMALL PRACTICAL EXAMPLE
    // ============================================================

    def calculateFinalPrice(
                             basePrice: BigDecimal,
                             discountPercent: BigDecimal = BigDecimal(0),
                             taxPercent: BigDecimal = BigDecimal(23)
                           ): BigDecimal = {
      val discount = basePrice * discountPercent / 100
      val priceAfterDiscount = basePrice - discount
      val tax = priceAfterDiscount * taxPercent / 100

      priceAfterDiscount + tax
    }

    val finalPrice1 = calculateFinalPrice(BigDecimal(100))
    val finalPrice2 = calculateFinalPrice(
      basePrice = BigDecimal(100),
      discountPercent = BigDecimal(10)
    )

    println(finalPrice1)
    println(finalPrice2)

    // This example shows:
    // - immutable values
    // - default arguments
    // - named arguments
    // - expression-based style

    // ============================================================
    // 25. SUMMARY
    // ============================================================

    println("Scala basics demo finished")
  }
}