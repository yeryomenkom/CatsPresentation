package yeryomenkom

object TestApp extends App {

  case class Person(name: String, age: Int)

  import cats.Show

  implicit val showPerson: Show[Person] = new Show[Person] {
    override def show(t: Person): String = s"${t.name} is ${t.age} years old person."
  }

  import cats.syntax.show._

  val anna = Person("Anna", 18)
  val mykola = Person("Mykola", 20)

  println(anna.show)                  //Anna is 18 years old person.
  println(mykola.show)                //Mykola is 20 years old person.

  import cats.instances.list._

  val persons = List(anna, mykola)    //List(Anna is 18 years old person., Mykola is 20 years old person.)
  println(persons.show)


}
