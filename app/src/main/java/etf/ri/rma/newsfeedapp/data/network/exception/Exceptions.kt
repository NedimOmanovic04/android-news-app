package etf.ri.rma.newsfeedapp.data.network.exception

class InvalidUUIDException(message: String = "UUID nije u ispravnom formatu") : Exception(message)

class InvalidImageURLException(message: String = "Image URL nije ispravan") : Exception(message)
