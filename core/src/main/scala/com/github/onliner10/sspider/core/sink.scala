package com.github.onliner10.sspider.core

import cats.effect.IO
import cats.effect.kernel.Resource

trait Sink[A, R]:
  def handle: Resource[IO, R]
  def sink(resource: R)(item: A): IO[Unit]
