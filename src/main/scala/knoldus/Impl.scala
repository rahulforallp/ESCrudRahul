package knoldus

/**
  * Created by knoldus on 3/4/16.
  */
object Impl extends ElasticSearchImpl with App {

  val client = getClient()
  val mappedClient = addMappingToIndex("twitter", client)

  val inserted = insert(client)
  println("Inserted : " + inserted.getItems.length)
  Thread.sleep(1000)

  val updated = update(client, "twitter", "tweet", "2")
  println("Updated ")
  Thread.sleep(1000)

  val deleted = deleteById(client, "twitter", "tweet", "2")
  println("Deleted ")
  Thread.sleep(1000)

  val deleteIndexResponse = deleteIndex(client, "twitter")
  println("Delete index response " + deleteIndexResponse)
}
