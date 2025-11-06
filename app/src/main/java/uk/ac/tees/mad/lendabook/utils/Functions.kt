package uk.ac.tees.mad.lendabook.utils


fun getValidPassword(password: String): List<String> {

    val errors = mutableListOf<String>()
    val specialChars = """^$*.[]{}()?"!@#%&/\\,><':;|_~"""

    if (password.length < 6) errors.add("Minimum 6 characters required")
    if (password.length > 4096) errors.add("Password too long")
    if (!password.any { it.isLowerCase() }) errors.add("At least one lowercase letter required")
    if (!password.any { it.isUpperCase() }) errors.add("At least one uppercase letter required")
    if (!password.any { it.isDigit() }) errors.add("At least one number required")
    if (!password.any { it in specialChars }) errors.add("At least one special character required")

    return errors
}