package com.github.jac3km4.wakfutcp.Protocol

sealed trait Message

trait OutputMessage extends Message

trait InputMessage extends Message
