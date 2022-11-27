package com.github.onliner10.sspider.core

import io.lemonlabs.uri.Url

trait RateLimits:
  def Resolve(url: Url): Int
