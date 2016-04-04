package knoldus

import java.io.{File, PrintWriter}

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.{UpdateRequest, UpdateResponse}
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.xcontent.XContentFactory._
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.search.sort.SortOrder

import scala.io.Source

/**
  * Created by knoldus on 3/4/16.
  */
trait JsonInputOutput {

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

  def insertJsonDocument(client: Client): BulkResponse = {
    val inputJson = Source.fromFile("src/main/resources/inputJson.json").getLines().toList
    val preparedBulk = client.prepareBulk()
    for (i <- 0 until inputJson.size) {
      preparedBulk.add(client.prepareIndex("twitter", "tweet", (i + 1).toString).setSource(inputJson(i)))
    }
    preparedBulk.execute().actionGet()
  }

  def retrieveJson(client: Client):Boolean={
    val data=client.prepareSearch("twitter").setTypes("tweet").execute().get()
    val pw=new PrintWriter(new File("/src/test/resources/OutputJson.json"))
    pw.write(data.toString)
    true
  }

}
