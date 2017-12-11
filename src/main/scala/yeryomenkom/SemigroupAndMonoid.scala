package yeryomenkom

object Skeletons {

  trait Semigroup[A] {
    //Associative operation taking which combines two values.
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    //Return the identity element for this monoid.
    def empty: A
  }

}

object TestApp8 extends App {
  import cats.instances.int._
  import cats.instances.map._
  import cats.Semigroup
  import cats.syntax.semigroup._

  val nestedMap: Map[String, Map[String, Int]] = Map(
    "a" -> Map("first" -> 1, "second" -> 5),
    "b" -> Map("first" -> 1, "second" -> 5)
  )

  val merged1 = nestedMap |+| nestedMap
  println(merged1)

  implicit val multiplyIntSemigroup: Semigroup[Int] = new Semigroup[Int] {
    override def combine(x: Int, y: Int): Int = x * y
  }

  val merged2 = nestedMap |+| nestedMap
  println(merged2)

}

object TestApp9 extends App {
  import cats.instances.map._
  import cats.syntax.semigroup._
  import cats.Semigroup

  val nestedMap: Map[String, Map[String, Int]] = Map(
    "a" -> Map("first" -> 1, "second" -> 5),
    "b" -> Map("first" -> 1, "second" -> 5)
  )

  implicit val multiplyIntSemigroup: Semigroup[Int] = new Semigroup[Int] {
    override def combine(x: Int, y: Int): Int = x * y
  }

  val merged1 = nestedMap |+| nestedMap
  println(merged1)      //Map(a -> Map(first -> 1, second -> 25), b -> Map(first -> 1, second -> 25))

  import cats.instances.int._
  val merged2 = nestedMap |+| nestedMap
  println(merged2)      //Map(a -> Map(first -> 2, second -> 10), b -> Map(first -> 2, second -> 10))

}

object TestApp10 extends App {

  val values = List(1, 2, 3, 4, 5)

  val product1 = values.foldLeft(1)(_ * _)
  println(product1)                             //120

  import cats.syntax.foldable._
  import cats.instances.list._

  import cats.Monoid

  implicit val multiplyIntMonoid: Monoid[Int] = new Monoid[Int] {
    override def empty = 1
    override def combine(x: Int, y: Int) = x * y
  }

  val product2 = values.foldMap(identity)
  println(product2)                             //120

  import cats.instances.int._

  val sum1 = values.foldMap(identity)
  println(sum1)                                 //15

  case class Payment(ownerId: Int, money: Int)

  val payments = 1 to 10 map { i => Payment(i, 10) }
  val moneySum = payments.toList.foldMap(_.money)
  println(moneySum)                             //100

}