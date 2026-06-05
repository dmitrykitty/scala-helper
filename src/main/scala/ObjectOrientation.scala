package com.dnikitin

@main def objectOrientationDemo(): Unit = {

  // ============================================================
  // 1. BASIC CLASS
  // ============================================================

  class Animal {
    // In Scala, fields and methods are public by default.
    // Java equivalent:
    // public class Animal {
    //     public final int age = 0;
    //     public void eat() { System.out.println("Animal eating"); }
    // }

    val age: Int = 0

    // Unit is similar to Java's void.
    // In Scala, we usually skip empty parentheses for pure methods 
    // but use parentheses for methods with side effects, like println.
    def eat(): Unit = println("Animal eating")

    def description: String = s"Animal, age = $age"
  }

  val animal = new Animal
  animal.eat()
  println(animal.description)

  // `val` is like Java's final reference:
  // final Animal animal = new Animal();

  // `var` is a mutable variable:
  // Animal animal = new Animal(); // reassignable in Java
  var mutableNumber: Int = 10
  mutableNumber = 20

  // ============================================================
  // 2. CONSTRUCTOR ARGUMENTS VS FIELDS
  // ============================================================

  class Dog(nameOnlyConstructorArg: String) extends Animal {
    // nameOnlyConstructorArg is only a constructor parameter.
    // It is visible inside this class, but not from outside.
    //
    // Java rough equivalent:
    // class Dog extends Animal {
    //     public Dog(String nameOnlyConstructorArg) { ... }
    // }

    override def eat(): Unit =
      println(nameOnlyConstructorArg + " eating")

    def bark(): Unit =
      println(s"$nameOnlyConstructorArg says: woof!")
  }

  val dog1 = new Dog("Puppy")
  dog1.eat()
  dog1.bark()

  // dog1.nameOnlyConstructorArg // does not compile, because it is not a field

  class Cat(val name: String, var lives: Int) extends Animal {
    // `val name` creates a public read-only field.
    // `var lives` creates a public mutable field.
    //
    // Java rough equivalent:
    // class Cat extends Animal {
    //     private final String name;
    //     private int lives;
    //
    //     public String name() { return name; }
    //     public int lives() { return lives; }
    //     public void lives_$eq(int value) { lives = value; }
    // }

    override def eat(): Unit =
      println(s"$name eating")

    def loseLife(): Unit =
      lives -= 1
  }

  val cat = new Cat("Tom", 9)
  println(cat.name)
  println(cat.lives)
  cat.lives = 8
  println(cat.lives)

  // ============================================================
  // 3. INHERITANCE AND OVERRIDE
  // ============================================================

  class Labrador(name: String) extends Dog(name) {
    override def bark(): Unit =
      println("Friendly labrador bark!")
  }

  val labrador = new Labrador("Max")
  labrador.eat()
  labrador.bark()

  // In Scala, `override` is required when overriding a concrete method.
  // Java allows override without @Override, but @Override is recommended.
  // Scala forces this explicitly.

  // ============================================================
  // 4. POLYMORPHISM
  // ============================================================

  val aDog: Animal = new Dog("Polymorphic dog")

  // Runtime dispatch works like in Java.
  // Even though the reference type is Animal,
  // the actual object is Dog, so Dog.eat() is called.
  aDog.eat()

  val animals: List[Animal] = List(
    new Animal,
    new Dog("Rex"),
    new Cat("Kitty", 9)
  )

  animals.foreach(animal => animal.eat())

  // Java equivalent:
  // List<Animal> animals = List.of(new Animal(), new Dog("Rex"), new Cat("Kitty", 9));
  // for (Animal animal : animals) {
  //     animal.eat();
  // }

  // ============================================================
  // 5. ABSTRACT CLASS
  // ============================================================

  abstract class WalkingAnimal {
    // `protected` works similarly to Java,
    // but Scala's exact access rules are a little different.
    protected val hasLegs: Boolean = true

    // Abstract method. No implementation.
    // Java equivalent:
    // public abstract void walk();
    def walk(): Unit

    def canWalk: Boolean = hasLegs
  }

  class Horse extends WalkingAnimal {
    override def walk(): Unit =
      println("Horse walking")
  }

  val horse = new Horse
  horse.walk()
  println(horse.canWalk)

  // ============================================================
  // 6. TRAITS = SIMILAR TO JAVA INTERFACES, BUT MORE POWERFUL
  // ============================================================

  trait Carnivore {
    // Abstract method.
    // Java equivalent:
    // interface Carnivore {
    //     void eat(Animal animal);
    // }

    infix def eat(animal: Animal): Unit
  }

  trait Dangerous {
    // Traits can contain implemented methods.
    // Modern Java interfaces can also have default methods,
    // but Scala traits are used much more often in design.

    def warning: String = "This animal can be dangerous"
  }

  // Scala supports single class inheritance,
  // but multiple traits.
  class Crocodile extends Animal with Carnivore with Dangerous {
    override def eat(): Unit =
      println("Crocodile eating normal food")

    override infix def eat(animal: Animal): Unit =
      println("Crocodile: I'm eating another animal")

    override def warning: String =
      "Crocodile is very dangerous"
  }

  val aCroc = new Crocodile
  aCroc.eat()
  aCroc.eat(aDog)
  aCroc eat aDog // infix notation; works because `eat` is marked as infix
  println(aCroc.warning)

  // ============================================================
  // 7. METHODS, OPERATORS, AND INFIX NOTATION
  // ============================================================

  val x = 1 + 2
  val y = 1.+(2)

  println(x)
  println(y)

  // Operators are methods.
  // `1 + 2` is just syntactic sugar for `1.+(2)`.
  //
  // Java equivalent:
  // int x = 1 + 2;
  //
  // But in Java, + is a language operator.
  // In Scala, it is method syntax.

  class Counter(val value: Int) {
    def +(amount: Int): Counter =
      new Counter(value + amount)

    def increment(): Counter =
      this + 1

    override def toString: String =
      s"Counter($value)"
  }

  val counter = new Counter(10)
  println(counter + 5)
  println(counter.increment())

  // ============================================================
  // 8. ANONYMOUS CLASSES
  // ============================================================

  val dinosaur = new Carnivore {
    override infix def eat(animal: Animal): Unit =
      println("Dinosaur: I'm eating something")
  }

  dinosaur eat aDog

  // Compiler creates something conceptually similar to:
  //
  // class CarnivoreAnonymous extends Carnivore {
  //     override def eat(animal: Animal): Unit =
  //         println("Dinosaur: I'm eating something")
  // }
  //
  // val dinosaur = new CarnivoreAnonymous

  // Java equivalent:
  //
  // Carnivore dinosaur = new Carnivore() {
  //     @Override
  //     public void eat(Animal animal) {
  //         System.out.println("Dinosaur: I'm eating something");
  //     }
  // };

  // ============================================================
  // 9. SINGLETON OBJECT
  // ============================================================

  object Singleton {
    // Scala object is a singleton.
    // Java rough equivalent:
    //
    // public final class Singleton {
    //     public static final Singleton INSTANCE = new Singleton();
    //     private Singleton() {}
    // }

    def mySpecialMethod(): Int = 5537

    def apply(x: Int): Int = x + 1
  }

  println(Singleton.mySpecialMethod())

  println(Singleton.apply(65))
  println(Singleton(65)) // same as Singleton.apply(65)

  // `apply` is special syntax.
  // obj(args) is translated to obj.apply(args).

  // ============================================================
  // 10. COMPANION CLASS AND COMPANION OBJECT
  // ============================================================

  class User private (val name: String, val age: Int) {
    override def toString: String =
      s"User(name = $name, age = $age)"
  }

  object User {
    // Companion object has the same name as the class.
    // It is often used instead of static factory methods from Java.
    //
    // Java equivalent:
    // public static User of(String name, int age) { ... }

    def apply(name: String, age: Int): User =
      new User(name, age)

    def adult(name: String): User =
      new User(name, 18)
  }

  val user1 = User("Alice", 25)
  val user2 = User.adult("Bob")

  println(user1)
  println(user2)

  // new User("Alice", 25) // does not compile because constructor is private

  // ============================================================
  // 11. CASE CLASSES
  // ============================================================

  case class Person(name: String, age: Int)

  // Case classes automatically give you:
  // - constructor parameters as val fields
  // - equals
  // - hashCode
  // - toString
  // - copy
  // - companion object with apply
  // - pattern matching support

  val bob = Person("Bob", 54)

  // This calls Person.apply("Bob", 54), generated by the compiler.
  // That is why `new` is not needed.
  println(bob.name)
  println(bob.age)
  println(bob)

  val anotherBob = Person("Bob", 54)
  println(bob == anotherBob) // true, structural equality

  // Java equivalent would need record or a lot of boilerplate:
  //
  // public record Person(String name, int age) {}
  //
  // or older Java:
  // class Person {
  //     private final String name;
  //     private final int age;
  //     equals(...)
  //     hashCode()
  //     toString()
  // }

  val olderBob = bob.copy(age = 55)
  println(olderBob)

  // ============================================================
  // 12. PATTERN MATCHING
  // ============================================================

  def describePerson(person: Person): String =
    person match {
      case Person("Bob", age) if age >= 18 =>
        s"Adult Bob, age = $age"

      case Person(name, age) if age < 18 =>
        s"$name is underage"

      case Person(name, age) =>
        s"$name is $age years old"
    }

  println(describePerson(bob))
  println(describePerson(Person("Tom", 12)))
  println(describePerson(Person("Alice", 20)))

  // Pattern matching is much more powerful than Java switch.
  // Modern Java has pattern matching too, but Scala uses it very heavily.

  // ============================================================
  // 13. EXCEPTIONS
  // ============================================================

  val result: String =
    try {
      val r: String = null
      r.length.toString
    } catch {
      case e: NullPointerException =>
        "NullPointerException happened"

      case e: Exception =>
        "Some other exception happened"
    } finally {
      println("finally block executed")
    }

  println(result)

  // try/catch is an expression in Scala.
  // It can return a value.
  //
  // Java equivalent:
  //
  // String result;
  // try {
  //     String r = null;
  //     result = Integer.toString(r.length());
  // } catch (NullPointerException e) {
  //     result = "NullPointerException happened";
  // } finally {
  //     System.out.println("finally block executed");
  // }

  // ============================================================
  // 14. OPTION INSTEAD OF NULL
  // ============================================================

  def findUserName(id: Int): Option[String] =
    if (id == 1) Some("Alice")
    else None

  val maybeName = findUserName(1)

  maybeName match {
    case Some(name) => println(s"Found user: $name")
    case None       => println("User not found")
  }

  val nameOrDefault = findUserName(100).getOrElse("Unknown")
  println(nameOrDefault)

  // In idiomatic Scala, Option is preferred over null.
  //
  // Java equivalent:
  // Optional<String> maybeName = findUserName(1);

  // ============================================================
  // 15. GENERICS
  // ============================================================

  abstract class MyList[T] {
    def head: T
    def tail: MyList[T]
  }

  val intList: List[Int] = List(1, 2, 3)
  val stringList: List[String] = List("a", "b", "c")

  println(intList.head)
  println(intList.tail)

  println(stringList.head)
  println(stringList.tail)

  // Java equivalent:
  // List<Integer> intList = List.of(1, 2, 3);
  // Integer first = intList.get(0);

  // ============================================================
  // 16. IMMUTABILITY
  // ============================================================

  val numbers = List(1, 2, 3)

  val reversed = numbers.reverse

  println(numbers)
  println(reversed)

  // `reverse` does not change the original list.
  // It returns a new list.
  //
  // This is different from many mutable Java collections,
  // where methods may modify the original object.

  val added = 0 :: numbers
  println(added)
  println(numbers)

  // `::` creates a new list with element at the beginning.
  // Original list is unchanged.

  // ============================================================
  // 17. METHODS WITH AND WITHOUT PARENTHESES
  // ============================================================

  class MethodStyleExample {
    // Usually: no parentheses when method is pure and just returns information.
    def size: Int = 42

    // Usually: parentheses when method performs side effects.
    def printSize(): Unit = println(size)
  }

  val methodExample = new MethodStyleExample
  println(methodExample.size)
  methodExample.printSize()

  // Rule of thumb:
  // - def value: Int       -> property-like method
  // - def doSomething(): Unit -> side-effecting action
  //
  // That is why List.reverse is usually called as:
  // numbers.reverse
  // not:
  // numbers.reverse()

  // ============================================================
  // 18. HIGHER-ORDER FUNCTIONS
  // ============================================================

  val doubled = numbers.map(n => n * 2)
  println(doubled)

  val evenOnly = numbers.filter(n => n % 2 == 0)
  println(evenOnly)

  val sum = numbers.reduce((a, b) => a + b)
  println(sum)

  // Shorter syntax:
  val doubledShort = numbers.map(_ * 2)
  val sumShort = numbers.reduce(_ + _)

  println(doubledShort)
  println(sumShort)

  // Java equivalent:
  //
  // List<Integer> doubled = numbers.stream()
  //     .map(n -> n * 2)
  //     .toList();

  // ============================================================
  // 19. DEFAULT AND NAMED ARGUMENTS
  // ============================================================

  class Connection(
                    val host: String,
                    val port: Int = 8080,
                    val useSsl: Boolean = false
                  ) {
    override def toString: String =
      s"Connection(host = $host, port = $port, useSsl = $useSsl)"
  }

  val connection1 = new Connection("localhost")
  val connection2 = new Connection("localhost", 9000)
  val connection3 = new Connection(
    host = "example.com",
    useSsl = true,
    port = 443
  )

  println(connection1)
  println(connection2)
  println(connection3)

  // Java usually needs constructor overloading or builder pattern for this.

  // ============================================================
  // 20. SEALED TRAITS AND ENUM-LIKE DESIGN
  // ============================================================

  sealed trait PaymentStatus

  object PaymentStatus {
    case object Pending extends PaymentStatus
    case object Completed extends PaymentStatus
    case object Failed extends PaymentStatus
  }

  def describeStatus(status: PaymentStatus): String =
    status match {
      case PaymentStatus.Pending   => "Payment is pending"
      case PaymentStatus.Completed => "Payment completed"
      case PaymentStatus.Failed    => "Payment failed"
    }

  println(describeStatus(PaymentStatus.Pending))

  // `sealed` means all direct subtypes must be known in this file.
  // This helps the compiler check pattern matching completeness.
  //
  // Java rough equivalent:
  // enum PaymentStatus {
  //     PENDING, COMPLETED, FAILED
  // }

  // In Scala 3, you can also use real enums.
  enum OrderStatus {
    case Created, Paid, Shipped, Delivered
  }

  val orderStatus = OrderStatus.Paid
  println(orderStatus)

  // ============================================================
  // 21. SMALL PRACTICAL EXAMPLE
  // ============================================================

  trait Repository[T] {
    def save(entity: T): Unit
    def findById(id: Int): Option[T]
  }

  case class Product(id: Int, name: String, price: BigDecimal)

  class InMemoryProductRepository extends Repository[Product] {
    private var products: Map[Int, Product] = Map.empty

    override def save(product: Product): Unit =
      products = products + (product.id -> product)

    override def findById(id: Int): Option[Product] =
      products.get(id)
  }

  val repository = new InMemoryProductRepository

  repository.save(Product(1, "Laptop", BigDecimal(4999.99)))
  repository.save(Product(2, "Mouse", BigDecimal(99.99)))

  println(repository.findById(1))
  println(repository.findById(999))

  // This is similar to Java:
  //
  // interface Repository<T> {
  //     void save(T entity);
  //     Optional<T> findById(int id);
  // }
}