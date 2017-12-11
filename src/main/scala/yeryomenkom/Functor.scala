package yeryomenkom

import simulacrum._

import scala.language.higherKinds

// A functor is a type class that abstracts over type constructors (F[_]) that
// can define a map function.
// @typeclass from simulacrum gives ops, implicit summoning.
@typeclass trait Functor[F[_]] { self =>
  // Get F from Functor; A and B from type parameters to map
  def map[A, B](fa: F[A])(f: A => B): F[B]

  // Lift the pure function A => B to a function executing with values wrapped
  // in the functor's context.
  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)
}

object Functor {

  // This must be def because we require the type parameter A, and vals cannot
  // have type parameters. X => ? is a type constructor that when you apply a
  // proper type A, you get back a function X => A.
  // We cannot do Function1[X,B], because that's a proper type, whereas
  // Functors need type constructors of one argument (F[_] from the trait).
  // Likewise, can't just do Functor[Function1] as Function1 is a binary type
  // constructor: it needs input AND output type.
  // [X => ?] is sugar from kind-projector for:
  // [Lambda[X => Function1[X, ?]]], which is more kind-projector sugar for:
  // [({type l[a] = Function1[X,a]})#l], where a = ?

  implicit def function1Functor[X]: Functor[X => ?] = new Functor[X => ?] {
    def map[A, B](fa: X => A)(f: A => B): X => B = fa andThen f
  }

}

object TestApp5 extends App {

  import Functor.ops._

  type FunctionFromInt[T] = Int => T

  implicit val function1IntFunctor: Functor[FunctionFromInt] = new Functor[FunctionFromInt] {
    override def map[A, B](fa: FunctionFromInt[A])(f: A => B): FunctionFromInt[B] = fa andThen f
  }

  val plus1: FunctionFromInt[Int] = (_: Int) + 1
  val multiplyBy2 = (_: Int) * 2

  println(plus1.map(multiplyBy2)(2))         //6

}

object TestApp6 extends App {

  import Functor.ops._

  val plus1 = (_: Int) + 1
  val multiplyBy2 = (_: Int) * 2

  println(plus1.map(multiplyBy2)(2))         //6

}
