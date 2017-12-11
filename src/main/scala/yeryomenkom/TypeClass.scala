package yeryomenkom

import simulacrum._

object Printable {

  implicit val printableInt: Printable[Int] = new Printable[Int] {
    override def pformat(value: Int): String = value.toString + " :)"
  }

  implicit val printableString: Printable[String] = new Printable[String] {
    override def pformat(value: String): String = value + " :)"
  }

}

@typeclass trait Printable[A] { self =>
  def pformat(value: A): String
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A)(implicit printable: Printable[A]) {
    def pformat: String = printable.pformat(value)
  }
}

object TestApp1 extends App {
  import PrintableSyntax._

  println(1.pformat)        //1 :)
  println("ok".pformat)     //ok :)
}

object TestApp2 extends App {
  import Printable.ops._

  println(1.pformat)        //1 :)
  println("ok".pformat)     //ok :)
}