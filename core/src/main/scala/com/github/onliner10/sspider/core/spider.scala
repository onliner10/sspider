package com.github.onliner10.sspider.core

import cats.effect.IO
import cats.free.Free
import cats.free.Free.liftF

type HttpResponseMessage = String

enum LogLevel:
  case Info, Warning, Error

sealed trait SpiderA[T, A]
case class Fetch[T](url: String) extends SpiderA[T, HttpResponseMessage]
case class YieldResult[T](item: T) extends SpiderA[T, Unit]
case class Log[T](level: LogLevel, reason: String) extends SpiderA[T, Unit]

type Spider[T, A] = Free[SpiderA[T, _], A]

object Spider {
  def fetch[T](url: String): Spider[T, HttpResponseMessage] =
    liftF(Fetch(url))

  def yieldResult[T](item: T): Spider[T, Unit] =
    liftF(YieldResult(item))

  def log[T](level: LogLevel, reason: String): Spider[T, Unit] =
    liftF(Log(level, reason))

  private def runWithResource[T, R](
      spider: Spider[T, Unit],
      sink: R => T => IO[Unit]
  )(resource: R): IO[Unit] =
    val sinker = sink(resource)

    spider.foldMap(SpiderCompiler.compile(sinker))

  def run[T, R](spider: Spider[T, Unit], sink: Sink[T, R]): IO[Unit] =
    sink.handle.use(runWithResource(spider, sink.sink))
}
