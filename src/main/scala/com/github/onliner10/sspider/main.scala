package com.github.onliner10.sspider
import com.github.onliner10.sspider.Spider._
import cats.arrow.FunctionK
import cats.{Id, ~>}
import cats.Traverse

//https://www.scrapethissite.com/pages/forms/
case class HockeyTeam(
  teamName: String,
  year: Int,
  wins: Int,
  losses: Int
)

def extractLinks(resp: HttpResponseMessage): List[String] = List("example.com/1", "example.com/2", "example.com/3", "example.com/4")

def subSpider (url: String): Spider[HockeyTeam, Unit] =
  for
    body <- fetch(url)
    shouldFail = url.endsWith("2")

    dummyRecord = HockeyTeam(body, 2012,1,1)

    _ <- if shouldFail then log(LogLevel.Error, "BOOM") else yieldResult(dummyRecord)
  yield ()

def rootSpider : Spider[HockeyTeam, Unit] =
  for
    _ <- log(LogLevel.Info, "Starting root spider")
    body <- fetch("https://www.scrapethissite.com/pages/forms/")
    links = extractLinks(body)

    _ <- Traverse[List].traverse(links)(subSpider)

    _ <- log(LogLevel.Info, "Quitting root spider")
  yield ()

def sampleSink(x: HockeyTeam) =
  println(s"I'm sinking: $x")

@main
def main(): Unit = {
  println(rootSpider.foldMap(compiler(sampleSink)))
}