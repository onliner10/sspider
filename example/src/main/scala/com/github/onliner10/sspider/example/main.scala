package com.github.onliner10.sspider.example
import cats.Id
import cats.Traverse
import cats.arrow.FunctionK
import cats.effect.IO
import cats.effect.IOApp
import cats.~>
import com.github.onliner10.sspider.core.Spider._
import com.github.onliner10.sspider.core._
import com.github.onliner10.sspider.sinks.csv.CsvSink
import io.lemonlabs.uri.Url

//https://www.scrapethissite.com/pages/forms/
case class HockeyTeam(
    teamName: String,
    year: Int,
    wins: Int,
    losses: Int
)

def extractLinks(resp: String): List[Url] =
  List("example.com/1", "example.com/2", "example.com/3", "example.com/4").map(
    Url.parse
  )

def subSpider(url: Url): Spider[HockeyTeam, Unit] =
  for
    body <- fetch(url)
    shouldFail = url.path.toString.endsWith("2")

    dummyRecord = HockeyTeam(body, 2012, 1, 1)

    _ <-
      if shouldFail then log(LogLevel.Error, "BOOM")
      else yieldResult(dummyRecord)
  yield ()

def rootSpider: Spider[HockeyTeam, Unit] =
  for
    _ <- log(LogLevel.Info, "Starting root spider")
    body <- fetch(Url.parse("https://www.scrapethissite.com/pages/forms/"))
    links = extractLinks(body)

    _ <- Traverse[List].traverse(links)(subSpider)

    _ <- log(LogLevel.Info, "Quitting root spider")
  yield ()

def sampleSink(x: HockeyTeam) =
  println(s"I'm sinking: $x")

object ExampleApp extends IOApp.Simple {
  override def run =
    val csvSink = new CsvSink[HockeyTeam]()
    Spider.run(rootSpider, csvSink)
}
