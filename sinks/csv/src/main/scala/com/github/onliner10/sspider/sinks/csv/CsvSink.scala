package com.github.onliner10.sspider.sinks.csv

import cats.effect.IO
import cats.effect.kernel.Resource
import com.github.onliner10.sspider.core.Sink

class CsvSink[T] extends Sink[T, String] {
  def handle = Resource.make(IO.println("Acquiring resource") >> IO("Sth"))(x =>
    IO.println("Releasing resource")
  )

  def sink(resource: String)(item: T): IO[Unit] =
    IO.println(s"Sinkig using resource: $resource : ${item.toString()}")

}
