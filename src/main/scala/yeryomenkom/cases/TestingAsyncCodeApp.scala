package yeryomenkom.cases

import cats.instances.list._
import cats.syntax.traverse._
import cats.syntax.functor._
import cats.{Applicative, Id}

import scala.concurrent.Future
import scala.language.higherKinds

object TestingAsyncCodeApp extends App {

  trait UptimeClient[F[_]] {
    def getUptime(hostname: String): F[Int]
  }

  class UptimeService[F[_]: Applicative](client: UptimeClient[F]) {
    def getTotalUptime(hostnames: List[String]): F[Int] = {
      hostnames.traverse(client.getUptime).map(_.sum)
    }
  }

  trait RealUptimeClient extends UptimeClient[Future] {
    def getUptime(hostname: String): Future[Int]
  }

  trait TestUptimeClient extends UptimeClient[Id] {
    override def getUptime(hostname: String): Id[Int]
  }

  class TestUptimeClientImpl(hosts: Map[String, Int]) extends TestUptimeClient {
    override def getUptime(hostname: String): Int = hosts.getOrElse(hostname, 0)
  }

  def testTotalUptime() = {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClientImpl(hosts)
    val service = new UptimeService(client)

    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum

    assert(actual == expected)
  }

  testTotalUptime()
}
