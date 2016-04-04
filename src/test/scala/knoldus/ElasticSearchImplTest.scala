package knoldus

import org.scalatest.FunSuite

/**
  * Created by knoldus on 4/4/16.
  */
class ElasticSearchImplTest extends FunSuite with ElasticSearchImpl{

  val client = getClient()
  val mappedClient = addMappingToIndex("twitter", client)

  test("Add"){
    val inserted = insert(client)
    assert(inserted.getItems.length==2)
  }

  test("Update"){
    val updated = update(client, "twitter", "tweet", "2")
    assert(updated.getVersion==2)
  }

  test("Delete"){
    val deleted = deleteById(client, "twitter", "tweet", "2")
    assert(deleted.isFound==true)
  }

  test("Retrieved"){
    val retrieved = retrieve(client)
    assert(retrieved==2)
  }

  test("delete Index"){
    val deleted=deleteIndex(client,"twitter")
    assert(deleted===true)
  }

}
