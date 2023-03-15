package pt.isel.ls.server.exceptions;

import org.http4k.core.Status;
import java.lang.Exception;

sealed class TrelloException(message: String, val status: Status): Exception(message)
