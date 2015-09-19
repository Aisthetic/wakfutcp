package com.github.wakfutcp.protocol

sealed trait Message

trait OutputMessage extends Message

trait InputMessage extends Message
