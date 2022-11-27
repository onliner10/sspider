package com.github.onliner10.sspider
import com.github.onliner10.sspider.Spider._
import cats.arrow.FunctionK
import cats.{Id, ~>}
import cats.Traverse

def extractLinks(resp: HttpResponseMessage): List[String] = List("example.com/1", "example.com/2", "example.com/3")

def subSpider (url: String): Spider[String, Unit] =
  for
    body <- fetch(url)
    shouldFail = url.endsWith("2")
    _ <- if shouldFail then log(LogLevel.Error, "BOOM") else yieldResult(body)
  yield ()

def rootSpider : Spider[String, Unit] =
  for
    _ <- log(LogLevel.Info, "Starting root spider")
    body <- fetch("example.com")
    links = extractLinks(body)

    _ <- Traverse[List].traverse(links)(subSpider)

    _ <- log(LogLevel.Info, "Quitting root spider")
  yield ()

def sampleSink(x:String) =
  println(s"I'm sinking: $x")

@main
def main(): Unit = {
  println(rootSpider.foldMap(compiler(sampleSink)))
}