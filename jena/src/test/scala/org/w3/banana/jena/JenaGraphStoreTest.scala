package org.w3.banana.jena

import org.apache.jena.query.{Dataset, DatasetFactory}
import org.apache.jena.tdb.{TDB, TDBFactory}
import org.w3.banana._
import org.w3.banana.jena.Jena._

import scala.util.Try

import org.w3.banana.util.tryInstances._

abstract class JenaGraphStoreTest(dataset: Dataset) extends GraphStoreTest[Jena, Try, Dataset](dataset) {

  import graphStore.graphStoreSyntax._
  import ops._

  val defaultGraph: Jena#Graph = dataset.asDatasetGraph.getDefaultGraph

  "adding a named graph should not pollute the default graph" in {
    dataset.appendToGraph(makeUri("http://example.com/foo"), graph).get
    assert(defaultGraph.size == 0)
  }

}

class JenaMemGraphStoreTest extends JenaGraphStoreTest({
  DatasetFactory.createMem()
})

class JenaTDBGraphStoreTest extends JenaGraphStoreTest({
  val dataset = TDBFactory.createDataset("test.tdb")
  dataset.getContext.set(TDB.symUnionDefaultGraph, false)
  dataset
})

