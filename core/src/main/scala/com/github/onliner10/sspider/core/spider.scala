package com.github.onliner10.sspider.core

import cats.effect.IO
import cats.free.Free
import cats.free.Free.liftF
import io.lemonlabs.uri.Url

type Spider[T, A] = Free[SpiderA[T, _], A]

object Spider {
  def fetch[T](url: Url): Spider[T, String] =
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
