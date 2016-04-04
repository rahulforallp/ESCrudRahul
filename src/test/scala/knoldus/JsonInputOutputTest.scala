package knoldus

import org.scalatest.FunSuite

/**
  * Created by knoldus on 4/4/16.
  */
class JsonInputOutputTest extends FunSuite with JsonInputOutput{

  val client = getClient()
  val mappedClient = addMappingToIndex("twitter", client)

  test("Insert JSon"){
    val inserted = insertJsonDocument(client)
    assert(inserted.getItems.length==270)
  }

  test("Retrieve JSon"){
    val retrieved = retrieveJson(client)
    assert(retrieved==true)
  }

}
