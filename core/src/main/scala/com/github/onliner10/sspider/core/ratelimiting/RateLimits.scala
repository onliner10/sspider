package com.github.onliner10.sspider.core.ratelimitng

import io.lemonlabs.uri.Url

import java.time.temporal.TemporalAmount
import java.util as ju

class ConstantRateLimits(config: Map[Url, TemporalAmount]):
  def getDelay: Url => Option[TemporalAmount] = config.get

  def withLimit(urlPrefix: Url, delay: TemporalAmount): ConstantRateLimits =
    ConstantRateLimits(config + (urlPrefix -> delay))

object ConstantRateLimits:
  def empty = ConstantRateLimits(Map.empty)
