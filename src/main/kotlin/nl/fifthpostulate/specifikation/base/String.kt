package nl.fifthpostulate.specifikation.base

val isBlank =
    String::isBlank.toSpecification("string is not blank")

val isNotBlank =
    String::isNotBlank.toSpecification("string is blank")

val isEmpty =
    String::isEmpty.toSpecification("string is not empty")

val isNotEmpty =
    String::isNotEmpty.toSpecification("string is empty")

val isNullOrBlank =
    String?::isNullOrBlank.toSpecification("string is not null nor blank")

val isNullOrEmpty =
    String?::isNullOrEmpty.toSpecification("string is not null nor empty")
