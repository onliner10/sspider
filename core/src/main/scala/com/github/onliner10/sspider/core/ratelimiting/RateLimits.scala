package com.github.onliner10.sspider.core.ratelimitng

import io.lemonlabs.uri.Url

import java.time.Duration
import java.util as ju

class ConstantRateLimits(config: Map[Url, Duration]):
  def getDelay: Url => Option[Duration] = config.get

  def withLimit(urlPrefix: Url, delay: Duration): ConstantRateLimits =
    ConstantRateLimits(config + (urlPrefix -> delay))

object ConstantRateLimits:
  def empty = ConstantRateLimits(Map.empty)
