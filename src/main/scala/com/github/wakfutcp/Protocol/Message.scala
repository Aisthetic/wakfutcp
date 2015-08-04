package com.github.wakfutcp.Protocol

sealed trait Message

trait OutputMessage extends Message

trait InputMessage extends Message
