package yeryomenkom

import cats.instances.AllInstances
import cats.kernel.laws.GroupLaws
import cats.syntax.AllSyntax
import cats.tests.{StrictCatsEquality, TestSettings}
import cats.{Eq, Monoid}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, FunSuiteLike, Matchers}
import org.typelevel.discipline.scalatest.Discipline

trait CatsSuite extends FunSuite
  with Matchers
  with GeneratorDrivenPropertyChecks
  with Discipline
  with TestSettings
  with AllInstances
  with AllSyntax
  with StrictCatsEquality {
  self: FunSuiteLike =>
}

class LawsSpec extends CatsSuite {

  val additionIntMonoid: Monoid[Int] = new Monoid[Int] {
    override def empty = 0
    override def combine(x: Int, y: Int) = x + y
  }

  checkAll("Int addition monoid laws", GroupLaws[Int].monoid(additionIntMonoid))

  val subtractionIntMonoid: Monoid[Int] = new Monoid[Int] {
    override def empty = 0
    override def combine(x: Int, y: Int) = x - y
  }

  checkAll("Int subtraction monoid laws", GroupLaws[Int].monoid(subtractionIntMonoid))

  case class MilliCents(value: Int)

  val millicentsAdditionMonoid: Monoid[MilliCents] = new Monoid[MilliCents] {
    override def empty: MilliCents = MilliCents(0)
    override def combine(x: MilliCents, y: MilliCents): MilliCents = MilliCents(x.value + y.value)
  }

  implicit val millicentsArb: Arbitrary[MilliCents] = Arbitrary(Gen.chooseNum(-1000, 1000).map(MilliCents.apply))
  implicit val millicentsEq: Eq[MilliCents] = Eq[Int].contramap(_.value)

  checkAll("Millicents addition monoid laws", GroupLaws[MilliCents].monoid(millicentsAdditionMonoid))

}
