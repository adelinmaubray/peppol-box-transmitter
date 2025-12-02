package be.compuwave.peppol_box_transmitter.utils

fun printlnInRed(message: String?) = println("\u001B[31m$message\u001B[0m")
fun printlnInGreen(message: String?) = println("\u001B[32m$message\u001B[0m")
fun printlnInYellow(message: String?) = println("\u001B[33m$message\u001B[0m")
fun printInCyan(message: String?) = println("\u001B[36m$message\u001B[0m")

fun printWithTab(message: String?) = println("\t$message")
