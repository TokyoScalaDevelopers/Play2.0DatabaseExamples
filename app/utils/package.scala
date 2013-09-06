package object utils {
  def cleanTitle(longTitle: String, limit: Int = 30): String = {
    // RFC 1738
    val allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ$-_.+!*'(),"
    longTitle.take(limit).map({
      case c if(allowedCharacters contains c) => c
      case _ => '_'
    })
  }
}
