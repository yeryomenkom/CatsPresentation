package yeryomenkom

import simulacrum._

import scala.language.higherKinds

@typeclass trait Traverse[F[_]] {
  //  Abstracting away the G (still imagining F to be List),
  //  traverse says given a collection of data, and a function
  //  that takes a piece of data and returns an effectful value,
  //  it will traverse the collection, applying the function and aggregating
  //  the effectful values (in a List) as it goes.
  //  In the most general form, F[_] is some sort of context which
  //  may contain a value (or several). While List tends to be among the most general cases,
  //  there also exist Traverse instances for Option, Either, and Validated (among others).
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]

  def sequence[G[_]: Applicative, A](fga: F[G[A]]): G[F[A]] =
    traverse(fga)(ga => ga)
}

object TestApp11 extends App {

  import cats.syntax.option._

  implicit val listTraverse: Traverse[List] = new Traverse[List] {
    override def traverse[G[_] : Applicative, A, B](fa: List[A])(f: A => G[B]): G[List[B]] = {
      val ap = implicitly[Applicative[G]]
      fa.foldRight[G[List[B]]](ap.pure(List.empty))((value, acc) => ap.map2(f(value), acc)(_  :: _))
    }
  }

  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
    override def pure[A](a: A): Option[A] = a.some
    override def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] =
      ff.flatMap(f => fa.map(f))
  }

  import Traverse.ops._

  val list1 = List(1.some, 2.some)
  println(list1.traverse(identity))     //Some(List(1, 2))

  val list2 = List(1.some, none)
  println(list2.traverse(identity))     //None


  println(list1.sequence)               //Some(List(1, 2))
  println(list2.sequence)               //None

}
