package knoldus

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.update.{UpdateRequest, UpdateResponse}
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.xcontent.XContentFactory._
import org.elasticsearch.node.NodeBuilder._

/**
  * Created by knoldus on 3/4/16.
  */
trait ElasticSearchImpl {

  val contentBuilder = jsonBuilder
    .startObject
    .startObject("twitter")
    .startObject("_timestamp")
    .field("enabled", true)
    .field("store", true)
    .field("path", "post_date")
    .endObject
    .endObject
    .endObject

  def getClient(): Client = {
    val client = nodeBuilder().local(true).node.client()
    client
  }

  def addMappingToIndex(indexName: String, client: Client): CreateIndexResponse = {
    val settingsStr = ImmutableSettings.settingsBuilder().
      put("index.number_of_shards", 5).put("index.number_of_replicas", 1).build()
    client.admin().indices().prepareCreate(indexName)
      .setSettings(settingsStr)
      .addMapping(indexName, contentBuilder).execute()
      .actionGet()
  }

  def insert(client: Client): BulkResponse = {

    val bulkRequest = client.prepareBulk()
    bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
      .setSource(jsonBuilder()
        .startObject()
        .field("name", "Apple")
        .endObject()))
    bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
      .setSource(jsonBuilder()
        .startObject()
        .field("name", "Mango")
        .endObject()))
    bulkRequest.execute().actionGet()
  }

  def update(client: Client, indexName: String, typeName: String, id: String): UpdateResponse = {
    val updateRequest = new UpdateRequest(indexName, typeName, id)
      .doc(jsonBuilder()
        .startObject()
        .field("name", "Banana")
        .endObject())
    client.update(updateRequest).get()
  }

  def deleteById(client: Client, indexName: String, typeName: String, id: String): DeleteResponse = {

    val delResponse = client.prepareDelete("twitter", "tweet", id)
      .execute()
      .actionGet()
    delResponse
  }

  def retrieve(client: Client):Long={
    client.prepareSearch("twitter").execute().actionGet().getHits.totalHits()
  }

  def deleteIndex(client: Client, indexName: String): Boolean = {
    val deleteIndexRequest = new DeleteIndexRequest(indexName)
    val deleteIndexResponse = client.admin().indices().delete(deleteIndexRequest).actionGet()
    deleteIndexResponse.isAcknowledged()
  }

}
