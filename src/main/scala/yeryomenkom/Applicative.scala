package yeryomenkom

import simulacrum._

import scala.language.higherKinds

@typeclass trait Applicative[F[_]] extends Functor[F] { self =>

  // Example: take 1 and lift into List[Int](1)
  // Called pure because it takes a raw, "pure" value that exists outside of
  // "effect system" and lifts into effect system. These are not side-effects,
  // rather that, for example, Option models the "effect" of having or not
  // having a value. List models the "effect" of having multiple values.
  def pure[A](a: A): F[A]

  // Takes two proper types, A and B, and an F[A], but instead of taking A => B,
  // as with Functor's map, takes a type that exists _within_ the type
  // constructor. Applicative operates inside the "container", Functor "unwraps"
  // and "rewraps".
  def apply[A, B](fa: F[A])(ff: F[A => B]): F[B]

  /* Derived methods */

  def apply2[A, B, Z](fa: F[A], fb: F[B])(ff: F[(A, B) => Z]): F[Z] =
    apply(fa)(apply(fb)(map(ff)(f => b => a => f(a,b))))

  // Map is just apply but with function not wrapped in type constructor F.
  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    apply(fa)(pure(f))

  def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
    apply(fa)(map(fb)(b => f(_,b))) // or: apply(fb)(map(fa)(f.curried))

  def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    map2(fa, fb)((a, b) => (a, b))
}

object Applicative {

  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
    def pure[A](a: A): Option[A] = Some(a)

    def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] = (fa, ff) match {
      case (None, _) => None
      case (Some(a), None) => None
      case (Some(a), Some(f)) => Some(f(a))
    }
  }

  implicit val listApplicative: Applicative[List] = new Applicative[List] {
    def pure[A](a: A): List[A] = List(a)

    def apply[A, B](fa: List[A])(ff: List[A => B]): List[B] = for {
      a <- fa
      f <- ff
    } yield f(a)
  }

}

object TestApp7 extends App {

  import cats.syntax.option._

  case class Person(name: String, age: Int)

  val maybeName = "Anna".some
  val maybeAge = 18.some

  //monadic style
  val annaMonadic = for {
    name <- maybeName
    age <- maybeAge
  } yield Person(name, age)

  println(annaMonadic)              //Some(Person(Anna,18))

  //applicative
  val annaApplicative = implicitly[Applicative[Option]].map2(maybeName, maybeAge)(Person.apply)

  println(annaApplicative)          //Some(Person(Anna,18))

  //cats applicative
  import cats.instances.option._
  import cats.syntax.apply._

  val annaApplicativeCats = (maybeName, maybeAge).mapN(Person.apply)

  println(annaApplicativeCats)      //Some(Person(Anna,18))


}
