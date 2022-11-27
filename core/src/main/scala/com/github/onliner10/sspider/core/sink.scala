package com.github.onliner10.sspider.core

import cats.effect.kernel.Resource
import cats.effect.IO

trait Sink[A, R]:
  def handle: Resource[IO, R]
  def sink(resource: R)(item: A): IO[Unit]
